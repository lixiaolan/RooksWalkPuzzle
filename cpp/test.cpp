#include "BeeLineLJJ.hpp"

#include <iostream>
#include <fstream>
#include <sstream>

int main(int argc, char *argv[])
{
  ofstream ofs;
  ifstream ifs;
  stringstream ss;
  ss << argv[1] << " " << argv[2] << " " << argv[3] << " " << argv[4];
  int a, b, c, d;
  ss >> a;
  ss >> b;
  ss >> c;
  ss >> d;

  BeeLinePuzzle BLP(a, b, c, d);

  cout << "original:" << endl;
  BLP.printPuzzle();

  ofs.open("iotest.txt");
  ofs << BLP;
  ofs.close();

  ifs.open("iotest.txt");
  
  BeeLinePuzzle BLPT;
  
  ifs >> BLPT;
  
  ifs.close();

  cout << endl << "copied:" << endl;
  BLPT.printPuzzle();

  //  cout << BLP.uniqueCounter << endl;

  return 0;
}
