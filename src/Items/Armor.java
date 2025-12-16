package Items;

import Enums.ArmorPlacement;
import Enums.Rarity;

public class Armor extends Item {
    private int durability;
    private ArmorPlacement armorPlacement;
    private Rarity rarity;

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

    @Override
    public String toString() {
        return String.format(
                "%-4s | %-20s | %-8s | %-8s | %-12s | %-10s | %-12s%n" +
                        "%-4d | %-20s | %-8.1f | %-8d | %-12d | %-10s | %-12s%n",
                "Id", "Name", "Weight", "Value", "Durability", "Rarity", "Placement",
                getDbId(), getName(), getWeight(), getValue(),
                getDurability(), getRarity(), getArmorPlacement()
        );
    }
}
