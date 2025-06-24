package Service;

import Factory.InvoiceFactory;
import Model.Invoice;
import Model.InvoiceInfo;
import utils.NationCode;

public class InvoicePrinter {

    public Invoice invoice;
    public InvoiceFactory invoiceFactory = new InvoiceFactory();

    public void procInvoiceBasicInfo(NationCode nationCode, String stateCode){
        invoice = invoiceFactory.createInvoice(nationCode, stateCode);
    }

    public void setOtherInvoiceInfo(InvoiceInfo invoiceInfo){
        invoice.setInvoiceInfo(invoiceInfo);
    }

    public String printInvoice(){
        return invoice.printInvoice();
    }

}
