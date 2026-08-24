package got.client.speech;

/**
 * Client-side speech presentation switches matching the old GOTConfig behavior.
 *
 * Defaults intentionally mirror the immersive mode used by the original:
 * - immersive overhead speech enabled
 * - duplicate chat logging disabled
 *
 * These setters are public so the later Settings GUI/config cleanup can expose
 * them without changing the speech packet or renderer.
 */
public final class GOTSpeechClientSettings {
    private static boolean immersiveSpeech = true;
    private static boolean immersiveSpeechChatLog = false;

    private GOTSpeechClientSettings() {}

    public static boolean immersiveSpeech() {
        return immersiveSpeech;
    }

    public static void setImmersiveSpeech(boolean value) {
        immersiveSpeech = value;
    }

    public static boolean immersiveSpeechChatLog() {
        return immersiveSpeechChatLog;
    }

    public static void setImmersiveSpeechChatLog(boolean value) {
        immersiveSpeechChatLog = value;
    }
}
