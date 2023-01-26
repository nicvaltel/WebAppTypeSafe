package org.nicvaltel.Domain;

import org.nicvaltel.Common.Empty;
import org.nicvaltel.Domain.Types.Email;
import org.nicvaltel.Domain.Types.VerificationCode;

public interface EmailVerificationNotif {
    Empty notifyEmailVerification(Email email, VerificationCode verificationCode);
}
