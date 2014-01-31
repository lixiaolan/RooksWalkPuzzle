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

  // ifs.open("iotest.txt");
  // ifs >> PP;
  // ifs.close();
  
  string str;
  ss >> str;
  ss >> str;
  
  if (str == "generate") {
    //row, col, length, hints
    BeeLinePuzzle BLP;
    int a;
    ss >> a;

    srand( unsigned (time(0)) );

    vector<unsigned int> seeds;
    for (int i = 0; i < a; i++) {
      seeds.push_back(rand());
    }

    ofs.open("iotest.txt");
    for (int i = 0; i < a; i++) {
      std::cout << (i+1) << std::endl;
      do {
	BLP = BeeLinePuzzle(6,6,14,3,seeds[i]+unsigned(time(0)));
      } while (BLP.uniqueCounter != 1);
      ofs << BLP;
    }
    ofs.close();
  }

  else if (str == "print") {
    ifs.open("iotest.txt");

    xml_document<> d;
    xml_document<> *doc = &d;
    xml_node<> *dailyPuzzles;

    dailyPuzzles = doc->allocate_node(node_element, "DailyPuzzles");
    doc->append_node(dailyPuzzles);
    
    int i = 0;
    while (true) {
      i++;
      BeeLinePuzzle BLP;
      if (ifs >> BLP) {
	BLP.buildXMLSimple(doc, dailyPuzzles, i,"flower1","flower1color");
      }
      else {
	break;
      }
    }

    ofs.open("DailyPuzzles.xml");
    ofs << *doc;
    ifs.close();
    ofs.close();
  }

  else {
    cout << "command not recognized!!!" << endl;
  }

  return 0;
}
