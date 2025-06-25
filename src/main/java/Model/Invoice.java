package Model;

import java.math.BigDecimal;

public interface Invoice {

    void procAmtInfo(BigDecimal preTaxTotalAmt);

    void setInvoiceInfo(InvoiceInfo invoiceInfo);
    String printInvoice();
    BigDecimal getTaxRate();
}
