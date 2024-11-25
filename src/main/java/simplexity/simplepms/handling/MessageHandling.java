package simplexity.simplepms.handling;

public class MessageHandling {
    private static MessageHandling instance;

    private MessageHandling() {
    }

    public static MessageHandling getInstance() {
        if (instance == null) instance = new MessageHandling();
        return instance;
    }

}
