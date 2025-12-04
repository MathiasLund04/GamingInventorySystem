package Items;

import Enums.ArmorPlacement;

public class Armor extends Item {
    private int durability;
    ArmorPlacement armorPlacement;

    public Armor(String name, String type, String rarity, double weight, int value, int durability, ArmorPlacement armorPlacement) {
        super(name, type, rarity, weight, value);
        this.durability = durability;
        this.armorPlacement = armorPlacement;
    }

}
