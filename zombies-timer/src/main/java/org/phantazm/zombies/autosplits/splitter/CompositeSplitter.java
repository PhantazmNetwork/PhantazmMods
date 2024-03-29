package org.phantazm.zombies.autosplits.splitter;

import net.minecraft.client.MinecraftClient;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.util.Collection;
import java.util.Objects;

public class CompositeSplitter {

    private final MinecraftClient client;

    private final Logger logger;

    private final Collection<AutoSplitSplitter> splitters;

    private boolean enabled = true;

    public CompositeSplitter(@NotNull MinecraftClient client, @NotNull Logger logger,
        @NotNull Collection<AutoSplitSplitter> splitters) {
        this.client = Objects.requireNonNull(client);
        this.logger = Objects.requireNonNull(logger);
        this.splitters = Objects.requireNonNull(splitters);
    }

    public void split() {
        if (!enabled) {
            return;
        }

        for (AutoSplitSplitter splitter : splitters) {
            splitter.startOrSplit().whenComplete((ignored, throwable) -> {
                if (throwable != null) {
                    logger.warn("Failed to split", throwable);
                    client.execute(this::warnFail);
                }
            });
        }
    }

    private void warnFail() {
        if (client.player != null) {
            MutableText message = Text.literal("Failed to split!").formatted(Formatting.RED);
            client.player.sendMessage(message, false);
        }
    }

    public boolean toggle() {
        return enabled = !enabled;
    }

}
