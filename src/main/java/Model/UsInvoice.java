package Model;

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
        preTaxAmt = preTaxTotalAmt;
        taxAmt = preTaxTotalAmt.multiply(taxRate);
    }
    @Override
    public void setInvoiceInfo(InvoiceInfo invoiceInfo){
        items = invoiceInfo.getItems();
    }

    @Override
    public String printInvoice() {
        StringBuilder sb = new StringBuilder();
        sb.append("US invoice\n")
                .append("======\n")
                .append("items: \n");

        for(String item : items){
            sb.append(item + "\n");
        }

        sb.append(String.format("preTaxAmt: %s \n", preTaxAmt.setScale(2)))
                .append(String.format("stateCode: %s \n", stateCode));

        return sb.toString();

    }
}
