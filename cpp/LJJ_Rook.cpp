#include "LJJ_Rook.hpp"

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

RookBoard::RookBoard(int h, int w, int l) : height(h), width(w), length(l) {
  vector<int> temp;
  for (int j = 0; j < width; j++) {
    temp.push_back(0);
  }
  for (int i = 0; i < height; i++) {
    moveArea.push_back(temp);
  }

  makeBoard(l);

  for (int j = 0; j < width; j++) {
    int sum = 0;
    for (int i = 0; i < height; i++) {
      sum += moveArea[i][j];
    }
    colSums.push_back(sum);
  }
  for (int i = 0; i < height; i++) {
    int sum = 0;
    for (int j = 0; j < width; j++) {
      sum += moveArea[i][j];
    }
    rowSums.push_back(sum);
  }
  markUnused();
}

vector<pos> RookBoard::legalMoves() {
  set<int> rowNums;
  set<int> colNums;

  pos last = *(positions.end()-1);

  vector<pos> legalMovesList;

  pos lastMove(0,0);

  if (positions.size() > 1) {
    lastMove = *(positions.end()-1)-*(positions.end()-2);
  }

  if (lastMove.r <= 0) {
    for (int i = last.r; i < height; i++) {
      int temp = i - last.r;
      if ((temp > 0)&&(goodPlay(pos(i,last.c),temp))) {
	legalMovesList.push_back(pos(i,last.c));
      }
    }
  }
  if (lastMove.r >= 0) {
    for (int i = last.r; i >= 0; i--) {
      int temp = last.r - i;
      if ((temp > 0)&&(goodPlay(pos(i,last.c),temp))) {
	legalMovesList.push_back(pos(i,last.c));
      }
    }
  }
  if (lastMove.c <= 0) {
    for (int i = last.c; i < width; i++) {
      int temp = i - last.c;
      if ((temp > 0)&&(goodPlay(pos(last.r,i),temp))) {
	legalMovesList.push_back(pos(last.r,i));
      }
    }
  }
  if (lastMove.c >= 0) {
    for (int i = last.c; i >= 0; i--) {
      int temp = last.c - i;
      if ((temp > 0)&&(goodPlay(pos(last.r,i),temp))) {
	legalMovesList.push_back(pos(last.r,i));
      }
    }
  }
  return legalMovesList;
}

//Function to reorder the elements of a vector to have the least amout possible of directional preference.
void RookBoard::reorderLegalMoves(vector<pos> &legalMoves) {
  pos last = *(positions.end()-1);
  vector<pos> result;
  srand ( unsigned (time(0) ) );
  random_shuffle(legalMoves.begin(), legalMoves.end() );
  vector<pos> directions[4];
  for (int i = 0; i < legalMoves.size(); i++ ) {
    pos test = legalMoves[i]-last;
    if (test.r == 0) {
      if (test.c > 0) {
	directions[0].push_back(legalMoves[i]);
      }
      else {
	directions[1].push_back(legalMoves[i]);
      }
    }
    if (test.c == 0) {
      if (test.r > 0) {
	directions[2].push_back(legalMoves[i]);
      }
      else {
	directions[3].push_back(legalMoves[i]);
      }
    }
  }
  for (int i = 0; i < legalMoves.size(); i++) {
    vector<int> options;
    for (int j = 0; j < 4; j++) {
      if (directions[j].size() != 0)
	options.push_back(j);
    }
    srand ( unsigned (time(0) ) );
    random_shuffle(options.begin(), options.end() );
    result.push_back(directions[options[0]].back());
    directions[options[0]].pop_back();
  }
  legalMoves = result;
}

bool RookBoard::goodPlay(pos play, int num) {
  if (moveArea[play.r][play.c] != 0) return false;
  
  set<int> rowColNums;
  for (int i = 0; i < height; i++) {
    if (moveArea[i][play.c]>0) {
      rowColNums.insert(moveArea[i][play.c]);
    }
  }
  for (int i = 0; i < width; i++) {
    if (moveArea[play.r][i]>0) {
      rowColNums.insert(moveArea[play.r][i]);
    }
  }
  if (rowColNums.find(num) == rowColNums.end())
    return true;
  return false;
}

void RookBoard::print() {
  for (int i = 0; i < height; i++) {
    for (int j = 0; j < width; j++) {
      if (moveArea[i][j]>=0)
	cout << " ";
      cout << moveArea[i][j];
    }
    cout << "\n";
  }
  cout << "row sums:" << endl;
  for (auto i : rowSums) {
    cout << i << " ";
  }
  cout << endl;
  cout << "col sums:" << endl;
  for (auto i : colSums) {
    cout << i << " ";
  }
  cout << endl;
}

bool RookBoard::makeBoard(int depth) {
  if (depth == 0) {
    bool a = ((*(positions.begin())).r == (*(positions.end()-1)).r);
    bool b = ((*(positions.begin())).c == (*(positions.end()-1)).c);
    if (a&b) return true;
    else return false;
  }
  
  if (positions.size() == 0) {
    srand ( unsigned ( time(0) ) );
    int x = rand() % height;
    int y = rand() % width;
    positions.push_back(pos(x,y));
  }
  
  vector<pos> lm = legalMoves();

  if (lm.size() == 0) return false;

  reorderLegalMoves(lm);
 
  srand ( unsigned ( time(0) ) );
  random_shuffle(lm.begin(), lm.end());
  
  for (int i = 0; i < lm.size(); i++) {
    positions.push_back(lm[i]);
    pos last = (*(positions.end()-1) - *(positions.end()-2));
    moveArea[lm[i].r][lm[i].c] = (last.r == 0) ? abs(last.c) : abs(last.r);
    if (makeBoard(depth-1)) return true;
    positions.pop_back();
    moveArea[lm[i].r][lm[i].c] = 0;
  }
  return false;
}

void RookBoard::markUnused() {
  for (int i = 0; i < height; i++) {
    for (int j = 0; j < width; j++) {
      if (moveArea[i][j] == 0) 
	moveArea[i][j] = -1; 
    }
  }
  for (int i = 0; i < positions.size()-1; i++) {
    pos b = positions[i+1];
    pos a = positions[i];
    if (a.r > b.r) {
      for (int s = b.r; s < a.r; s++) {
	if (moveArea[s][a.c] == -1)
	  moveArea[s][a.c] = 0;
      }
    }
    if (a.r < b.r) {
      for (int s = a.r; s < b.r; s++) {
	if (moveArea[s][a.c] == -1)
	  moveArea[s][a.c] = 0;
      }
    }
    if (a.c > b.c) {
      for (int s = b.c; s < a.c; s++) {
	if (moveArea[a.r][s] == -1)
	  moveArea[a.r][s] = 0;
      }
    }
    if (a.c < b.c) {
      for (int s = a.c; s < b.c; s++) {
	if (moveArea[a.r][s] == -1)
	  moveArea[a.r][s] = 0;
      }
    }
  }
}

ofstream &operator<<(ofstream &ofs, RookBoard &RB) {
  ofs << RB.height << '\n'
      << RB.width << '\n';
  for (int i = 0; i < RB.height; i++) {
    for (int j = 0; j < RB.width; j++) {
      ofs << RB.moveArea[i][j] << '\n';
    }
  }
  ofs << RB.positions.size() << '\n';
  for (int i = 0; i < RB.positions.size(); i++) {
    ofs << RB.positions[i].r << '\n'
	<< RB.positions[i].c << '\n';
  }
  return ofs;
}
