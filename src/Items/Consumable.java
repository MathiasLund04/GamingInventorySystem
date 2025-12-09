package Items;

import Enums.ConsumableType;

public class Consumable extends Item {
    private String description;
    ConsumableType consumableType;

    public Consumable(String name, double weight, int value, String description, ConsumableType consumableType) {
        super(name, weight, value);
        this.description = description;
        this.consumableType = consumableType;
    }

    public String getDescription() {
        return description;
    }
    public String getConsumableType() {
        return consumableType.toString();
    }

    public void setConsumableType(ConsumableType consumableType) {
        this.consumableType = consumableType;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Name     |  Weight  |   Value    |  Description   |   Type of Consumable  \n" +
                getName() + " | " + getWeight() + " | " + getValue() + " | " + getDescription() + " | " + getConsumableType() + "\n";
    }
}
