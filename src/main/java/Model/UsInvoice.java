package Model;

import utils.ErrorCode;
import utils.InvoiceFormat;
import utils.NationCode;
import utils.StateTaxCodeRate;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public class UsInvoice implements Invoice{

    private BigDecimal taxRate;
    private BigDecimal preTaxAmt;
    private BigDecimal taxAmt;
    private String stateCode;
    private List<String> items;

    public UsInvoice(String stateCode){
        switch(stateCode){
            case "MA":
                taxRate = StateTaxCodeRate.MA.getTaxRate();
                break;
            case "NY":
                taxRate = StateTaxCodeRate.NY.getTaxRate();
                break;
        }
        this.stateCode = stateCode;
    }


    @Override
    public void procAmtInfo(BigDecimal preTaxTotalAmt) {
        if(preTaxTotalAmt==null || preTaxTotalAmt.intValue()==0){
            String errMsg = String.format("%s %s", InvoiceFormat.invoiceFormatUs[0],ErrorCode.E04.getErrorDescription());
            throw new IllegalArgumentException(errMsg);
        }
        preTaxAmt = preTaxTotalAmt;
        taxAmt = preTaxTotalAmt.multiply(taxRate);
    }
    @Override
    public void setInvoiceInfo(InvoiceInfo invoiceInfo){
        if(invoiceInfo==null){
            String errMsg = String.format("%s %s", InvoiceFormat.invoiceFormatUs[0],ErrorCode.E02.getErrorDescription());
            throw new IllegalArgumentException(errMsg);
        }
        items = invoiceInfo.getItems();
    }

    @Override
    public String printInvoice() {

        if(items==null||items.size()==0){
            throw new IllegalArgumentException(ErrorCode.E02.getErrorDescription());
        }

        StringBuilder sb = new StringBuilder();
        sb.append(InvoiceFormat.invoiceFormatUs[0] + "\n")
                .append(InvoiceFormat.invoiceFormatUs[1] + "\n")
                .append(String.format("%s: \n", InvoiceFormat.invoiceFormatUs[2]));

        for(String item : items){
            sb.append(item + "\n");
        }

        sb.append(String.format("%s: %s \n", InvoiceFormat.invoiceFormatUs[3], preTaxAmt.setScale(2)))
                .append(String.format("%s: %s \n", InvoiceFormat.invoiceFormatUs[4], stateCode));

        return sb.toString();

    }

    @Override
    public BigDecimal getTaxRate() {
        return taxRate;
    }

    @Override
    public String getStateCode() {
        return stateCode;
    }
}
