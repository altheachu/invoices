package Model;

import utils.ErrorCode;
import utils.InvoiceFormat;
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
        if (commonNo==null || invoiceNo==null || invoiceType==null){
            throw new IllegalArgumentException(ErrorCode.E02.getErrorDescription());
        }
        StringBuilder sb = new StringBuilder();
        sb.append(InvoiceFormat.invoiceFormatTw[0] + "\n")
                .append(InvoiceFormat.invoiceFormatTw[1] + "\n")
                .append(String.format("%s: %s \n", InvoiceFormat.invoiceFormatTw[2] ,commonNo))
                .append(String.format("%s: %s \n", InvoiceFormat.invoiceFormatTw[3] ,invoiceNo))
                .append(String.format("%s: %s \n",InvoiceFormat.invoiceFormatTw[4] ,invoiceType))
                .append(String.format("%s: %s \n", InvoiceFormat.invoiceFormatTw[5] ,taxRate.setScale(2)))
                .append(String.format("%s: %s \n", InvoiceFormat.invoiceFormatTw[6], taxAmt.setScale(2)))
                .append(String.format("%s: %s \n", InvoiceFormat.invoiceFormatTw[7],taxIncludedAmt.setScale(2)));
        return sb.toString();
    }

    @Override
    public BigDecimal getTaxRate() {
        return taxRate;
    }
}
