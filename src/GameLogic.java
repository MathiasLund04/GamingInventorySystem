import Enums.Rarity;
import Enums.WeaponHandleType;
import Enums.WeaponType;
import Items.Item;
import Items.Weapon;

import java.util.ArrayList;
import java.util.Random;

public class GameLogic {
    Random random = new Random();
 int rand = (int) (Math.random() * 5);
 public static ArrayList<Item> items = new ArrayList<>();
 public static ArrayList<Weapon> weapons = new ArrayList<>();

 private static final String[] AXE_NAMES = {"axe","Grimnir's axe", "axe of the mountains"};

    public void generateItem() {
        int times = rand;
        int choice =  (int) (Math.random() * 3);

        for (int i = 0; i < times; i++) {
            switch (choice) {
                case 1:
                case 2:



            }


        }

    }

    public Weapon generateWeapon(){
        int rand = (int) (Math.random() * 5);
        WeaponType weaponType = WeaponType.values()[rand];
        WeaponHandleType weaponHandleType = WeaponHandleType.values()[rand];
        int damage= 0;
        double weight= 0;
        int value = 0;
        String name = "";
        Rarity randomRarity = Rarity.values()[random.nextInt(Rarity.values().length)];

        switch (rand) {
            case 1:
                weaponType = WeaponType.AXE;
                 damage = 3;
                 weight = 0.5;
                 value = 4;
                 weaponHandleType = WeaponHandleType.ONEHANDED;
                 name = AXE_NAMES[random.nextInt(AXE_NAMES.length)];
                break;
            case 2:
                weaponType = WeaponType.BOW;
                damage = 3;
                weight = 1;
                value = 7;
                break;
            case 3:
                weaponType = WeaponType.WARHAMMER;
                damage = 7;
                weight = 2;
                value = 8;
                break;
            case 4:
                weaponType = WeaponType.LONGSWORD;
                damage = 5;
                weight = 1.5;
                value = 5;
                break;
            case 5:
                weaponType = WeaponType.SWORD;
                damage = 4;
                weight = 1;
                value = 4;
                break;
        }
            return new Weapon(name,weaponType,randomRarity,weight,value,damage,weaponHandleType);

    }

}
