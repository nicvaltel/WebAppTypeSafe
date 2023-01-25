package org.nicvaltel.Domain.Types;

public class VerificationCode {
    private final String code;


    public VerificationCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    @Override
    public String toString() {
        return "VerificationCode {" + code + "}";
    }
}
