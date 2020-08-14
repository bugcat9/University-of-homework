#Tkinter is the module we will use to create the GUI.
import tkinter

#Your app is a subclass of the Tkinter class Frame.
class MyApp(tkinter.Frame):

    #Constructor for our app.  The master parameter represents the root object
    #that will hold out application.  It is basically a window, with our application
    #existing in a frame inside that window.
    def __init__(self, master):
        #Call the constructor of the Frame superclass.  The pad options create padding
        #between the edge of the Frame and the Widgits inside.
        tkinter.Frame.__init__(self, master, padx=10, pady=10)
        #Give our window a title by calling the .title() method on the master object.
        master.title("Sample Application")
        #Set a minimum size through the master object, just to make our UI a little
        #nicer to look at.
        master.minsize(width=250, height=100)
        #.pack() is a necessary method to get our app ready to be displayed.  Packing
        #objects puts them in a simple column.  For a more complex way to arrange your
        #widgets, see .grid():
        #http://effbot.org/tkinterbook/grid.htm
        self.pack()

        #Now let's make some buttons using the Tkinter class Button.  The "text" parameter
        #indicates the text to be displayed in the button and the "command" parameter
        #specifies a procedure to execute if the button is clicked.  In this app, we will
        #have buttons that increase and decrease a variable.
        self.upButton = tkinter.Button(self, text="friend1", command=self.increment)
        self.upButton.pack() #Individual objects also must be packed to appear.
        self.downButton = tkinter.Button(self, text="friend2", command=self.decrement)
        self.downButton.pack()
        self.quitButton1 = tkinter.Button(self, text="friend3", command=self.create_quit_window)
        self.quitButton1.pack()
        self.quitButton2 = tkinter.Button(self, text="friend4", command=self.create_quit_window)
        self.quitButton2.pack()
        self.quitButton3 = tkinter.Button(self, text="friend5", command=self.create_quit_window)
        self.quitButton3.pack()
        self.quitButton4 = tkinter.Button(self, text="friend6", command=self.create_quit_window)
        self.quitButton4.pack()
        self.quitButton11 = tkinter.Button(self, text="friend7", command=self.create_quit_window)
        self.quitButton11.pack()
        self.quitButton21 = tkinter.Button(self, text="friend8", command=self.create_quit_window)
        self.quitButton21.pack()
        self.quitButton31 = tkinter.Button(self, text="friend9", command=self.create_quit_window)
        self.quitButton31.pack()
        self.quitButton41 = tkinter.Button(self, text="friendA", command=self.create_quit_window)
        self.quitButton41.pack()

        #The variable that we'll be incrementing and decrementing.
        self.value = 0
        #When you want to integrate a variable with your Widgets (eg buttons, labels, etc),
        #you make it a special type of Tkinter variable.  In this case, a StringVar.  There
        #is also IntVar, DoubleVar, and BooleanVar.  These are essentially mutable versions
        #of primitive types.  If we assign a normal string varaible to be the text of a button,
        #then change that string variable, the text of the button would be unchanged.  If we
        #instead use a StringVar, the button text will update automatically.  You'll see!
        self.value_str = tkinter.StringVar()
        self.value_str.set("0") #You set all Tkinter variable objects with the .set() method.

        #A Label to display our value.  Labels are like buttons except with no click effect.
        #Note that we use the textvariable parameter instead of text so that the text on
        #this label will automatically update with our StringVar.
        self.valueLabel = tkinter.Label(self, textvariable=self.value_str)
        self.valueLabel.pack()

        #Lastly, a quit button, which will call the .create_quit_window() method defined below,
        #which displays a new window asking whether the user want's to quit.
        self.quitButton = tkinter.Button(self, text="Quit", command=self.create_quit_window)
        self.quitButton.pack()

    #Methods that will be called when the up and down buttons are pressed.
    def increment(self):
        self.value += 1
        #When we reset the value of the StringVar, the text on valueLabel will change!
        self.value_str.set(str(self.value))

    def decrement(self):
        self.value -= 1
        self.value_str.set(str(self.value))

    #This method creates a new window (which will be a child of the master of our frame,
    #not of our frame itself).  The quit window will ask the user if they really want to quit.
    #If the user clicks yes, the application will close.  If they say no, the quit window
    #will close.
    def create_quit_window(self):
        #The Toplevel class makes a window.  It's simpler than the Frame class.  We will make
        #it a child of our application's master object, but since it is a Toplevel object, it
        #will create a whole new window rather than one that is part of the application window.
        quit_window = tkinter.Toplevel(self.master)
        #Give our quit window a title and minimum size.
        quit_window.title("Quit?")
        quit_window.minsize(width=150, height=50)
        #Display a message to the user asking if they want to quit.
        quit_label = tkinter.Label(quit_window, text="Are you sure you want to quit?")
        quit_label.pack()
        #We give our window a yes and no button.  One quits the application and one quits
        #the window.
        yes_button = tkinter.Button(quit_window, text="Yes", command=self.quit)
        yes_button.pack()
        no_button = tkinter.Button(quit_window, text="No", command=quit_window.destroy)
        no_button.pack()


#We make a Tk object to serve as the root of our interface.  We're making
#something to use as an argument for the master parameter, to be the "parent"
#of our frame.  We can use it to do things like set the size.  Other than
#that, you don't have to worry too much about this step.
root = tkinter.Tk()
#Initialize our app object and run the Frame method .mainloop() to begin!
#You should see a small window with up and down buttons, a label displaying
#a number, and a quit button when you run this program.
app = MyApp(root)
app.mainloop()
