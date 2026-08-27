package got.speech;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimplePreparableReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * Modern equivalent of the legacy GOTSpeech speech-bank map.
 *
 * Files live at data/got/speech/en/<bank>.txt. Each nonblank line is one
 * candidate speech line. Banks are addressed without .txt, e.g.
 * "standard/default_friendly".
 */
public final class GOTSpeechBankRegistry extends SimplePreparableReloadListener<Map<String, List<String>>> {
    private static volatile Map<String, List<String>> BANKS = Map.of();
    private static final Random RANDOM = new Random();

    @Override
    protected Map<String, List<String>> prepare(ResourceManager manager, ProfilerFiller profiler) {
        Map<String, List<String>> loaded = new LinkedHashMap<>();
        manager.listResources("speech/en", id -> id.getPath().endsWith(".txt")).forEach((id, resource) -> {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(resource.open(), StandardCharsets.UTF_8))) {
                String path = id.getPath();
                String bank = path.substring("speech/en/".length(), path.length() - 4);
                List<String> lines = new ArrayList<>();
                String line;
                while ((line = reader.readLine()) != null) {
                    line = stripBom(line).trim();
                    if (!line.isBlank()) lines.add(line);
                }
                loaded.put(bank, List.copyOf(lines));
            } catch (Exception ex) {
                throw new RuntimeException("Failed loading GOT speech bank " + id, ex);
            }
        });
        return Map.copyOf(loaded);
    }

    @Override
    protected void apply(Map<String, List<String>> prepared, ResourceManager manager, ProfilerFiller profiler) {
        BANKS = prepared;
    }

    public static boolean has(String bank) {
        List<String> lines = BANKS.get(bank);
        return lines != null && !lines.isEmpty();
    }

    public static String random(String bank) {
        List<String> lines = BANKS.get(bank);
        if (lines == null || lines.isEmpty()) {
            return "Speech bank " + bank + " could not be found!";
        }
        int first = !lines.isEmpty() && lines.get(0).startsWith("!") ? 1 : 0;
        if (first >= lines.size()) return "Speech bank " + bank + " has no speech lines!";
        return lines.get(first + RANDOM.nextInt(lines.size() - first));
    }

    public static String at(String bank, int line) {
        List<String> lines = BANKS.get(bank);
        if (lines == null || lines.isEmpty()) return "Speech bank " + bank + " could not be found!";
        int first = !lines.isEmpty() && lines.get(0).startsWith("!") ? 1 : 0;
        int index = first + line - 1; // legacy GOTSpeech.getSpeechAtLine is one-based
        if (line < 1 || index < first || index >= lines.size()) return "Speech line " + line + " is out of range!";
        return lines.get(index);
    }

    public static Set<String> banks() {
        return BANKS.keySet();
    }

    private static String stripBom(String s) {
        return s != null && !s.isEmpty() && s.charAt(0) == '\uFEFF' ? s.substring(1) : s;
    }
}
