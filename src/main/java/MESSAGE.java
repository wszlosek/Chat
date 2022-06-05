public record MESSAGE(String sender, String channel, String channel_type, String message) {
    MESSAGE(String sender, String channel_type, String message) {
        this(sender, "", channel_type, message);
    }
}
