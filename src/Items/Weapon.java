package Items;

import Enums.Rarity;
import Enums.WeaponHandleType;
import Enums.WeaponType;

public class Weapon extends Item {
    private final int damage;
    WeaponType type;
    WeaponHandleType handleType;
    Rarity rarity;

    public Weapon (String name, WeaponType type, Rarity rarity, double weight, int value, int damage, WeaponHandleType handleType) {
        super(name, weight, value);
        this.damage = damage;
        this.handleType = handleType;
        this.type =  type;
        this.rarity = rarity;
    }

    public int getDamage() {
        return damage;
    }

    public WeaponHandleType getHandleType() {
        return handleType;
    }

    public WeaponType getType() {
        return type;
    }



}
