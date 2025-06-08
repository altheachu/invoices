package Provider;

import Model.Invoice;
import Model.UsInvoice;
import utils.NationCode;

public class UsInvoiceProvider implements InvoiceProvider{
    @Override
    public NationCode getInvoiceType() {
        return NationCode.US;
    }

    @Override
    public Invoice createInvoice(String stateCode) {
        return new UsInvoice(stateCode);
    }
}
