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
    public String printInvoice() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%s invoice", "Japan") + "\n")
                .append("======" + "\n")
                .append(String.format("supplierNo: %s \n", supplierNo))
                .append(String.format("preTaxAmt: %s \n", preTaxAmt.setScale(2)))
                .append(String.format("taxRate: %s \n", taxRate.setScale(2)))
                .append(String.format("taxAmt: %s \n", taxAmt.setScale(2)))
                .append(String.format("taxFree: %s \n", taxFreeFlag));
        return sb.toString();
    }
}
