package net.opsonmc.core.api.achievements;

import javax.annotation.Nullable;

public interface Achievement {
    String getName();
    String getDescription();
    @Nullable
    String getGame();
}
