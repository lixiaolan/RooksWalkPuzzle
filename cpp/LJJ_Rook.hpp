#ifndef LJJ_Rook
#define LJJ_Rook

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

class RooksWalk{
private:
  shared_ptr<RookBoard> board;
  void print() const;
  //  void export() const;
  RooksWalk(shared_ptr<RookBoard > rb): board(rb) {};
};

class RookBoard {
private:
  friend class RooksWalk;
  friend ofstream &operator<<(ofstream &, RookBoard &);
  int height;
  int width;
  int length;
  vector< vector<int> > moveArea;
  vector<int> rowSums;
  vector<int> colSums;
  vector<pos> positions;
  
  void reorderLegalMoves(vector<pos> &);
  vector<pos> legalMoves();
  bool makeBoard(int);
  bool goodPlay(pos, int);
  void markUnused();
public:
  RookBoard(int, int ,int);
  void print();
};

ofstream &operator<<(ofstream &, RookBoard &);  

#endif
