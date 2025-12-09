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

    public int getDurability() {
        return durability;
    }
    public Rarity getRarity() {
        return rarity;
    }
    public ArmorPlacement getArmorPlacement() {
        return armorPlacement;
    }

    public void setDurability(int durability) {
        this.durability = durability;
    }
    public void setRarity(Rarity rarity) {
        this.rarity = rarity;
    }
    public void setArmorPlacement(ArmorPlacement armorPlacement) {
        this.armorPlacement = armorPlacement;
    }

    @Override
    public String toString() {
        return "Name     |  Weight  |   Value    |  Durability   |   Rarity      |   Placement  \n" +
                getName() + " | " + getWeight() + " | " + getValue() + " | " + getDurability() + " | " + getRarity() + " | " + getArmorPlacement() + "\n";
    }
}
