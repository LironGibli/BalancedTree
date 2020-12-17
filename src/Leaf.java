public class Leaf extends Node{
    Value value;
    Leaf(Key key, Value value){
        this.setMinKey(key);
        this.setKey(key);
        this.value = value;
        this.setSum(value);
        this.setSize(1);
    }
}
