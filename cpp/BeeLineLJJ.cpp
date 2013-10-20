#include "BeeLineLJJ.hpp"

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

BeeLinePuzzle::BeeLinePuzzle(int h, int w, int l, int hintNum) : height(h), width(w), length(l) {
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
    vertical.push_back(bTemp);
    leftUp.push_back(bTemp);
  }
  
  makeBoard(l);
  markUnused();
  do {
    getHints(hintNum);//select the hints to be used;
    checkUnique();
    cout << "try" << endl;
  } while (uniqueCounter > 1);

  // cout << printUnique() << endl;
  // cout << "tPosVec.size(): "<<tPosVec.size() << endl;
}

void BeeLinePuzzle::BeeLinePuzzle(string tag) {
  
}

void BeeLinePuzzle::getHints(int num) {
  vector<int> temp;
  hintsPos.clear();
  hintsNum.clear();
  hintsVertical.clear();
  hintsLeftUp.clear();
  for (int i = 1; i < positions.size(); i++) {
    temp.push_back(i);
  }
  random_shuffle(temp.begin(), temp.end());
  for (int i = 0; i < num; i++) {
    
    hintsPos.push_back(positions[temp[i]]);
    hintsNum.push_back(moveArea[positions[temp[i]].r][positions[temp[i]].c]);

    pos diff = positions[temp[i]] - positions[temp[i]-1];

    hintsVertical.push_back(diff.c == 0);
    hintsLeftUp.push_back((diff.c+diff.r)>0);
  }
  // for (int i = 0; i < hintsPos.size(); i++) {
  //   hintsPos[i].print();
  //   cout << " ";
  // }
  // cout << endl;
}

//function for generating legal moves lists
vector<pos> BeeLinePuzzle::legalMoves() {
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
   if (lastMove.r >= 0) {
    for (int i = last.r; i < height; i++) {
      int temp = i - last.r;
      if (temp > 0) {
	if (pos(i,last.c) == im) {
	  if (goodPlay(pos(i,last.c),last)) {
	    legalMovesList.push_back(pos(i,last.c));
	  }
	  break;
	}
	if (upDown[i][last.c]==false) {
	  if (leftRight[i][last.c]==false)
	    if (goodPlay(pos(i,last.c),last)) {
	      legalMovesList.push_back(pos(i,last.c));
	    }
	}
	else
	  break;
      }
    }
  }
  if (lastMove.r <= 0) {
    for (int i = last.r; i >= 0; i--) {
      int temp = last.r - i;
      if (temp > 0) {
	if (pos(i,last.c) == im) {
	  if (goodPlay(pos(i,last.c),last))
	    legalMovesList.push_back(pos(i,last.c));
	  break;
	}
	if (upDown[i][last.c]==false) {
	  if (leftRight[i][last.c]==false)
	    if (goodPlay(pos(i,last.c),last))
	      legalMovesList.push_back(pos(i,last.c));
	}
	else
	  break;
	
      }
    }
  }
  if (lastMove.c >= 0) {
    for (int i = last.c; i < width; i++) {
      int temp = i - last.c;
      if (temp > 0) {
	if (pos(last.r,i) == im) {
	  if (goodPlay(pos(last.r,i),last))
	    legalMovesList.push_back(pos(last.r,i));
	  break;
	}
	if (leftRight[last.r][i] == false) {
	  if (upDown[last.r][i] == false)
	    if (goodPlay(pos(last.r,i),last))
	      legalMovesList.push_back(pos(last.r,i));
	}
	else
	  break;
      }
    }
  }
  if (lastMove.c <= 0) {
    for (int i = last.c; i >= 0; i--) {
      int temp = last.c - i;
      if (temp > 0) {
	if (pos(last.r,i) == im) {
	  if (goodPlay(pos(last.r,i),last))
	    legalMovesList.push_back(pos(last.r,i));
	  break;
	}
	if (leftRight[last.r][i]==false){
	  if (upDown[last.r][i] == false)
	    if (goodPlay(pos(last.r,i),last))
	      legalMovesList.push_back(pos(last.r,i));
	}
	else
	  break;
      }
    }
  }
  return legalMovesList;
}

vector<pos> BeeLinePuzzle::legalMovesTestUnique() {
  // Find the last position.
  pos last = *(tPosVec.end()-1);
  // Initiate the LegalMovesList
  vector<pos> legalMovesList;
  //Find the last move made.
  pos lastMove(0,0);
  if (tPosVec.size() > 1) {
    lastMove = *(tPosVec.end()-1)-*(tPosVec.end()-2);
  }
 
  pos im = *(tPosVec.begin());
 
  //Do a search for legal moves in the four directions
  // This checks first that the direction is in fact legal
   if (lastMove.r >= 0) {
    for (int i = last.r; i < height; i++) {
      int temp = i - last.r;
      if (temp > 0) {
	if (pos(i,last.c) == im) {
	  if (goodPlayTestUnique(pos(i,last.c),last)) {
	    legalMovesList.push_back(pos(i,last.c));
	  }
	  break;
	}
	if (upDown[i][last.c]==false) {
	  if (leftRight[i][last.c]==false)
	    if (goodPlayTestUnique(pos(i,last.c),last)) {
	      legalMovesList.push_back(pos(i,last.c));
	    }
	}
	else
	  break;
      }
    }
  }
  if (lastMove.r <= 0) {
    for (int i = last.r; i >= 0; i--) {
      int temp = last.r - i;
      if (temp > 0) {
	if (pos(i,last.c) == im) {
	  if (goodPlayTestUnique(pos(i,last.c),last))
	    legalMovesList.push_back(pos(i,last.c));
	  break;
	}
	if (upDown[i][last.c]==false) {
	  if (leftRight[i][last.c]==false)
	    if (goodPlayTestUnique(pos(i,last.c),last))
	      legalMovesList.push_back(pos(i,last.c));
	}
	else
	  break;
	
      }
    }
  }
  if (lastMove.c >= 0) {
    for (int i = last.c; i < width; i++) {
      int temp = i - last.c;
      if (temp > 0) {
	if (pos(last.r,i) == im) {
	  if (goodPlayTestUnique(pos(last.r,i),last))
	    legalMovesList.push_back(pos(last.r,i));
	  break;
	}
	if (leftRight[last.r][i] == false) {
	  if (upDown[last.r][i] == false)
	    if (goodPlayTestUnique(pos(last.r,i),last))
	      legalMovesList.push_back(pos(last.r,i));
	}
	else
	  break;
      }
    }
  }
  if (lastMove.c <= 0) {
    for (int i = last.c; i >= 0; i--) {
      int temp = last.c - i;
      if (temp > 0) {
	if (pos(last.r,i) == im) {
	  if (goodPlayTestUnique(pos(last.r,i),last))
	    legalMovesList.push_back(pos(last.r,i));
	  break;
	}
	if (leftRight[last.r][i]==false){
	  if (upDown[last.r][i] == false)
	    if (goodPlayTestUnique(pos(last.r,i),last))
	      legalMovesList.push_back(pos(last.r,i));
	}
	else
	  break;
      }
    }
  }
  return legalMovesList;
}

//Takes a sugested play and returns weither or not it is legal.
bool BeeLinePuzzle::goodPlay(pos play, pos orig) {
  // If the suggest move is already occupied, return false
  if (moveArea[play.r][play.c] != 0) return false;
  
  // Otherwise, check that the number placed is a proper
  // distance from any matching number in the row or col.

  pos diff = play - orig;
  int num = abs(diff.r)+abs(diff.c);    


  if (!goodDir( play, num, diff.c == 0 ,(diff.c+diff.r)>0)) {
    return false;
  }
  if (moveArea[orig.r][orig.c] == num)
    return false;

  for (int i = play.r; i < height; i++) {
    if (i != play.r) {
      if (moveArea[i][play.c] == num && vertical[i][play.c] && leftUp[i][play.c]) {
  	return false;
      }
    }
  }

  for (int i = play.r; i >= 0; i--) {
    if (i != play.r) {
      if (moveArea[i][play.c] == num && vertical[i][play.c] && !leftUp[i][play.c]) {
	return false;
      }
    }
  }

  for (int i = play.c; i < width; i++) {
    if (i != play.c) {
      if (moveArea[play.r][i] == num && !vertical[play.r][i] && leftUp[play.r][i]) {
  	return false;
      }
    }
  }

  for (int i = play.c; i >= 0; i--) {
    if (i != play.c) {
      if (moveArea[play.r][i] == num && !vertical[play.r][i] && !leftUp[play.r][i]) {
  	return false;
      }
    }
  }
  return true;
}

bool BeeLinePuzzle::goodPlayTestUnique(pos play, pos orig) {
  // If the suggest move is already occupied, return false
  if (gameBoard[play.r][play.c] != 0) return false;
  
  // Otherwise, check that the number placed is a proper
  // distance from any matching number in the row or col.

  pos diff = play - orig;
  int num = abs(diff.r)+abs(diff.c);    
  
  bool vert = (diff.c == 0);
  bool leup = ((diff.c+diff.r)>0);

  for (int i = 0; i < hintsPos.size(); i++) {
    if (hintsPos[i] == play) {
      if (hintsNum[i] == num && hintsVertical[i] == vert && hintsLeftUp[i] == leup) {
      }
      else {
	return false;
      }
    }
  }
  
  if (!goodDirTestUnique( play, num, vert, leup)) {
    return false;
  }
  if (gameBoard[orig.r][orig.c] == num) {
    return false;
  }

  for (int i = play.r; i < height; i++) {
    if (i != play.r) {
      if (gameBoard[i][play.c] == num && vertical[i][play.c] && leftUp[i][play.c]) {
  	return false;
      }
    }
  }

  for (int i = play.r; i >= 0; i--) {
    if (i != play.r) {
      if (gameBoard[i][play.c] == num && vertical[i][play.c] && !leftUp[i][play.c]) {
	return false;
      }
    }
  }

  for (int i = play.c; i < width; i++) {
    if (i != play.c) {
      if (gameBoard[play.r][i] == num && !vertical[play.r][i] && leftUp[play.r][i]) {
  	return false;
      }
    }
  }

  for (int i = play.c; i >= 0; i--) {
    if (i != play.c) {
      if (gameBoard[play.r][i] == num && !vertical[play.r][i] && !leftUp[play.r][i]) {
  	return false;
      }
    }
  }
  return true;
}

bool BeeLinePuzzle::goodDir(pos in, int temp, bool v, bool ul) {
  if (positions.size() == 1)
    return true;

  //int temp = moveArea[in.r][in.c];


  if (v) {
    if (ul) {
      for (int i = in.r; i >=0; i--) {
  	if (i != in.r && moveArea[i][in.c]==temp) {
  	  return false;
  	}
      }
    }
    else {
      for (int i = in.r; i < height; i++) {
  	if (i != in.r && moveArea[i][in.c]==temp) {
  	  return false;
  	}
      }
    }
  }
  else {
    if (ul) {
      for (int i = in.c; i >=0; i--) {
  	if (i != in.c && moveArea[in.r][i]==temp) {
  	  return false;
  	}
      }
    }
    else {
      for (int i = in.c; i < width; i++) {
  	if (i != in.c && moveArea[in.r][i]==temp) {
  	  return false;
  	}
      }
    }
  }
  return true;
}

bool BeeLinePuzzle::goodDirTestUnique(pos in, int temp, bool v, bool ul) {
  if (tPosVec.size() == 1)
    return true;

  if (v) {
    if (ul) {
      for (int i = in.r; i >=0; i--) {
  	if (i != in.r && gameBoard[i][in.c]==temp) {
  	  return false;
  	}
      }
    }
    else {
      for (int i = in.r; i < height; i++) {
  	if (i != in.r && gameBoard[i][in.c]==temp) {
  	  return false;
  	}
      }
    }
  }
  else {
    if (ul) {
      for (int i = in.c; i >=0; i--) {
  	if (i != in.c && gameBoard[in.r][i]==temp) {
  	  return false;
  	}
      }
    }
    else {
      for (int i = in.c; i < width; i++) {
  	if (i != in.c && gameBoard[in.r][i]==temp) {
  	  return false;
  	}
      }
    }
  }
  return true;
}

//The recrusive board maker.
bool BeeLinePuzzle::makeBoard(int depth) {
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
  // if (depth == 0)
  //   return true;

  
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
  //vector<pos> lm = legalMovesNoPassOver();
  //cout << lm.size() << endl;

  // If there are none, return false
  if (lm.size() == 0) return false;
  // Otherwise, reorder the legal moves according to preference
  // See "reorderLegalMoves" for detail>s!
  

  random_shuffle(lm.begin(), lm.end() );
  //sortLegalMoves(*(positions.end()-1) , lm);
  //reorderLegalMoves(lm);
 
  // Loop though all possible legal moves
  for (int i = 0; i < lm.size(); i++) {
    // Add the ith legal move to the list
    positions.push_back(lm[i]);
    // and put the correct number into the borad.
    pos last = (*(positions.end()-1) - *(positions.end()-2));
    pos secLast = *(positions.end() -2);
    moveArea[lm[i].r][lm[i].c] = (last.r == 0) ? abs(last.c) : abs(last.r);
    markLRUD(*(positions.end()-1), *(positions.end()-2));
    setDir(last, *(positions.end()-1));
    // printBool(leftRight);
    // cout << endl;
    // printBool(upDown);
    // int ii;
    // cin >> ii; 
    // Now recursivly call makeBoard with one less depth.
    if (makeBoard(depth-1)) return true;
    // If the recrusive call fails, we remove the move we maid from the list,
    // clear that spot on the board and try the next legal move.
    unMarkLRUD(*(positions.end()-1), *(positions.end()-2));
    positions.pop_back();
    moveArea[lm[i].r][lm[i].c] = 0;
  }
  // Finally if none of our legal moves work out, we return false.
  return false;
}

void BeeLinePuzzle::checkUnique() {
  clearBoolMats();
  gameBoard = moveArea;
  tPosVec.clear();
  for (int i = 0; i < height; i++) {
    for (int j = 0; j < width; j++) {
      if (gameBoard[i][j] != -1)
	gameBoard[i][j] = 0;
    }
  }
  tPosVec.push_back(hintsPos[0]);
  uniqueCounter = 0;

  countAllSolutions();

}

void BeeLinePuzzle::countAllSolutions() {
  bool a = ((*(tPosVec.begin())) == (*(tPosVec.end()-1)));
  if (a) {
    if (boardIsFull() ) {
      bool b = true;
      for (int i = 0; i < hintsPos.size(); i++) {
	b &= (gameBoard[hintsPos[i].r][hintsPos[i].c] == hintsNum[i]); 
      }
      if (b) {
	uniqueCounter += 1;
	// cout << uniqueCounter << endl;
	// for (int i = 0; i < tPosVec.size(); i++) {
	//   tPosVec[i].print();
	//   cout << endl;
	// ;	
	// cout << printUnique(); 
	// cout << endl;
	// printBool(leftRight);
	// cout << endl;
	// printBool(upDown);
	// cout << endl;
	// cout << endl;
      }
    }
  }
    
  vector<pos> lm = legalMovesTestUnique();
  // If there are none, return
  if (lm.size() == 0) return;   
  // Loop though all possible legal moves
  for (int i = 0; i < lm.size(); i++) {
    // Add the ith legal move to the list
    tPosVec.push_back(lm[i]);
    // and put the correct number into the borad.
    pos last = (*(tPosVec.end()-1) - *(tPosVec.end()-2));
    pos secLast = *(tPosVec.end()-2);
    
    gameBoard[lm[i].r][lm[i].c] = (last.r == 0) ? abs(last.c) : abs(last.r);
    markLRUD(*(tPosVec.end()-1), *(tPosVec.end()-2));
    setDir(last, *(tPosVec.end()-1));
    // Now recursivly call makeBoard with one less depth.
    countAllSolutions();
    // clear that spot on the board and try the next legal move.
    unMarkLRUD(*(tPosVec.end()-1), *(tPosVec.end()-2));
    tPosVec.pop_back();
    gameBoard[lm[i].r][lm[i].c] = 0;
  }
}

//EXTRA UTILITY FUNCTIONS
bool BeeLinePuzzle::boardIsFull() {

  for (int i = 0; i < height; i++) {
    for (int j = 0; j < width; j++) {
      if (!(gameBoard[i][j] == -1) && (!upDown[i][j]) && (!leftRight[i][j])) {
	return false;
      }
    }
  }
  return true;
}

void BeeLinePuzzle::markLRUD(pos start, pos end) {
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

void BeeLinePuzzle::unMarkLRUD(pos start, pos end) {
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

void BeeLinePuzzle::setDir(pos lastMove, pos last) {
  if (lastMove.r == 0) {
    vertical[last.r][last.c] = false;
    if (lastMove.c > 0) {
      leftUp[last.r][last.c] = true;
    }
    else {
      leftUp[last.r][last.c] = false;
    }
  }
  else {
    vertical[last.r][last.c] = true;
    if (lastMove.r > 0) {
      leftUp[last.r][last.c] = true;
    }
    else {
      leftUp[last.r][last.c] = false;
    }
  }
}  

void BeeLinePuzzle::clearBoolMats() {
  for (int i = 0; i < height; i++) {
    for (int j = 0; j < width; j++) {
      leftUp[i][j] = false;
      vertical[i][j] = false;
      upDown[i][j] = false;
      leftRight[i][j] = false;
    }
  }
}

// This marks all spots on the final game board which were never
// visited or passed through during the course of the puzzle.
void BeeLinePuzzle::markUnused() {
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

//PRINTING BOARDS TO FILE

//A text based print out of the generated game board.
string BeeLinePuzzle::print() {
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

string BeeLinePuzzle::printUnique() {
  ostringstream oss;
  for (int i = 0; i < height; i++) {
    for (int j = 0; j < width; j++) {
      if (gameBoard[i][j] == -1) {
	oss << "x" << " ";
      }
      else {
	oss << gameBoard[i][j] << " ";
      }
    }
    oss << "\n";
  }
  return oss.str();
}

string BeeLinePuzzle::printPuzzle() {
  ostringstream oss;
  for (int i = 0; i < height; i++) {
    for (int j = 0; j < width; j++) {
      if (moveArea[i][j] == -1) {
	oss << "x" << " ";
      } else {
	pos t(i,j);
	bool b = false;
	for (int k = 0; k < hintsPos.size(); k++) {	  
	  if (t == hintsPos[k]) {
	    oss << hintsNum[k];
	    b = true;
	  }
	}
	if (!b) {
	  oss << "0";
	} 
      }
    }
    oss << "\n";
  }
  return oss.str();
}

// Overloaded ofstream &<< to print the game board in a standard format
// which can be ready by any other program.
ofstream &operator<<(ofstream &ofs, BeeLinePuzzle &RB) {
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
