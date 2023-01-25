package org.nicvaltel.Adapter.InMemory;

import org.javatuples.Pair;
import org.nicvaltel.Domain.Types.Email;
import org.nicvaltel.Domain.Types.SessionId;
import org.nicvaltel.Domain.Types.UserId;
import org.nicvaltel.Domain.Types.VerificationCode;
import org.nicvaltel.Domain.Types.Auth;

import java.util.*;

public class State {
    private List<Pair<UserId, Auth>> auth;
    private Map<VerificationCode, Email> unverifiedEmails;
    private Set<Email> verifiedEmails;
    private int userIdCounter;
    private Map<Email,VerificationCode> notifications;
    private Map<SessionId, UserId> sessions;


    private State() {
        this.auth = new ArrayList<>();
        this.unverifiedEmails = new HashMap<>();
        this.verifiedEmails = new HashSet<>();
        this.userIdCounter = 0;
        this.notifications = new HashMap<>();
        this.sessions = new HashMap<>();
    }

    public static State initialState(){
        return new State();
    }

    public Map<Email, VerificationCode> getNotifications() {
        return notifications;
    }

    public Map<SessionId, UserId> getSessions() {
        return sessions;
    }

    public List<Pair<UserId, Auth>> getAuth() {
        return auth;
    }

    public Map<VerificationCode, Email> getUnverifiedEmails() {
        return unverifiedEmails;
    }

    public Set<Email> getVerifiedEmails() {
        return verifiedEmails;
    }

//    public int getUserIdCounter() {
//        return userIdCounter;
//    }

    public int incrementUserIdCounter(){
        userIdCounter++;
        return userIdCounter;
    }

}
