#ifndef PUZZLE_BOOK
#define PUZZLE_BOOK

#include "PuzzleChapter.hpp"

#include <iostream>
#include <fstream>
#include <sstream>
#include <map>
class PuzzleBook {
public:
  friend ofstream &operator<<(ofstream&, PuzzleBook&);
  friend ifstream &operator>>(ifstream&, PuzzleBook&);
  void add(PuzzleChapter);
  void printXML(ofstream&);
  PuzzleBook(vector<string>,string, string, vector<string>, vector<string>, vector<string>, vector< vector <string> >, vector<string>, vector<string>, int);
  
  PuzzleBook(vector<string>,string, string, vector<string>, vector<string>, vector<string>, vector< vector <string> >, vector<string>, vector<string>,map<int, string>, int);

private:
  int *puzzleIndex;
  int pi;
  map<int, string> textMap;
  string bookTitle;
  string bookStyle;
  vector<PuzzleChapter> chapters;
  vector<string> chapterTitles;
  vector<string> chapterEndText;
  vector<string> beforeImages;
  vector< vector<string> > afterImages;
  vector<string> beforeFlower;
  vector<string> afterFlower;

  
  void loadChapters(vector<string>);  
};

ofstream &operator<<(ofstream&, PuzzleBook&);
ifstream &operator>>(ifstream&, PuzzleBook&);


#endif
