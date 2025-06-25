package Model;

import utils.ErrorCode;
import utils.InvoiceFormat;
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
        this.taxRate = BigDecimal.valueOf(0.10);
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
        if (supplierNo==null || taxFreeFlag==null){
            throw new IllegalArgumentException(ErrorCode.E02.getErrorDescription());
        }
        StringBuilder sb = new StringBuilder();
        sb.append(InvoiceFormat.invoiceFormatJp[0] + "\n")
                .append(InvoiceFormat.invoiceFormatJp[1] + "\n")
                .append(String.format("%s: %s \n", InvoiceFormat.invoiceFormatJp[2], supplierNo))
                .append(String.format("%s: %s \n", InvoiceFormat.invoiceFormatJp[3], preTaxAmt.setScale(2)))
                .append(String.format("%s: %s \n", InvoiceFormat.invoiceFormatJp[4], taxRate.setScale(2)))
                .append(String.format("%s: %s \n", InvoiceFormat.invoiceFormatJp[5], taxAmt.setScale(2)))
                .append(String.format("%s: %s \n", InvoiceFormat.invoiceFormatJp[6], taxFreeFlag));
        return sb.toString();
    }

    @Override
    public BigDecimal getTaxRate() {
        return taxRate;
    }
}
