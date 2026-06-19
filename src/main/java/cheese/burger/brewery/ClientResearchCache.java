package cheese.burger.brewery;

import net.minecraft.util.Identifier;

import java.util.*;

public class ClientResearchCache {
    private static final Map<UUID, Set<String>> knownPotions = new HashMap<>();

    public static void markAsKnown(UUID uuid, Identifier id) {
        knownPotions.computeIfAbsent(uuid, k -> new HashSet<>());
        knownPotions.get(uuid).add(id.toString());
    }

    public static boolean isKnown(UUID uuid, Identifier id) {
        Set<String> known = knownPotions.get(uuid);
        return known != null && known.contains(id.toString());
    }

    public static void clear() {
        knownPotions.clear();
    }
}
