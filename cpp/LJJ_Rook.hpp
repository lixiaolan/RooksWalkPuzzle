#ifndef LJJ_Rook
#define LJJ_Rook

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

class RookBoard;

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

class RookBoard {
private:
  friend class RooksWalk;
  friend ofstream &operator<<(ofstream &, RookBoard &);
  int height;
  int width;
  int length;
  vector< vector<int> > moveArea;
  vector< vector<bool> > leftRight;
  vector< vector<bool> > upDown;

  vector< vector<bool> > vertical;
  vector< vector<bool> > leftUp;

  vector<int> rowSums;
  vector<int> colSums;
  vector<pos> positions;
  
  void reorderLegalMoves(vector<pos> &);
  void sortLegalMoves(pos, vector<pos> &);

  vector<pos> legalMoves();
  vector<pos> legalMovesRightAngles();
  vector<pos> legalMovesRightAnglesAnyNum();
  vector<pos> legalMovesRightAnglesAnyNumNoPassOver();
  vector<pos> legalMovesNoPassOver();
  vector<pos> legalMovesNoPassOverNoPoint();

  bool goodDir(pos, int, bool, bool);

  bool makeBoard(int);
  bool makeBoardRightAnglesNoPassOver(int);
  bool makeBoardNoPassOver(int);
  bool goodPlay(pos, int);
  bool goodPlayAnyNum(pos, int);
  bool goodPlayProperDist(pos, int);
  bool goodPlayNoPoint(pos, pos);
  void markUnused();

  void setDir(pos, pos);
  void markLRUD(pos, pos);
  void unMarkLRUD(pos, pos);
  

public:
  RookBoard(int, int ,int);
  string print();
};

ofstream &operator<<(ofstream &, RookBoard &);  

void printBool(vector<vector<bool> > &in);

int lengthOfMove(pos);

#endif
