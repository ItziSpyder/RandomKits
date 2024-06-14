package io.github.itzispyder.randomkits.data;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Kit implements Serializable, ConfigurationSerializable {

    private ItemStack[] contents;

    public Kit(ItemStack... contents) {
        this.contents = contents;
    }

    public ItemStack[] getContents() {
        return contents;
    }

    public void setContents(ItemStack... contents) {
        this.contents = contents;
    }

    public boolean isEmpty() {
        return Arrays.stream(contents).noneMatch(Objects::nonNull);
    }

    @Override
    public @NotNull Map<String, Object> serialize() {
        return new HashMap<>();
    }
}
