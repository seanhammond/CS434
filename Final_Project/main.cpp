//#include <windows.h>
#include <stdio.h>
#include <iostream>
#include <string>
#include <stdlib.h>
#include <fstream>
#include <string.h>
#include <set>
#include <array>
#include <list>

using namespace std;

#include <cstddef>



int main( int argc, char *argv[ ] )
{
	string fileToOpen = "Day7.svm";
	string fileToCreate = "LessDay7.svm";

	array<int, 64> feature = {4, 5, 6, 8, 11, 16, 17, 18, 19, 21, 22, 23, 30, 33, 35, 39, 41, 43, 55, 57, 59, 61, 63, 65, 67, 69, 71, 73, 75, 77, 79, 81, 83, 85, 87, 89, 91, 93, 95, 97, 99, 101, 103, 105, 107, 109, 111, 113, 120, 126, 132, 134, 136, 138, 140, 142, 144, 146, 148, 150, 161, 194, 270, 7801 };
	set<int> features;// (featureNum, featureNum.size());
	for (int i = 0; i < feature.size(); i++){
		features.insert(feature[i]);
	}

	ifstream input;
	input.open(fileToOpen);
	ofstream file(fileToCreate, ofstream::out);
	list<string> line_tokens;
	char * pch;
	for (std::string line; getline(input, line);)
	{
		char *cstr = new char[line.length() + 1];
		strcpy(cstr, line.c_str());
		pch = strtok (cstr, " ");
		while (pch != NULL)
		{
			line_tokens.push_back(pch);
			pch = strtok (NULL, " ");
		}
		file << line_tokens.front() << " ";
		//cout << "class label:" << line_tokens.front() << " ";
		line_tokens.pop_front();
		for (std::list<string>::iterator it=line_tokens.begin(); it != line_tokens.end(); ++it)
		{
			string temp = *it;
			char *ctemp = new char[20];
			strcpy(ctemp, temp.c_str());
			char *cnum = strtok (ctemp, ":");
			int num = atoi(cnum);
			//cout << "Number is " << num << endl;
			std::set<int>::iterator in;
			in = features.find(num);
			if (in != features.end()){
				file << temp << " ";
				//cout << "data " << temp << " ";
			}
		}
		file << "\n";
		line_tokens.clear();
	}
	file.close();
	input.close();
	return 0;
}

