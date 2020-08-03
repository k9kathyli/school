/*****************************************************************************
                                                                    Kathy Li 
                                                         CSE 12, Spring 2019
                                                                May 17, 2019
                                                                     cs12xee   
                              Assignment Seven
Filename:    Driver.java 
Description: This program tests the functions of an binary search tree.
******************************************************************************/
#include <cstdlib>
#include <cstdio>
#include <iostream>
#include <string.h>
#include "Tree.h"
using namespace std;

#ifndef TRUE
#define TRUE 1
#endif

#ifndef FALSE
#define FALSE 0
#endif

static const char ALLOCATE[] = " - Allocating]\n";
static const char DEALLOCATE[] = " has been deallocated]\n";
static const char AND[] = " and ";
static const char COMPARE[] = " - Comparing ";
static const char INSERT[] = " - Inserting ";
static const char TREE[] = "[Tree ";


//=========================================================================
// struct Node
//
// Description: Implements the node of the binary search tree data structure.
//     Each node contains two children, the left child which contains
//     data "less than" the data of the current node, and the right child
//     which contains data "greater than" the data of the current node.
//
// Data Fields:
//     data (Base *)  - holds the data stored in the current node
//     left (TNode *)  - the left child
//     right (TNode *) - the right child
//	   parent (TNode *) - the parent node
//	   balance (long) - the balance of the current node
//	   height (long) - the height of the current node
//	   hasBeenDeleted (unsigned long) - whether the node has been deleted or not
//
// Public functions:
//     delete_AllNodes - deletes all the children of the current node
//     Write - display the current node
//     Write_AllNodes - display the current node and all its children
//
//==========================================================================
struct TNode {
	Base * data;
	TNode * left, * right, *parent;
	static long occupancy;
        unsigned long hasBeenDeleted;
	long balance;
	long height;

	TNode (Base * element) : data (element), left (0), right (0),
	        parent (0), hasBeenDeleted (FALSE), balance (0), height(0) {
		if (element){
			// Every time a TNode is initialized, the occupancy increases 
			occupancy++;		
		}
	}
	~TNode (void) {
		delete data;
	}
    ostream & Write (ostream & stream) const {
                stream << "at height:  " << height << "  with balance:  "
			<< balance << "  ";
		return data->Write (stream) << "\n";
        }

	ostream & Write_AllTNodes (ostream & stream) const {
		if (left)
			left->Write_AllTNodes (stream);
		if (!hasBeenDeleted)
			Write (stream);
		if (right)
			right->Write_AllTNodes (stream);

		return stream;
	}
	/***************************************************************************
	% Routine Name : delete_AllTNodes
	% File :         Tree.c
	% 
	% Description :  Recursively deallocates the nodes of the tree
	***************************************************************************/
	void delete_AllTNodes (void) {
		if (!this){
			return;
		}
		(this->left)->delete_AllTNodes();
		(this->right)->delete_AllTNodes();
		delete this; 

		
	}
};

// initialization of static data fields of Tree
long TNode :: occupancy = 0;
bool Tree :: debug_on = 0;


/***************************************************************************
% Routine Name : Tree:: Set_Debug (public)
% File :         Tree.c
% 
% Description :  This function sets debug mode on or off
%
% Parameters descriptions :
% 
% name               description
% ------------------ ------------------------------------------------------
% option             true should set debug mode on, false should set debug
%                    mode off.
***************************************************************************/
void Tree :: Set_Debug (bool option) {

    if (option){
		debug_on = true;
	}else{
		debug_on = false;
	}
}

Tree :: Tree (const char * name) : root (0), tree_name (strdup (name))
/***************************************************************************
% Routine Name : Tree :: Tree  (public)
% File :         Tree.c
% 
% Description :  Initializes root pointer to NULL, and occupancy to 0.
***************************************************************************/

{		
	// Initialize occupancy to 0
	root->occupancy = 0;

	// Debug
	if (debug_on){
		cerr << TREE << name << ALLOCATE; 
	}
}

Tree :: ~Tree (void)
/***************************************************************************
% Routine Name : Tree :: ~Tree  (public)
% File :         Tree.c
% 
% Description :  deallocates memory associated with the Tree.  It
%                will also delete all the memory of the elements within
%                the table. No return value.
***************************************************************************/

{
	// Delete all nodes from root 
	root->delete_AllTNodes();
	// Free tree name
	free((char*) tree_name);
	
}	/* end: ~Tree */

/***************************************************************************
% Routine Name : Tree :: IsEmpty (public)
% File :         Tree.c
% 
% Description :  Checks if the tree is empty or not
% Output:		 Returns 1 if empty, 0 otherwise
***************************************************************************/
unsigned long Tree :: IsEmpty () const {
	
	if (!root || root->occupancy == 0){
		return 1;
	}
	return 0;
}

/***************************************************************************
% Routine Name : Tree :: Insert (public)
% File :         Tree.c
% 
% Description :  Inserts the element in the binary tree. Duplicate 
%                 insertions will cause the existing element to be 
%                 deleted, and the duplicate to take its place.
% Input:         element: complete elements to insert
% Output:        Returns true if successfully inserted, false otherwise. 
***************************************************************************/
unsigned long Tree :: Insert (Base * element) {
    TNode *working = root;               // Working node
    long rightChildHeight = -1;         // Right child height
    long leftChildHeight = -1;          // Left child height 

	// Insert item at root or elsewhere down the tree 
    if (!root){
		if (debug_on){
			cerr << tree_name << INSERT << (const char*) *element << "\n";
		}		
		root = new TNode(element);
				
	}else{
		while (working){
			if (debug_on){
				cerr << tree_name << COMPARE << (const char*) *element << AND 
				<< (const char*) *working->data << "\n";
			}
			// Handle duplicate insert 
			if (*element == *(working->data)){
				if (working->hasBeenDeleted){
					// Not actually a duplicate insert if it's been deleted
					root->occupancy++;
					
				}
				working->hasBeenDeleted = false;
				working->data = element;
				break;

			//Check to go right 	
			}else if ((*element) > (*working -> data)){
							
				// If right node is null, insert element
				if (!working->right){

					if (debug_on){
						cerr << tree_name << INSERT << 
						(const char*) *element << "\n";
					}	

					working->right = new TNode(element);
					working->right->parent = working;		
					break;

				// Else keep traversing down
				}else{
					working = working->right;
				}
			// Else go left 
			}else{
				if (!working->left){

					if (debug_on){
						cerr << tree_name << INSERT << 
						(const char*) *element << "\n";
					}

					working->left = new TNode(element);
					working->left->parent = working;
					break;
				// Else keep traversing left 
				}else{
					working = working->left;
				
				}
			}
		}
	}

	// Use parent pointer to travel back up the tree 
	while (working){ 
		// Set left and right children heights
		if (working->left){
			leftChildHeight = working->left->height;
		}
		if (!working->left){
			leftChildHeight = -1;
		}
	
		if (working->right){
			 rightChildHeight = working->right->height;
		}
		if (!working->right){
			rightChildHeight = -1;
		}
		// Calculate height and balance of current node
		if (leftChildHeight >= rightChildHeight){
			working->height = leftChildHeight + 1;
		}else{
			working->height = rightChildHeight  +1;
		} 
		working->balance = leftChildHeight - rightChildHeight; 
		
		// Move up the tree
		working = working->parent;
	}
	return 0;
}
/***************************************************************************
% Routine Name : Tree :: Lookup (public)
% File :         Tree.c
% 
% Description :  Looks up an element in the tree
% Input:         element: incomplete elements (No number)
% Output:        Returns the element if found, null otherwise
***************************************************************************/
const Base * Tree :: Lookup (const Base * element) const {
	TNode *working = root;			// Working node

	while (working){
		// Debug
		if (debug_on){
			cerr << tree_name << COMPARE << (const char*) *element << AND <<
			(const char*) *(working->data) << "\n";
		}
		//Check root node 
		if (*element == *(working->data) && !working->hasBeenDeleted){
			return working->data;
		}
		// Check to go right
		if (*element > *(working->data)){
			// If node is null, continue
			if(!working->right){
				break;
			// If right node is null, insert element
			}else if (*(working->right->data) == *(element) && 
				!working->right->hasBeenDeleted){
				return working->right->data;				

			// Else keep traversing down
			}else {
				working = working->right;
			}
		// Else go left
		}else{ 
			// If node is null, element is missing 
			if (!working->left){
				break;
			// If node is found and it's not deleted, return it
			}else if (*(working->left->data) == *(element) && 
				!working->left->hasBeenDeleted){
				return working->left->data;
			/// Else keep traversing down
			}else{
				working = working->left;
		
			}
		}		
	}
	return 0; 		// Element not there
}

/***************************************************************************
% Routine Name : Tree :: Remove (public)
% File :         Tree.c
% 
% Description :  Removes an element in the tree
% Input:         element: incomplete elements (No number)
% Output:        Returns and deletes the element if found, null otherwise
***************************************************************************/
Base * Tree :: Remove (const Base * element) {
    TNode *working = root;			// Working node
				
	while (working){

		// Debug
		if (debug_on){
			cerr << tree_name << COMPARE << (const char*) *element << 
			AND << (const char*)  *(working->data) << "\n";
		}

		// Check for removal of root node
		if (*element == *(working->data)){
			// Unless it has already been deleted
			if (working->hasBeenDeleted){
				return 0;
			}
			working->hasBeenDeleted = true;
			working->occupancy--;
			return working->data;
		}
		//Check to go right 	
		if (*element > *(working->data)){
			// If node is null, continue
			if (!working->right){
				break;
			// If not is found, delete it and return it
			}else if (*(working->right->data) == *element){
				if (working->right->hasBeenDeleted){
					return 0; 
				}
				working->occupancy--;
				working->right->hasBeenDeleted = true;
				return working->right->data;				

			// Else keep traversing down
			}else {
				if (working->right && working->right->hasBeenDeleted){
					break;
				}
				working = working->right;
			}
		// Else go left 
		}else{ 
			// If node is null, continue
			if (!working->left){
				break;
			// If node is found, delete it and return it
			}else if (*(working->left->data) == *element){
				if (working->left->hasBeenDeleted){
					return 0; 
				}	
				working->occupancy--;
				working->left->hasBeenDeleted = true;
				return working->left->data;
			// Else keep traversing down
			}else{
				// Unless the node has been deleted
				if (working->left && working->left->hasBeenDeleted){
					break;
				}

				working = working->left;
		
			}
		}
	}
	
	return 0;
}

ostream & Tree :: Write (ostream & stream) const
/***************************************************************************
% Routine Name : Tree :: Write (public)
% File :         Tree.c
% 
% Description : This function will output the contents of the Tree table
%               to the stream specificed by the caller.  The stream could be
%               cerr, cout, or any other valid stream.
%
% Parameters descriptions :
% 
% name               description
% ------------------ ------------------------------------------------------
% stream             A reference to the output stream.
% <return>           A reference to the output stream.
***************************************************************************/

{
        stream << "Tree " << tree_name << ":\n"
		<< "occupancy is " << TNode :: occupancy << " elements.\n";

	return (root) ? root->Write_AllTNodes (stream) : stream;
}       /* end: Write */

