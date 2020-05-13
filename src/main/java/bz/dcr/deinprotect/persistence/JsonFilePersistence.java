package bz.dcr.deinprotect.persistence;

import bz.dcr.deinprotect.DeinProtectPlugin;
import bz.dcr.deinprotect.block.BlockLocation;
import bz.dcr.deinprotect.protection.entity.Protection;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class JsonFilePersistence implements Persistence {

    private ExecutorService executorService;

    private Gson gson;
    private File file;

    private Map<UUID, Protection> protections;

    public JsonFilePersistence(File file, Long saveInterval) throws IOException {
        this.executorService = Executors.newSingleThreadExecutor();
        this.gson = new GsonBuilder()
                .setPrettyPrinting()
                .serializeNulls()
                .create();
        this.file = file;
        this.protections = new ConcurrentHashMap<>();

        if (!file.exists()) {
            file.getParentFile().mkdirs();
            file.createNewFile();
            saveToFile();
        }

        loadFromFile();

        startSaveJob(saveInterval);
    }

    private void startSaveJob(Long interval) {
        Bukkit.getScheduler().runTaskLater(DeinProtectPlugin.getPlugin(), () -> {
            executorService.submit(() -> {
                try {
                    saveToFile();
                } catch (IOException e) {
                    DeinProtectPlugin.getPlugin().getLogger().severe(ChatColor.RED + "Failed to save protection data! Please send this message and the following error message to the plugin author.");
                    e.printStackTrace();
                }
            });
        }, interval * 20);
    }

    private void saveToFile() throws IOException {
        final String json = gson.toJson(protections);
        try (PrintWriter out = new PrintWriter(file)) {
            out.print(json);
            out.flush();
        }
    }

    private void loadFromFile() throws IOException {
        try (JsonReader reader = new JsonReader(new FileReader(file))) {
            final Type type = new TypeToken<Map<UUID, Protection>>(){}.getType();
            protections = gson.fromJson(reader, type);
        }
    }

    @Override
    public void saveProtection(Protection protection) {
        if (protection.getId() == null) {
            protection.setId(UUID.randomUUID());
        }

        protections.put(protection.getId(), protection);
    }

    @Override
    public void deleteProtection(Protection protection) {
        protections.remove(protection.getId());
    }

    @Override
    public Protection getProtection(BlockLocation location) {
        for (Protection protection : protections.values()) {
            if (protection.hasPart(location)) {
                return protection;
            }
        }
        return null;
    }

    @Override
    public void close() throws IOException {
        saveToFile();
    }

}
