package Items;

import Enums.ArmorPlacement;
import Enums.Rarity;

public class Armor extends Item {
    private int durability;
    ArmorPlacement armorPlacement;
    Rarity rarity;

    public Armor(String name, Rarity rarity, double weight, int value, int durability, ArmorPlacement armorPlacement) {
        super(name, weight, value);
        this.durability = durability;
        this.rarity = rarity;
        this.armorPlacement = armorPlacement;
    }

    public Rarity getRarity() {
        return rarity;
    }
    public int getDurability(){
        return durability;
    }
}
