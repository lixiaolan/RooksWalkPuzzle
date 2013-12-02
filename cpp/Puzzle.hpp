#ifndef PUZZLE
#define PUZZLE

#include "rapidxml.hpp"
#include "rapidxml_print.hpp"

#include "Pos.hpp"
#include <string.h>
#include <jni.h>
#include <iostream>
#include <fstream>
#include <sstream>
#include <memory>
#include <set>
#include <vector>
#include <algorithm> 
#include <cmath>
#include <ctime>
#include <cstdlib> 
#include <string>


using namespace rapidxml;
using namespace std;

class Puzzle {
private:
  vector< vector<int> > gameBoard;
  vector<pos> positions;
  vector<int> hintsIndex;
  int id;
  string text;
  string before_flower;
  string after_flower;
public:
  //This will do what it did before using local data instaead of input
  void writeXML(xml_document<> *doc, xml_node<> *chapter);
  //This will have to read in the data and create the various hint data objects used for printing
  void readXML(xml_document<> *doc, xml_node<> *chapter);
  //This should be the same functions as before if everything is set up correclty:
  void printPuzzle();
  void printSolution();
  
};

ifstream &operator>>(ifstream &, BeeLinePuzzle &);

ofstream &operator<<(ofstream &, BeeLinePuzzle &);  

#endif
