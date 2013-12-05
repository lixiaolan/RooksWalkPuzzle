#include "PuzzleBook.hpp"

#include <iostream>
#include <fstream>
#include <sstream>
#include <map>


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


  else if (str == "printStoryPackOne") {
    
    vector<string> files;
    files.push_back("BeeDokuPuzzleBooks/StoryPackOne/StoryPackOneChapterOne.txt");
    files.push_back("BeeDokuPuzzleBooks/StoryPackOne/StoryPackOneChapterTwo.txt");
    files.push_back("BeeDokuPuzzleBooks/StoryPackOne/StoryPackOneChapterThree.txt");

    string bookTitle = "storyPack1Banner";

    string kickBack = "1";

    string bookStyle = "default";
    
    vector<string> chapterTitles;
    chapterTitles.push_back("Beatrice Buzzes");
    chapterTitles.push_back("The flowers in the garden");
    chapterTitles.push_back("Raise up to greet her");

    vector<string> chapterEndText;
    chapterEndText.push_back("Congratulations!^ You completed your very first chapter!");
    chapterEndText.push_back("Congratulations!^ Wow, that was fast! Let's try the last chapter...");
    chapterEndText.push_back("Congratulations!^ Beatrice is so happy! Help her collect flowers in level pack two!");
    
    vector<string> beforeImages;
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


    vector<string> afterFlower;
    afterFlower.push_back("flower1color");
    afterFlower.push_back("flower2color");
    afterFlower.push_back("flower3color");

    int startIndex = 0;

    map<int, string> textMap;
    textMap[0] = " Close the path. Touch the square with the circle to enter a 3 and then swipe right.";
    textMap[1] = " Nice work. The yellow squares are part of a path we are giving you.";
    textMap[2] = " TIP: Beatrice loves you. If you touch her, she'll give you a hint. Touch the questionmark to see the rules.";
    textMap[3] = " This puzzle is harder then it looks. Remember Beatrice's rule!";
    textMap[4] = " Not all paths have to be rectangles!";
    textMap[5] = " Some paths have to cross themselves.";
    textMap[6] = " Keep it up!";
    textMap[7] = " This puzzle is easier then it looks! You are almost done!";
    textMap[8] = " Looks like you got the hang of things. Touch Beatrice if you need help.";

    PuzzleBook PB = PuzzleBook(files, bookTitle, bookStyle, chapterTitles, chapterEndText, beforeImages, afterImages, beforeFlower, afterFlower,textMap, startIndex);
    
    ofs.open("StoryPackOne.xml");
    PB.printXML(ofs);
    ofs.close();

  }

  else if (str == "printStoryPackTwo") {

    vector<string> files;
    files.push_back("BeeDokuPuzzleBooks/StoryPackTwo/StoryPackTwoChapterOne.txt");
    files.push_back("BeeDokuPuzzleBooks/StoryPackTwo/StoryPackTwoChapterTwo.txt");
    files.push_back("BeeDokuPuzzleBooks/StoryPackTwo/StoryPackTwoChapterThree.txt");
    files.push_back("BeeDokuPuzzleBooks/StoryPackTwo/StoryPackTwoChapterFour.txt");
    files.push_back("BeeDokuPuzzleBooks/StoryPackTwo/StoryPackTwoChapterFive.txt");
    files.push_back("BeeDokuPuzzleBooks/StoryPackTwo/StoryPackTwoChapterSix.txt");
    files.push_back("BeeDokuPuzzleBooks/StoryPackTwo/StoryPackTwoChapterSeven.txt");

    string bookTitle = "storyPack2Banner";

    string kickBack = "1";

    string bookStyle = "lp2";
    
    vector<string> chapterTitles;
    chapterTitles.push_back("The Natural Log");
    chapterTitles.push_back("The Babbling Brook");
    chapterTitles.push_back("The Cherry Tree");
    chapterTitles.push_back("Cats And Lilies");
    chapterTitles.push_back("Over The Hills");
    chapterTitles.push_back("Tigers In The Grass");
    chapterTitles.push_back("Beatrice's Garden");

    vector<string> chapterEndText;
    chapterEndText.push_back("Congratulations!^ You helped Beatrice visit all the flowers on the log!");
    chapterEndText.push_back("Congratulations!^ You helped Beatrice find the flowers along side the brook!");
    chapterEndText.push_back("Congratulations!^ You helped Beatrice pollinate every flower in the tree!");
    chapterEndText.push_back("Congratulations!^ Do you feel emotionally connected to Beatrice yet?");
    chapterEndText.push_back("Congratulations!^ You thought those were tricky? Just wait!");
    chapterEndText.push_back("Congratulations!^ Don't worry, Beatrice still loves you!");
    chapterEndText.push_back("Congratulations!^ You did it! You helped Beatrice bring color to her world!");
    
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

    int startIndex = 100;

    PuzzleBook PB = PuzzleBook(files, bookTitle, bookStyle, chapterTitles, chapterEndText, beforeImages, afterImages, beforeFlower, afterFlower, startIndex);
    
    ofs.open("StoryPackTwo.xml");
    PB.printXML(ofs);
    ofs.close();
  }

  else if (str == "printChallengePackOne") {

    vector<string> files;
    files.push_back("BeeDokuPuzzleBooks/ChallengePackOne/ChallengePackOneChapterOne.txt");
    files.push_back("BeeDokuPuzzleBooks/ChallengePackOne/ChallengePackOneChapterTwo.txt");
    files.push_back("BeeDokuPuzzleBooks/ChallengePackOne/ChallengePackOneChapterThree.txt");
    files.push_back("BeeDokuPuzzleBooks/ChallengePackOne/ChallengePackOneChapterFour.txt");
    files.push_back("BeeDokuPuzzleBooks/ChallengePackOne/ChallengePackOneChapterFive.txt");
    files.push_back("BeeDokuPuzzleBooks/ChallengePackOne/ChallengePackOneChapterSix.txt");
    files.push_back("BeeDokuPuzzleBooks/ChallengePackOne/ChallengePackOneChapterSeven.txt");

    string bookTitle = "challengePack1Banner";
    
    string kickBack = "0";
    
    string bookStyle = "lp2";
    
    vector<string> chapterTitles;
    chapterTitles.push_back("One");
    chapterTitles.push_back("Two");
    chapterTitles.push_back("Three");
    chapterTitles.push_back("Four");
    chapterTitles.push_back("Five");
    chapterTitles.push_back("Six");
    chapterTitles.push_back("Seven");

    vector<string> chapterEndText;
    chapterEndText.push_back("Congratulations!^ You Bee Awesome");
    chapterEndText.push_back("Congratulations!^ You Bee Awesome");
    chapterEndText.push_back("Congratulations!^ You Bee Awesome");
    chapterEndText.push_back("Congratulations!^ You Bee Awesome");
    chapterEndText.push_back("Congratulations!^ You Bee Awesome");
    chapterEndText.push_back("Congratulations!^ You Bee Awesome");
    chapterEndText.push_back("Congratulations!^ You Bee Awesome");
    
    vector<string> beforeImages;
    beforeImages.push_back("challengePackOne");
    beforeImages.push_back("challengePackOne");
    beforeImages.push_back("challengePackOne");
    beforeImages.push_back("challengePackOne");
    beforeImages.push_back("challengePackOne");
    beforeImages.push_back("challengePackOne");
    beforeImages.push_back("challengePackOne");

    vector<string> temp;
    vector< vector<string> > afterImages;    
    temp.clear();
    temp.push_back("challengePackOne");
    afterImages.push_back(temp);
    
    temp.clear();
    temp.push_back("challengePackOne");
    afterImages.push_back(temp);
    
    temp.clear();
    temp.push_back("challengePackOne");
    afterImages.push_back(temp);
    
    temp.clear();
    temp.push_back("challengePackOne");
    afterImages.push_back(temp);
    
    temp.clear();
    temp.push_back("challengePackOne");
    afterImages.push_back(temp);
    
    temp.clear();
    temp.push_back("challengePackOne");
    afterImages.push_back(temp);
    
    temp.clear();
    temp.push_back("challengePackOne");

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

    int startIndex = 200;

    PuzzleBook PB = PuzzleBook(files, bookTitle, bookStyle, chapterTitles, chapterEndText, beforeImages, afterImages, beforeFlower, afterFlower, startIndex);
    
    ofs.open("ChallengePackOne.xml");
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
