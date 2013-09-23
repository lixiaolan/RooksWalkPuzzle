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

// class RooksWalk{
// private:
//   shared_ptr<RookBoard> board;
//   void print() const;
//   //  void export() const;
//   RooksWalk(shared_ptr<RookBoard > rb): board(rb) {};
// };

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
  vector<int> rowSums;
  vector<int> colSums;
  vector<pos> positions;
  
  void reorderLegalMoves(vector<pos> &);
  vector<pos> legalMoves();
  vector<pos> legalMovesRightAngles();
  vector<pos> legalMovesRightAnglesAnyNum();
  vector<pos> legalMovesRightAnglesAnyNumNoPassOver();

  bool makeBoard(int);
  bool makeBoardRightAnglesNoPassOver(int);
  bool goodPlay(pos, int);
  bool goodPlayAnyNum(pos, int);
  void markUnused();

  void markLRUD(pos, pos);
  void unMarkLRUD(pos, pos);
  

public:
  RookBoard(int, int ,int);
  string print();
};

ofstream &operator<<(ofstream &, RookBoard &);  

void printBool(vector<vector<bool> > &in);

#endif
