public class Leaf extends Node{
    Value value;
    Leaf(Key key, Value value){
        this.minKey = key;
        this.key = key;
        this.value = value;
        this.sum = value;
        this.size = 1;
    }
}
