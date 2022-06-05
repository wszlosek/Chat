public record MESSAGE(String sender, String channel, String channel_type, String message) {
    MESSAGE(String sender, String channel_type, String message) {
        this(sender, "", channel_type, message);
    }

    MESSAGE(String sender, String channel, ChannelType channel_type, String message) {
        this(sender, channel, channel_type.toString(), message);
    }

    MESSAGE(String sender, ChannelType channel_type, String message) {
        this(sender, "", channel_type.toString(), message);
    }

    public String getChatMessage() {
        String channelSource = switch (channel_type) {
            case "GLOBAL" -> "<GLOBAL>";
            case "NORMAL" -> String.format("[%s]", channel);
            case "DIRECT" -> "(DIRECT)";
            default -> "{UNKNOWN}";
        };
        return String.format("%s %s: %s", channelSource, sender, message);
    }
}

