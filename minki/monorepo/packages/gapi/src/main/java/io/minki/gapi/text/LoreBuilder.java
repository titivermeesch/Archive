package io.minki.gapi.text;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;

import java.util.ArrayList;

public class LoreBuilder {
    private final ArrayList<TextComponent> components = new ArrayList<>();

    public LoreBuilder() {
    }

    public LoreBuilder add(String line, int color) {
        TextComponent component = Component.text(line).color(TextColor.color(color)).decoration(TextDecoration.ITALIC, false);
        components.add(component);
        return this;
    }

    public LoreBuilder add(String line, Colors color) {
        return this.add(line, color.getValue());
    }

    public LoreBuilder addEmpty() {
        components.add(Component.empty());
        return this;
    }

    public ArrayList<TextComponent> build() {
        return components;
    }
}
