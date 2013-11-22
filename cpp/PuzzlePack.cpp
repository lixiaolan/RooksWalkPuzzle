#include "PuzzleBook.hpp"

#include <iostream>
#include <fstream>
#include <sstream>

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
    //row, col, length, hints
    int a, b, c, d;
    ss >> a;
    ss >> b;
    ss >> c;
    ss >> d;
    PP.add(a, b, c, d);
  }

  else if (str == "add_hint") {
    int p, r, c;
    ss >> p;
    ss >> r;
    ss >> c;
    PP.add_hint(p,r,c);
  }

  else if (str == "clear_hint") {
    int p, r, c;
    ss >> p;
    ss >> r;
    ss >> c;
    PP.clear_hint(p,r,c);
  }

  else if (str == "test_unique") {
    int a;
    ss >> a;
    bool unique = PP.test_unique(a);
    if (unique) {
      cout << "Unique!" << endl;
    }
    else {
      cout << "Not Unique!" << endl;
    }
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

    vector<string> files;
    files.push_back("PuzzleBooks/BookOne/BookOneChapterOne.txt");
    files.push_back("PuzzleBooks/BookOne/BookOneChapterTwo.txt");
    files.push_back("PuzzleBooks/BookOne/BookOneChapterThree.txt");
    files.push_back("PuzzleBooks/BookOne/BookOneChapterFour.txt");
    files.push_back("PuzzleBooks/BookOne/BookOneChapterFive.txt");
    files.push_back("PuzzleBooks/BookOne/BookOneChapterSix.txt");
    files.push_back("PuzzleBooks/BookOne/BookOneChapterSeven.txt");

    string bookTitle = "Beatrice the bee";

    string bookStyle = "default";
    
    vector<string> chapterTitles;
    chapterTitles.push_back("Chapter One");
    chapterTitles.push_back("Chapter Two");
    chapterTitles.push_back("Chapter Three");
    chapterTitles.push_back("Chapter Four");
    chapterTitles.push_back("Chapter Five");
    chapterTitles.push_back("Chapter Six");
    chapterTitles.push_back("Chapter Seven");

    vector<string> chapterEndText;
    chapterEndText.push_back("test text");
    chapterEndText.push_back("test text");
    chapterEndText.push_back("test text");
    chapterEndText.push_back("test text");
    chapterEndText.push_back("test text");
    chapterEndText.push_back("test text");
    chapterEndText.push_back("test text");
    
    vector<string> beforeImages;
    beforeImages.push_back("questionmark");
    beforeImages.push_back("questionmark");
    beforeImages.push_back("questionmark");
    beforeImages.push_back("questionmark");
    beforeImages.push_back("questionmark");
    beforeImages.push_back("questionmark");
    beforeImages.push_back("questionmark");

    vector<string> temp;
    vector< vector<string> > afterImages;    
    temp.clear();
    temp.push_back("book1chapter1");
    temp.push_back("book1chapter1_1");
    temp.push_back("book1chapter1_2");
    afterImages.push_back(temp);
    
    temp.clear();
    temp.push_back("book1chapter2");
    temp.push_back("book1chapter2_1");
    temp.push_back("book1chapter2_2");
    afterImages.push_back(temp);
    
    temp.clear();
    temp.push_back("book1chapter3");
    temp.push_back("book1chapter3_1");
    temp.push_back("book1chapter3_2");
    afterImages.push_back(temp);
    
    temp.clear();
    temp.push_back("book1chapter4");
    temp.push_back("book1chapter4_1");
    temp.push_back("book1chapter4_2");
    afterImages.push_back(temp);
    
    temp.clear();
    temp.push_back("book1chapter5");
    temp.push_back("book1chapter5_1");
    temp.push_back("book1chapter5_2");
    afterImages.push_back(temp);
    
    temp.clear();
    temp.push_back("book1chapter6");
    temp.push_back("book1chapter6_1");
    temp.push_back("book1chapter6_2");
    afterImages.push_back(temp);
    
    temp.clear();
    temp.push_back("book1chapter7");
    temp.push_back("book1chapter7_1");
    temp.push_back("book1chapter7_2");
    temp.push_back("book1chapter8");
    temp.push_back("book1chapter8_1");
    temp.push_back("book1chapter8_2");
    temp.push_back("book1chapter9");
    temp.push_back("book1chapter9_1");
    temp.push_back("book1chapter9_2");
    temp.push_back("book1chapter9_3");

    afterImages.push_back(temp);
    

    vector<string> beforeFlower;
    beforeFlower.push_back("flower1");
    beforeFlower.push_back("flower2");
    beforeFlower.push_back("flower3");
    beforeFlower.push_back("flower4");
    beforeFlower.push_back("flower5");
    beforeFlower.push_back("flower6");
    beforeFlower.push_back("flower7");


    vector<string> afterFlower;
    afterFlower.push_back("flower1color");
    afterFlower.push_back("flower2color");
    afterFlower.push_back("flower3color");
    afterFlower.push_back("flower4color");
    afterFlower.push_back("flower5color");
    afterFlower.push_back("flower6color");
    afterFlower.push_back("flower7color");

    int startIndex = 0;

    PuzzleBook PB = PuzzleBook(files, bookTitle, bookStyle, chapterTitles, chapterEndText, beforeImages, afterImages, beforeFlower, afterFlower, startIndex);
    
    ofs.open("XMLOut.xml");
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
