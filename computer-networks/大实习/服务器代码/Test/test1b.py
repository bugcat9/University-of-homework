#!/usr/bin/env python

# Author: Junda Liu (liujd@cs)
# Author: DK Moon (dkmoon@cs)

import fcntl, os, random, re, signal, socket, struct, sys, time, types
import curses.ascii
from subprocess import Popen, PIPE, STDOUT


# Turn on this flag if need more debugging messages.
verbose = 1

# Environment set up.
_version_=0.8
c_bin = './client'
try:
    s_bin = "./"+sys.argv[1]
except:
    s_bin = "./server"
s_port = random.randrange(30000,65000)
s_players = ['mario', 'peach', 'ryu', 'ken', 'guile', 'chunli', 'sophitia',
             'cassandra', 'mitsurugi', 'siegfried', 'taki', 'lizardman',
             'kasumi', 'ayane', 'hitomi', 'helena', 'kokoro', 'leon',
             'zack', 'lisa', 'honda', 'dhalsim']
s_time_allowance = 1.0  # in sec

EE122_VALID_VERSION = 4
EE122_X_SIZE = 100
EE122_Y_SIZE = 100
MALFORMED_MESSAGE_TEXT = 'The gate to the tiny world of warcraft has disappeared.'


# Checks the python version.
if sys.version_info < (2,4):
    print 'This script requires python 2.4 or higher. Exit now.'
    sys.exit(1)


#########################################################################
# Functions for packet sanity checking.
#########################################################################
def verify_hp(hp):
    if hp < 0 or hp >= (1 << 31):
        raise ValueError('Invalid HP (%d)' % hp)

def verify_exp(exp):
    if exp < 0 or exp >= (1 << 31):
        raise ValueError('Invalid EXP (%d)' % exp)

def verify_x(x):
    if x < 0 or x >= EE122_X_SIZE:
        raise ValueError('Invalid X (%d)' % x)

def verify_y(y):
    if y < 0 or y >= EE122_Y_SIZE:
        raise ValueError('Invalid Y (%d)' % y)

def extract_name(name):
    null_loc = name.find('\x00')
    if null_loc < 0:
        raise ValueError('Name is not null terminated')
    name = name[:null_loc]
    if not name.isalnum():
        raise ValueError('Name is not alphanumeric')
    return name

def extract_speak_message(msg):
    null_loc = msg.find('\x00')
    if null_loc < 0:
        raise ValueError('Speak message is not null terminated')
    msg = msg[:null_loc]
    for c in msg:
        if not curses.ascii.isprint(c):
            raise ValueError('Speak message is not printable')
    return msg


#########################################################################
# Class for each possible reply packet.
#########################################################################
class _EE122Reply:
    class LoginReply:
        def __init__(self, type, bytes):
            self.type_ = type
            if len(bytes) < 11:
                raise ValueError('Insufficient length (%d) for LoginReply'
                                 % len(bytes))
            (self.error_code_, self.hp_, self.exp_, self.x_, self.y_) \
                = struct.unpack('!BiiBB', bytes[0:11])

            # Sanity checking if the error code is "normal login".
            if self.error_code_ == 0:
              verify_hp(self.hp_)
              verify_exp(self.exp_)
              verify_x(self.x_)
              verify_y(self.y_)

    class MoveNotify:
        def __init__(self, type, bytes):
            self.type_ = type
            if len(bytes) < 20:
                raise ValueError('Insufficient length (%d) for MoveNotify'
                                 % len(bytes))
            self.player_name_ = ''.join(bytes[0:10])
            (self.x_, self.y_, self.hp_, self.exp_) \
                = struct.unpack('!BBii', bytes[10:])

            # Sanity checking.
            self.player_name_ = extract_name(self.player_name_)
            verify_hp(self.hp_)
            verify_exp(self.exp_)
            verify_x(self.x_)
            verify_y(self.y_)

    class AttackNotify:
        def __init__(self, type, bytes):
            self.type_ = type
            if len(bytes) < 25:
                raise ValueError('Insufficient length (%d) for AttackNotify'
                                 % len(bytes))
            self.attacker_name_ = ''.join(bytes[0:10])
            self.victim_name_ = ''.join(bytes[10:20])
            (self.damage, self.hp_) = struct.unpack('!Bi', bytes[20:25])

            # Sanity checking.
            self.attacker_name_ = extract_name(self.attacker_name_)
            self.victim_name_ = extract_name(self.victim_name_)
            verify_hp(self.hp_)

    class SpeakNotify:
        def __init__(self, type, bytes):
            self.type_ = type
            if len(bytes) < 11:
                raise ValueError('Insufficient length (%d) for SpeakNotify'
                                 % len(bytes))
            self.speaker_name_ = ''.join(bytes[0:10])
            self.message_ = ''.join(bytes[10:])

            # Sanity checking.
            self.speaker_name_ = extract_name(self.speaker_name_)
            self.message_ = extract_speak_message(self.message_)

    class LogoutNotify:
        def __init__(self, type, bytes):
            if len(bytes) < 10:
                raise ValueError('Insufficient length (%d) for LogoutNotify'
                                 % len(bytes))
            self.player_name_ = ''.join(bytes[0:10])
            self.player_name_ = extract_name(self.player_name_)

    class InvalidState:
        def __init__(self, type, bytes):
            self.type_ = type
            if len(bytes) < 1:
                raise ValueError('Insufficient length (%d) for InvalidState'
                                 % len(bytes))
            self.code_, = struct.unpack('B', bytes[0:1])

    message_map = {
        2: LoginReply,
        4: MoveNotify,
        6: AttackNotify,
        8: SpeakNotify,
        10: LogoutNotify,
        11: InvalidState,
    }


def construct_packet(packet_in_string):
    """ Parses the packet string in the form of [00 00 00 00 ...] and 
        constructs a reply instance"""

    bytes = [chr(int(e, 16)) for e in packet_in_string.split()]
    bytes = ''.join(bytes)
    version, length, type = struct.unpack('!BHB', bytes[0:4])
   
    # Header sanity checking.
    if version != EE122_VALID_VERSION:
        raise ValueError('Invalid version (%d)' % version)
    if length % 4:
        raise ValueError('Packet length (%d) not a multiple of 4 bytes'
                         % length)
    if length != len(bytes):
        raise ValueError('Length in header (%d) not matching the received'
                         'packet size (%d)' % (length, len(bytes)))
    if not _EE122Reply.message_map.has_key(type):
        raise ValueError('Invalid msg type (%d)' % type)

    # Constructs a packet.
    try:
        return _EE122Reply.message_map[type](type, bytes[4:])
    except ValueError, e:
        raise ValueError(str(_EE122Reply.message_map[type]) + ':' + str(e))


def kill_subproc(subplst):
    """Utility function to kill a list of subprocesses"""
    for p in subplst:
        try:
            os.kill(p.pid, signal.SIGTERM)
        except:
            pass


def check_client_binary():
    """ Verifies the client binary is executable file. """
    if not os.path.isfile(c_bin):
        print "Can't find client binary for test."
        print "You should get it from project website. Exit now."
        sys.exit(1)
    if not os.access(c_bin, os.X_OK):
        print "Client file '%s' is not executable." % c_bin
        sys.exit(1)


def check_server_binary():
    """ Verifies the server binary is executable file. """
    if not os.path.isfile(s_bin):
        print "Can't find server binary. "
        print "You should put it in the same folder. Exit now."
        sys.exit(1)
    if not os.access(s_bin, os.X_OK):
        print "Server file '%s' is not executable" % s_bin
        sys.exit(1)


def launch_server():
    null = open('/dev/null', 'w')
    server = Popen([s_bin, '-p', str(s_port)], stdout = null)
    if server.poll():
        raise RuntimeError('! Failed to launch the server.')
    return server


def launch_client(server_ip_addr = '127.0.0.1'):
    client = Popen([c_bin, '-s', server_ip_addr, '-p', str(s_port), '-v'],
                   stdin = PIPE, stdout = PIPE, stderr = STDOUT)
    if client.poll():
        raise RuntimeError('! Failed to launch the client.')
    v = fcntl.fcntl(client.stdout, fcntl.F_GETFL)
    fcntl.fcntl(client.stdout, fcntl.F_SETFL, v | os.O_NONBLOCK)
    return client


def parse_output(output):
    """ Builds a list of packets and a list of messages. """
    packets, messages = [], []
    for line in output.split('\n'):
        s = re.search('received msg ver:\d+ len:\d+ type:\d+ '
                      'raw_pkt\(net_byte_order\)=\[([0-9a-fA-F ]+)\]', line)
        # Line for packet data?                                                                 
        if s:
            try:
                packets.append(construct_packet(s.group(1)))
            except Exception, e:
                if verbose:
                    print 'Skipping an invalid packet: ', e
        else:
            messages.append(line)
    return packets, messages


def sleep(duration):
    end = time.time() + duration
    while time.time() < end:
        remain = end - time.time()
        if remain <= 0: break
        time.sleep(remain)


##########################################################################
# Tests 
(TEST_TYPE_FUNCTION,
 TEST_TYPE_EXCEPTION,
 TEST_TYPE_PERFORMANCE) = range(3)

def ee122_test_connection(server, clients):
    """connection test using the host's real IP address"""
    client = clients[0]
    sleep(s_time_allowance)
    if client.poll():
        raise RuntimeError('The client abnormally terminated.')
    if server.poll():
        raise RuntimeError('The server abnormally terminated.')
    return True


def ee122_test_login(server, clients):
    """Login command test"""
    client = clients[0]
    # Injects a command.
    client.stdin.write('login %s\n' % s_players[0])
    sleep(s_time_allowance)
    # Parses the output packet.
    outputstr = client.stdout.read().replace('command>', '')
    packets, output_lines = parse_output(outputstr)
    # We expect LOGIN_REPLY.
    found = False
    for packet in packets:
        if (packet.__class__.__name__ == _EE122Reply.LoginReply.__name__ and
            packet.error_code_ == 0):
            found = True
            break

    if client.poll():
        raise RuntimeError('The client abnormally terminated.')
    if server.poll():
        raise RuntimeError('The server abnormally terminated.')
    if not found:
        raise RuntimeError('No valid LOGIN_REPLY found.\n'
                           'Client output: %s' % outputstr)
    return True

        
def ee122_test_duplicate_login(server, clients):
    """Duplicated login test"""
    client1, client2 = clients
    # Injects a command.
    client1.stdin.write('login %s\n' % s_players[0])
    sleep(s_time_allowance)
    client2.stdin.write('login %s\n' % s_players[0])
    sleep(s_time_allowance)

    # We expect the client1 is OK and the client2 got a LOGIN_REPLY with
    # error code == 1
    try:
        outputstr = client1.stdout.read().replace('command>', '')
        raise RuntimeError('Player1 must not receive a message. '
                           'But got: %s' % outputstr)
    except:
        pass

    found = False
    outputstr = client2.stdout.read().replace('command>', '')
    packets, output_lines = parse_output(outputstr)
    for packet in packets:
        if (packet.__class__.__name__ == _EE122Reply.LoginReply.__name__ and
            packet.error_code_ == 1):
            found = True
            break
    if client1.poll():
        raise RuntimeError('The client1 abnormally terminated.')
    if client2.poll():
        raise RuntimeError('The client2 abnormally terminated.')
    if server.poll():
        raise RuntimeError('The server abnormally terminated.')
    if not found:
        raise RuntimeError('No valid LOGIN_REPLY found.\n'
                           'Client output: %s' % outputstr)
    return True
    

def ee122_test_invalid_state(server, clients):
    """Invalid state test (speak/move before login)"""
    client = clients[0]
    # Injects commands.
    client.stdin.write('speak must_not_shown\n')
    client.stdin.write('move north\n')
    sleep(s_time_allowance)
    # Parses the output packet.
    outputstr = client.stdout.read().replace('command>', '')
    packets, output_lines = parse_output(outputstr)

    # We expect exactly 2 invalid state packets.
    if len(packets) != 2:
        raise RuntimeError('Only 2 packets are expected.')

    count = 0
    for packet in packets:
        if packet.__class__.__name__ == _EE122Reply.InvalidState.__name__:
            # The code must be zero.
            if packet.code_ == 0: count += 1

    if client.poll():
        raise RuntimeError('The client abnormally terminated.')
    if server.poll():
        raise RuntimeError('The server abnormally terminated.')
    if count != 2:
        raise RuntimeError('# INVALID_STATE is not 2. Got %d.\n'
                           'Client output: %s' % (count, outputstr))
    return True
    

def ee122_test_invalid_state2(server, clients):
    """Invalid state test (login after login)"""
    client = clients[0]
    # Injects commands.
    client.stdin.write('login %s\n' % s_players[0])
    client.stdin.write('login %s\n' % s_players[0])
    sleep(s_time_allowance)
    # Parses the output packet.
    outputstr = client.stdout.read().replace('command>', '')
    packets, output_lines = parse_output(outputstr)
    # We expect one login reply, one move_notify and one invalid state.
    if len(packets) != 3:
        raise RuntimeError('Only 3 packets are expected.'
                           'Client output: %s' % outputstr)

    count = 0
    for packet in packets:
        if packet.__class__.__name__ == _EE122Reply.InvalidState.__name__:
            # The code must be 1.
            if packet.code_ == 1: count += 1

    if client.poll():
        raise RuntimeError('The client abnormally terminated.')
    if server.poll():
        raise RuntimeError('The server abnormally terminated.')
    if count != 1:
        raise RuntimeError('# INVALID_STATE is not 1. Got %d\n'
                           'Client output: %s' % (count, outputstr))
    return True


def ee122_test_move(server, clients):
    """Move command test"""
    client1, client2 = clients
    # Injects commands.
    client1.stdin.write('login %s\n' % s_players[0])
    client2.stdin.write('login %s\n' % s_players[1])
    sleep(s_time_allowance)
    client1.stdin.write('move north %s\n' % s_players[0])
    client1.stdin.write('move south %s\n' % s_players[0])
    client1.stdin.write('move east %s\n' % s_players[0])
    client1.stdin.write('move west %s\n' % s_players[0])
    sleep(s_time_allowance)
    # Parses the output packet.
    for client in clients:
        outputstr = client.stdout.read().replace('command>', '')
        packets, output_lines = parse_output(outputstr)
        # We expect five move notifications.
        move_packets = []
        for packet in packets:
            if packet.__class__.__name__ == _EE122Reply.MoveNotify.__name__:
                if packet.player_name_ == s_players[0]:
                    move_packets.append(packet)
        if server.poll():
            raise RuntimeError('The server abnormally terminated.')
        if client.poll():
            raise RuntimeError('The client abnormally terminated.')
        if len(move_packets) != 5:
            raise RuntimeError('# MOVE_NOTIFY about %s is not 5. Got %d. '
                               'Client output: %s' % (s_players[0],
                                                      len(move_packets),
                                                      outputstr))
        if ((move_packets[0].y_ - 3 + EE122_Y_SIZE) % EE122_Y_SIZE !=
              move_packets[1].y_):
            raise RuntimeError('Incorrect y location after move north.')
        if move_packets[1].x_ != move_packets[2].x_:
            raise RuntimeError('The x location changed by move south.')
        if (move_packets[1].y_ + 3) % EE122_Y_SIZE != move_packets[2].y_:
            raise RuntimeError('Incorrect y location after move south.')
        if move_packets[2].y_ != move_packets[3].y_:
            raise RuntimeError('The y location changed by move east.')
        if (move_packets[2].x_ + 3) % EE122_X_SIZE != move_packets[3].x_:
            raise RuntimeError('Incorrect x location after move east.')
        if move_packets[3].y_ != move_packets[4].y_:
            raise RuntimeError('The y location changed by move west.')
        if ((move_packets[3].x_ - 3 + EE122_X_SIZE) % EE122_X_SIZE !=
              move_packets[4].x_):
            raise RuntimeError('Incorrect x location after move west.')

    return True


def ee122_test_hp_regen(server, clients):
    """HP regeneration test"""
    client = clients[0]
    # Injects a command.
    client.stdin.write('login %s\n' % s_players[0])
    # Waits for enough time.         
    timeout = 7.0
    sleep(timeout)
    # Injects a move command to see the updated HP.
    client.stdin.write('move north\n')
    sleep(s_time_allowance)
    # Parses the output packet.
    outputstr = client.stdout.read().replace('command>', '')
    packets, output_lines = parse_output(outputstr)
    # We expect LOGIN_REPLY and MOVE_NOTIFY.
    login_reply, move_notify = None, None
    for packet in packets:
        if (packet.__class__.__name__ == _EE122Reply.LoginReply.__name__ and
            packet.error_code_ == 0):
            login_reply = packet
        if packet.__class__.__name__ == _EE122Reply.MoveNotify.__name__:
            move_notify = packet

    if client.poll():
        raise RuntimeError('The client abnormally terminated.')
    if server.poll():
        raise RuntimeError('The server abnormally terminated.')
    if not login_reply:
        raise RuntimeError('No valid LOGIN_REPLY found.\n'
                           'Client output: %s' % outputstr)
    if not move_notify:
        raise RuntimeError('No valid MOVE_NOTIFY found.\n'
                           'Client output: %s' % outputstr)
    if login_reply.hp_ >= move_notify.hp_:
        raise RuntimeError('No HP regeneration in %lf sec.\n'
                           'Client output: %s' % (timeout, outputstr))
    return True


def ee122_test_speak(server, clients, message):
    client1, client2 = clients
    # Injects commands.
    client1.stdin.write('login %s\n' % s_players[0])
    client2.stdin.write('login %s\n' % s_players[1])
    sleep(s_time_allowance)
    client1.stdin.write('speak %s\n' % message)
    sleep(s_time_allowance)
    for client in clients:
        # Parses the output packet.
        outputstr = client.stdout.read().replace('command>', '')
        packets, output_lines = parse_output(outputstr)
        # We expect one one speak notify from the player 1.
        count = 0
        for packet in packets:
            if packet.__class__.__name__ == _EE122Reply.SpeakNotify.__name__:
                if (packet.speaker_name_ == s_players[0] and
                    packet.message_ == message):
                    count += 1
        if server.poll():
            raise RuntimeError('The server abnormally terminated.')
        if client.poll():
            raise RuntimeError('The client abnormally terminated.')
        if count != 1:
            raise RuntimeError('# SPEAK_NOTIFY is not 1. Got %d. '
                               'Client output: %s' % (count, outputstr))
    return True


def ee122_test_speak_101bytes(server, clients):
    """Speak command test (100 bytes + null)"""
    message = '1234567890 ' * 9 + 'X'
    return ee122_test_speak(server, clients, message)


def ee122_test_speak_255bytes(server, clients):
    """Speak command test (254 bytes + null)"""
    message = '1234567890 ' * 23 + 'X'
    return ee122_test_speak(server, clients, message)


def ee122_test_attack(server, clients):
    """Attack command test."""
    print '* Now testing the attack command.'
    print '* This may take long since we move players.'
    client1, client2 = clients
    # Injects commands.
    client1.stdin.write('login %s\n' % s_players[0])
    client2.stdin.write('login %s\n' % s_players[1])
    sleep(1.0)
    locs = []
    for i in range(len(clients)):
        client = clients[i]
        # Parses the output packet.
        outputstr = client.stdout.read().replace('command>', '')
        packets, output_lines = parse_output(outputstr)
        # Gets the player location.
        move_packet = None
        for packet in packets:
            if packet.__class__.__name__ == _EE122Reply.MoveNotify.__name__:
                if packet.player_name_ == s_players[i]:
                    move_packet = packet
        if not move_packet:
            raise RuntimeError('Player %d did not receive MOVE_NOTIFY' % i)
        locs.append(move_packet)

    # Player1 moves toward Player2.
    if verbose:
        print 'Player1 loc: (%d,%d)' % (locs[0].x_, locs[0].y_)
        print 'Player2 loc: (%d,%d)' % (locs[1].x_, locs[1].y_)
    x_cnt = (((locs[1].x_ - locs[0].x_ + EE122_X_SIZE) % EE122_X_SIZE) / 3)
    y_cnt = (((locs[1].y_ - locs[0].y_ + EE122_Y_SIZE) % EE122_Y_SIZE) / 3)
    if verbose:
        print 'Player1 moves east %d times' % x_cnt
        print 'Player1 moves south %d times' % y_cnt
    for i in range(x_cnt): client1.stdin.write('move east\n')
    for i in range(y_cnt): client1.stdin.write('move south\n')
    if x_cnt or y_cnt: sleep(s_time_allowance * 20)
    if server.poll():
        raise RuntimeError('The server abnormally terminated.')
    if client1.poll() or client2.poll():
        raise RuntimeError('The client abnormally terminated.')
    # Parses the output packet.
    # If the players are at the same location, we will have no output.
    try:
        for i in range(len(clients)):
            client = clients[i]
            # Parses the output packet.
            outputstr = client.stdout.read().replace('command>', '')
            packets, output_lines = parse_output(outputstr)
            # Gets the player location.
            move_packet = None
            for packet in packets:
                if packet.__class__.__name__ == _EE122Reply.MoveNotify.__name__:
                    if packet.player_name_ == s_players[i]:
                        move_packet = packet
            if move_packet:
                locs[i] = move_packet
    except:
        pass
    # They now have to be in a reachable distance.
    dx = (locs[1].x_ - locs[0].x_ + EE122_X_SIZE) % EE122_X_SIZE
    dy = (locs[1].y_ - locs[0].y_ + EE122_Y_SIZE) % EE122_Y_SIZE
    if dx > 3 or dy > 3:
        raise RuntimeError('The players are not reachable.'
                           'player1 = (%d,%d), '
                           'player2 = (%d,%d)' % (locs[0].x_, locs[0].y_,
                                                  locs[1].x_, locs[1].y_))
    # Attack
    client1.stdin.write('attack %s\n' % s_players[1])
    sleep(s_time_allowance)

    # Parses the output packet.
    for client in clients:
        outputstr = client.stdout.read().replace('command>', '')
        packets, output_lines = parse_output(outputstr)
        count = 0
        for packet in packets:
            if packet.__class__.__name__ == _EE122Reply.AttackNotify.__name__:
                if (packet.attacker_name_ == s_players[0] and
                    packet.victim_name_ == s_players[1]):
                    count += 1
        if server.poll():
            raise RuntimeError('The server abnormally terminated.')
        if client1.poll() or client2.poll():
            raise RuntimeError('The client abnormally terminated.')
        if count != 1:
            raise RuntimeError('# ATTACK_NOTIFY is not 1. Got %d. '
                               'Client output: %s' % (count, outputstr))
    return True


def ee122_test_logout(server, clients):
    """Logout command test"""
    client1, client2 = clients
    # Injects commands.
    client1.stdin.write('login %s\n' % s_players[0])
    client2.stdin.write('login %s\n' % s_players[1])
    sleep(s_time_allowance)
    client1.stdin.write('logout\n')
    sleep(s_time_allowance)

    # At least the second client must receive LOGOUT_NOTIFY.
    # Parses the output packet.
    outputstr = client2.stdout.read().replace('command>', '')
    packets, output_lines = parse_output(outputstr)
    # We expect one one speak notify from the player 1.
    count = 0
    for packet in packets:
        if packet.__class__.__name__ == _EE122Reply.LogoutNotify.__name__:
            if packet.player_name_ == s_players[0]:
                count += 1
    if server.poll():
        raise RuntimeError('The server abnormally terminated.')
    if client2.poll():
        raise RuntimeError('The client2 abnormally terminated.')
    if count != 1:
        raise RuntimeError('# LOGOUT_NOTIFY is not 1. Got %d. '
                           'Client output: %s' % (count, outputstr))
    return True


def ee122_test_data_persistency(server, clients):
    """Player data persistency test (normal logout)"""
    client1, client2 = clients
    # Injects a command.
    client1.stdin.write('login %s\n' % s_players[0])
    client1.stdin.write('move north\n')
    sleep(s_time_allowance)
    # Parses the output packet.
    outputstr = client1.stdout.read().replace('command>', '')
    packets, output_lines = parse_output(outputstr)
    # We expect 2 MOVE_NOTIFY.
    move_packets = []
    for packet in packets:
        if (packet.__class__.__name__ == _EE122Reply.MoveNotify.__name__ and
            packet.player_name_ == s_players[0]):
            move_packets.append(packet)

    if server.poll():
        raise RuntimeError('The server abnormally terminated.')
    if len(move_packets) != 2:
        raise RuntimeError('# MOVE_NOTIFY is not 2. Got %d\n'
                           'Client output: %s' % (len(move_packets), outputstr))
    # Log out the client. Allows enough time for the server to clean up.          
    client1.stdin.write('logout\n')
    sleep(s_time_allowance * 5)
    if server.poll():
        raise RuntimeError('The server abnormally terminated.')
    # Log on again.
    client2.stdin.write('login %s\n' % s_players[0])
    sleep(s_time_allowance)
    # Parses the output packet.
    outputstr = client2.stdout.read().replace('command>', '')
    packets, output_lines = parse_output(outputstr)
    # We expect 1 MOVE_NOTIFY.
    move_packets2 = []
    for packet in packets:
        if (packet.__class__.__name__ == _EE122Reply.MoveNotify.__name__ and
            packet.player_name_ == s_players[0]):
            move_packets2.append(packet)
    if server.poll():
        raise RuntimeError('The server abnormally terminated.')
    if len(move_packets2) != 1:
        raise RuntimeError('# MOVE_NOTIFY is not 1. Got %d\n'
                           'Client output: %s' % (len(move_packets2), outputstr))
    if (move_packets2[0].x_ != move_packets[-1].x_ or
        move_packets2[0].y_ != move_packets[-1].y_):
        raise RuntimeError('Locations mistmatch: (%d,%d) vs (%d,%d)'
                           % (move_packets2[0].x_, move_packets2[0].y_,
                              move_packets[-1].x_, move_packets[-1].y_))
    return True


def ee122_test_data_persistency2(server, clients):
    """Player data persistency test (client crash)"""
    client1, client2 = clients
    # Injects a command.
    client1.stdin.write('login %s\n' % s_players[0])
    client1.stdin.write('move north\n')
    sleep(s_time_allowance)
    # Parses the output packet.
    outputstr = client1.stdout.read().replace('command>', '')
    packets, output_lines = parse_output(outputstr)
    # We expect 2 MOVE_NOTIFY.
    move_packets = []
    for packet in packets:
        if (packet.__class__.__name__ == _EE122Reply.MoveNotify.__name__ and
            packet.player_name_ == s_players[0]):
            move_packets.append(packet)

    if server.poll():
        raise RuntimeError('The server abnormally terminated.')
    if len(move_packets) != 2:
        raise RuntimeError('# MOVE_NOTIFY is not 2. Got %d\n'
                           'Client output: %s' % (len(move_packets), outputstr))
    # Kill the client. Allows enough time for the server to clean up.          
    kill_subproc([client1])
    sleep(s_time_allowance * 5)
    if server.poll():
        raise RuntimeError('The server abnormally terminated.')
    if not client1.poll():
        raise RuntimeError('The client did not die')
    # Log on again.
    client2.stdin.write('login %s\n' % s_players[0])
    sleep(s_time_allowance)
    # Parses the output packet.
    outputstr = client2.stdout.read().replace('command>', '')
    packets, output_lines = parse_output(outputstr)
    # We expect 1 MOVE_NOTIFY.
    move_packets2 = []
    for packet in packets:
        if (packet.__class__.__name__ == _EE122Reply.MoveNotify.__name__ and
            packet.player_name_ == s_players[0]):
            move_packets2.append(packet)
    if server.poll():
        raise RuntimeError('The server abnormally terminated.')
    if len(move_packets2) != 1:
        raise RuntimeError('# MOVE_NOTIFY is not 1. Got %d\n'
                           'Client output: %s' % (len(move_packets2), outputstr))
    if (move_packets2[0].x_ != move_packets[-1].x_ or
        move_packets2[0].y_ != move_packets[-1].y_):
        raise RuntimeError('Locations mistmatch: (%d,%d) vs (%d,%d)'
                           % (move_packets2[0].x_, move_packets2[0].y_,
                              move_packets[-1].x_, move_packets[-1].y_))
    return True


def ee122_test_fault_version(server, clients):
    """Exception test (invalid version)"""
    client = clients[0]
    # Injects a command.
    client.stdin.write('login %s\n' % s_players[0])
    sleep(s_time_allowance)
    client.stdin.write('enable version\n')
    client.stdin.write('speak must_not_be_shown.\n')
    sleep(s_time_allowance)
    # Parses the output packet.
    outputstr = client.stdout.read().replace('command>', '')
    packets, output_lines = parse_output(outputstr)
    # We must not see the speak message.
    found = False
    for packet in packets:
        if packet.__class__.__name__ == _EE122Reply.SpeakNotify.__name__:
            raise RuntimeError('Got speak message despite an invalid version')

    if not MALFORMED_MESSAGE_TEXT in outputstr:
        raise RuntimeError('The server did not disconnect the client')
    if server.poll():
        raise RuntimeError('The server abnormally terminated.')
    return True

def ee122_test_fault_length(server, clients):
    """Exception test (invalid length)"""
    client = clients[0]
    # Injects a command.
    client.stdin.write('login %s\n' % s_players[0])
    sleep(s_time_allowance)
    client.stdin.write('enable length\n')
    client.stdin.write('speak must_not_be_shown.\n')
    sleep(s_time_allowance)
    # Parses the output packet.
    outputstr = client.stdout.read().replace('command>', '')
    packets, output_lines = parse_output(outputstr)
    # We must not see the speak message.
    found = False
    for packet in packets:
        if packet.__class__.__name__ == _EE122Reply.SpeakNotify.__name__:
            raise RuntimeError('Got speak message despite an invalid length')

    if not MALFORMED_MESSAGE_TEXT in outputstr:
        raise RuntimeError('The server did not disconnect the client')
    if server.poll():
        raise RuntimeError('The server abnormally terminated.')
    return True


def ee122_test_fault_message_type(server, clients):
    """Exception test (invalid msg type)"""
    client = clients[0]
    # Injects a command.
    client.stdin.write('login %s\n' % s_players[0])
    sleep(s_time_allowance)
    client.stdin.write('enable length\n')
    client.stdin.write('speak must_not_be_shown.\n')
    sleep(s_time_allowance)
    # Parses the output packet.
    outputstr = client.stdout.read().replace('command>', '')
    packets, output_lines = parse_output(outputstr)

    if not MALFORMED_MESSAGE_TEXT in outputstr:
        raise RuntimeError('The server did not disconnect the client')
    if server.poll():
        raise RuntimeError('The server abnormally terminated.')
    return True


def ee122_test_fault_direction(server, clients):
    """Exception test (invalid move direction)"""
    client = clients[0]
    # Injects a command.
    client.stdin.write('login %s\n' % s_players[0])
    sleep(s_time_allowance)
    client.stdin.write('enable direction\n')
    client.stdin.write('move north\n')
    sleep(s_time_allowance)
    # Parses the output packet.
    outputstr = client.stdout.read().replace('command>', '')
    packets, output_lines = parse_output(outputstr)
    # We must not see the speak message.
    count = 0
    for packet in packets:
        if (packet.__class__.__name__ == _EE122Reply.MoveNotify.__name__ and
            packet.player_name_ == s_players[0]):
            count += 1

    if not MALFORMED_MESSAGE_TEXT in outputstr:
        raise RuntimeError('The server did not disconnect the client. '
                           'Client output: %s' % outputstr)
    if server.poll():
        raise RuntimeError('The server abnormally terminated.')
    if count != 1:
        raise RuntimeError('# MOVE_NOTIFY is not 1. Got %d. '
                           'Client output: %s' % (count, outputstr))
    return True


def ee122_test_fault_player_name(server, clients):
    """Exception test (invalid player name)"""
    client = clients[0]
    # Injects a command.
    client.stdin.write('enable player_name\n')
    client.stdin.write('login %s\n' % s_players[0])
    sleep(s_time_allowance)
    # Parses the output packet.
    outputstr = client.stdout.read().replace('command>', '')
    packets, output_lines = parse_output(outputstr)
    if not MALFORMED_MESSAGE_TEXT in outputstr:
        raise RuntimeError('The server did not disconnect the client. '
                           'Client output: %s' % outputstr)
    if server.poll():
        raise RuntimeError('The server abnormally terminated.')
    # We must not see any message.
    if len(packets):
        raise RuntimeError('# packets is not 0. Got %d. '
                           'Client output: %s' % (len(packets), outputstr))
    return True


def ee122_test_fault_speak_message(server, clients):
    """Exception test (invalid speak message)"""
    client = clients[0]
    # Injects a command.
    client.stdin.write('login %s\n' % s_players[0])
    sleep(s_time_allowance)
    client.stdin.write('enable speak\n')
    client.stdin.write('speak must_not_show\n')
    sleep(s_time_allowance)
    # Parses the output packet.
    outputstr = client.stdout.read().replace('command>', '')
    packets, output_lines = parse_output(outputstr)
    # We must not see the speak message.
    count = 0
    for packet in packets:
        if packet.__class__.__name__ == _EE122Reply.SpeakNotify.__name__:
            count += 1

    if not MALFORMED_MESSAGE_TEXT in outputstr:
        raise RuntimeError('The server did not disconnect the client. '
                           'Client output: %s' % outputstr)
    if server.poll():
        raise RuntimeError('The server abnormally terminated.')
    if count != 0:
        raise RuntimeError('# SPEAK_NOTIFY is not 0. Got %d. '
                           'Client output: %s' % (count, outputstr))
    return True


def ee122_test_fault_slow_link(server, clients):
    """Exception test (emulating a slow link)"""
    client = clients[0]
    # Injects a command.
    client.stdin.write('login %s\n' % s_players[0])
    sleep(s_time_allowance)
    client.stdin.write('enable slow\n')
    client.stdin.write('speak %s\n' % ('1234567890' * 5))
    sleep(s_time_allowance * 5)
    # Parses the output packet.
    outputstr = client.stdout.read().replace('command>', '')
    packets, output_lines = parse_output(outputstr)
    # We must not see the speak message.
    count = 0
    for packet in packets:
        if packet.__class__.__name__ == _EE122Reply.SpeakNotify.__name__:
            count += 1

    if client.poll():
        raise RuntimeError('The client abnormally terminated.')
    if server.poll():
        raise RuntimeError('The server abnormally terminated.')
    if count != 1:
        raise RuntimeError('# SPEAK_NOTIFY is not 1. Got %d. '
                           'Client output: %s' % (count, outputstr))
    return True


def ee122_test_fault_truncated_last_packet(server, clients):
    """Exception test (truncated last packet)"""
    client1, client2 = clients
    # Injects commands.
    client2.stdin.write('login %s\n' % s_players[1])
    sleep(s_time_allowance)
    client1.stdin.write('login %s\n' % s_players[0])
    sleep(s_time_allowance)
    client1.stdin.write('speak this will be shown\n')
    client1.stdin.write('enable abnormal\n')
    client1.stdin.write('speak this must not be shown\n')
    sleep(s_time_allowance * 5)

    # The second client must receive LOGOUT_NOTIFY.
    # Parses the output packet.
    outputstr = client2.stdout.read().replace('command>', '')
    packets, output_lines = parse_output(outputstr)
    # We expect one one speak notify from the player 1.
    count = 0
    for packet in packets:
        if packet.__class__.__name__ == _EE122Reply.LogoutNotify.__name__:
            if packet.player_name_ == s_players[0]:
                count += 1
    if server.poll():
        raise RuntimeError('The server abnormally terminated.')
    if client2.poll():
        raise RuntimeError('The client2 abnormally terminated.')
    if count != 1:
        raise RuntimeError('# LOGOUT_NOTIFY is not 1. Got %d. '
                           'Client output: %s' % (count, outputstr))
    return True


def ee122_test_multi_clients(server, clients):
    assert(len(s_players) > len(clients))
    message = '1234567890 ' * 10
    # Injects commands.
    for i in range(len(clients)):
        client = clients[i]
        client.stdin.write('login %s\n' % s_players[i])
    sleep(s_time_allowance * 5)
    # Each player speaks at the same time. We must catch all the messages.
    for client in clients:
        client.stdin.write('speak %s\n' % message)
    sleep(s_time_allowance * 5)

    for client in clients:
        # Parses the output packet.
        outputstr = '' 
        while True:
            try:
                str = client.stdout.read().replace('command>', '')
                if not str: break
                else: outputstr += str
            except:
                break
        packets, output_lines = parse_output(outputstr)
        # We expect one one speak notify from the player 1.
        count = 0
        for packet in packets:
            if packet.__class__.__name__ == _EE122Reply.SpeakNotify.__name__:
                count += 1
        if server.poll():
            raise RuntimeError('The server abnormally terminated.')
        if client.poll():
            raise RuntimeError('The client abnormally terminated.')
        if count != len(clients):
            raise RuntimeError('# SPEAK_NOTIFY is not %d. Got %d. '
                               'Client output: %s' % (len(clients),
                                                      count, outputstr))
    return True


def ee122_test_multi_clients5(server, clients):
    """Performance test (speak messages by 5 clients)"""
    return ee122_test_multi_clients(server, clients)


def ee122_test_multi_clients10(server, clients):
    """Performance test (speak messages by 10 clients)"""
    return ee122_test_multi_clients(server, clients)


def ee122_test_multi_clients20(server, clients):
    """Performance test (speak messages by 20 clients)"""
    return ee122_test_multi_clients(server, clients)



if __name__ == '__main__':
    print '* This script is for Project 1B.'
    print '* Version:', _version_, ' for final grading!'
    print

    # Checks the binaries.
    check_client_binary()
    check_server_binary()

    print '* Using server server port: ', s_port
    print '* Time allowance for most tests is %f' % s_time_allowance
    print '* Starting tests...'

    server_ip_addr = socket.getaddrinfo(socket.gethostname(), None)[0][4][0]
    print '* IP address of the NIC: ', server_ip_addr

    COUNT, EARNED, MAX = range(3)
    points = {}
    points[TEST_TYPE_FUNCTION] = [0, 0, 0]
    points[TEST_TYPE_EXCEPTION] = [0, 0, 0]
    points[TEST_TYPE_PERFORMANCE] = [0, 0, 0]

    tests = [
              (ee122_test_connection, TEST_TYPE_FUNCTION, 10, 1),
              (ee122_test_login, TEST_TYPE_FUNCTION, 10, 1),
              (ee122_test_invalid_state, TEST_TYPE_FUNCTION, 10, 1),
              (ee122_test_invalid_state2, TEST_TYPE_FUNCTION, 10, 1),
              (ee122_test_duplicate_login, TEST_TYPE_FUNCTION, 10, 2),
              (ee122_test_move, TEST_TYPE_FUNCTION, 10, 2),
              (ee122_test_hp_regen, TEST_TYPE_FUNCTION, 10, 1),
              (ee122_test_speak_101bytes, TEST_TYPE_FUNCTION, 10, 2),
              (ee122_test_speak_255bytes, TEST_TYPE_FUNCTION, 10, 2),
              (ee122_test_attack, TEST_TYPE_FUNCTION, 10, 2),
              (ee122_test_logout, TEST_TYPE_FUNCTION, 10, 2),
              (ee122_test_data_persistency, TEST_TYPE_FUNCTION, 10, 2),
              (ee122_test_data_persistency2, TEST_TYPE_FUNCTION, 10, 2),
              (ee122_test_fault_version, TEST_TYPE_EXCEPTION, 10, 1),
              (ee122_test_fault_length, TEST_TYPE_EXCEPTION, 10, 1),
              (ee122_test_fault_message_type, TEST_TYPE_EXCEPTION, 10, 1),
              (ee122_test_fault_direction, TEST_TYPE_EXCEPTION, 10, 1),
              (ee122_test_fault_player_name, TEST_TYPE_EXCEPTION, 10, 1),
              (ee122_test_fault_speak_message, TEST_TYPE_EXCEPTION, 10, 1),
              (ee122_test_fault_slow_link, TEST_TYPE_EXCEPTION, 10, 1),
              (ee122_test_fault_truncated_last_packet, TEST_TYPE_EXCEPTION, 10, 2),
              (ee122_test_multi_clients5, TEST_TYPE_PERFORMANCE, 10, 5),
              (ee122_test_multi_clients10, TEST_TYPE_PERFORMANCE, 10, 10),
              (ee122_test_multi_clients20, TEST_TYPE_PERFORMANCE, 10, 20),
             ]
    tests_with_real_ip = [ee122_test_connection, ]

    for (test, type, max, num_clients) in tests:
        server, clients = None, []
        earned = 0
        try:
            server = launch_server()
            sleep(0.1)
            for i in range(num_clients):
                if test in tests_with_real_ip:
                    clients.append(launch_client(server_ip_addr))
                else:
                    clients.append(launch_client())
            try:
                if test(server, clients):
                    print '* %s: PASS' % test.__doc__
                    earned = max
                else:
                    print '! %s: FAIL' % test.__doc__
            except Exception, e:
                print '! %s: FAIL w/ an exception: ' % test.__doc__, e
        except Exception, e:
            print '! %s: FAIL: Failed to launch server and client(s): ' % test.__doc__, e

        try: kill_subproc([server])
        except: pass
        try: kill_subproc(clients)
        except: pass

        # To prevent launching another server before the old one dies.
        sleep(0.5)

        points[type][COUNT] += 1
        points[type][EARNED] += earned
        points[type][MAX] += max

    print '* Finished all the tests.'
    print 'Function tests (%d): %d/%d' % (points[TEST_TYPE_FUNCTION][COUNT],
                                          points[TEST_TYPE_FUNCTION][EARNED],
                                          points[TEST_TYPE_FUNCTION][MAX])
    print 'Exception tests (%d): %d/%d' % (points[TEST_TYPE_EXCEPTION][COUNT],
                                           points[TEST_TYPE_EXCEPTION][EARNED],
                                           points[TEST_TYPE_EXCEPTION][MAX])
    print 'Performance tests (%d): %d/%d' % (points[TEST_TYPE_PERFORMANCE][COUNT],
                                             points[TEST_TYPE_PERFORMANCE][EARNED],
                                             points[TEST_TYPE_PERFORMANCE][MAX]) 

    earned, max = 0, 0  
    for type in points:
      earned += points[type][EARNED]
      max += points[type][MAX]
    print 'Earned %d out of %d' % (earned, max)

    final = 100.0 * earned / max
    print 'Points: ', final

