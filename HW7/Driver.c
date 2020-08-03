/*****************************************************************************
                                                                   Kathy Li 
                                                        CSE 12, Spring 2019
                                                                May 7, 2019                                                                  
                                                                    cs12xee   
                              Assignment Seven
Filename:    Driver.java 
 Description: This program tests the functions of an array-based hash table

******************************************************************************/
#include <cstdlib>
#include <iostream>
#include <string.h>
#include "Base.h"
#include "SymTab.h"
using namespace std;

#ifdef NULL
#undef NULL
#define NULL 0
#endif

//=============================================================================
// Class Name: UCSDStudent
// Description: This class contains constructors and functions for the 
//     Base object UCSDStudent
// Data Fields: 
//     name (char*) 		- name of the student
//     studentnum (long) 	- ID of the student
// Public Functions/Operators: 
//     const char * 		- Gets name of student
//     == 					- Determines equality of two students
//     < 					- Determines precedence of two students
//     Write 				- toString method of student
//     long 				- Calculates the hashcode of a student
//============================================================================
class UCSDStudent : public Base {
	char * name;
	long studentnum;
public:
	/*-------------------------------------------------------------------------
    Constructor
    Input:              nm: The name of the student
                        num: The ID of the student
    --------------------------------------------------------------------------*/
    UCSDStudent(char * nm, long num = 0):
		name(strdup(nm)), studentnum(num){}

	~UCSDStudent(void){
		free(name);
	}

    /*-------------------------------------------------------------------------
    Operator:      		const char*
    Purpose:            Gets the name of student
    Input:              None.
    Output:             Returns the name of student
    --------------------------------------------------------------------------*/
	operator const char * (void) const {
		return name;
	}

	/*-------------------------------------------------------------------------
    Operator:      		==
    Purpose:            Checks equality of two students by comparing names
    Input:              bbb: Another Base object to compare to
    Output:             Returns 1 if equal, 0 if not.
    --------------------------------------------------------------------------*/
	long operator == (const Base & bbb) const {
		return ! strcmp (name, bbb);
	}

	/*-------------------------------------------------------------------------
    Operator:			<
    Purpose:            Checks precedence between two students
    Input:              bbb: Another Base object to compare to
    Output:             Returns 1 if input object is greater than calling
                        object, 0 otherwise.
    --------------------------------------------------------------------------*/
	long operator > (const Base & bbb) const {
		return (strcmp (name, bbb) > 0) ? 1 : 0;
	}

	/*-------------------------------------------------------------------------
    Function Name:		Write
    Purpose:            Converts student to String representation
    Input:              stream: stream to write to
    Output:             Returns string of student's name and ID
    --------------------------------------------------------------------------*/
	ostream & Write (ostream & stream) const {
		return stream << "name:  " << name
			<< "  studentnum:  " << studentnum;
	}
};

int main (int argc, char * const * argv) {
	char buffer[80];
	char command;
	long number;

	Tree::Set_Debug(0);

	if (argc != 1 && strcmp("-x", argv[1]) == 0) {
		Tree::Set_Debug(1);
	}
	
	SymTab * ST;
	ST = new SymTab ("UCSDStudentTree");
	ST->Write (cout << "Initial Symbol Table:\n");

	while (cin) {
		command = NULL;		// reset command each time in loop
		cout <<  "Please enter a command:  ((a)llocate, is(e)mpty," <<
			" (i)nsert, (l)ookup, (r)emove, (w)rite):  ";
		cin >> command;

		switch (command) {

		case 'a':
			cout << "Please enter name of new Tree to allocate:  ";
			cin >> buffer; 

			delete ST;
			ST = new SymTab (buffer);

			break;

		case 'e':
                        if (ST->IsEmpty ())
                                cout << "Tree is empty." << endl;
                        else
                                cout << "Tree is not empty." << endl;
                        break;
			
		case 'i':
			cout << "Please enter UCSD student name to insert:  ";
			cin >> buffer;	// formatted input

			cout << "Please enter UCSD student number:  ";
			cin >> number;

			// create student and place in symbol table
			ST->Insert (new UCSDStudent (buffer, number));
			break;

		case 'l': {
			const Base * found;	// whether found or not

			cout << "Please enter UCSD student name to lookup:  ";
			cin >> buffer;	// formatted input

			UCSDStudent stu (buffer, 0);
			found = ST->Lookup (&stu);
			
			if (found)
				found->Write (cout << "Student found!\n") << "\n";
			else
				cout << "student " << buffer << " not there!\n";
			}
			break;
		
		case 'r': {
			Base * removed;	// data to be removed

			cout << "Please enter UCSD student name to remove:  ";
			cin >> buffer;	// formatted input

			UCSDStudent stu (buffer, 0);
			removed = ST->Remove (&stu);

			if (removed)
				removed->Write (cout << "Student removed!\n") << "\n";
			else
				cout << "student " << buffer << " not there!\n";
			}
			break;


		case 'w':
			ST->Write (cout << "The Symbol Table contains:\n");
		}
	}

	ST->Write (cout << "\nFinal Symbol Table:\n");
	delete ST;
}
