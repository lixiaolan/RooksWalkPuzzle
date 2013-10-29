#include "Pos.hpp"

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
  bool ret = ((left.r==right.r)&&(left.c==right.c));
  return ret;
}
