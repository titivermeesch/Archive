package io.minki.gapi.text;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;

public class TextBuilder {
    private TextComponent component;

    public TextBuilder(String text) {
        this.component = Component.text(text).decoration(TextDecoration.ITALIC, false);
    }

    public static TextComponent empty() {
        return new TextBuilder("").build();
    }

    public TextBuilder color(int color) {
        this.component = component.color(TextColor.color(color));
        return this;
    }

    public TextBuilder color(Colors color) {
        this.component = component.color(TextColor.color(color.getValue()));
        return this;
    }

    public TextBuilder bold() {
        this.component = component.decoration(TextDecoration.BOLD, true);
        return this;
    }

    public TextComponent build() {
        return component;
    }
}
