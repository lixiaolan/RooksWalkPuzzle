#include "LJJ_Rook.hpp"
#include <iostream>
#include <fstream>
#include <sstream>

int main(int argc, char *argv[])
{
  ofstream ofs;
  stringstream ss;
  ss << argv[1] << " " << argv[2] << " " << argv[3];
  int a, b, c;
  ss >> a;
  ss >> b;
  ss >> c;
  
  RookBoard RB(a, b, c);

  ofs.open("iotest.txt");
  ofs << RB;
  ofs.close();
  return 0;
}
