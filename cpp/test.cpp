#include "LJJ_Rook.hpp"
#include <sstream>

using namespace std;

int main(int argc, char *argv[]) {
  if (argc != 4) {
    cout << "usage: " << argv[0] << " BoardLength BoardWidth PuzzleLength" << endl;
  }
  else {
    stringstream ss;
    for (int i = 1; i < argc; i++) {
      ss << string(argv[i]) << " ";
    }
    int a, b, c;
    ss >> a;
    ss >> b;
    ss >> c;
    ofstream of;
    RookBoard board(a,b,c);
    of.open("iotest.txt");
    of << board;
    of.close();
  }
  return argc;
}
