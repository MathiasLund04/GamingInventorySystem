package Items;

import Enums.ConsumableType;
import Logic.Inventory.Inventory;

public class Consumable extends Item {
    private String description;
    ConsumableType consumableType;
    private int consumableCount;

    public Consumable(String name, double weight, int value, String description, ConsumableType consumableType) {
        super(name, weight, value);
        this.description = description;
        this.consumableType = consumableType;
        this.consumableCount = 1;
    }

    public String getDescription() {
        return description;
    }
    public String getConsumableType() {
        return consumableType.toString();
    }
    public int getConsumableCount() {
        return consumableCount;
    }

    public void setConsumableCount(int consumableCount) {
        this.consumableCount = consumableCount;
    }

    public void increaseQuantity(int amount){
        this.consumableCount += amount;
    }
    public void decreaseQuantity(int amount)
    {
        if(this.consumableCount >= amount)
        {
            this.consumableCount -= amount;
        }
    }

    @Override
    public String toString() {
        return "Name     |  Weight  |   Value    |  Description   |   Type of Consumable    |   Stack count      \n" +
                getName() + " | " + getWeight() + " | " + getValue() + " | " + getDescription() + " | " + getConsumableType() + " | " + getConsumableCount() + "/" + Inventory.getMaxStack() + "\n";
    }
}
