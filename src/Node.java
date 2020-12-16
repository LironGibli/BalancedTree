public abstract class Node {
    Node left; // pointer to left child in T
    Node middle; // pointer to middle child in T
    Node right; // pointer to right child in T
    Node p; // pointer to parent in T
    public Key key; // maximum key in its subtree
    int size;
    Node(){
        left = null;
        middle = null;
        right = null;
        p = null;
        key = null;
        size = 0;
    }
//    public Node getLeft() {
//        return left;
//    }
//
//    public void setLeft(Node left) {
//        this.left = left;
//    }
//    public Node getMiddle() {
//        return middle;
//    }
//
//    public void setMiddle(Node middle) {
//        this.middle = middle;
//    }
//
//    public Node getRight() {
//        return right;
//    }
//
//    public void setRight(Node right) {
//        this.right = right;
//    }
//
//    public Node getP() {
//        return p;
//    }
//
//    public void setP(Node p) {
//        this.p = p;
//    }
//
//    public Key getKey() {
//        return key;
//    }
//
//    public void setKey(Key key) {
//        this.key = key;
//    }
}
