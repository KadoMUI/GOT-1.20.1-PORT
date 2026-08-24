package got.client.achievement;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public final class GOTClientAchievementState {
    private static Set<String> awarded=Set.of();
    private GOTClientAchievementState() {}
    public static void set(Set<String> ids) { awarded=Collections.unmodifiableSet(new HashSet<>(ids)); }
    public static boolean has(String id) { return awarded.contains(id); }
    public static int count() { return awarded.size(); }
}
