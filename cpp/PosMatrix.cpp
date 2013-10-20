#include "PosMatrix.hpp"

using namespace std;

class pos {
public:
  int r;
  int c;
  pos() : r(0), c(0) {};
  pos(int x, int y) : r(x), c(y) {};
  void print() {
    cout << "(" << r << "," << c << ")";
  }
};

pos operator-(pos left, pos right) {
  left.r = left.r - right.r;
  left.c = left.c - right.c;
  return left;
}

pos operator+(pos left, pos right) {
  left.r = left.r + right.r;
  left.c = left.c + right.c;
  return left;
}

bool operator==(pos left, pos right) {
  bool ret = (left.r==right.r)&(left.c==right.c);
  return ret;
}

template <typename T>
class PosMatrix {
private:
  vector< vector <T> > matrix;
public:
  T &operator()(pos p) {
    return matrix[p.r][p.c];
  }
  PosMatrix(vector< vector <T> > in): matrix(in) {};
};
