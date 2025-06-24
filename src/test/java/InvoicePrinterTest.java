import Factory.InvoiceFactory;
import Model.*;
import Service.InvoicePrinter;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import utils.NationCode;
import utils.StateTaxCodeRate;

import java.math.BigDecimal;
import java.util.Arrays;

public class InvoicePrinterTest {

    public InvoicePrinter invoicePrinter;
    public InvoiceInfo invoiceInfo;
    @Before
    public void initPrinter(){
        invoicePrinter = new InvoicePrinter();
        invoiceInfo = new InvoiceInfo();
    }
    @Test
    public void procInvoiceBasicInfoTest(){
        invoicePrinter.procInvoiceBasicInfo(NationCode.TW, null);
        Assert.assertTrue(invoicePrinter.invoice instanceof TwInvoice);
        invoicePrinter.procInvoiceBasicInfo(NationCode.JP, null);
        Assert.assertTrue(invoicePrinter.invoice instanceof JpInvoice);
        invoicePrinter.procInvoiceBasicInfo(NationCode.US, StateTaxCodeRate.MA.getStateCode());
        Assert.assertTrue(invoicePrinter.invoice instanceof UsInvoice);
    }

    @Test
    public void setOtherInvoiceInfoTest() {

        String[] itemArray = {"蘋果","banana","みかん"};
        invoiceInfo.setItems(Arrays.asList(itemArray));
        // Taiwan
        invoiceInfo.setCommonNo("0000");
        invoiceInfo.setInvoiceNo("5678");
        invoiceInfo.setInvoiceType("電子發票");
        // Jpaan
        invoiceInfo.setSupplierNo("1234");
        invoiceInfo.setTaxFreeFlag("Y");

        Assert.assertEquals("0000", invoiceInfo.getCommonNo());
        Assert.assertEquals("5678", invoiceInfo.getInvoiceNo());
        Assert.assertEquals("電子發票", invoiceInfo.getInvoiceType());
        Assert.assertEquals("1234", invoiceInfo.getSupplierNo());
        Assert.assertEquals("Y", invoiceInfo.getTaxFreeFlag());
        Assert.assertArrayEquals(itemArray, invoiceInfo.getItems().toArray(new String[itemArray.length]));
    }

    @Test
    public void printInvoiceTest(){
        // TW
        invoicePrinter.procInvoiceBasicInfo(NationCode.TW, null);
        invoicePrinter.invoice.procAmtInfo(BigDecimal.valueOf(500));
        invoiceInfo.setCommonNo("0000");
        invoiceInfo.setInvoiceNo("5678");
        invoiceInfo.setInvoiceType("電子發票");
        invoicePrinter.setOtherInvoiceInfo(invoiceInfo);

        StringBuilder sbTw = new StringBuilder();
        sbTw.append("Taiwan invoice" + "\n")
                .append("======" + "\n")
                .append("commonNo: 0000 \n")
                .append("invoiceNo: 5678 \n")
                .append("invoiceType: 電子發票 \n")
                .append("taxRate: 0.05 \n")
                .append("taxAmt: 25.00 \n")
                .append("taxIncludedAmt: 525.00 \n");
        Assert.assertEquals(sbTw.toString(), invoicePrinter.printInvoice());

        // JP
        invoicePrinter.procInvoiceBasicInfo(NationCode.JP, null);
        invoicePrinter.invoice.procAmtInfo(BigDecimal.valueOf(500));
        invoiceInfo = new InvoiceInfo();
        invoiceInfo.setSupplierNo("1234");
        invoiceInfo.setTaxFreeFlag("Y");
        invoicePrinter.setOtherInvoiceInfo(invoiceInfo);
        StringBuilder sbJp = new StringBuilder();
        sbJp.append("Japan invoice" + "\n")
                .append("======" + "\n")
                .append("supplierNo: 1234 \n")
                .append("preTaxAmt: 500.00 \n")
                .append("taxRate: 0.10 \n")
                .append("taxAmt: 50.00 \n")
                .append("taxFree: Y \n");

        Assert.assertEquals(sbJp.toString(), invoicePrinter.printInvoice());

        //US
        invoicePrinter.procInvoiceBasicInfo(NationCode.US, StateTaxCodeRate.MA.getStateCode());
        invoicePrinter.invoice.procAmtInfo(BigDecimal.valueOf(500));
        String[] itemArray = {"apple","banana","orange"};
        invoiceInfo = new InvoiceInfo();
        invoiceInfo.setItems(Arrays.asList(itemArray));
        invoicePrinter.setOtherInvoiceInfo(invoiceInfo);
        StringBuilder sbUs = new StringBuilder();
        sbUs.append("US invoice" + "\n")
                .append("======" + "\n")
                .append("items: \n");

        for(String item:itemArray){
            sbUs.append(item + "\n");
        }

        sbUs.append("preTaxAmt: 500.00 \n")
                .append("stateCode: MA \n");

        Assert.assertEquals(sbUs.toString(), invoicePrinter.printInvoice());
    }

}
