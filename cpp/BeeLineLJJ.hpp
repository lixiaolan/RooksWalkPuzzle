#ifndef BeeLineLJJ
#define BeeLineLJJ

#include <string.h>
#include <jni.h>
#include <iostream>
#include <fstream>
#include <sstream>
#include <memory>
#include <set>
#include <vector>
#include <algorithm> 
#include <cmath>
#include <ctime>
#include <cstdlib> 
#include <string>

using namespace std;

class pos {
public:
  int r;
  int c;
  pos() : r(0), c(0) {};
  pos(int x, int y) : r(x), c(y) {};
  void print() {
    cout << "(" << r << "," << c << ")";
  }
};

pos operator-(pos left, pos right); 

pos operator+(pos left, pos right);

bool operator==(pos left, pos right);

template <typename T>
class PosMatrix {
private:
  vector< vector <T> > matrix;
public:
  T &operator()(pos p) {
    return matrix[p.r][p.c];
  }
  PosMatrix(vector< vector <T> > in): matrix(in) {};
};

class BeeLinePuzzle {
private:
  friend ofstream &operator<<(ofstream &, BeeLinePuzzle &);
  int height;
  int width;
  int length;

  vector< vector<int> > moveArea;
  vector< vector<int> > gameBoard;

  vector< vector<bool> > leftRight;
  vector< vector<bool> > upDown;
  vector< vector<bool> > vertical;
  vector< vector<bool> > leftUp;
  vector<pos> positions;
  vector<pos> tPosVec;

  vector<pos> hintsPos;
  vector<int> hintsNum;
  vector<bool> hintsVertical;
  vector<bool> hintsLeftUp;
  
  void getHints(int);
  void checkUnique();

  vector<pos> legalMoves();
  vector<pos> legalMovesTestUnique();
  bool goodDir(pos, int, bool, bool);
  bool goodDirTestUnique(pos, int, bool, bool);
  bool makeBoard(int);
  void countAllSolutions();
  bool goodPlay(pos, pos);
  bool goodPlayTestUnique(pos, pos);
  void markUnused();
  void clearBoolMats();

  void setDir(pos, pos);
  void markLRUD(pos, pos);
  void unMarkLRUD(pos, pos);
  bool boardIsFull();

public:
  BeeLinePuzzle(int, int ,int, int);
  string print();
  string printUnique();
  string printPuzzle();
  int uniqueCounter;
};

ofstream &operator<<(ofstream &, BeeLinePuzzle &);  

void printBool(vector<vector<bool> > &in);

int lengthOfMove(pos);

#endif
