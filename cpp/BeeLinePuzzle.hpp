#ifndef BEE_LINE_PUZZLE
#define BEE_LINE_PUZZLE

#include "rapidxml.hpp"
#include "rapidxml_print.hpp"

#include "Pos.hpp"
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


using namespace rapidxml;
using namespace std;

string intTooString(int in);

class BeeLinePuzzle {
private:
  friend ofstream &operator<<(ofstream &, BeeLinePuzzle &);
  friend ifstream &operator>>(ifstream &, BeeLinePuzzle &);
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
  vector<int> hintsIndex;
  vector<bool> hintsVertical;
  vector<bool> hintsLeftUp;
  
  void getHints(int);
  bool checkUnique();
  
  vector<pos> legalMoves();
  vector<pos> legalMovesTestUnique();
  bool goodDir(pos, int, bool, bool);
  bool goodDirTestUnique(pos, int, bool, bool);
  bool makeBoard(int);
  void countAllSolutions();
  bool isUnique();
  bool goodPlay(pos, pos);
  bool goodPlayTestUnique(pos, pos);
  void markUnused();
  void clearBoolMats();
  
  void setDir(pos, pos);
  void markLRUD(pos, pos);
  void unMarkLRUD(pos, pos);
  bool boardIsFull();
  
  
  void buildBoard();
  
public:
  BeeLinePuzzle(int, int ,int, int);
  BeeLinePuzzle();
  void print();
  void printSoln();
  string printUnique();
  void printPuzzle();
  int uniqueCounter;
  void plotToFile(ofstream &ofs);
  int getLength();
  void buildXML(xml_document<> *doc, xml_node<> *chapter, int*);
  string getHintDir(int i);
  string getBoardXML();
  string getPathXML();
};

ifstream &operator>>(ifstream &, BeeLinePuzzle &);

ofstream &operator<<(ofstream &, BeeLinePuzzle &);  

void printBool(vector<vector<bool> > &in);

int lengthOfMove(pos);

#endif
