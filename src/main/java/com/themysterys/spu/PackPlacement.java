package com.themysterys.spu;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.server.packs.PackSelectionConfig;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class PackPlacement {

    private static final Path FILE = FabricLoader.getInstance().getConfigDir().resolve("spu-positions.json");
    private static final Type MAP_TYPE = new TypeToken<Map<String, PackSpot>>() {}.getType();
    private static final Gson GSON = new Gson();

    private static Map<String, PackSpot> saved;

    private PackPlacement() {
    }

    public static synchronized PackSpot spotOf(Pack pack) {
        return load().get(keyOf(pack));
    }

    public static synchronized void remember(Pack pack, PackSpot spot) {
        String key = keyOf(pack);
        if (spot.equals(load().get(key))) {
            return;
        }
        saved.put(key, spot);
        try (Writer writer = Files.newBufferedWriter(FILE)) {
            GSON.toJson(saved, MAP_TYPE, writer);
        } catch (IOException e) {
            System.err.println("[SPU] Could not save pack positions: " + e);
        }
    }

    public static boolean isFixedBottom(PackSelectionConfig config) {
        return config.fixedPosition() && config.defaultPosition() == Pack.Position.BOTTOM;
    }

    public static String keyOf(Pack pack) {
        String id = pack.getId();
        if (pack.getPackSource() != PackSource.SERVER) {
            return id;
        }
        String last = id.substring(id.lastIndexOf('/') + 1);
        try {
            return UUID.fromString(last).toString();
        } catch (IllegalArgumentException e) {
            return id;
        }
    }

    private static Map<String, PackSpot> load() {
        if (saved == null) {
            saved = new HashMap<>();
            if (Files.isRegularFile(FILE)) {
                try (Reader reader = Files.newBufferedReader(FILE)) {
                    Map<String, PackSpot> read = GSON.fromJson(reader, MAP_TYPE);
                    if (read != null) {
                        saved.putAll(read);
                    }
                } catch (Exception e) {
                    System.err.println("[SPU] Could not read pack positions, starting over: " + e);
                }
            }
        }
        return saved;
    }
}
