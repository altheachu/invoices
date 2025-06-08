package Model;

import utils.NationCode;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public class TwInvoice implements Invoice{

    private BigDecimal taxRate;
    private BigDecimal taxIncludedAmt;
    private BigDecimal taxAmt;

    public String commonNo;
    public String invoiceNo;
    public String invoiceType;

    public TwInvoice(){
        this.taxRate = BigDecimal.valueOf(0.05);
    }

    @Override
    public void procAmtInfo(BigDecimal preTaxTotalAmt) {
        taxAmt = preTaxTotalAmt.multiply(taxRate);
        taxIncludedAmt = preTaxTotalAmt.multiply(taxRate.add(BigDecimal.valueOf(1)));
    }
    @Override
    public void setInvoiceInfo(InvoiceInfo invoiceInfo){
        commonNo = invoiceInfo.getCommonNo();
        invoiceNo = invoiceInfo.getInvoiceNo();
        invoiceType = invoiceInfo.getInvoiceType();
    }

    @Override
    public void printInvoice(){
        System.out.println(String.format("%s invoice", "Taiwan"));
        System.out.println("======");
        System.out.println("commonNo: " + commonNo);
        System.out.println("invoiceNo: " + invoiceNo);
        System.out.println("invoiceType: " + invoiceType);
        System.out.println("taxRate: " + taxRate);
        System.out.println("taxAmt: " + taxAmt);
        System.out.println("taxIncludedAmt: " + taxIncludedAmt);
    }
}
