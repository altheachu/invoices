package utils;

import java.math.BigDecimal;

public enum ErrorCode {

    E01("invalid nation code"),
    E02("insufficient information for printing invoice"),
    E03("invalid state code"),
    E04("invalid pre-tax amount");
    private final String errorDescription;

    ErrorCode(String errorDescription) {
        this.errorDescription = errorDescription;
    }

    public String getErrorDescription(){
        return errorDescription;
    }
}
