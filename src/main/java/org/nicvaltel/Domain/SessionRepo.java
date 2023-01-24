package org.nicvaltel.Domain;

import org.nicvaltel.Domain.Types.SessionId;
import org.nicvaltel.Domain.Types.UserId;

import java.util.Optional;

public interface SessionRepo {

    SessionId newSession(UserId userId);

    Optional<UserId> findUserIdBySessionId(SessionId sessionId);
}
