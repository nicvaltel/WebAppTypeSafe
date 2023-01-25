package org.nicvaltel.Domain.Types;

public class SessionId {
    private final String sessionId;


    public SessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getSessionId() {
        return sessionId;
    }

    @Override
    public String toString() {
        return "SessionId {" + sessionId + "}";
    }
}
