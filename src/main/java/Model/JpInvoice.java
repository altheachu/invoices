package Model;

import utils.NationCode;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public class JpInvoice implements Invoice{

    private BigDecimal taxRate;
    private BigDecimal preTaxAmt;
    private BigDecimal taxAmt;

    private String taxFreeFlag;
    private String supplierNo;

    public JpInvoice(){
        this.taxRate = BigDecimal.valueOf(0.1);
    }


    @Override
    public void procAmtInfo(BigDecimal preTaxTotalAmt) {
        preTaxAmt = preTaxTotalAmt;
        taxAmt = preTaxTotalAmt.multiply(taxRate);
    }
    @Override
    public void setInvoiceInfo(InvoiceInfo invoiceInfo){
        taxFreeFlag = invoiceInfo.getTaxFreeFlag();
        supplierNo = invoiceInfo.getSupplierNo();
    }

    @Override
    public void printInvoice() {
        System.out.println(String.format("%s invoice", "Japan"));
        System.out.println("======");
        System.out.println("supplierNo: " + supplierNo);
        System.out.println("preTaxAmt: " + preTaxAmt);
        System.out.println("taxRate: " + taxRate);
        System.out.println("taxAmt: " + taxAmt);
        System.out.println("taxFree: " + taxFreeFlag);
    }
}
