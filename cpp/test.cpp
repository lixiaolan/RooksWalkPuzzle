#include "BeeLineLJJ.hpp"

#include <iostream>
#include <fstream>
#include <sstream>

int main(int argc, char *argv[])
{
  ofstream ofs;
  stringstream ss;
  ss << argv[1] << " " << argv[2] << " " << argv[3] << " " << argv[4];
  int a, b, c, d;
  ss >> a;
  ss >> b;
  ss >> c;
  ss >> d;

  BeeLinePuzzle BLP(a, b, c, d);

  ofs.open("iotest.txt");
  ofs << BLP;
  ofs.close();

  cout << BLP.uniqueCounter << endl;
  cout << BLP.printPuzzle();
  

  //  cout << BLP.uniqueCounter << endl;

  return 0;
}
