import Model.InvoiceInfo;

import java.util.Arrays;
import java.util.List;

public class InvoiceInfoBuilder {

    private final InvoiceInfo invoiceInfo;

    public InvoiceInfoBuilder() {
        this.invoiceInfo = new InvoiceInfo();
    }

    public InvoiceInfoBuilder withCommonNo(String commonNo){
        invoiceInfo.setCommonNo(commonNo);
        return this;
    }

    public InvoiceInfoBuilder withInvoiceNo(String invoiceNo){
        invoiceInfo.setInvoiceNo(invoiceNo);
        return this;
    }

    public InvoiceInfoBuilder withInvoiceType(String invoiceType){
        invoiceInfo.setInvoiceType(invoiceType);
        return this;
    }

    public InvoiceInfoBuilder withSupplierNo(String supplierNo){
        invoiceInfo.setSupplierNo(supplierNo);
        return this;
    }

    public InvoiceInfoBuilder withTaxFreeFlag(String taxFreeFlag){
        invoiceInfo.setTaxFreeFlag(taxFreeFlag);
        return this;
    }

    public InvoiceInfoBuilder withItems(List<String> items){
        invoiceInfo.setItems(items);
        return this;
    }

    public InvoiceInfo build(){
        return invoiceInfo;
    }
}
