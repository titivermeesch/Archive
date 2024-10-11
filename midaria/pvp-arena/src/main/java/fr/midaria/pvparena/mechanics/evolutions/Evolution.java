package fr.midaria.pvparena.mechanics.evolutions;

import org.bukkit.Material;

import java.util.List;

public interface Evolution {
    EvolutionType getType();

    String getName();

    List<String> getDescription();

    int getMaxLevel();

    List<EvolutionLevel> getLevels();

    Material getIcon();
}
