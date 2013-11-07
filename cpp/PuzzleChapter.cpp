#include "PuzzleChapter.hpp"

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

void PuzzleChapter::buildXML(xml_document<> *doc, xml_node<> *levelpack, string title, string beforeImage, string afterImage,string beforeFlower, string afterFlower, int *puzzleIndex) {
  xml_node<> *chapter;
  xml_node<> *node;
  xml_attribute<> *attr;
  char *name;

  chapter = doc->allocate_node(node_element, "chapter");
  levelpack->append_node(chapter);
  name = doc->allocate_string(title.c_str());
  attr = doc->allocate_attribute("title", name);
  chapter->append_attribute(attr);
  name = doc->allocate_string(beforeImage.c_str());
  attr = doc->allocate_attribute("before_image", name);
  chapter->append_attribute(attr);
  name = doc->allocate_string(afterImage.c_str());
  attr = doc->allocate_attribute("after_image", name);
  chapter->append_attribute(attr);
  node = doc->allocate_node(node_element, "width", "4");
  chapter->append_node(node);
  node = doc->allocate_node(node_element, "height", "4");
  chapter->append_node(node);

  for (BeeLinePuzzle puz : puzzles) {
    puz.buildXML(doc, chapter, beforeFlower, afterFlower ,puzzleIndex);
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
