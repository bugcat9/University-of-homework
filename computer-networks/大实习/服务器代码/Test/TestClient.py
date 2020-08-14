from Client.client import client

def main():
    cli = client("127.0.0.1",58)
    cli.do_login("ttt")

if __name__ == '__main__':
        main()