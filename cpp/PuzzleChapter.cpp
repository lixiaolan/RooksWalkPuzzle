#include "PuzzleChapter.hpp"

void PuzzleChapter::add(int h, int w, int l, int hints) {
  BeeLinePuzzle BLP;
  //do {
  BLP = BeeLinePuzzle(h, w, l, hints);
  //} while (BLP.uniqueCounter > 1);
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

void PuzzleChapter::add_hint(int p, int r, int c) {
  puzzles[p-1].addHint(r, c);
  return;
}

void PuzzleChapter::clear_hint(int p, int r, int c) {
  puzzles[p-1].clearHint(r, c);
  return;
}

bool PuzzleChapter::test_unique(int p) {
  return puzzles[p-1].checkUnique();
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

void PuzzleChapter::buildXML(xml_document<> *doc, xml_node<> *levelpack, string title, string endText, string beforeImage, vector<string> afterImage,string beforeFlower, string afterFlower,map<int, string> textMap,int *puzzleIndex) {
  xml_node<> *chapter;
  xml_node<> *afterIm;
  xml_node<> *node;
  xml_attribute<> *attr;
  char *name;

  chapter = doc->allocate_node(node_element, "chapter");
  levelpack->append_node(chapter);
  name = doc->allocate_string(title.c_str());
  attr = doc->allocate_attribute("title", name);
  chapter->append_attribute(attr);
  name = doc->allocate_string(endText.c_str());
  attr = doc->allocate_attribute("end_text", name);
  chapter->append_attribute(attr);
  name = doc->allocate_string(beforeImage.c_str());
  attr = doc->allocate_attribute("before_image", name);
  chapter->append_attribute(attr);

  afterIm = doc->allocate_node(node_element, "afterImage");
  chapter->append_node(afterIm);

  for (string s : afterImage) {
    node = doc->allocate_node(node_element, "image");
    name = doc->allocate_string(s.c_str());
    attr = doc->allocate_attribute("image", name);
    node->append_attribute(attr);
    afterIm->append_node(node);
  }
  if (puzzles.size() == 4) {
    node = doc->allocate_node(node_element, "width", "2");
    chapter->append_node(node);
    node = doc->allocate_node(node_element, "height", "2");
    chapter->append_node(node);
  }

  if (puzzles.size() == 9) {
    node = doc->allocate_node(node_element, "width", "3");
    chapter->append_node(node);
    node = doc->allocate_node(node_element, "height", "3");
    chapter->append_node(node);
  }

  if (puzzles.size() == 16) {
    node = doc->allocate_node(node_element, "width", "4");
    chapter->append_node(node);
    node = doc->allocate_node(node_element, "height", "4");
    chapter->append_node(node);
  }

    
  for (BeeLinePuzzle puz : puzzles) {
    puz.buildXML(doc, chapter, beforeFlower, afterFlower ,textMap ,puzzleIndex);
  }
}

void PuzzleChapter::buildXML(xml_document<> *doc, xml_node<> *levelpack, PuzzleBookData &PBD) {
  xml_node<> *chapter;
  xml_node<> *afterIm;
  xml_node<> *node;
  xml_attribute<> *attr;
  char *name;

  chapter = doc->allocate_node(node_element, "chapter");
  levelpack->append_node(chapter);
  name = doc->allocate_string(PBD.getChapterTitle().c_str());
  attr = doc->allocate_attribute("title", name);
  chapter->append_attribute(attr);
  name = doc->allocate_string(PBD.getChapterEndText().c_str());
  attr = doc->allocate_attribute("end_text", name);
  chapter->append_attribute(attr);
  name = doc->allocate_string(PBD.getBeforeImage().c_str());
  attr = doc->allocate_attribute("before_image", name);
  chapter->append_attribute(attr);

  afterIm = doc->allocate_node(node_element, "afterImage");
  chapter->append_node(afterIm);

  for (string s : PBD.getAfterImages()) {
    node = doc->allocate_node(node_element, "image");
    name = doc->allocate_string(s.c_str());
    attr = doc->allocate_attribute("image", name);
    node->append_attribute(attr);
    afterIm->append_node(node);
  }
  if (puzzles.size() == 4) {
    node = doc->allocate_node(node_element, "width", "2");
    chapter->append_node(node);
    node = doc->allocate_node(node_element, "height", "2");
    chapter->append_node(node);
  }

  if (puzzles.size() == 9) {
    node = doc->allocate_node(node_element, "width", "3");
    chapter->append_node(node);
    node = doc->allocate_node(node_element, "height", "3");
    chapter->append_node(node);
  }

  if (puzzles.size() == 16) {
    node = doc->allocate_node(node_element, "width", "4");
    chapter->append_node(node);
    node = doc->allocate_node(node_element, "height", "4");
    chapter->append_node(node);
  }
    
  for (BeeLinePuzzle puz : puzzles) {
    puz.buildXML(doc, chapter, PBD);
  }
}

ofstream &operator<<(ofstream &ofs, PuzzleChapter &PP) {
  for (int i = 0; i < PP.puzzles.size(); i++) {
    ofs << PP.puzzles[i];
  }
  return ofs;
}

ifstream &operator>>(ifstream &ifs, PuzzleChapter &PC) {
  PC.puzzles.clear();

  while (true) {
    BeeLinePuzzle BLP;
    if (ifs >> BLP) {
      PC.puzzles.push_back(BLP);
    }
    else {
      break;
    }
  }
  return ifs;
}
