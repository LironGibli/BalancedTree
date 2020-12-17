public abstract class Node {
    Node left; // pointer to left child in T
    Node middle; // pointer to middle child in T
    Node right; // pointer to right child in T
    Node p; // pointer to parent in T
    Key key; // maximum key in its subtree
    Key minKey; // minimum key in its subtree
    int size;
    Value sum;
    Node(){
        left = null;
        middle = null;
        right = null;
        p = null;
        key = null;
        size = 0;
        sum = null;
    }
}
