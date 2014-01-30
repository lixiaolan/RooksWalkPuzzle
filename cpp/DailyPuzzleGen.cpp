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

  BeeLinePuzzle BLP;
  // ifs.open("iotest.txt");
  // ifs >> PP;
  // ifs.close();
  
  string str;
  ss >> str;
  ss >> str;
  
  if (str == "generate") {
    //row, col, length, hints
    int a;
    ss >> a;

    srand( unsigned (time(0)) );

    vector<int> seeds;
    for (int i = 0; i < a; i++) {
      seeds.push_back(rand());
    }

    ofs.open("iotest.txt");
    for (int i = 0; i < a; i++) {
      std::cout << (i+1) << std::endl;
      BLP = BeeLinePuzzle(6,6,14,3,seeds[i]);
      ofs << BLP;
    }
    ofs.close();
  }

  else if (str == "print") {
    ifs.open("iotest.txt");
    ofs.open("DailyPuzzles.xml");
    
    int i = 0;
    while (ifs >> BLP) {
      i++;
      BLP.printXML(ofs,i);
    }

    ifs.close();
    ofs.close();
  }

  else {
    cout << "command not recognized!!!" << endl;
  }

  return 0;
}
