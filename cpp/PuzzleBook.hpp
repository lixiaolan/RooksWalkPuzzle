#ifndef PUZZLE_BOOK
#define PUZZLE_BOOK

#include "PuzzleChapter.hpp"

#include <iostream>
#include <fstream>
#include <sstream>

class PuzzleBook {
public:
  friend ofstream &operator<<(ofstream&, PuzzleBook&);
  friend ifstream &operator>>(ifstream&, PuzzleBook&);
  void add(PuzzleChapter);
  void printXML(ofstream&);
  PuzzleBook(vector<string>,string, string, vector<string>, vector<string>, vector<string>, int);
private:
  int *puzzleIndex;
  int pi;
  string bookTitle;
  string bookStyle;
  vector<PuzzleChapter> chapters;
  vector<string> chapterTitles;
  vector<string> beforeImages;
  vector<string> afterImages;

  
  void loadChapters(vector<string>);  
};

ofstream &operator<<(ofstream&, PuzzleBook&);
ifstream &operator>>(ifstream&, PuzzleBook&);


#endif
