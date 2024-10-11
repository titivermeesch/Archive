package fr.midaria.pvparena.mechanics.evolutions;

import java.util.List;

public class EvolutionLevel {
    private final int level;
    private final List<String> description;
    private final int price;

    public EvolutionLevel(int level, List<String> description, int price) {
        this.level = level;
        this.description = description;
        this.price = price;
    }

    public int getLevel() {
        return level;
    }

    public List<String> getDescription() {
        return description;
    }

    public int getPrice() {
        return price;
    }
}
