This is my project folder for the Rook's Walke puzzle. This is intented to eventually be ported onto Android, but for now, I need the basic functionality that my Matlab code had before.

Outline of design structure:

Since at the moment, I don't know exactly what rules the final product is likely to have, I would like to start with a base puzzle class that I can effectively build off of later if I decide to modify the rules to make a different puzzle. Also, within any given puzzle, there will be different difficulties which have different rules defined so using some base classes may be useful for that also...

Board
Holds the board info. Should be a template so that size can be changed. It should have a base that knows how to print itself, but does not know how to makeBoard. The makeBoard will create the board with the needed hints and the desired size. Ok, I am ok wit this

RooksWalk:

Should be a template that creates a board of the desired size... But should not derive from a base for the time beaing.



asdl;flkasjdf
asdfl;askjdf;lskad
