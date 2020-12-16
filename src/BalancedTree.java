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
    public void delete(Key key) {
        Node nodeToDelete = keySearch(root, key);
        if (nodeToDelete != null) {
            Node nodeParent = nodeToDelete.p;
            if ((nodeParent == root) && (nodeParent.middle == null)) {
                nodeParent.left = null;
                nodeParent.key = null;
                nodeParent.size = 0;
            }
            else {
                if (nodeToDelete == nodeParent.left) {
                    setChildren(nodeParent, nodeParent.middle, nodeParent.right, null);
                }
                else if (nodeToDelete == nodeParent.middle) {
                    setChildren(nodeParent, nodeParent.left, nodeParent.right, null);
                }
                else {
                    setChildren(nodeParent, nodeParent.left, nodeParent.middle, null);
                }
                while (nodeParent != null) {
                    if (nodeParent.middle == null) {
                        if (nodeParent != root) {
                            nodeParent = borrowOrMerge(nodeParent);
                        }
                        else {
                            if (!(nodeParent.left.getClass().getSimpleName().equals(Leaf.class.getSimpleName()))) {
                                root = (InternalNode) nodeParent.left;
                                nodeParent.left.p = null;
                                return;
                            }
                            else {
                                updateKey(nodeParent);
                                nodeParent = nodeParent.p;
                            }
                        }
                    }
                    else {
                        updateKey(nodeParent);
                        nodeParent = nodeParent.p;
                    }
                }
            }
        }
    };
    public Value search(Key key){
        Leaf foundNode = keySearch(root,key);
        if (foundNode != null){
            return foundNode.value.createCopy();
        }
        else{
            return null;
        }
    };
    public int rank(Key key){
        Leaf foundNode = keySearch(root,key);
        if (foundNode != null){
           int rank = 1;
           Node nodeParent = foundNode.p;
           Node climbingNode = foundNode;
           while (nodeParent != null){
               if (climbingNode == nodeParent.middle){
                   rank += nodeParent.left.size;
               }
               else if(climbingNode == nodeParent.right){
                   rank += nodeParent.left.size + nodeParent.middle.size;
               }
               climbingNode = nodeParent;
               nodeParent = nodeParent.p;
           }
           return rank;
        }
        else{
            return 0;
        }
    };


//    public Key select(int index){};
//    public Value sumValuesInInterval (Key key1, Key key2){};

    private void updateKey(Node node){
        node.size = 0;
        node.key = node.left.key;
        node.size += node.left.size;
        if (node.middle != null) {
            node.key = node.middle.key;
            node.size += node.middle.size;
        }
        if (node.right != null) {
            node.key = node.right.key;
            node.size += node.right.size;
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
    private Node borrowOrMerge(Node node){
        Node nodeParent = node.p;
        if(node == nodeParent.left){
            Node nodeSibling = nodeParent.middle;
            if (nodeSibling.right != null) {
                setChildren(node, node.left, nodeSibling.left, null);
                setChildren(nodeSibling, nodeSibling.middle, nodeSibling.right, null);
            } else {
                setChildren(nodeSibling, node.left, nodeSibling.left, nodeSibling.middle);
                //GARBAGE COLLECT THE NODE
                setChildren(nodeParent, nodeSibling, nodeParent.right, null);
            }
            return nodeParent;
        }
        else if(node == nodeParent.middle){
            Node nodeSibling = nodeParent.left;
            if (nodeSibling.right != null) {
                setChildren(node, nodeSibling.right, node.left, null);
                setChildren(nodeSibling, nodeSibling.left, nodeSibling.middle, null);
            } else {
                setChildren(nodeSibling, nodeSibling.left, nodeSibling.middle, node.left);
                //GARBAGE COLLECT THE NODE
                setChildren(nodeParent, nodeSibling, nodeParent.right, null);
            }
            return nodeParent;
        }
        Node nodeSibling = nodeParent.middle;
        if (nodeSibling.right != null) {
            setChildren(node, nodeSibling.right, node.left, null);
            setChildren(nodeSibling, nodeSibling.left, nodeSibling.middle, null);
        } else {
            setChildren(nodeSibling, nodeSibling.left, nodeSibling.middle, node.left);
            //GARBAGE COLLECT THE NODE
            setChildren(nodeParent, nodeParent.left, nodeSibling, null);
        }
        return nodeParent;
        }
    private Leaf keySearch(Node node, Key key){
        if(node.getClass().getSimpleName().equals(Leaf.class.getSimpleName())){
            if(node.key == key){
                return (Leaf)node;
            }
            else{
                return null;
            }
        }
        if(key.compareTo(node.left.key)<=0){
            return keySearch(node.left, key);
        }
        else if(key.compareTo(node.middle.key)<=0){
            return keySearch(node.middle, key);
        }
        else{
            if((node.right != null) && (key.compareTo(node.right.key)<=0)){
                return keySearch(node.right, key);
            }
            else{
                return null;
            }
        }
    }
//    public static void main(String[] args) {
//        Node a = new Leaf(new MyKey("ss",1),new MyValue(1));
//        Node b = new InternalNode();
//        System.out.println(a.getClass().getSimpleName().equals(b.getClass().getSimpleName()));
//
//    }
}
