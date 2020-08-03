/*****************************************************************************
                                                                    Kathy Li 
                                                         CSE 12, Spring 2019
                                                                May 17, 2019
                                                                     cs12xee   
                              Assignment Seven
Filename:    Driver.java 
Description: This program tests the functions of an binary search tree.
******************************************************************************/

//=============================================================================
// Class Name: Tree
// Description: This class contains the constuctor and functions for a BST
// Data Fields: 
//     occupancy (long) - number of items stored in table
//     root (TNode) -  the root node of the tree
//     treeName (String) - the name of the tree
// Public Functions: 
//     DebugOn - Sets debug on
//     DebugOff - Sets debug off
//     insert - Inserts an element into the tree
//     lookup - Looks up an element in the tree
//     remove - Removes as element from the tree
//     isEmpty - Checks if the tree is empty
//     toString - Writes out the tree to String
//============================================================================
public class Tree<Whatever extends Base> extends Base {

    /* data fields */
    private TNode root;
    private long occupancy; 
    private String treeName;

    /* debug flag */
    private static boolean debug;

    /* debug messages */
    private static final String ALLOCATE = " - Allocating]\n";
    private static final String AND = " and ";
    private static final String COMPARE = " - Comparing ";
    private static final String INSERT = " - Inserting ";
    private static final String TREE = "[Tree ";

    /*----------------------------------------------------------------------
    Constructor         Tree
    Purpose:            Allocates and initializes the memory associated 
                        with a binary tree.
    Input:              name: The name of the tree to allocate
    Output:             No return value
    ----------------------------------------------------------------------*/
    public Tree (String name) {
        // Initialize data fields
        treeName = name; 
        occupancy = 0;
        root = null; 
        
        // Debug message
        if (debug){
            System.err.print(TREE + name + ALLOCATE);
        }
        
    }

    /*----------------------------------------------------------------------
    Function Name:      debugOff
    Purpose:            Turns off debugging
    Input:              None
    Output:             No return value
    ----------------------------------------------------------------------*/
    public static void debugOff () {
        debug = false;
    }

    /*----------------------------------------------------------------------
    Function Name:      setDebugOn
    Purpose:            Turns on debugging
    Input:              None
    Output:             No return value
    ----------------------------------------------------------------------*/
    public static void debugOn () {
        debug = true;
    }

    /**
     * Returns the tree's name
     * @return name of the tree
     */
    public String getName() {
        return treeName;
    }

    /*----------------------------------------------------------------------
    Function Name:      isEmpty
    Purpose:            Checks if the tree is empty
    Input:              None
    Output:             Returns true if the tree is empty, else false
    ----------------------------------------------------------------------*/
    public boolean isEmpty () {
    
        if (occupancy == 0 || root == null){
            return true;
        }
        return false;
    }

    /*----------------------------------------------------------------------
    Function Name:      insert
    Purpose:            Inserts the element in the binary tree. Duplicate 
                        insertions will cause the existing element to be 
                        deleted, and the duplicate to take its place.
    Input:              element: complete elements to insert
    Output:             Returns true if successfully inserted, false
                        otherwise.
    ----------------------------------------------------------------------*/
    public boolean insert (Whatever element) {
        TNode working = root;               // Working node
        long rightChildHeight = -1;         // Right child height
        long leftChildHeight = -1;          // Left child height 

        // Insert item at root or elsewhere down the tree 
        if (working == null){
            if (debug){
                System.err.println(treeName + INSERT + element.getName());
            }
            occupancy++;
            root = new TNode(element);
    
        }else{    
            while (working != null){
                if (debug){ 
                    System.err.println(treeName + COMPARE + 
                    element.getName() + AND +  working.data.getName());
                }
                // Handle duplicate insert 
                if (element.equals(working.data)){
                    if (working.hasBeenDeleted){
                        // Not actually a duplicate insert if it's been deleted
                        occupancy++;
                    }
                    working.hasBeenDeleted = false;
                    working.data = element;
                    break;

                //Check to go right     
                }else if (element.isGreaterThan(working.data)){
                    
                    // If right node is null, insert element
                    if (working.right == null){

                        if (debug){
                            System.err.println
                            (treeName + INSERT + element.getName());
                        }

                        occupancy++;
                        working.right = new TNode(element);
                        working.right.parent = working;
                        break;

                    // Else keep traversing right
                    }else{
                        working = working.right;
                    }
                // Else go left
                }else{ 
                    if (working.left == null){

                        if (debug){
                        System.err.println
                            (treeName + INSERT + element.getName());
                        }

                        occupancy++;
                        working.left = new TNode(element);
                        working.left.parent = working;
                        break;
                    // Else keep traversing left
                    }else{
                        working = working.left;
            
                    }
                }
            }
        }    
    
    
        // Use parent pointer to travel back up the tree
        while (working != null){ 
            // Set left and right children heights 
            if (working.left != null){
                leftChildHeight = working.left.height;
            }
            if (working.left == null){
                leftChildHeight = -1;
            }
        
            if (working.right != null){
                 rightChildHeight = working.right.height;
            }
            if (working.right == null){
                rightChildHeight = -1;
            }

            // Calculate height and balance of current node
            if (leftChildHeight >= rightChildHeight){
                working.height = leftChildHeight + 1;
            }else{
                working.height = rightChildHeight  +1;
            } 
            working.balance = leftChildHeight - rightChildHeight; 

            // Move up the tree
            working = working.parent;
        
        }

        return true;
    }

    /*----------------------------------------------------------------------
    Function Name:      lookup
    Purpose:            Looks up an element in the tree.
    Input:              element: incomplete elements (No number)
    Output:             Returns the element if found, null otherwise
    ----------------------------------------------------------------------*/
    public Whatever lookup (Whatever element) {
        TNode working = root;           // Working node

        while (working != null){
            // Debug
            if (debug){
                System.err.println(treeName + COMPARE + element.getName() 
                + AND +  working.data.getName());
            }

            //Check root node
            if (element.equals(working.data) && !working.hasBeenDeleted){
                return working.data;
            }
              
            // Check to go right  
            if (element.isGreaterThan(working.data)){
                // If node is null, continue
                if(working.right == null){
                    break;
                // If node is found and it's not "deleted", return it
                }else if ((working.right.data).equals(element) && 
                    !working.right.hasBeenDeleted){
                    return working.right.data;                

                // Else keep traversing down
                }else {
                    working = working.right;
                }
            // Else go left 
            }else{ 
                // If node is null, element is missing
                if (working.left == null){
                    break;

                // If node is found and it's not "deleted", return it
                }else if ((working.left.data).equals(element) && 
                    !working.left.hasBeenDeleted){
                    return working.left.data;
                
                // Else keep traversing down
                }else{
                    working = working.left;
                }
            }
        }
        return null;    //Element not there
    }

    /*----------------------------------------------------------------------
    Function Name:      remove
    Purpose:            Removes an element in the tree.
    Input:              element: incomplete elements (No number)
    Output:             Returns and deletes the element if found, null otherwise
    ----------------------------------------------------------------------*/
    public Whatever remove (Whatever element) {
        TNode working = root;           // Working node
    
        while (working != null){

            // Debug
            if (debug){
                System.err.println(treeName + COMPARE + 
                element.getName() + AND +  working.data.getName());
            }

            // Check if root node is being removed
            if (element.equals(working.data)){
                if (working.hasBeenDeleted){
                    // Unless it has already been "deleted"
                    return null;
                }
                working.hasBeenDeleted = true;
                occupancy--;
                return working.data;
            }
            //Check to go right     
            if (element.isGreaterThan(working.data)){
                // If node is null, continue
                if(working.right == null){
                    break;
                // If node is found, delete it and return it 
                }else if ((working.right.data).equals(element)){
                    if (working.right.hasBeenDeleted){
                        return null; 
                    }
                    occupancy--;
                    working.right.hasBeenDeleted = true;
                    return working.right.data;                

                // Else keep traversing down
                }else {
                    // Unless the right child has been deleted
                    if (working.right != null && working.right.hasBeenDeleted){
                        break;
                    }
                    
                    working = working.right;
                }
            // Else go left
            }else{ 
                // If node is null, continue
                if (working.left == null){
                    break;

                // If node is found, delete it and return it 
                }else if ((working.left.data).equals(element)){
                    if (working.left.hasBeenDeleted){
                        return null; 
                    }    
                    occupancy--;
                    working.left.hasBeenDeleted = true;
                    return working.left.data;
                // Else keep traversing down
                }else{
                    // Unless the left child has been deleted
                    if (working.left != null && working.left.hasBeenDeleted){
                        break;
                    }

                    working = working.left;
            
                }
            }
        }   
    
    return null;    // not there
    }   

    /**
     * Creates a string representation of this tree. This method first
     * adds the general information of this tree, then calls the
     * recursive TNode function to add all nodes to the return string 
     *
     * @return  String representation of this tree 
     */
    public String toString () {
        String string = "Tree " + treeName + ":\noccupancy is ";
        string += occupancy + " elements.";

        if(root != null)
            string += root.writeAllTNodes();

        return string;
    }


    //==========================================================================
    // Class Name: TNode
    // Description: This class contains the constuctor and functions for a node
    // Data Fields: 
    //     data (Whatever) - data that the node contains
    //     left, right, parent (TNode) - the nodes of the left, right, and 
    //          parent of the current node
    //     hasBeenDeleted (boolean) - Check whether the node has been deleted 
    // Public Functions: 
    //     toString - Writes a node to a string
    //     writeAllNodes - Writes all nodes to string
    //==========================================================================
    private class TNode {

        public Whatever data;
        public TNode left, right, parent;
        public boolean hasBeenDeleted;

        /* left child's height - right child's height */
        public long balance;
        /* 1 + height of tallest child, or 0 for leaf */
        public long height;

        public TNode (Whatever element) {
            /* Initialize data fields */
            data = element; 
            balance = 0;
            height = 0;
            left = right = parent = null; 
        }

        /**
         * Creates a string representation of this node. Information
         * to be printed includes this node's height, its balance,
         * and the data its storing.
         *
         * @return  String representation of this node 
         */

        public String toString () {
            return "at height:  " + height + "  with balance: " +
                balance + "  " + data;
        }

        /**
         * Writes all TNodes to the String representation field. 
         * This recursive method performs an in-order
         * traversal of the entire tree to print all nodes in
         * sorted order, as determined by the keys stored in each
         * node. To print itself, the current node will append to
         * tree's String field.
         */
        public String writeAllTNodes () {
            String string = "";
            if (left != null)
                string += left.writeAllTNodes ();
            if (!hasBeenDeleted) 
                string += "\n" + this;    
            if (right != null)
                string += right.writeAllTNodes ();

            return string;
        }
    }
}
