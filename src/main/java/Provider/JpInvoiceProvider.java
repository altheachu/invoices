package Provider;

import Model.Invoice;
import Model.JpInvoice;
import utils.NationCode;

import java.math.BigDecimal;

public class JpInvoiceProvider implements InvoiceProvider{

    @Override
    public NationCode getInvoiceType() {
        return NationCode.JP;
    }

    @Override
    public Invoice createInvoice(String stateCode) {
        return new JpInvoice();
    }
}
