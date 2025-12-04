package Items;

import Enums.ConsumableType;

public class Consumable extends Item {
    private String description;
    ConsumableType consumableType;



    public Consumable(String name, String type, String rarity, double weight, int value, String description, ConsumableType consumableType) {
        super(name, type, rarity, weight, value);
        this.description = description;
        this.consumableType = consumableType;
    }


}
