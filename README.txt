Running the code:

1.) To compile the cpp, type make inside of the cpp directory. You may need to modify the CPP var inside the makefile for your specific compiler.

2.) To compile the android project (assuming you already have the android SDK configured), simply type ``ant debug'' inside of the android folder as you would with any other android project. Currently, anroid NDK is not requried since the cpp work is unrealted to the android stuff at the moment.

Other Notes:

Output format for the cpp code:
The format of output file (currently "iotest.txt") is the following:

<n # of rows>
<m # of cols>
<The numbers contained in the puzzle board listed by row first (n*m numbers). The format is: (numbers > 0) stand for numbers in the board, 0 stands for blank white square, -1 stands for black squares>
<l # of positions>
<2*l numbers which are the l positions in the board given by x,y cooridnates>




#First release min content/fixes:

1.) Tutorial ``slide show'' (see the Nikoli site).
    Make the ``tutorial'' a self learning process as much as possible.
2.) Rework the interface (Tam's fat fingers could not handle the swiping).
    Make and ``active tile'' before input.
3.) Clever system for caching puzzles.
    You can only save one puzzle.
4.) A difficulty rating system.
    Lenghts + hints.
5.) Re-structure the model.
7.) Add the bee flower theme.
6.) Cosmetic changes:
    a. Need rotating tiles in the background spaced out and there is a bee dancing!
    b. Tiles need a pattened texture on the back.
    c. Need a cute animation for getting a puzzle correct.
    d. perhaps change the ``grey circles''
    e. Optional: Add ability to track path.
    f. Optional: Add extra display option (red arrows underneath numbers).
    g. Optional: create an animation which follows the path of the solution.

7.) Optional: Daily puzzle site needs to be up.

8.) Think of a fucking name!
9.) display the correct dimensions for any display.
10.)Look into the legal side of protecting the puzzle concept.
11.)Optional: Sound effects.
12.)Basic marketing strategy.

Lets have a first release by October 1. Two weeks to alpha.


#Revised first releas content/fixes

1.)  Tutorial!!
2.)  Interface: swipe sensitivity needs to go down.
3.)  Parceling!
4.)  Different difficulties which load different boards!
5.)
     a. Get artwork from Nathan
     b. Change the color of circles 
     c. Change the font of the numbers
     e. Optional: ability to track path.
6.)  Look into the legal side of protecting the puzzle concept.
7.)  Optional: Sound effects.
8.)  Basic marketing strategy.
9.)  Restructure touched!
10.) Make the menus version dependant.
11.) Swipe sensitivity turned down.
12.) Hints or no hints.
13.) States system.
