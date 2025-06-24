package Model;

import java.util.List;

public class InvoiceInfo {

    private String taxFreeFlag;
    private String supplierNo;
    private String commonNo;
    private String invoiceNo;
    private String invoiceType;
    private List<String> items;

    public String getCommonNo() {
        return commonNo;
    }

    public String getInvoiceNo() {
        return invoiceNo;
    }

    public String getInvoiceType() {
        return invoiceType;
    }

    public String getTaxFreeFlag() { return taxFreeFlag;}

    public String getSupplierNo() {
        return supplierNo;
    }

    public List<String> getItems() {
        return items;
    }

    public void setItems(List<String> items) {
        this.items = items;
    }

    public void setTaxFreeFlag(String taxFreeFlag) {
        this.taxFreeFlag = taxFreeFlag;
    }

    public void setSupplierNo(String supplierNo) {
        this.supplierNo = supplierNo;
    }

    public void setCommonNo(String commonNo) {
        this.commonNo = commonNo;
    }

    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
    }

    public void setInvoiceType(String invoiceType) {
        this.invoiceType = invoiceType;
    }

}
