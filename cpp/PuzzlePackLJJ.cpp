#include "BeeLineLJJ.hpp"

#include <iostream>
#include <fstream>
#include <sstream>


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
  void plotToFile(ofstream &ofs);
  void printSoln();
  void buildXML(xml_document<> *doc, xml_node<> *levelpack);
private:
  vector<BeeLinePuzzle> puzzles;
};

void PuzzleChapter::add(int h, int w, int l, int hints) {
  BeeLinePuzzle BLP(h, w, l, hints);
  puzzles.push_back(BLP);
  return;
}

void PuzzleChapter::print() {
  for (int i = 0; i < puzzles.size(); i++) {
    cout << "Puzzle:" << (i+1) <<" length:" << puzzles[i].getLength() << endl;
    puzzles[i].printPuzzle();
    cout << endl;
    cout << endl;
  }
  return;
}

void PuzzleChapter::printSoln() {
  for (int i = 0; i < puzzles.size(); i++) {
    cout << "Puzzle:" << (i+1) <<" length:" << puzzles[i].getLength() << endl;
    puzzles[i].printSoln();
    cout << endl;
    cout << endl;
  }
  return;
}

void PuzzleChapter::clearAll() {
  puzzles.clear();
  return;
}

void PuzzleChapter::clear(int index) {
  puzzles.erase(puzzles.begin()+index);
  return;
}

void PuzzleChapter::pop_back() {
  puzzles.pop_back();
  return;
}

void PuzzleChapter::swap(int a, int b) {
  if ((max(a,b) > puzzles.size()) || (min(a,b) <= 0)) {
    cout << "Bad index" << endl;
  }
  else {
    BeeLinePuzzle BLP = puzzles[a-1];
    puzzles[a-1] = puzzles[b-1];
    puzzles[b-1] = BLP;
  }
  return;
}

void PuzzleChapter::plotToFile(ofstream &ofs) {
 
  for (int i = 0; i < puzzles.size(); i++) {
    ofs << "Puzzle: " << (i+1) << endl;
    puzzles[i].plotToFile(ofs);
    ofs << endl;
    ofs << endl;
  }
  return;
}

void PuzzleChapter::buildXML(xml_document<> *doc, xml_node<> *levelpack) {
  xml_node<> *chapter;
  xml_node<> *node;
  xml_attribute<> *attr;
  chapter = doc->allocate_node(node_element, "chapter");
  levelpack->append_node(chapter);
  attr = doc->allocate_attribute("title", "ChapterTitle");
  chapter->append_attribute(attr);
  node = doc->allocate_node(node_element, "width", "4");
  chapter->append_node(node);
  node = doc->allocate_node(node_element, "height", "4");
  chapter->append_node(node);
  for (BeeLinePuzzle puz : puzzles) {
    puz.buildXML(doc, chapter);
  }
}

ofstream &operator<<(ofstream &ofs, PuzzleChapter &PP) {
  for (int i = 0; i < PP.puzzles.size(); i++) {
    ofs << PP.puzzles[i];
  }
  return ofs;
}

ifstream &operator>>(ifstream &ifs, PuzzleChapter &PP) {
  PP.puzzles.clear();

  while (true) {
    BeeLinePuzzle BLP;
    if (ifs >> BLP) {
      PP.puzzles.push_back(BLP);
    }
    else {
      break;
    }
  }
  return ifs;
}

class PuzzleBook {
public:
  friend ofstream &operator<<(ofstream&, PuzzleBook&);
  friend ifstream &operator>>(ifstream&, PuzzleBook&);
  void add(PuzzleChapter);
  void printXML(ofstream&);
  PuzzleBook(vector<string>);
private:
  vector<PuzzleChapter> chapters;
  void loadChapters(vector<string>);  
};

ofstream &operator<<(ofstream &ofs, PuzzleBook &PB) {
  for (int i = 0; i < PB.chapters.size(); i++) {
    ofs << PB.chapters[i];
  }
  return ofs;
}

ifstream &operator>>(ifstream &ifs, PuzzleBook &PB) {
  PB.chapters.clear();

  while (true) {
    PuzzleChapter PC;
    if (ifs >> PC) {
      PB.chapters.push_back(PC);
    }
    else {
      break;
    }
  }
  return ifs;
}

void PuzzleBook::printXML(ofstream& ofs) {

  xml_document<> d;
  xml_document<> *doc = &d;
  xml_node<> *node;
  xml_node<> *levelpack;
  xml_node<> *chapter;
  xml_node<> *puzzle;
  xml_node<> *hint;
  xml_attribute<> *attr;

  levelpack = doc->allocate_node(node_element, "levelpack");
  doc->append_node(levelpack);
  node = doc->allocate_node(node_element, "title", "SomeTitle");
  levelpack->append_node(node);
  
  for (auto ch : chapters) {
    ch.buildXML(doc, levelpack);
  }

  ofs << *doc;
  return;
}

void PuzzleBook::add(PuzzleChapter PC) {
  chapters.push_back(PC);
}

void PuzzleBook::loadChapters(vector<string> files) {
  ifstream ifs;
  PuzzleChapter PC;
  chapters.clear();
  for (int i = 0; i < files.size(); i++) {
    ifs.open(files[i]);
    ifs >> PC;
    ifs.close();
    chapters.push_back(PC);
  }
  return;
}

PuzzleBook::PuzzleBook(vector<string> files) {
  loadChapters(files);
}

int main(int argc, char *argv[]){

  ofstream ofs;
  ifstream ifs;
  stringstream ss;

  for (int i = 0; i < argc; i++) {
    ss << argv[i] << " ";
  }
  // string sss;
  // while (ss >> sss) {
  //   cout << sss << endl;
  // }
  // return 0;

  PuzzleChapter PP;
  ifs.open("iotest.txt");
  ifs >> PP;
  ifs.close();
  
  string str;
  ss >> str;
  ss >> str;
  
  if (str == "add") {
    int a, b, c, d;
    ss >> a;
    ss >> b;
    ss >> c;
    ss >> d;
    PP.add(a, b, c, d);
  }

  else if (str == "clear") {
    int a;
    ss >> a;
    PP.clear(a-1);
  }

  else if (str == "clear_all") {
    PP.clearAll();
  }
        
  else if (str == "print") {
    PP.print();
  }

  else if (str == "pop_back") {
    PP.pop_back();
  }
  
  else if (str == "swap") {
    int a, b;
    ss >> a;
    ss >> b;
    PP.swap(a,b);
  }

  else if (str == "plot") {
    ofstream t;
    t.open("plot.txt");
    PP.plotToFile(t);
    t.close();
  }

  else if (str == "printSoln") {
    PP.printSoln();
  }

  else if (str == "printXML") {
    vector<string> vs;
    vs.push_back("BookRandChapterOne.txt");
    vs.push_back("BookRandChapterTwo.txt");
    PuzzleBook PB = PuzzleBook(vs);
    
    ofs.open("testXML.xml");
    PB.printXML(ofs);
    ofs.close();
  }

  else {
    cout << "command not recognized!!!" << endl;
  }

  ofs.open("iotest.txt");
  ofs << PP;
  ofs.close();

  return 0;
}
