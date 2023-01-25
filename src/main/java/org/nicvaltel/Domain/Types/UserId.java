package org.nicvaltel.Domain.Types;

public class UserId {
    private final Integer userId;

    public UserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getUserId() {
        return userId;
    }

    @Override
    public String toString() {
        return "UserId {" + userId + "}";
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof UserId)
            return ((UserId)obj).getUserId().equals(this.userId);
        else
            return false;

    }
}
