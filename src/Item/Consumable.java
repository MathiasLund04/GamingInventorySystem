package Item;

public class Consumable extends Item {
    private String description;



    public Consumable(String name, String type, String rarity, double weight, int value, String description) {
        super(name, type, rarity, weight, value);
        this.description = description;
    }


}
