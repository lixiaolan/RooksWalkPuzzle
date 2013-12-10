#include <vector>

using namespace std;

class PuzzleBookData {
public:
  PuzzleBookData(){};
  vector<string> files;
  string bookTitle;
  string kickBack = "1";
  string bookStyle = "default";  
  vector<string> chapterTitles;
  vector<string> chapterEndText;  
  vector<string> beforeImages;
  vector<string> temp;
  vector< vector<string> > afterImages;    
  vector<string> beforeFlower;
  vector<string> afterFlower;
  int startIndex = 0;
  map<int, string> textMap;
}
