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
    
    PuzzleBookData PBD;

    PBD.files.push_back("BeeDokuPuzzleBooks/StoryPackOne/StoryPackOneChapterOne.txt");
    PBD.files.push_back("BeeDokuPuzzleBooks/StoryPackOne/StoryPackOneChapterTwo.txt");
    PBD.files.push_back("BeeDokuPuzzleBooks/StoryPackOne/StoryPackOneChapterThree.txt");

    PBD.puzzleBookAttr["id"] = "storyPack1";
    PBD.puzzleBookAttr["title"] = "storyPack1Banner";
    PBD.puzzleBookAttr["purchaseId"] = "storypack1";
    PBD.puzzleBookAttr["purchaseBanner"] = "storyPack1PurchaseBanner";
    PBD.puzzleBookAttr["purchaseTag"] = "unlockTag";
    PBD.puzzleBookAttr["bookStyle"] = "default";
    PBD.puzzleBookAttr["kickback"] = "2";

    PBD.chapterTitles.push_back("Beatrice Stirs");
    PBD.chapterTitles.push_back("A New Leaf");
    PBD.chapterTitles.push_back("Busy As A...");

    PBD.chapterEndText.push_back(" Congratulations!^ You completed your very first chapter!");
    PBD.chapterEndText.push_back(" Congratulations!^ Wow, that was fast! Let's try the last chapter...");
    PBD.chapterEndText.push_back(" Congratulations!^ Beatrice is so happy! Now help her collect flowers in story pack 2!");
    

    PBD.beforeImages.push_back("questionmark");
    PBD.beforeImages.push_back("questionmark");
    PBD.beforeImages.push_back("questionmark");

    vector<string> temp;
    temp.clear();
    temp.push_back("storypack1chapter1");
    temp.push_back("storypack1chapter1_1");
    temp.push_back("storypack1chapter1_2");
    PBD.afterImages.push_back(temp);
    
    temp.clear();
    temp.push_back("storypack1chapter2");
    temp.push_back("storypack1chapter2_1");
    temp.push_back("storypack1chapter2_2");
    PBD.afterImages.push_back(temp);
    
    temp.clear();
    temp.push_back("storypack1chapter3");
    temp.push_back("storypack1chapter3_1");
    temp.push_back("storypack1chapter3_2");
    temp.push_back("storypack1chapter4");
    temp.push_back("storypack1chapter4_1");
    temp.push_back("storypack1chapter4_2");
    PBD.afterImages.push_back(temp);    

    PBD.beforeFlower.push_back("flower1");
    PBD.beforeFlower.push_back("flower2");
    PBD.beforeFlower.push_back("flower3");

    PBD.afterFlower.push_back("flower1color");
    PBD.afterFlower.push_back("flower2color");
    PBD.afterFlower.push_back("flower3color");

    PBD.startIndex = 0;

    PBD.textMap[0] = " Close the path. Touch the square with the dot to enter a 3 and then swipe right.";
    PBD.textMap[1] = " Create a loop. The yellow squares are part of the path given as hints.";
    PBD.textMap[2] = " Nice work. Again, close the loop!";
    PBD.textMap[3] = " Remember Beatrice's rule: A number cannot appear twice in a row or column.";
    PBD.textMap[4] = " The path can start at any yellow square as long as it finally comes back there.";
    PBD.textMap[5] = " Some paths have to cross themselves. Remember, your path must return to the 4.";
    PBD.textMap[6] = " Remember, the path can start at any yellow square as long as it finally comes back there.";
    PBD.textMap[7] = " Remember Beatrice's rule: A number cannot appear twice in a row or column.";
    PBD.textMap[8] = " Looks like you got the hang of things. Touch Beatrice to get a hint.";
    PBD.textMap[9] = " This looks complicated! Just trace out the path carefully.";
    PBD.textMap[10] = " Tip: You can play any puzzle in a chapter, but you must solve them all to move on.";
    PBD.textMap[11] = " Remember, your path must fill all the empty tiles on the board.";
    

    PuzzleBook PB = PuzzleBook(PBD);
    
    ofs.open("StoryPackOne.xml");
    PB.printXML2(ofs);
    ofs.close();
  }

  else if (str == "printStoryPackTwo") {

    PuzzleBookData PBD;

    PBD.files.push_back("BeeDokuPuzzleBooks/StoryPackTwo/StoryPackTwoChapterOne.txt");
    PBD.files.push_back("BeeDokuPuzzleBooks/StoryPackTwo/StoryPackTwoChapterTwo.txt");
    PBD.files.push_back("BeeDokuPuzzleBooks/StoryPackTwo/StoryPackTwoChapterThree.txt");
    PBD.files.push_back("BeeDokuPuzzleBooks/StoryPackTwo/StoryPackTwoChapterFour.txt");
    PBD.files.push_back("BeeDokuPuzzleBooks/StoryPackTwo/StoryPackTwoChapterFive.txt");
    PBD.files.push_back("BeeDokuPuzzleBooks/StoryPackTwo/StoryPackTwoChapterSix.txt");
    PBD.files.push_back("BeeDokuPuzzleBooks/StoryPackTwo/StoryPackTwoChapterSeven.txt");

    PBD.puzzleBookAttr["id"] = "storyPack2";
    PBD.puzzleBookAttr["title"] = "storyPack2Banner";
    PBD.puzzleBookAttr["purchaseId"] = "storypack2";
    PBD.puzzleBookAttr["purchaseBanner"] = "storyPack2PurchaseBanner";
    PBD.puzzleBookAttr["purchaseTag"] = "unlockTag";
    PBD.puzzleBookAttr["bookStyle"] = "lp2";
    PBD.puzzleBookAttr["kickback"] = "1";
    
    PBD.chapterTitles.push_back("The Natural Log");
    PBD.chapterTitles.push_back("The Babbling Brook");
    PBD.chapterTitles.push_back("The Cherry Tree");
    PBD.chapterTitles.push_back("Cats And Lilies");
    PBD.chapterTitles.push_back("Over The Hills");
    PBD.chapterTitles.push_back("Tigers In The Grass");
    PBD.chapterTitles.push_back("Beatrice's Garden");

    PBD.chapterEndText.push_back("Congratulations!^ You helped Beatrice visit all the flowers on the log!");
    PBD.chapterEndText.push_back("Congratulations!^ You helped Beatrice find the flowers along side the brook!");
    PBD.chapterEndText.push_back("Congratulations!^ You helped Beatrice pollinate every flower in the tree!");
    PBD.chapterEndText.push_back("Congratulations!^ Do you feel emotionally connected to Beatrice yet?");
    PBD.chapterEndText.push_back("Congratulations!^ You thought those were tricky? Just wait!");
    PBD.chapterEndText.push_back("Congratulations!^ Don't worry, Beatrice still loves you!");
    PBD.chapterEndText.push_back("Congratulations!^ You did it! You helped Beatrice bring color to her world!");
    
    PBD.beforeImages.push_back("questionmark");
    PBD.beforeImages.push_back("questionmark");
    PBD.beforeImages.push_back("questionmark");
    PBD.beforeImages.push_back("questionmark");
    PBD.beforeImages.push_back("questionmark");
    PBD.beforeImages.push_back("questionmark");
    PBD.beforeImages.push_back("questionmark");

    vector<string> temp;
    temp.clear();
    temp.push_back("book1chapter1");
    temp.push_back("book1chapter1_1");
    temp.push_back("book1chapter1_2");
    PBD.afterImages.push_back(temp);
    
    temp.clear();
    temp.push_back("book1chapter2");
    temp.push_back("book1chapter2_1");
    temp.push_back("book1chapter2_2");
    PBD.afterImages.push_back(temp);
    
    temp.clear();
    temp.push_back("book1chapter3");
    temp.push_back("book1chapter3_1");
    temp.push_back("book1chapter3_2");
    PBD.afterImages.push_back(temp);
    
    temp.clear();
    temp.push_back("book1chapter4");
    temp.push_back("book1chapter4_1");
    temp.push_back("book1chapter4_2");
    PBD.afterImages.push_back(temp);
    
    temp.clear();
    temp.push_back("book1chapter5");
    temp.push_back("book1chapter5_1");
    temp.push_back("book1chapter5_2");
    PBD.afterImages.push_back(temp);
    
    temp.clear();
    temp.push_back("book1chapter6");
    temp.push_back("book1chapter6_1");
    temp.push_back("book1chapter6_2");
    PBD.afterImages.push_back(temp);
    
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

    PBD.afterImages.push_back(temp);
    
    PBD.beforeFlower.push_back("flower1");
    PBD.beforeFlower.push_back("flower2");
    PBD.beforeFlower.push_back("flower3");
    PBD.beforeFlower.push_back("flower4");
    PBD.beforeFlower.push_back("flower5");
    PBD.beforeFlower.push_back("flower6");
    PBD.beforeFlower.push_back("flower7");

    PBD.afterFlower.push_back("flower1color");
    PBD.afterFlower.push_back("flower2color");
    PBD.afterFlower.push_back("flower3color");
    PBD.afterFlower.push_back("flower4color");
    PBD.afterFlower.push_back("flower5color");
    PBD.afterFlower.push_back("flower6color");
    PBD.afterFlower.push_back("flower7color");

    PBD.startIndex = 100;

    PuzzleBook PB = PuzzleBook(PBD);
    
    ofs.open("StoryPackTwo.xml");
    PB.printXML2(ofs);
    ofs.close();
  }

  else if (str == "printChallengePackOne") {
    
    PuzzleBookData PBD;

    PBD.files.push_back("BeeDokuPuzzleBooks/ChallengePackOne/ChallengePackOneChapterOne.txt");
    PBD.files.push_back("BeeDokuPuzzleBooks/ChallengePackOne/ChallengePackOneChapterTwo.txt");
    PBD.files.push_back("BeeDokuPuzzleBooks/ChallengePackOne/ChallengePackOneChapterThree.txt");
    PBD.files.push_back("BeeDokuPuzzleBooks/ChallengePackOne/ChallengePackOneChapterFour.txt");
    PBD.files.push_back("BeeDokuPuzzleBooks/ChallengePackOne/ChallengePackOneChapterFive.txt");
    PBD.files.push_back("BeeDokuPuzzleBooks/ChallengePackOne/ChallengePackOneChapterSix.txt");
    PBD.files.push_back("BeeDokuPuzzleBooks/ChallengePackOne/ChallengePackOneChapterSeven.txt");

    PBD.puzzleBookAttr["id"] = "challengePack1";
    PBD.puzzleBookAttr["title"] = "challengePack1Banner";
    PBD.puzzleBookAttr["purchaseId"] = "storypack2";
    PBD.puzzleBookAttr["purchaseBanner"] = "challengePack1PurchaseBanner";
    PBD.puzzleBookAttr["purchaseTag"] = "unlockTag";
    PBD.puzzleBookAttr["bookStyle"] = "lp2";
    PBD.puzzleBookAttr["kickback"] = "2";
    
    PBD.chapterTitles.push_back("One");
    PBD.chapterTitles.push_back("Two");
    PBD.chapterTitles.push_back("Three");
    PBD.chapterTitles.push_back("Four");
    PBD.chapterTitles.push_back("Five");
    PBD.chapterTitles.push_back("Six");
    PBD.chapterTitles.push_back("Seven");

    PBD.chapterEndText.push_back("Congratulations!^ You Bee Awesome");
    PBD.chapterEndText.push_back("Congratulations!^ You Bee Awesome");
    PBD.chapterEndText.push_back("Congratulations!^ You Bee Awesome");
    PBD.chapterEndText.push_back("Congratulations!^ You Bee Awesome");
    PBD.chapterEndText.push_back("Congratulations!^ You Bee Awesome");
    PBD.chapterEndText.push_back("Congratulations!^ You Bee Awesome");
    PBD.chapterEndText.push_back("Congratulations!^ You Bee Awesome");
    
    PBD.beforeImages.push_back("questionmark");
    PBD.beforeImages.push_back("questionmark");
    PBD.beforeImages.push_back("questionmark");
    PBD.beforeImages.push_back("questionmark");
    PBD.beforeImages.push_back("questionmark");
    PBD.beforeImages.push_back("questionmark");
    PBD.beforeImages.push_back("questionmark");

    vector<string> temp;
    temp.clear();
    temp.push_back("challengepack1chapter1");
    temp.push_back("challengepack1chapter1_1");
    PBD.afterImages.push_back(temp);
    
    temp.clear();
    temp.push_back("challengepack1chapter2");
    temp.push_back("challengepack1chapter2_1");
    PBD.afterImages.push_back(temp);
    
    temp.clear();
    temp.push_back("challengepack1chapter3");
    temp.push_back("challengepack1chapter3_1");
    PBD.afterImages.push_back(temp);
    
    temp.clear();
    temp.push_back("challengepack1chapter4");
    temp.push_back("challengepack1chapter4_1");
    PBD.afterImages.push_back(temp);
    
    temp.clear();
    temp.push_back("challengepack1chapter5");
    temp.push_back("challengepack1chapter5_1");
    PBD.afterImages.push_back(temp);
    
    temp.clear();
    temp.push_back("challengepack1chapter6");
    temp.push_back("challengepack1chapter6_1");
    PBD.afterImages.push_back(temp);
    
    temp.clear();
    temp.push_back("challengepack1chapter7");
    temp.push_back("challengepack1chapter7_1");
    temp.push_back("challengepack1chapter8");
    temp.push_back("challengepack1chapter8_1");
    PBD.afterImages.push_back(temp);
    
    PBD.beforeFlower.push_back("flower1");
    PBD.beforeFlower.push_back("flower2");
    PBD.beforeFlower.push_back("flower3");
    PBD.beforeFlower.push_back("flower4");
    PBD.beforeFlower.push_back("flower5");
    PBD.beforeFlower.push_back("flower6");
    PBD.beforeFlower.push_back("flower7");

    PBD.afterFlower.push_back("flower1color");
    PBD.afterFlower.push_back("flower2color");
    PBD.afterFlower.push_back("flower3color");
    PBD.afterFlower.push_back("flower4color");
    PBD.afterFlower.push_back("flower5color");
    PBD.afterFlower.push_back("flower6color");
    PBD.afterFlower.push_back("flower7color");

    PBD.startIndex = 200;

    PuzzleBook PB = PuzzleBook(PBD);
    
    ofs.open("ChallengePackOne.xml");
    PB.printXML2(ofs);
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
