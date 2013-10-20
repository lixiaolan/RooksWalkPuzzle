#ifndef PosMatrixLJJ
#define PosMatrixLJJ

#include <iostream>
#include <vector>

using namespace std;

class pos;

pos operator-(pos left, pos right); 

pos operator+(pos left, pos right);

bool operator==(pos left, pos right);

template <typename T>
class PosMatrix;

#endif
