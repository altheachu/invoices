package utils;

import java.math.BigDecimal;

public enum ErrorCode {

    E01("invalid nation code"),
    E02("insufficient information for printing invoice");

    private final String errorDescription;

    ErrorCode(String errorDescription) {
        this.errorDescription = errorDescription;
    }

    public String getErrorDescription(){
        return errorDescription;
    }
}
