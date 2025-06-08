package Provider;

import Model.Invoice;
import Model.TwInvoice;
import utils.NationCode;

public class TwInvoiceProvider implements InvoiceProvider{

    @Override
    public NationCode getInvoiceType() {
        return NationCode.TW;
    }

    @Override
    public Invoice createInvoice(String stateCode) {
        return new TwInvoice();
    }
}
