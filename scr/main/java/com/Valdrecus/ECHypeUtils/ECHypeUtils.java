package com.Valdrecus.ECHypeUtils;

import me.clip.placeholderapi.PlaceholderAPI;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class ECHypeUtils extends PlaceholderExpansion {

    private static final DecimalFormat formatter = new DecimalFormat("#,###");

    @Override
    public boolean canRegister() {
        return true;
    }

    @Override
    public String getIdentifier() {
        return "ECHypeUtils";
    }

    @Override
    public String getAuthor() {
        return "Valdrecus";
    }

    @Override
    public String getVersion() {
        return "1.0";
    }

    @Override
    public String onPlaceholderRequest(Player player, String identifier) {
        if (identifier == null) return null;

        if (identifier.equals("sospechoso") || identifier.startsWith("sospechoso_[")) {
            Player evaluatedPlayer = player;

            if (identifier.startsWith("sospechoso_[")) {
                String extracted = extractName(identifier.substring(identifier.indexOf("_[") + 1));
                if (extracted == null || extracted.isEmpty()) return "VPN, nuevo, poco tiempo";

                OfflinePlayer resolved = findPlayerOptimized(extracted);
                if (resolved == null || (!resolved.isOnline() && !resolved.hasPlayedBefore())) return "VPN, nuevo, poco tiempo";
                if (!(resolved instanceof Player)) return "VPN, nuevo, poco tiempo";

                evaluatedPlayer = (Player) resolved;
            }

            if (evaluatedPlayer == null || !evaluatedPlayer.isOnline()) return "VPN, nuevo, poco tiempo";

            List<String> razones = new ArrayList<>();
            String ip = Objects.requireNonNull(evaluatedPlayer.getAddress()).getAddress().getHostAddress();

            // Tiempo < 30m
            int seconds = evaluatedPlayer.getStatistic(Statistic.PLAY_ONE_MINUTE) / 20;
            if (seconds < 1800) {
                razones.add("poco tiempo");
            }

            try {
                URL url = new URL("http://ip-api.com/json/" + ip + "?fields=proxy,hosting,status");
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setConnectTimeout(3000);
                con.setReadTimeout(3000);
                con.setRequestMethod("GET");

                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String json = in.lines().reduce("", (acc, line) -> acc + line);
                in.close();

                if (json.contains("\"status\":\"success\"")) {
                    if (json.contains("\"proxy\":true") || json.contains("\"hosting\":true")) {
                        return "VPN";
                    }
                } else {
                    return "VPN (error)";
                }
            } catch (Exception e) {
                return "VPN (fallo)";
            }
            long joinedAt = evaluatedPlayer.getFirstPlayed();
            long now = System.currentTimeMillis();
            if ((now - joinedAt) < (1000L * 60 * 60 * 24)) {
                return "nuevo";
            }
            return "Limpio";
        }
        return null;
    }
    
    private String extractName(String input) {
        if (input.startsWith("[") && input.contains("]")) {
            return input.substring(1, input.indexOf("]"));
        }
        return null;
    }

    private OfflinePlayer findPlayerOptimized(String name) {
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (p.getName().equalsIgnoreCase(name)) return p;
        }

        for (OfflinePlayer p : Bukkit.getOfflinePlayers()) {
            if (p.getName() != null && p.getName().equalsIgnoreCase(name)) {
                return p;
            }
        }

        if (Bukkit.getOnlineMode()) {
            return null;
        }

        UUID uuid = UUID.nameUUIDFromBytes(("OfflinePlayer:" + name).getBytes(StandardCharsets.UTF_8));
        OfflinePlayer fallback = Bukkit.getOfflinePlayer(uuid);
        return fallback.hasPlayedBefore() ? fallback : null;
    }
} 
