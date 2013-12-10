#ifndef PUZZLE_BOOK_DATA
#define PUZZLE_BOOK_DATA

#include <vector>
#include <map>

using namespace std;

//Simple Data storage class
class PuzzleBookData {
public:
  PuzzleBookData(){};

  map<string, string> puzzleBookAttr;  

  vector<string> files;
  vector<string> chapterTitles;
  vector<string> chapterEndText;  
  vector<string> beforeImages;
  vector< vector<string> > afterImages;    
  vector<string> beforeFlower;
  vector<string> afterFlower;
  int startIndex = 0;
  map<int, string> textMap;
  
  int currChapter = 0;

  string getChapterTitle(){return chapterTitles[currChapter];};
  string getChapterEndText(){return chapterEndText[currChapter];};
  string getBeforeImage(){return beforeImages[currChapter];};
  vector<string> getAfterImages(){return afterImages[currChapter];};
  string getBeforeFlower(){return beforeFlower[currChapter];};
  string getAfterFlower(){return afterFlower[currChapter];};
};
#endif
