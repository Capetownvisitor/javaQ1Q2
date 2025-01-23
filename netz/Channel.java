package netz;

public class Channel {
    // TODO: add functionality to the Channel Class
    enum ChannelType {
        GLOBAL ("@all"),
        PRIVATE ("@pm"),
        GROUP ("@gm");

        private final String prefix;

        ChannelType(String s) {
            this.prefix = s;
        }

        public String getPrefix() {
            return prefix;
        }
    }

    private String channelName;
    private ChannelType type;
    
}
