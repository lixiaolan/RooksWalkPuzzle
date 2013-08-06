#Define the compile command to be used:
CC=x86_64-apple-darwin10.8.0-g++ -std=c++11
#Define the flags to be used with the compile statment:
CFLAGS = -g -Wall
#Define the varios groups of .h and .o files used in compilation:
DEPS = LJJ_Rook.hpp
PROG = test.o LJJ_Rook.o

#Define the rules in the dependancy tree:

%.o: %.cpp $(DEPS)
	$(CC) -c -o $@ $< $(CFLAGS)

test: $(PROG)
	$(CC) -o $@ $^ $(CFLAGS)
