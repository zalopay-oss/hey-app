package model;

import java.io.Serializable;
import java.util.List;

public class ChatMessageRequest extends WsMessage implements Serializable {
    private String message;
    private String sessionId;
    private List<String> usernames;
    private boolean groupChat;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public List<String> getUsernames() {
        return usernames;
    }

    public void setUsernames(List<String> usernames) {
        this.usernames = usernames;
    }

    public boolean isGroupChat() {
        return groupChat;
    }

    public void setGroupChat(boolean groupChat) {
        this.groupChat = groupChat;
    }
}
