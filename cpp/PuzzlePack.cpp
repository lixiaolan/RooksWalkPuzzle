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
    vector<string> files;
    files.push_back("PuzzleBooks/BookOne/BookOneChapterOne.txt");
    files.push_back("PuzzleBooks/BookOne/BookOneChapterTwo.txt");
    files.push_back("PuzzleBooks/BookOne/BookOneChapterThree.txt");
    files.push_back("PuzzleBooks/BookOne/BookOneChapterFour.txt");
    files.push_back("PuzzleBooks/BookOne/BookOneChapterFive.txt");

    string bookTitle = "Beatrice the bee";
    
    vector<string> chapterTitles;
    chapterTitles.push_back("Chapter One: A lone bee encounters a dreary, lifeless world.");
    chapterTitles.push_back("Chapter Two: Wherein our heroic bee sets forth to better her surroundings but is met with great adversary.");
    chapterTitles.push_back("Chapter Three: A time during which Beatrice questions the meaning of right and wrong and ultimately comes to no really conclusion");
    chapterTitles.push_back("Chapter Four: Beatrice continues her struggle to bring life and color to the landscape with moderate success");
    chapterTitles.push_back("Chapter Five: Wherein our magnanimous bee ponders the confines of her mortal shell");
    
    vector<string> beforeImages;
    beforeImages.push_back("Locked");
    beforeImages.push_back("Locked");
    beforeImages.push_back("Locked");
    beforeImages.push_back("Locked");
    beforeImages.push_back("Locked");

    vector<string> afterImages;
    afterImages.push_back("BookOneChOne");
    afterImages.push_back("BookOneChTwo");
    afterImages.push_back("BookOneChThree");
    afterImages.push_back("BookOneChFour");
    afterImages.push_back("BookOneChFive");

    PuzzleBook PB = PuzzleBook(files, bookTitle, chapterTitles, beforeImages, afterImages);
    
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
