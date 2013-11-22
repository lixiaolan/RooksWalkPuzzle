#include "PuzzleBook.hpp"

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
  char *name;

  levelpack = doc->allocate_node(node_element, "levelpack");
  doc->append_node(levelpack);
  
  name = doc->allocate_string(bookTitle.c_str());
  attr = doc->allocate_attribute("title", name);
  levelpack->append_attribute(attr);

  name = doc->allocate_string(bookStyle.c_str());
  attr = doc->allocate_attribute("bookStyle", name);
  levelpack->append_attribute(attr);

  for (int i = 0; i < chapters.size(); i++) {
    chapters[i].buildXML(doc, levelpack, chapterTitles[i], chapterEndText[i], beforeImages[i], afterImages[i], beforeFlower[i], afterFlower[i] , puzzleIndex);
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

PuzzleBook::PuzzleBook(vector<string> files, string a, string aa ,vector<string> b, vector<string> bb, vector<string> c,vector< vector <string> > d, vector<string> cc,vector<string> dd, int e): bookTitle(a), bookStyle(aa), chapterTitles(b), chapterEndText(bb), beforeImages(c), afterImages(d), beforeFlower(cc), afterFlower(dd), pi(e) {
  puzzleIndex = &pi;
  loadChapters(files);
}
