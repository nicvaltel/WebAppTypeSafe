package org.nicvaltel.Domain;

import org.nicvaltel.Domain.Types.Email;
import org.nicvaltel.Domain.Types.VerificationCode;

public interface EmailVerificationNotif {
    Void notifyEmailVerification(Email email, VerificationCode verificationCode);
}
