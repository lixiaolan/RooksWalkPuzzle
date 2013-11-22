#ifndef PUZZLE_CHAPTER
#define PUZZLE_CHAPTER

#include "BeeLinePuzzle.hpp"

#include <iostream>
#include <fstream>
#include <sstream>
#include <vector>

class PuzzleChapter {
public:
  friend ofstream &operator<<(ofstream&, PuzzleChapter&);
  friend ifstream &operator>>(ifstream&, PuzzleChapter&);
  void print();
  void add(int, int, int, int);
  void clearAll();

  void clear(int index);
  void pop_back();
  void swap(int a, int b);
  void add_hint(int p, int r, int c);
  void clear_hint(int p, int r, int c);
  bool test_unique(int p);

  void plotToFile(ofstream &ofs);
  void printSoln();
  void buildXML(xml_document<> *doc, xml_node<> *levelpack, string title, string endText, string beforeImage, vector<string> afterImage, string beforeFlower, string afterFlower, int*);
private:
  vector<BeeLinePuzzle> puzzles;
};

ofstream &operator<<(ofstream&, PuzzleChapter&);

ifstream &operator>>(ifstream&, PuzzleChapter&);

#endif
