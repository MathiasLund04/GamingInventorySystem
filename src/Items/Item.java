package Items;

public abstract class Item {
    private String name;
    private double weight;
    private int value;

    public Item(String name, double weight, int value) {
        this.name = name;
        this.weight = weight;
        this.value = value;
    }

    public String getName() {return name;}
    public double getWeight() {return weight;}
    public int getValue() {return value;}

    public void setName(String name) {this.name = name;}
    public void setWeight(double weight) {this.weight = weight;}
    public void setValue(int value) {this.value = value;}
}
