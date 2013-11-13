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
    files.push_back("PuzzleBooks/BookOne/BookOneChapterSix.txt");
    files.push_back("PuzzleBooks/BookOne/BookOneChapterSeven.txt");

    string bookTitle = "Beatrice the bee";

    string bookStyle = "default";
    
    vector<string> chapterTitles;
    chapterTitles.push_back("Chapter One: A lone bee encounters a dreary, lifeless world.");
    chapterTitles.push_back("Chapter Two: Wherein our heroic bee sets forth to better her surroundings but is met with great adversary.");
    chapterTitles.push_back("Chapter Three: A time during which Beatrice questions the meaning of right and wrong and ultimately comes to no really conclusion");
    chapterTitles.push_back("Chapter Four: Beatrice continues her struggle to bring life and color to the landscape with moderate success");
    chapterTitles.push_back("Chapter Five: Wherein our magnanimous bee ponders the confines of her mortal shell");
    chapterTitles.push_back("Chapter Six: ");
    chapterTitles.push_back("Chapter Seven: ");
    
    vector<string> beforeImages;
    beforeImages.push_back("locked");
    beforeImages.push_back("locked");
    beforeImages.push_back("locked");
    beforeImages.push_back("locked");
    beforeImages.push_back("locked");
    beforeImages.push_back("locked");
    beforeImages.push_back("locked");

    vector<string> temp;
    vector< vector<string> > afterImages;    
    temp.clear();
    temp.push_back("book1chapter1");
    temp.push_back("book1chapter1_1");
    temp.push_back("book1chapter1_2");
    afterImages.push_back(temp);
    
    temp.clear();
    temp.push_back("book1chapter2");
    afterImages.push_back(temp);
    
    temp.clear();
    temp.push_back("book1chapter3");
    afterImages.push_back(temp);
    
    temp.clear();
    temp.push_back("book1chapter4");
    afterImages.push_back(temp);
    
    temp.clear();
    temp.push_back("book1chapter5");
    afterImages.push_back(temp);
    
    temp.clear();
    temp.push_back("book1chapter6");
    afterImages.push_back(temp);
    
    temp.clear();
    temp.push_back("book1chapter7");
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

    PuzzleBook PB = PuzzleBook(files, bookTitle, bookStyle, chapterTitles, beforeImages, afterImages, beforeFlower, afterFlower, startIndex);
    
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
