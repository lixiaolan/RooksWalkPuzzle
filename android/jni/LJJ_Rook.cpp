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

bool operator==(pos left, pos right) {
  bool ret = (left.r==right.r)&(left.c==right.c);
  return ret;
}

RookBoard::RookBoard(int h, int w, int l) : height(h), width(w), length(l) {
  vector<int> temp;
  vector<bool> bTemp;
  for (int j = 0; j < width; j++) {
    temp.push_back(0);
    bTemp.push_back(false);
  }
  for (int i = 0; i < height; i++) {
    moveArea.push_back(temp);
    leftRight.push_back(bTemp);
    upDown.push_back(bTemp);
  }
  //makeBoardRightAnglesNoPassOver(l);
  makeBoardNoPassOver(l);

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

// Generate all legal moves from a given position and board state:
vector<pos> RookBoard::legalMoves() {
  // Find the last position.
  pos last = *(positions.end()-1);
  // Initiate the LegalMovesList
  vector<pos> legalMovesList;
  //Find the last move made.
  pos lastMove(0,0);
  if (positions.size() > 1) {
    lastMove = *(positions.end()-1)-*(positions.end()-2);
  }
  
  //Do a search for legal moves in the four directions
  // This checks first that the direction is in fact legal
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

// Generate all legal moves FOR RIGHT ANGLE MOVES
// from a given position and board state:
vector<pos> RookBoard::legalMovesRightAngles() {
  // Find the last position.
  pos last = *(positions.end()-1);
  // Initiate the LegalMovesList
  vector<pos> legalMovesList;
  //Find the last move made.
  pos lastMove(0,0);
  if (positions.size() > 1) {
    lastMove = *(positions.end()-1)-*(positions.end()-2);
  }
  
  //Do a search for legal moves in the four directions
  // This checks first that the direction is in fact legal
  if (lastMove.r == 0) {
    for (int i = last.r; i < height; i++) {
      int temp = i - last.r;
      if ((temp > 0)&&(goodPlay(pos(i,last.c),temp))) {
	legalMovesList.push_back(pos(i,last.c));
      }
    }
  }
  if (lastMove.r == 0) {
    for (int i = last.r; i >= 0; i--) {
      int temp = last.r - i;
      if ((temp > 0)&&(goodPlay(pos(i,last.c),temp))) {
	legalMovesList.push_back(pos(i,last.c));
      }
    }
  }
  if (lastMove.c == 0) {
    for (int i = last.c; i < width; i++) {
      int temp = i - last.c;
      if ((temp > 0)&&(goodPlay(pos(last.r,i),temp))) {
	legalMovesList.push_back(pos(last.r,i));
      }
    }
  }
  if (lastMove.c == 0) {
    for (int i = last.c; i >= 0; i--) {
      int temp = last.c - i;
      if ((temp > 0)&&(goodPlay(pos(last.r,i),temp))) {
	legalMovesList.push_back(pos(last.r,i));
      }
    }
  }
  return legalMovesList;
}

// Generate all legal moves FOR RIGHT ANGLE MOVES
// from a given position and board state:
vector<pos> RookBoard::legalMovesRightAnglesAnyNum() {
  // Find the last position.
  pos last = *(positions.end()-1);
  // Initiate the LegalMovesList
  vector<pos> legalMovesList;
  //Find the last move made.
  pos lastMove(0,0);
  if (positions.size() > 1) {
    lastMove = *(positions.end()-1)-*(positions.end()-2);
  }
  
  //Do a search for legal moves in the four directions
  // This checks first that the direction is in fact legal
  if (lastMove.r == 0) {
    for (int i = last.r; i < height; i++) {
      int temp = i - last.r;
      if ((temp > 0)&&(goodPlayAnyNum(pos(i,last.c),temp))) {
	legalMovesList.push_back(pos(i,last.c));
      }
    }
  }
  if (lastMove.r == 0) {
    for (int i = last.r; i >= 0; i--) {
      int temp = last.r - i;
      if ((temp > 0)&&(goodPlayAnyNum(pos(i,last.c),temp))) {
	legalMovesList.push_back(pos(i,last.c));
      }
    }
  }
  if (lastMove.c == 0) {
    for (int i = last.c; i < width; i++) {
      int temp = i - last.c;
      if ((temp > 0)&&(goodPlayAnyNum(pos(last.r,i),temp))) {
	legalMovesList.push_back(pos(last.r,i));
      }
    }
  }
  if (lastMove.c == 0) {
    for (int i = last.c; i >= 0; i--) {
      int temp = last.c - i;
      if ((temp > 0)&&(goodPlayAnyNum(pos(last.r,i),temp))) {
	legalMovesList.push_back(pos(last.r,i));
      }
    }
  }
  return legalMovesList;
}

// Generate all legal moves FOR RIGHT ANGLE MOVES
// from a given position and board state:
vector<pos> RookBoard::legalMovesRightAnglesAnyNumNoPassOver() {
  // Find the last position.
  pos last = *(positions.end()-1);
  // Initiate the LegalMovesList
  vector<pos> legalMovesList;
  //Find the last move made.
  pos lastMove(0,0);
  if (positions.size() > 1) {
    lastMove = *(positions.end()-1)-*(positions.end()-2);
  }
 
  pos im = *(positions.begin());
 
  //Do a search for legal moves in the four directions
  // This checks first that the direction is in fact legal
   if (lastMove.r == 0) {
    for (int i = last.r; i < height; i++) {
      int temp = i - last.r;
      if (temp > 0) {
	if (pos(i,last.c) == im) {
	  if (goodPlayAnyNum(pos(i,last.c),temp)) {
	    legalMovesList.push_back(pos(i,last.c));
	  }
	  break;
	}
	if (upDown[i][last.c]==false) {
	  if (leftRight[i][last.c]==false)
	    if (goodPlayAnyNum(pos(i,last.c),temp)) {
	      legalMovesList.push_back(pos(i,last.c));
	    }
	}
	else
	  break;
      }
    }
  }
  if (lastMove.r == 0) {
    for (int i = last.r; i >= 0; i--) {
      int temp = last.r - i;
      if (temp > 0) {
	if (pos(i,last.c) == im) {
	  if (goodPlayAnyNum(pos(i,last.c),temp))
	    legalMovesList.push_back(pos(i,last.c));
	  break;
	}
	if (upDown[i][last.c]==false) {
	  if (leftRight[i][last.c]==false)
	    if (goodPlayAnyNum(pos(i,last.c),temp))
	      legalMovesList.push_back(pos(i,last.c));
	}
	else
	  break;
	
      }
    }
  }
  if (lastMove.c == 0) {
    for (int i = last.c; i < width; i++) {
      int temp = i - last.c;
      if (temp > 0) {
	if (pos(last.r,i) == im) {
	  if (goodPlayAnyNum(pos(last.r,i),temp))
	    legalMovesList.push_back(pos(last.r,i));
	  break;
	}
	if (leftRight[last.r][i] == false) {
	  if (upDown[last.r][i] == false)
	    if (goodPlayAnyNum(pos(last.r,i),temp))
	      legalMovesList.push_back(pos(last.r,i));
	}
	else
	  break;
      }
    }
  }
  if (lastMove.c == 0) {
    for (int i = last.c; i >= 0; i--) {
      int temp = last.c - i;
      if (temp > 0) {
	if (pos(last.r,i) == im) {
	  if (goodPlayAnyNum(pos(last.r,i),temp))
	    legalMovesList.push_back(pos(last.r,i));
	  break;
	}
	if (leftRight[last.r][i]==false){
	  if (upDown[last.r][i] == false)
	    if (goodPlayAnyNum(pos(last.r,i),temp))
	      legalMovesList.push_back(pos(last.r,i));
	}
	else
	  break;
      }
    }
  }
  return legalMovesList;
}

vector<pos> RookBoard::legalMovesNoPassOver() {
  // Find the last position.
  pos last = *(positions.end()-1);
  // Initiate the LegalMovesList
  vector<pos> legalMovesList;
  //Find the last move made.
  pos lastMove(0,0);
  if (positions.size() > 1) {
    lastMove = *(positions.end()-1)-*(positions.end()-2);
  }
 
  pos im = *(positions.begin());
 
  //Do a search for legal moves in the four directions
  // This checks first that the direction is in fact legal
   if (lastMove.r == 0) {
    for (int i = last.r; i < height; i++) {
      int temp = i - last.r;
      if (temp > 0) {
	if (pos(i,last.c) == im) {
	  if (goodPlay(pos(i,last.c),temp)) {
	    legalMovesList.push_back(pos(i,last.c));
	  }
	  break;
	}
	if (upDown[i][last.c]==false) {
	  if (leftRight[i][last.c]==false)
	    if (goodPlay(pos(i,last.c),temp)) {
	      legalMovesList.push_back(pos(i,last.c));
	    }
	}
	else
	  break;
      }
    }
  }
  if (lastMove.r == 0) {
    for (int i = last.r; i >= 0; i--) {
      int temp = last.r - i;
      if (temp > 0) {
	if (pos(i,last.c) == im) {
	  if (goodPlay(pos(i,last.c),temp))
	    legalMovesList.push_back(pos(i,last.c));
	  break;
	}
	if (upDown[i][last.c]==false) {
	  if (leftRight[i][last.c]==false)
	    if (goodPlay(pos(i,last.c),temp))
	      legalMovesList.push_back(pos(i,last.c));
	}
	else
	  break;
	
      }
    }
  }
  if (lastMove.c == 0) {
    for (int i = last.c; i < width; i++) {
      int temp = i - last.c;
      if (temp > 0) {
	if (pos(last.r,i) == im) {
	  if (goodPlay(pos(last.r,i),temp))
	    legalMovesList.push_back(pos(last.r,i));
	  break;
	}
	if (leftRight[last.r][i] == false) {
	  if (upDown[last.r][i] == false)
	    if (goodPlay(pos(last.r,i),temp))
	      legalMovesList.push_back(pos(last.r,i));
	}
	else
	  break;
      }
    }
  }
  if (lastMove.c == 0) {
    for (int i = last.c; i >= 0; i--) {
      int temp = last.c - i;
      if (temp > 0) {
	if (pos(last.r,i) == im) {
	  if (goodPlay(pos(last.r,i),temp))
	    legalMovesList.push_back(pos(last.r,i));
	  break;
	}
	if (leftRight[last.r][i]==false){
	  if (upDown[last.r][i] == false)
	    if (goodPlay(pos(last.r,i),temp))
	      legalMovesList.push_back(pos(last.r,i));
	}
	else
	  break;
      }
    }
  }
  return legalMovesList;
}

//Function to reorder the elements of a vector to have the least amout possible of directional preference.
void RookBoard::reorderLegalMoves(vector<pos> &legalMoves) {
  pos last = *(positions.end()-1);
  vector<pos> result;
  // Randomize the input:
  srand ( unsigned (time(0) ) );
  random_shuffle(legalMoves.begin(), legalMoves.end() );
  // Define a vector to hold legal moves organized by direction
  vector<pos> directions[4];
  // Organize the legal moves by direction:
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
  // Build the result by at each step, calling a move randomly
  // From the availiable directions.
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
  // Assigne the result to legalMoves
  legalMoves = result;
  return;
}

// //Function to put legal moves in shortest to longest order
// void RookBoard::sortLegalMoves(pos initialPos, vector<pos> &legalMoves) {
//   auto f = [initialPos](pos left, pos right) -> bool {return lengthOfMove(initialPos-left)<lengthOfMove(initialPos-right); };
//   sort(legalMoves.begin(), legalMoves.end(), f);
// }

// int lengthOfMove(pos move) {
//   return (abs(move.r) + abs(move.c) );
// }

//Takes a sugested play and returns weither or not it is legal.
bool RookBoard::goodPlay(pos play, int num) {
  // If the suggest move is already occupied, return false
  if (moveArea[play.r][play.c] != 0) return false;
  
  // Otherwise, check that the number placed does not already
  // appear in the row or collumn of the suggested move
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

//Takes a sugested play and returns weither or not it is legal.
bool RookBoard::goodPlayAnyNum(pos play, int num) {
  // If the suggest move is already occupied, return false
  if (moveArea[play.r][play.c] != 0) return false;
  else return true;
}

//A text based print out of the generated game board.
string RookBoard::print() {
  ostringstream oss;
  oss << height << '\n'
      << width << '\n';
  for (int i = 0; i < height; i++) {
    for (int j = 0; j < width; j++) {
      oss << moveArea[i][j] << '\n';
    }
  }
  oss << positions.size() << '\n';
  for (int i = 0; i < positions.size(); i++) {
    oss << positions[i].r << '\n'
	<< positions[i].c << '\n';
  }
  return oss.str();
}

//The recrusive board maker.
bool RookBoard::makeBoard(int depth) {
  // If our puzzle has already reached the desired length, make sure
  // It has both come full circle and that the direction with which
  // it approaches the end location is compatible with the first move
  // made in the puzzle (ie they cannot be the same direction!);
  //cout << depth << endl;
  if (depth == 0) {
    bool a = ((*(positions.begin())).r == (*(positions.end()-1)).r);
    bool b = ((*(positions.begin())).c == (*(positions.end()-1)).c);
    pos dir1 = *(positions.begin()+1)-*(positions.begin());
    pos dir2 = *(positions.end()-1)-*(positions.end()-2);
    bool c = ((dir1.r*dir2.r + dir1.c*dir2.c) <= 0); 
    if (a&b&c) return true;
    else return false;
  }

  // If the puzzle has not been started, choose a random starting point
  if (positions.size() == 0) {
    srand ( unsigned ( time(0) ) );
    int x = rand() % height;
    int y = rand() % width;
    positions.push_back(pos(x,y));
  }
  //Generate all legalMoves from current location
  // vector<pos> lm = legalMoves();

  vector<pos> lm = legalMoves();
  //cout << lm.size() << endl;

  // If there are none, return false
  if (lm.size() == 0) return false;
  // Otherwise, reorder the legal moves according to preference
  // See "reorderLegalMoves" for details!
  

  random_shuffle(lm.begin(), lm.end() );
  //reorderLegalMoves(lm);
 
  // Loop though all possible legal moves
  for (int i = 0; i < lm.size(); i++) {
    // Add the ith legal move to the list
    positions.push_back(lm[i]);
    // and put the correct number into the borad.
    pos last = (*(positions.end()-1) - *(positions.end()-2));
    moveArea[lm[i].r][lm[i].c] = (last.r == 0) ? abs(last.c) : abs(last.r);
    // Now recursivly call makeBoard with one less depth.
    if (makeBoard(depth-1)) return true;
    // If the recrusive call fails, we remove the move we maid from the list,
    // clear that spot on the board and try the next legal move.
    positions.pop_back();
    moveArea[lm[i].r][lm[i].c] = 0;
  }
  // Finally if none of our legal moves work out, we return false.
  return false;
}

//The recrusive board maker.
bool RookBoard::makeBoardRightAnglesNoPassOver(int depth) {
  // If our puzzle has already reached the desired length, make sure
  // It has both come full circle and that the direction with which
  // it approaches the end location is compatible with the first move
  // made in the puzzle (ie they cannot be the same direction!);
  //cout << depth << endl;
  if (positions.size() != 0) {
    bool a = ((*(positions.begin())).r == (*(positions.end()-1)).r);
    bool b = ((*(positions.begin())).c == (*(positions.end()-1)).c);
    bool c = (depth==0);
    // pos dir1 = *(positions.begin()+1)-*(positions.begin());
    // pos dir2 = *(positions.end()-1)-*(positions.end()-2);
    // bool c = ((dir1.r*dir2.r + dir1.c*dir2.c) <= 0); 
    if (a&b&c) return true;
    if (a&b&!c) return false;
    if (c&!(a&b)) return false;
  }
  
  // If the puzzle has not been started, choose a random starting point
  if (positions.size() == 0) {
    srand ( unsigned ( time(0) ) );
    int x = rand() % height;
    int y = rand() % width;
    positions.push_back(pos(x,y));
  }
  //Generate all legalMoves from current location
  // vector<pos> lm = legalMoves();


  vector<pos> lm = legalMovesRightAnglesAnyNumNoPassOver();
  //cout << lm.size() << endl;

  // If there are none, return false
  if (lm.size() == 0) return false;
  // Otherwise, reorder the legal moves according to preference
  // See "reorderLegalMoves" for details!
  

  random_shuffle(lm.begin(), lm.end() );
  //reorderLegalMoves(lm);
 
  // Loop though all possible legal moves
  for (int i = 0; i < lm.size(); i++) {
    // Add the ith legal move to the list
    positions.push_back(lm[i]);
    // and put the correct number into the borad.
    pos last = (*(positions.end()-1) - *(positions.end()-2));
    moveArea[lm[i].r][lm[i].c] = (last.r == 0) ? abs(last.c) : abs(last.r);
    markLRUD(*(positions.end()-1), *(positions.end()-2));
    // printBool(leftRight);
    // cout << endl;
    // printBool(upDown);
    // int ii;
    // cin >> ii; 
    // Now recursivly call makeBoard with one less depth.
    if (makeBoardRightAnglesNoPassOver(depth-1)) return true;
    // If the recrusive call fails, we remove the move we maid from the list,
    // clear that spot on the board and try the next legal move.
    unMarkLRUD(*(positions.end()-1), *(positions.end()-2));
    positions.pop_back();
    moveArea[lm[i].r][lm[i].c] = 0;
  }
  // Finally if none of our legal moves work out, we return false.
  return false;
}

//The recrusive board maker.
bool RookBoard::makeBoardNoPassOver(int depth) {
  // If our puzzle has already reached the desired length, make sure
  // It has both come full circle and that the direction with which
  // it approaches the end location is compatible with the first move
  // made in the puzzle (ie they cannot be the same direction!);
  //cout << depth << endl;
  if (positions.size() != 0) {
    bool a = ((*(positions.begin())).r == (*(positions.end()-1)).r);
    bool b = ((*(positions.begin())).c == (*(positions.end()-1)).c);
    bool c = (depth==0);
    // pos dir1 = *(positions.begin()+1)-*(positions.begin());
    // pos dir2 = *(positions.end()-1)-*(positions.end()-2);
    // bool c = ((dir1.r*dir2.r + dir1.c*dir2.c) <= 0); 
    if (a&b&c) return true;
    if (a&b&!c) return false;
    if (c&!(a&b)) return false;
  }
  
  // If the puzzle has not been started, choose a random starting point
  if (positions.size() == 0) {
    srand ( unsigned ( time(0) ) );
    int x = rand() % height;
    int y = rand() % width;
    positions.push_back(pos(x,y));
  }
  //Generate all legalMoves from current location
  // vector<pos> lm = legalMoves();


  vector<pos> lm = legalMovesNoPassOver();
  //cout << lm.size() << endl;

  // If there are none, return false
  if (lm.size() == 0) return false;
  // Otherwise, reorder the legal moves according to preference
  // See "reorderLegalMoves" for details!
  

  random_shuffle(lm.begin(), lm.end() );
  //sortLegalMoves(*(positions.end()-1) , lm);
  //reorderLegalMoves(lm);
 
  // Loop though all possible legal moves
  for (int i = 0; i < lm.size(); i++) {
    // Add the ith legal move to the list
    positions.push_back(lm[i]);
    // and put the correct number into the borad.
    pos last = (*(positions.end()-1) - *(positions.end()-2));
    moveArea[lm[i].r][lm[i].c] = (last.r == 0) ? abs(last.c) : abs(last.r);
    markLRUD(*(positions.end()-1), *(positions.end()-2));
    // printBool(leftRight);
    // cout << endl;
    // printBool(upDown);
    // int ii;
    // cin >> ii; 
    // Now recursivly call makeBoard with one less depth.
    if (makeBoardNoPassOver(depth-1)) return true;
    // If the recrusive call fails, we remove the move we maid from the list,
    // clear that spot on the board and try the next legal move.
    unMarkLRUD(*(positions.end()-1), *(positions.end()-2));
    positions.pop_back();
    moveArea[lm[i].r][lm[i].c] = 0;
  }
  // Finally if none of our legal moves work out, we return false.
  return false;
}

void RookBoard::markLRUD(pos start, pos end) {
  if (start.r == end.r) {
    for (int i = min(end.c,start.c); i <= max(end.c,start.c); i++) {
      leftRight[start.r][i] = true;
    }
  }
  else {
    for (int i = min(end.r,start.r); i <= max(end.r,start.r); i++) {
      upDown[i][start.c] = true;
    }
  }
  upDown[start.r][start.c] = true;
  upDown[end.r][end.c] = true;
  leftRight[start.r][start.c] = true;
  leftRight[end.r][end.c] = true;

}

void RookBoard::unMarkLRUD(pos start, pos end) {
  if (start.r == end.r) {
    for (int i = min(end.c,start.c); i <= max(end.c,start.c); i++) {
      leftRight[start.r][i] = false;
    }
  }
  else {
    for (int i = min(end.r,start.r); i <= max(end.r,start.r); i++) {
      upDown[i][start.c] = false;
    }
  }

  upDown[start.r][start.c] = false;
  upDown[end.r][end.c] = false;
  leftRight[start.r][start.c] = false;
  leftRight[end.r][end.c] = false;

}

// This marks all spots on the final game borad which were never
// visited or passed through during the course of the puzzle.
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

// Overloaded ofstream &<< to print the game board in a standard format
// which can be ready by any other program.
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

void printBool(vector<vector<bool> > &in) {
  for (int i = 0; i < in.size(); i++) {
    for (int j = 0; j < in[0].size(); j++) {
      cout << in[i][j] << " ";
    }
    cout << endl;
  }
  //return os;
}
