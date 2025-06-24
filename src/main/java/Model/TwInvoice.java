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
    public String printInvoice(){
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%s invoice", "Taiwan") + "\n")
                .append("======" + "\n")
                .append(String.format("commonNo: %s \n", commonNo))
                .append(String.format("invoiceNo: %s \n", invoiceNo))
                .append(String.format("invoiceType: %s \n", invoiceType))
                .append(String.format("taxRate: %s \n", taxRate.setScale(2)))
                .append(String.format("taxAmt: %s \n", taxAmt.setScale(2)))
                .append(String.format("taxIncludedAmt: %s \n", taxIncludedAmt.setScale(2)));
        return sb.toString();
    }
}
