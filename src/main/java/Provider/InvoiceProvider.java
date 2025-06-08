package Provider;

import Model.Invoice;
import Model.InvoiceInfo;
import utils.NationCode;

import java.math.BigDecimal;

public interface InvoiceProvider {

    NationCode getInvoiceType();

    Invoice createInvoice(String stateCode);

}
