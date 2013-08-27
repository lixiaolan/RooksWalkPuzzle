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
