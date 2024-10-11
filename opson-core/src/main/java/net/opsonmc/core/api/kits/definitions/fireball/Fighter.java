package net.opsonmc.core.api.kits.definitions.fireball;

import net.opsonmc.core.api.achievements.Achievement;
import net.opsonmc.core.api.game.GameType;
import net.opsonmc.core.api.kits.Kit;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class Fighter implements Kit {
    @Override
    public GameType getGame() {
        return GameType.FIREBALL;
    }

    @Override
    public String getName() {
        return "Fighter";
    }

    @Override
    public String getDescription() {
        return "Simple kit";
    }

    @Nullable
    @Override
    public List<ItemStack> getItems() {
        return null;
    }

    @Nullable
    @Override
    public Achievement getRequiredAchievement() {
        return null;
    }
}
