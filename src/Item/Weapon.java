package Item;

import Enums.WeaponHandleType;
import Enums.WeaponType;

public class Weapon extends Item {
    private int damage;
    WeaponType weaponType;
    WeaponHandleType handleType;


    public Weapon (String name, String type, String rarity, double weight, int value, int damage, WeaponType weaponType, WeaponHandleType handleType) {
        super(name, type, rarity, weight, value);
        this.damage = damage;
        this.weaponType = weaponType;
        this.handleType = handleType;
    }


}
