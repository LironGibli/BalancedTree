public class BalancedTree {
    InternalNode root;
    BalancedTree(){
        root = new InternalNode();
    }
    public void insert(Key newKey, Value newValue){
        Leaf newLeaf = new Leaf(newKey.createCopy(), newValue.createCopy());
        Node placementNode = root;
        if (placementNode.getLeft() == null){ // this will happen only when we have tree with just root internal node <=> first leaf insertion
            setChildren(placementNode, newLeaf, null, null);
        }
        else if (placementNode.getMiddle() == null){ // second leaf insertion
            if (newLeaf.getKey().compareTo(placementNode.getLeft().getKey()) < 0){
                setChildren(placementNode, newLeaf, placementNode.getLeft(), null);
            }
            else{
                setChildren(placementNode, placementNode.getLeft(), newLeaf, null);
            }
        }
        else{
            while (!(placementNode instanceof Leaf)){
                if (newLeaf.getKey().compareTo(placementNode.getLeft().getKey()) < 0){
                    placementNode = placementNode.getLeft();
                }
                else if ((newLeaf.getKey().compareTo(placementNode.getMiddle().getKey()) < 0)){
                    placementNode = placementNode.getMiddle();
                }
                else{
                    if (placementNode.getRight() != null){
                        placementNode = placementNode.getRight();
                    }
                    else{
                        placementNode = placementNode.getMiddle();
                    }
                }
            }
            Node parent = placementNode.getP();
            Node climbingNode = insertAndSplit(parent, newLeaf);
            while (!parent.equals(root)){
                parent = parent.getP();
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
    }
    public void delete(Key key) {
        Node nodeToDelete = keySearch(root, key);
        if (nodeToDelete != null) {
            Node nodeParent = nodeToDelete.getP();
            if ((nodeParent.equals(root)) && (nodeParent.getMiddle() == null)) {
                nodeParent.setLeft(null);
                nodeParent.setKey(null);
                nodeParent.setMinKey(null);
                nodeParent.setSum(null);
                nodeParent.setSize(0);
            }
            else {
                if (nodeToDelete.equals(nodeParent.getLeft())) {
                    setChildren(nodeParent, nodeParent.getMiddle(), nodeParent.getRight(), null);
                }
                else if (nodeToDelete.equals(nodeParent.getMiddle())) {
                    setChildren(nodeParent, nodeParent.getLeft(), nodeParent.getRight(), null);
                }
                else {
                    setChildren(nodeParent, nodeParent.getLeft(), nodeParent.getMiddle(), null);
                }
                while (nodeParent != null) {
                    if (nodeParent.getMiddle() == null) {
                        if (!nodeParent.equals(root)) {
                            nodeParent = borrowOrMerge(nodeParent);
                        }
                        else {
                            if (!(nodeParent.getLeft() instanceof Leaf)) {
                                root = (InternalNode) nodeParent.getLeft();
                                nodeParent.getLeft().setP(null);
                                return;
                            }
                            else {
                                updateKey(nodeParent);
                                nodeParent = nodeParent.getP();
                            }
                        }
                    }
                    else {
                        updateKey(nodeParent);
                        nodeParent = nodeParent.getP();
                    }
                }
            }
        }
    }
    public Value search(Key key){
        Leaf foundNode = keySearch(root,key);
        if (foundNode != null){
            return foundNode.value.createCopy();
        }
        else{
            return null;
        }
    }
    public int rank(Key key){
        Leaf foundNode = keySearch(root,key);
        if (foundNode != null){
           int rank = 1;
           Node nodeParent = foundNode.getP();
           Node climbingNode = foundNode;
           while (nodeParent != null){
               if (climbingNode.equals(nodeParent.getMiddle())){
                   rank += nodeParent.getLeft().getSize();
               }
               else if(climbingNode.equals(nodeParent.getRight())){
                   rank += nodeParent.getLeft().getSize() + nodeParent.getMiddle().getSize();
               }
               climbingNode = nodeParent;
               nodeParent = nodeParent.getP();
           }
           return rank;
        }
        else{
            return 0;
        }
    }
    public Key select(int index){
        if (index < 1){
            return null;
        }
        else{
            Node foundNode = selectRecursive(root, index);
            if (foundNode != null){
                return foundNode.getKey().createCopy();
            }
            else {
                return null;
            }
        }
    }
    public Value sumValuesInInterval (Key key1, Key key2){
        return sumValuesInIntervalRecursive(root, key1, key2);
        }
    private void updateKey(Node node){
        node.setKey(node.getLeft().getKey());
        node.setMinKey(node.getLeft().getMinKey());
        node.setSize(node.getLeft().getSize());
        node.setSum(node.getLeft().getSum().createCopy());
        if (node.getMiddle() != null) {
            node.setKey(node.getMiddle().getKey());
            node.setSize(node.getSize()+ node.getMiddle().getSize());
            node.getSum().addValue(node.getMiddle().getSum());
        }
        if (node.getRight() != null) {
            node.setKey(node.getRight().getKey());
            node.setSize(node.getSize() + node.getRight().getSize());
            node.getSum().addValue(node.getRight().getSum());
        }
    }
    private void setChildren(Node parent, Node left, Node middle, Node right){
        parent.setLeft(left);
        parent.setMiddle(middle);
        parent.setRight(right);
        left.setP(parent);
        if (middle != null) middle.setP(parent);
        if (right != null) right.setP(parent);
        updateKey(parent);
    }
    private Node insertAndSplit(Node parent, Node children){
        Node left = parent.getLeft();
        Node middle = parent.getMiddle();
        Node right = parent.getRight();
        if (right == null){
            if (children.getKey().compareTo(left.getKey()) < 0) { // children.key < left.key
                setChildren(parent, children, left, middle);
            }
            else if (children.getKey().compareTo(middle.getKey()) < 0){
                setChildren(parent, left, children, middle);
            }
            else{
                setChildren(parent, left, middle, children);
            }
            return null;
        }
        InternalNode parentSibling = new InternalNode(parent.getP());
        if (children.getKey().compareTo(left.getKey()) < 0){ // HERE WE NEED TO CHECK WHAT IS THE DIFFERENCE BETWEEN HAVING SENTINELS AND NOT HAVING THEM, MAYBE WE NEED TO CHECK IF IT IS THE SMALLEST KEY IN THE LEAVES? MAYBE ADDING ATTRIBUTE TO THE DATA STRUCTURE OF SMALLEST AND LARGEST LEAF?
            setChildren(parent, children, left, null);
            setChildren(parentSibling, middle, right, null);
        }
        else if(children.getKey().compareTo(middle.getKey()) < 0){
            setChildren(parent, left, children, null);
            setChildren(parentSibling, middle, right, null);
        }
        else if(children.getKey().compareTo(right.getKey()) < 0){
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
        Node nodeParent = node.getP();
        if(node.equals(nodeParent.getLeft())){
            Node nodeSibling = nodeParent.getMiddle();
            if (nodeSibling.getRight() != null) {
                setChildren(node, node.getLeft(), nodeSibling.getLeft(), null);
                setChildren(nodeSibling, nodeSibling.getMiddle(), nodeSibling.getRight(), null);
            } else {
                setChildren(nodeSibling, node.getLeft(), nodeSibling.getLeft(), nodeSibling.getMiddle());
                //GARBAGE COLLECT THE NODE
                setChildren(nodeParent, nodeSibling, nodeParent.getRight(), null);
            }
            return nodeParent;
        }
        else if(node.equals(nodeParent.getMiddle())){
            Node nodeSibling = nodeParent.getLeft();
            if (nodeSibling.getRight() != null) {
                setChildren(node, nodeSibling.getRight(), node.getLeft(), null);
                setChildren(nodeSibling, nodeSibling.getLeft(), nodeSibling.getMiddle(), null);
            } else {
                setChildren(nodeSibling, nodeSibling.getLeft(), nodeSibling.getMiddle(), node.getLeft());
                //GARBAGE COLLECT THE NODE
                setChildren(nodeParent, nodeSibling, nodeParent.getRight(), null);
            }
            return nodeParent;
        }
        Node nodeSibling = nodeParent.getMiddle();
        if (nodeSibling.getRight() != null) {
            setChildren(node, nodeSibling.getRight(), node.getLeft(), null);
            setChildren(nodeSibling, nodeSibling.getLeft(), nodeSibling.getMiddle(), null);
        } else {
            setChildren(nodeSibling, nodeSibling.getLeft(), nodeSibling.getMiddle(), node.getLeft());
            //GARBAGE COLLECT THE NODE
            setChildren(nodeParent, nodeParent.getLeft(), nodeSibling, null);
        }
        return nodeParent;
        }
    private Leaf keySearch(Node node, Key key){
        if(node.equals(root) && node.getLeft() == null){
            return null;
        }
        if(node instanceof Leaf){
            if(node.getKey().compareTo(key) == 0 ){
                    return (Leaf)node;
            }
            else{
                return null;
            }
        }
        if(key.compareTo(node.getLeft().getKey())<=0){
            return keySearch(node.getLeft(), key);
        }
        else if((node.getMiddle() != null) && key.compareTo(node.getMiddle().getKey())<=0){
            return keySearch(node.getMiddle(), key);
        }
        else{
            if((node.getRight() != null) && (key.compareTo(node.getRight().getKey())<=0)){
                return keySearch(node.getRight(), key);
            }
            else{
                return null;
            }
        }
    }
    private Node selectRecursive(Node node,int index){
        if (node.getSize() < index){
            return null;
        }
        if (node instanceof Leaf){
            return node;
        }
        int sizeLeftMiddle = 0;
        int sizeLeft = node.getLeft().getSize();
        if (node.getMiddle() != null){
            sizeLeftMiddle = sizeLeft + node.getMiddle().getSize();
        }
        if (index <= sizeLeft){
            return selectRecursive(node.getLeft(), index);
        }
        else if (index <= sizeLeftMiddle && node.getMiddle() != null){
            return selectRecursive(node.getMiddle(), index - sizeLeft);
        }
        else{
            return selectRecursive(node.getRight(), index - sizeLeftMiddle);
        }
    }
    private Value sumValuesInIntervalRecursive(Node node,Key key1, Key key2){
        if(node.equals(root) && node.getLeft() == null){
            return null;
        }
        if ((node.getMinKey().compareTo(key1) >= 0) && (node.getKey().compareTo(key2) <= 0)){
            return node.getSum().createCopy();
        }
        else if ((node.getMinKey().compareTo(key2) > 0) || (node.getKey().compareTo(key1) < 0)){
            return null;
        }
        else{
            Value val1 = sumValuesInIntervalRecursive(node.getLeft(), key1, key2);
            Value val2 = null;
            if (node.getMiddle() != null) {
                val2 = sumValuesInIntervalRecursive(node.getMiddle(), key1, key2);
            }
            Value val3 = null;
            if (node.getRight() != null) {
                val3 = sumValuesInIntervalRecursive(node.getRight(), key1, key2);
            }
            if (val1 != null){
                if (val2 != null) {
                    val1.addValue(val2);
                }
                if (val3 != null){
                    val1.addValue(val3);
                }
                return val1;
            }
            else if (val2 != null){
                if (val3 != null) {
                    val2.addValue(val3);
                }
                return val2;
            }
            else{
                return val3;
            }
        }
    }
}
