package org.phantazm.zombies.mapeditor.client;

import com.github.steanky.ethylene.core.ConfigCodec;
import com.github.steanky.ethylene.core.bridge.Configuration;
import com.github.steanky.ethylene.mapper.MappingProcessorSource;
import com.github.steanky.vector.Vec3I;
import org.jetbrains.annotations.NotNull;
import org.phantazm.commons.FileUtils;
import org.phantazm.zombies.map.*;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Objects;

public final class MapWriter {
    private final ConfigCodec codec;
    private final MappingProcessorSource mappingProcessorSource;
    private final String ext;

    public MapWriter(@NotNull ConfigCodec codec, @NotNull MappingProcessorSource mappingProcessorSource) {
        this.codec = Objects.requireNonNull(codec);
        this.mappingProcessorSource = Objects.requireNonNull(mappingProcessorSource);

        String ext = codec.getPreferredExtension();
        this.ext = ext.isEmpty() ? "" : "." + ext;
    }

    private Path file(Path path, String name) {
        return path.resolve(name + ext);
    }

    private static String formatVec(Vec3I vec) {
        return vec.x() + "_" + vec.y() + "_" + vec.z();
    }

    public void writeMap(@NotNull MapInfo mapInfo, @NotNull Path root) throws IOException {
        FileUtils.ensureDirectories(root);

        //setup directories
        Path mapDirectory = root.resolve(mapInfo.key().value());
        FileUtils.deleteRecursivelyIfExists(mapDirectory);
        FileUtils.ensureDirectories(mapDirectory);

        //write top-level files
        Configuration.write(file(mapDirectory, "coins"), codec,
            mappingProcessorSource.processorFor(PlayerCoinsInfo.class), mapInfo.playerCoins());

        Configuration.write(file(mapDirectory, "corpse"), mapInfo.corpse(), codec);
        Configuration.write(file(mapDirectory, "endless"), mapInfo.endless(), codec);
        Configuration.write(file(mapDirectory, "leaderboard"), mapInfo.leaderboard(), codec);

        Configuration.write(file(mapDirectory, "settings"), codec,
            mappingProcessorSource.processorFor(MapSettingsInfo.class), mapInfo.settings());
        Configuration.write(file(mapDirectory, "sidebar"), mapInfo.scoreboard(), codec);

        Configuration.write(file(mapDirectory, "webhook"), codec,
            mappingProcessorSource.processorFor(WebhookInfo.class), mapInfo.webhook());

        //subdirectories
        Path doorsDirectory = mapDirectory.resolve("doors");
        Path roomsDirectory = mapDirectory.resolve("rooms");
        Path roundsDirectory = mapDirectory.resolve("rounds");
        Path shopsDirectory = mapDirectory.resolve("shops");
        Path spawnpointsDirectory = mapDirectory.resolve("spawnpoints");
        Path spawnrulesDirectory = mapDirectory.resolve("spawnrules");
        Path windowsDirectory = mapDirectory.resolve("windows");

        FileUtils.ensureDirectories(doorsDirectory, roomsDirectory, roundsDirectory, shopsDirectory,
            spawnpointsDirectory, spawnrulesDirectory, windowsDirectory);

        //write all doors, spawnpoints, etc
        for (DoorInfo door : mapInfo.doors()) {
            Configuration.write(file(doorsDirectory, door.id().value()), codec,
                mappingProcessorSource.processorFor(DoorInfo.class), door);
        }

        for (RoomInfo room : mapInfo.rooms()) {
            Configuration.write(file(roomsDirectory, room.id().value()), codec,
                mappingProcessorSource.processorFor(RoomInfo.class), room);
        }

        for (RoundInfo round : mapInfo.rounds()) {
            Configuration.write(file(roundsDirectory, "r" + round.round()), codec,
                mappingProcessorSource.processorFor(RoundInfo.class), round);
        }

        for (ShopInfo shop : mapInfo.shops()) {
            Configuration.write(file(shopsDirectory,
                    formatVec(shop.trigger().immutableOrigin()) + "-" + shop.id().value()), codec,
                mappingProcessorSource.processorFor(ShopInfo.class), shop);
        }

        for (SpawnpointInfo spawnpoint : mapInfo.spawnpoints()) {
            Configuration.write(file(spawnpointsDirectory, formatVec(spawnpoint.position())), codec,
                mappingProcessorSource.processorFor(SpawnpointInfo.class), spawnpoint);
        }

        for (SpawnruleInfo spawnrule : mapInfo.spawnrules()) {
            Configuration.write(file(spawnrulesDirectory, spawnrule.id().value()), codec,
                mappingProcessorSource.processorFor(SpawnruleInfo.class), spawnrule);
        }

        for (WindowInfo window : mapInfo.windows()) {
            Configuration.write(file(windowsDirectory, formatVec(window.frameRegion().immutableOrigin())), codec,
                mappingProcessorSource.processorFor(WindowInfo.class), window);
        }
    }
}
