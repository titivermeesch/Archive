package net.opsonmc.core.api.kits;

import net.opsonmc.core.api.kits.definitions.fireball.Fighter;

import java.util.ArrayList;
import java.util.List;

// Utility class to load all kits
public class KitsList {
    private final List<Kit> kits = new ArrayList<>();

    public KitsList() {
        kits.add(new Fighter());
    }

    public List<Kit> get() {
        return kits;
    }
}
