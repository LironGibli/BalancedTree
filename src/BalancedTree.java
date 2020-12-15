public class BalancedTree {
    InternalNode root;
    BalancedTree(){
        root = new InternalNode();
    }
    public void insert(Key newKey, Value newValue){
        Leaf newLeaf = new Leaf(newKey.createCopy(), newValue.createCopy());
        Node placementNode = root;
        if (placementNode.left == null){ // this will happen only when we have tree with just root internal node <=> first leaf insertion
            setChildren(placementNode, newLeaf, null, null);
        }
        else if (placementNode.middle == null){ // second leaf insertion
            if (newLeaf.key.compareTo(placementNode.left.key) < 0){
                setChildren(placementNode, newLeaf, placementNode.left, null);
            }
            else{
                setChildren(placementNode, placementNode.left, newLeaf, null);
            }
        }
        else{
            while (!placementNode.getClass().getSimpleName().equals(newLeaf.getClass().getSimpleName())){
                if (newLeaf.key.compareTo(placementNode.left.key) < 0){
                    placementNode = placementNode.left;
                }
                else if ((newLeaf.key.compareTo(placementNode.middle.key) < 0)){
                    placementNode = placementNode.middle;
                }
                else{
                    if (placementNode.right != null){
                        placementNode = placementNode.right;
                    }
                    else{
                        placementNode = placementNode.middle;
                    }
                }
            }
            Node parent = placementNode.p;
            Node climbingNode = insertAndSplit(parent, newLeaf);
            while (parent != root){
                parent = parent.p;
                if(climbingNode != null){
                    climbingNode = insertAndSplit(parent, climbingNode);
                }
                else {
                    updateKey(parent);
                }
            }
            if (climbingNode != null){
                InternalNode newRoot = new InternalNode();
                setChildren(newRoot, parent, climbingNode, null);
                root = newRoot;
            }
        }
    };
    public void delete(Key key){};
    public Value search(Key key){};
    public int rank(Key key){};
    public Key select(int index){};
    public Value sumValuesInInterval (Key key1, Key key2){};

    private void updateKey(Node node){
        node.key = node.left.key;
        if (node.middle != null) {
            node.key = node.middle.key;
        }
        if (node.right != null) {
            node.key = node.right.key;
        }
    }
    private void setChildren(Node parent, Node left, Node middle, Node right){
        parent.left = left;
        parent.middle = middle;
        parent.right = right;
        left.p = parent;
        if (middle != null) middle.p = parent;
        if (right != null) right.p = parent;
        updateKey(parent);
    }
    private Node insertAndSplit(Node parent, Node children){
        Node left = parent.left;
        Node middle = parent.middle;
        Node right = parent.right;
        if (right == null){
            if (children.key.compareTo(left.key) < 0) { // children.key < left.key
                setChildren(parent, children, left, middle);
            }
            else if (children.key.compareTo(middle.key) < 0){
                setChildren(parent, left, children, middle);
            }
            else{
                setChildren(parent, left, middle, children);
            }
            return null;
        }
        InternalNode parentSibling = new InternalNode(parent.p);
        if (children.key.compareTo(left.key) < 0){ // HERE WE NEED TO CHECK WHAT IS THE DIFFERENCE BETWEEN HAVING SENTINELS AND NOT HAVING THEM, MAYBE WE NEED TO CHECK IF IT IS THE SMALLEST KEY IN THE LEAVES? MAYBE ADDING ATTRIBUTE TO THE DATA STRUCTURE OF SMALLEST AND LARGEST LEAF?
            setChildren(parent, children, left, null);
            setChildren(parentSibling, middle, right, null);
        }
        else if(children.key.compareTo(middle.key) < 0){
            setChildren(parent, left, children, null);
            setChildren(parentSibling, middle, right, null);
        }
        else if(children.key.compareTo(right.key) < 0){
            setChildren(parent, left, middle, null);
            setChildren(parentSibling, children, right, null);
        }
        else{
            setChildren(parent, left, middle, null);
            setChildren(parentSibling, right, children, null);
        }
        return parentSibling;
    }
}
