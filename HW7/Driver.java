/*****************************************************************************
                                                                    Kathy Li 
                                                         CSE 12, Spring 2019
                                                                May 17, 2019
                                                                     cs12xee   
                              Assignment Seven
Filename:    Driver.java 
Description: This program tests the functions of an binary search tree.
******************************************************************************/

import java.io.*;

//=============================================================================
// Class Name: UCSDStudent
// Description: This class contains constructors and functions for the Base
//     object UCSDStudent
// Data Fields: 
//     name (String) - name of the student
//     studentnum (long) - ID of the student
// Public Functions: 
//     getName - Gets name of student
//     equals - Determines equality of two students
//     isGreaterThan - Determines precedence of two students
//     toString - toString method of student
 //============================================================================
class UCSDStudent extends Base {

    private String name;
    private long studentnum;

    /*-------------------------------------------------------------------------
    Constructor
    Input:              nm: The name of the student
                        num: The ID of the student
    --------------------------------------------------------------------------*/
    public UCSDStudent(String nm, long num){
		name = new String(nm);
		studentnum = num;
	}
	
    /*-------------------------------------------------------------------------
    Function Name:      getName
    Purpose:            gets the name of student
    Input:              None.
    Output:             returns the name of student
    --------------------------------------------------------------------------*/
	public String getName(){
		return name;
	}

    /*-------------------------------------------------------------------------
    Function Name:      equals
    Purpose:            Checks equality of two students by comparing names
    Input:              object: Another object to compare to
    Output:             Returns true if equal, false if not.
    --------------------------------------------------------------------------*/
	public boolean equals(Object object){
		if (this == object){
		    return true;
		}

		if (!(object instanceof UCSDStudent)){
		    return false;
		}
		UCSDStudent otherStudent = (UCSDStudent) object;
		return name.equals(otherStudent.getName());
	}

    /*-------------------------------------------------------------------------
    Function Name:      isGreaterThan
    Purpose:            Checks precedence between two students
    Input:              base: Another Base object to compare to
    Output:             Returns true if input object is greater than calling
                        object, false otherwise.
    --------------------------------------------------------------------------*/
	public boolean isGreaterThan (Base base) {
        return (name.compareTo (base.getName ()) > 0) ? true : false;
    }

    /*-------------------------------------------------------------------------
    Function Name:      toString
    Purpose:            Converts student to String representation
    Input:              None.
    Output:             Returns string of student's name and ID
    --------------------------------------------------------------------------*/
    public String toString () {
        return "name:  " + name + "  studentnum:  " + studentnum;
    }
}

public class Driver {
    private static final short NULL = 0;

    public static void main (String [] args) {
    
        /* initialize debug states */
        Tree.debugOff();

        /* check command line options */
        for (int index = 0; index < args.length; ++index) {
            if (args[index].equals("-x"))
                Tree.debugOn();
        }


        /* The real start of the code */
        SymTab<UCSDStudent> symtab = new SymTab<UCSDStudent>("UCSDStudentTree");
        String buffer = null;
        char command;
        long number = 0;

        System.out.println ("Initial Symbol Table:\n" + symtab);

        while (true) {
            command = NULL; // reset command each time in loop
            System.out.print ("Please enter a command:  " + 
                "((a)llocate, is(e)mpty, (i)nsert, (l)ookup,"+
                " (r)emove, (w)rite):  ");

            try {
            command = MyLib.getchar ();
            MyLib.clrbuf (command); // get rid of return

            switch (command) {
            case 'a':
                System.out.print
                ("Please enter name of new Tree to " +
                 "allocate:  ");
                
                buffer = MyLib.getline ();// formatted input
                symtab = new SymTab<UCSDStudent>(buffer);
                break;
			case 'e':
				if(symtab.isEmpty()){
					System.out.println("Tree is empty.");
				} else {
					System.out.println("Tree is not empty.");
				}
				break;

            case 'i':
                System.out.print
                ("Please enter UCSD student name to insert:  ");

                buffer = MyLib.getline ();// formatted input

                System.out.print
                    ("Please enter UCSD student number:  ");

                number = MyLib.decin ();
                MyLib.clrbuf (command); // get rid of return

                // create student and place in symbol table
                symtab.insert(new UCSDStudent (buffer, number));
                break;

            case 'l': { 
                UCSDStudent found;    // whether found or not

                System.out.print
                ("Please enter UCSD student name to lookup:  ");
                buffer = MyLib.getline ();// formatted input

                UCSDStudent stu = new UCSDStudent (buffer, 0);
                found = symtab.lookup (stu);
                
                if (found != null) {
                    System.out.println("Student found!");
                    System.out.println(found);
                }
                else
                    System.out.println ("student " + buffer
                        + " not there!");
                }
                break;
            
            case 'r': { 
                UCSDStudent removed; // data to be removed

                System.out.print
                ("Please enter UCSD student name to remove:  ");

                buffer = MyLib.getline ();

                UCSDStudent stu = new UCSDStudent (buffer, 0);

                removed = symtab.remove(stu);

                if (removed != null) {
                    System.out.println("Student removed!"); 
                    System.out.println(removed);
                }
                else
                    System.out.println ("student " + buffer
                        + " not there!");
                }
                break;

            case 'w':
                System.out.println("The Symbol Table " +
                "contains:\n" + symtab);
            }
            }
            catch (EOFException eof) {
                break;
            }
        }
        System.out.println("\nFinal Symbol Table:\n" + symtab);
    }
}
