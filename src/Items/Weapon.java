package Items;

import Enums.Rarity;
import Enums.WeaponHandleType;
import Enums.WeaponType;

public class Weapon extends Item {
    private int damage;
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
    public Rarity getRarity(){
        return rarity;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }
    public void setHandleType(WeaponHandleType  handleType) {
        this.handleType = handleType;
    }
    public void setRarity(Rarity rarity) {
        this.rarity = rarity;
    }
    public void setType(WeaponType  type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Name     |  Weight  |   Value    |  Damage   |   Rarity      |   Weapon Type  \n" +
                getName() + " | " + getWeight() + " | " + getValue() + " | " + getDamage() + " | " + getRarity() + " | " + getType() + "\n";
    }



}
