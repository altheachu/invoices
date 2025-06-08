package utils;

import java.math.BigDecimal;

public enum StateTaxCodeRate {

    MA("MA", BigDecimal.valueOf(0.05)),
    NY("NY", BigDecimal.valueOf(0.025));
    private final BigDecimal taxRate;
    private final String stateCode;

    StateTaxCodeRate(String stateCode, BigDecimal taxRate) {
        this.taxRate = taxRate;
        this.stateCode = stateCode;
    }

    public BigDecimal getTaxRate() {
        return taxRate;
    }

    public String getStateCode() {
        return stateCode;
    }
}
