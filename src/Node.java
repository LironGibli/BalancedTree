public abstract class Node {
    public Node getLeft() {
        return left;
    }

    public void setLeft(Node left) {
        this.left = left;
    }

    public Node getMiddle() {
        return middle;
    }

    public void setMiddle(Node middle) {
        this.middle = middle;
    }

    public Node getRight() {
        return right;
    }

    public void setRight(Node right) {
        this.right = right;
    }

    public Node getP() {
        return p;
    }

    public void setP(Node p) {
        this.p = p;
    }

    public Key getKey() {
        return key;
    }

    public void setKey(Key key) {
        this.key = key;
    }

    public Key getMinKey() {
        return minKey;
    }

    public void setMinKey(Key minKey) {
        this.minKey = minKey;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public Value getSum() {
        return sum;
    }

    public void setSum(Value sum) {
        this.sum = sum;
    }

    private Node left; // pointer to left child in T
    private Node middle; // pointer to middle child in T
    private Node right; // pointer to right child in T
    private Node p; // pointer to parent in T
    private Key key; // maximum key in its subtree
    private Key minKey; // minimum key in its subtree
    private int size;
    private Value sum;
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
