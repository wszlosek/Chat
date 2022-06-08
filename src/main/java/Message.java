import java.util.Objects;

public final class Message {
    private final String sender;
    private final String channel;
    private final String channel_type;
    private final String message;

    public Message(String sender, String channel, String channel_type, String message) {
        this.sender = sender;
        this.channel = channel;
        this.channel_type = channel_type;
        this.message = message;
    }

    Message(String sender, String channel_type, String message) {
        this(sender, "", channel_type, message);
    }

    Message(String sender, String channel, ChannelType channel_type, String message) {
        this(sender, channel, channel_type.toString(), message);
    }

    Message(String sender, ChannelType channel_type, String message) {
        this(sender, "", channel_type.toString(), message);
    }

    public String sender() {
        return sender;
    }

    public String channel() {
        return channel;
    }

    public String channel_type() {
        return channel_type;
    }

    public String message() {
        return message;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (Message) obj;
        return Objects.equals(this.sender, that.sender) &&
                Objects.equals(this.channel, that.channel) &&
                Objects.equals(this.channel_type, that.channel_type) &&
                Objects.equals(this.message, that.message);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sender, channel, channel_type, message);
    }

    @Override
    public String toString() {
        String channelSource = switch (channel_type) {
            case "GLOBAL" -> "<GLOBAL>";
            case "NORMAL" -> String.format("[%s]", channel);
            case "DIRECT" -> "(DIRECT)";
            default -> "{UNKNOWN}";
        };
        return String.format("%s %s: %s", channelSource, sender, message);
    }

}

