#include "rapidxml.hpp"
#include "rapidxml_print.hpp"
#include <iostream>
#include <fstream>
#include <string>
#include <vector>

using namespace std;
using namespace rapidxml;

int main(int argc, char *argv[])
{
  // ifstream ifs("thing.xml");
  // string content((istreambuf_iterator<char>(ifs)), (istreambuf_iterator<char>() ));
 
  // ifs.close();

  // xml_document<> doc;
  // doc.parse<0>(const_cast<char *>(content.c_str() ) );

  xml_document<> doc;
  
  xml_node<> *node = doc.allocate_node(node_element, "aaa");
  doc.append_node(node);
  xml_node<> *node2 = doc.allocate_node(node_element, "bb","dog");
  node->append_node(node2);  
  node2 = doc.allocate_node(node_element, "bb","dog");
  node->append_node(node2);
  xml_attribute<> *attr = doc.allocate_attribute("href", "google.com");
  node->append_attribute(attr);
  

  // for (xml_node<> *node = doc.first_node()->first_node(); node; node = node->next_sibling()) {
  //   cout << node->value() << endl;
  // }


  cout << doc;


  // cout << doc.first_node()->name() << endl;



  return 0;
}
