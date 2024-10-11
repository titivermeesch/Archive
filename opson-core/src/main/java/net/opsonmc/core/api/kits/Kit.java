package net.opsonmc.core.api.kits;

import net.opsonmc.core.api.achievements.Achievement;
import net.opsonmc.core.api.game.GameType;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nullable;
import java.util.List;

public interface Kit {
    GameType getGame();
    String getName();
    String getDescription();
    @Nullable
    List<ItemStack> getItems();
    @Nullable
    Achievement getRequiredAchievement();
}
