package Items;

public abstract class Item {
    private String name;
    private String rarity;
    private double weight;
    private int value;

    public Item(String name, String rarity, double weight, int value) {
        this.name = name;
        this.rarity = rarity;
        this.weight = weight;
        this.value = value;
    }

    public String getName() {return name;}

    public String getRarity() {return rarity;}

    public double getWeight() {return weight;}

    public int getValue() {return value;}
}
