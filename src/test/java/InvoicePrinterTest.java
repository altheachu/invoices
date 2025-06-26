import Factory.InvoiceFactory;
import Model.*;
import Service.InvoicePrinter;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import utils.ErrorCode;
import utils.InvoiceFormat;
import utils.NationCode;
import utils.StateTaxCodeRate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;

public class InvoicePrinterTest {

    private final String commonNo = "0000";
    private final String invoiceNo = "5678";
    private final String invoiceType = "電子發票";
    private final String supplierNo = "1234";
    private final String taxFreeFlag = "Y";
    private final BigDecimal preTaxAmt = BigDecimal.valueOf(500);
    private final BigDecimal zeroPreTaxAmt = BigDecimal.valueOf(0);
    private final String invalidStateCode = "00";

    public InvoicePrinter invoicePrinter;
    public InvoiceInfo invoiceInfo;
    @Before
    public void initPrinter(){
        invoicePrinter = new InvoicePrinter();
        invoiceInfo = new InvoiceInfo();
    }
    @Test
    public void proc_invoice_basic_info_happy_path(){
        invoicePrinter.procInvoiceBasicInfo(NationCode.TW, null);
        Assert.assertTrue(invoicePrinter.invoice instanceof TwInvoice);
        invoicePrinter.procInvoiceBasicInfo(NationCode.JP, null);
        Assert.assertTrue(invoicePrinter.invoice instanceof JpInvoice);
        invoicePrinter.procInvoiceBasicInfo(NationCode.US, StateTaxCodeRate.MA.getStateCode());
        Assert.assertTrue(invoicePrinter.invoice instanceof UsInvoice);
    }

    @Test
    public void proc_invoice_basic_info_fail_path(){
        try {
            invoicePrinter.procInvoiceBasicInfo(NationCode.UK, null);
        } catch (Exception e){
            Assert.assertEquals(ErrorCode.E01.getErrorDescription(),e.getMessage());
        }
    }

    @Test
    public void proc_invoice_basic_info_fail_path_us(){
        try {
            invoicePrinter.procInvoiceBasicInfo(NationCode.US, invalidStateCode);
        } catch (Exception e){
            Assert.assertEquals(ErrorCode.E03.getErrorDescription(),e.getMessage());
        }
    }

    @Test
    public void set_other_invoice_info_happy_path() {

        String[] itemArray = {"蘋果","banana","みかん"};
        invoiceInfo.setItems(Arrays.asList(itemArray));
        // Taiwan
        invoiceInfo.setCommonNo(commonNo);
        invoiceInfo.setInvoiceNo(invoiceNo);
        invoiceInfo.setInvoiceType(invoiceType);
        // Jpaan
        invoiceInfo.setSupplierNo(supplierNo);
        invoiceInfo.setTaxFreeFlag(taxFreeFlag);

        Assert.assertEquals(commonNo, invoiceInfo.getCommonNo());
        Assert.assertEquals(invoiceNo, invoiceInfo.getInvoiceNo());
        Assert.assertEquals(invoiceType, invoiceInfo.getInvoiceType());
        Assert.assertEquals(supplierNo, invoiceInfo.getSupplierNo());
        Assert.assertEquals(taxFreeFlag, invoiceInfo.getTaxFreeFlag());
        Assert.assertArrayEquals(itemArray, invoiceInfo.getItems().toArray(new String[itemArray.length]));
    }

    @Test
    public void set_other_invoice_info_fail_path() {

        NationCode[] nationCodes = {NationCode.TW, NationCode.JP, NationCode.US};

        for(NationCode nationCode : nationCodes){
            String stateCode = null;
            if(nationCode.equals(NationCode.US)){
                stateCode = StateTaxCodeRate.NY.getStateCode();
            }
            invoicePrinter.procInvoiceBasicInfo(nationCode, stateCode);
            invoicePrinter.invoice.procAmtInfo(preTaxAmt);
            try {
                invoicePrinter.setOtherInvoiceInfo(null);
            } catch (Exception e){
                boolean isKeyErrMsgIncluded = e.getMessage().contains(ErrorCode.E02.getErrorDescription());
                Assert.assertTrue(isKeyErrMsgIncluded);
                if(!isKeyErrMsgIncluded){
                    Assert.assertEquals("log", e.getMessage());
                }
            }
        }
    }

    @Test
    public void print_invoice_tw_happy_path(){
        invoicePrinter.procInvoiceBasicInfo(NationCode.TW, null);
        invoicePrinter.invoice.procAmtInfo(preTaxAmt);

        invoiceInfo = new InvoiceInfo();
        invoiceInfo.setCommonNo(commonNo);
        invoiceInfo.setInvoiceNo(invoiceNo);
        invoiceInfo.setInvoiceType(invoiceType);
        invoicePrinter.setOtherInvoiceInfo(invoiceInfo);

        BigDecimal curPreTaxAmt = preTaxAmt.setScale(2);
        BigDecimal taxRate = invoicePrinter.invoice.getTaxRate().setScale(2);
        BigDecimal taxAmt = curPreTaxAmt.multiply(taxRate).setScale(2);

        StringBuilder sbTw = new StringBuilder();
        sbTw.append(InvoiceFormat.invoiceFormatTw[0] + "\n")
                .append(InvoiceFormat.invoiceFormatTw[1] + "\n")
                .append(String.format("%s: %s \n", InvoiceFormat.invoiceFormatTw[2], invoiceInfo.getCommonNo()))
                .append(String.format("%s: %s \n", InvoiceFormat.invoiceFormatTw[3] ,invoiceInfo.getInvoiceNo()))
                .append(String.format("%s: %s \n", InvoiceFormat.invoiceFormatTw[4], invoiceInfo.getInvoiceType()))
                .append(String.format("%s: %s \n", InvoiceFormat.invoiceFormatTw[5], taxRate))
                .append(String.format("%s: %s \n", InvoiceFormat.invoiceFormatTw[6], taxAmt))
                .append(String.format("%s: %s \n", InvoiceFormat.invoiceFormatTw[7], curPreTaxAmt.add(taxAmt)));
        Assert.assertEquals(sbTw.toString(), invoicePrinter.printInvoice());
    }

    @Test
    public void print_invoice_tw_fail_path_insufficient_info(){

        invoicePrinter.procInvoiceBasicInfo(NationCode.TW, null);
        invoicePrinter.invoice.procAmtInfo(preTaxAmt);

        invoiceInfo = new InvoiceInfo();
        invoiceInfo.setCommonNo(null);
        invoiceInfo.setInvoiceNo(invoiceNo);
        invoiceInfo.setInvoiceType(invoiceType);
        invoicePrinter.setOtherInvoiceInfo(invoiceInfo);

        try {
            invoicePrinter.printInvoice();
        } catch (Exception e){
            Assert.assertEquals(ErrorCode.E02.getErrorDescription(),e.getMessage());
        }

        invoiceInfo.setCommonNo(commonNo);
        invoiceInfo.setInvoiceNo(null);
        invoicePrinter.setOtherInvoiceInfo(invoiceInfo);

        try {
            invoicePrinter.printInvoice();
        } catch (Exception e){
            Assert.assertEquals(ErrorCode.E02.getErrorDescription(),e.getMessage());
        }

        invoiceInfo.setInvoiceNo(invoiceNo);
        invoiceInfo.setInvoiceType(null);
        invoicePrinter.setOtherInvoiceInfo(invoiceInfo);

        try {
            invoicePrinter.printInvoice();
        } catch (Exception e){
            Assert.assertEquals(ErrorCode.E02.getErrorDescription(),e.getMessage());
        }
    }

    @Test
    public void print_invoice_jp(){
        invoicePrinter.procInvoiceBasicInfo(NationCode.JP, null);
        invoicePrinter.invoice.procAmtInfo(preTaxAmt);
        invoiceInfo = new InvoiceInfo();
        invoiceInfo.setSupplierNo(supplierNo);
        invoiceInfo.setTaxFreeFlag(taxFreeFlag);
        invoicePrinter.setOtherInvoiceInfo(invoiceInfo);

        BigDecimal curPreTaxAmt = preTaxAmt.setScale(2);
        BigDecimal taxRate = invoicePrinter.invoice.getTaxRate().setScale(2);
        BigDecimal taxAmt = curPreTaxAmt.multiply(taxRate).setScale(2);

        StringBuilder sbJp = new StringBuilder();
        sbJp.append(InvoiceFormat.invoiceFormatJp[0] + "\n")
                .append(InvoiceFormat.invoiceFormatJp[1] + "\n")
                .append(String.format("%s: %s \n", InvoiceFormat.invoiceFormatJp[2], invoiceInfo.getSupplierNo()))
                .append(String.format("%s: %s \n", InvoiceFormat.invoiceFormatJp[3], curPreTaxAmt))
                .append(String.format("%s: %s \n", InvoiceFormat.invoiceFormatJp[4], taxRate))
                .append(String.format("%s: %s \n", InvoiceFormat.invoiceFormatJp[5], taxAmt))
                .append(String.format("%s: %s \n", InvoiceFormat.invoiceFormatJp[6], invoiceInfo.getTaxFreeFlag()));

        Assert.assertEquals(sbJp.toString(), invoicePrinter.printInvoice());
    }

    @Test
    public void print_invoice_jp_fail_path_insufficient_info(){

        invoicePrinter.procInvoiceBasicInfo(NationCode.JP, null);
        invoicePrinter.invoice.procAmtInfo(preTaxAmt);

        invoiceInfo = new InvoiceInfo();
        invoiceInfo.setSupplierNo(supplierNo);
        invoiceInfo.setTaxFreeFlag(null);
        invoicePrinter.setOtherInvoiceInfo(invoiceInfo);

        try {
            invoicePrinter.printInvoice();
        } catch (Exception e){
            Assert.assertEquals(ErrorCode.E02.getErrorDescription(),e.getMessage());
        }

        invoiceInfo.setSupplierNo(null);
        invoiceInfo.setTaxFreeFlag(taxFreeFlag);
        invoicePrinter.setOtherInvoiceInfo(invoiceInfo);

        try {
            invoicePrinter.printInvoice();
        } catch (Exception e){
            Assert.assertEquals(ErrorCode.E02.getErrorDescription(),e.getMessage());
        }

    }

    @Test
    public void print_invoice_us(){
        //US
        invoicePrinter.procInvoiceBasicInfo(NationCode.US, StateTaxCodeRate.MA.getStateCode());
        invoicePrinter.invoice.procAmtInfo(BigDecimal.valueOf(500));
        String[] itemArray = {"apple","banana","orange"};
        invoiceInfo = new InvoiceInfo();
        invoiceInfo.setItems(Arrays.asList(itemArray));
        invoicePrinter.setOtherInvoiceInfo(invoiceInfo);

        BigDecimal curPreTaxAmt = preTaxAmt.setScale(2);

        StringBuilder sbUs = new StringBuilder();
        sbUs.append(InvoiceFormat.invoiceFormatUs[0] + "\n")
                .append(InvoiceFormat.invoiceFormatUs[1] + "\n")
                .append(String.format("%s: \n", InvoiceFormat.invoiceFormatUs[2]));

        for(String item:itemArray){
            sbUs.append(item + "\n");
        }

        sbUs.append(String.format("%s: %s \n", InvoiceFormat.invoiceFormatUs[3], curPreTaxAmt))
                .append(String.format("%s: %s \n", InvoiceFormat.invoiceFormatUs[4], StateTaxCodeRate.MA.getStateCode()));

        Assert.assertEquals(sbUs.toString(), invoicePrinter.printInvoice());
        Assert.assertEquals(StateTaxCodeRate.MA.getTaxRate(), invoicePrinter.invoice.getTaxRate());
    }

    @Test
    public void print_invoice_us_fail_path_insufficient_info(){

        invoicePrinter.procInvoiceBasicInfo(NationCode.US, StateTaxCodeRate.MA.getStateCode());
        invoicePrinter.invoice.procAmtInfo(preTaxAmt);

        invoiceInfo = new InvoiceInfo();
        invoiceInfo.setItems(null);
        invoicePrinter.setOtherInvoiceInfo(invoiceInfo);

        try {
            invoicePrinter.printInvoice();
        } catch (Exception e){
            Assert.assertEquals(ErrorCode.E02.getErrorDescription(),e.getMessage());
        }

        invoiceInfo.setItems(new ArrayList<String>());
        invoicePrinter.setOtherInvoiceInfo(invoiceInfo);

        try {
            invoicePrinter.printInvoice();
        } catch (Exception e){
            Assert.assertEquals(ErrorCode.E02.getErrorDescription(),e.getMessage());
        }
    }

    @Test
    public void print_invoice_fail_path_invalid_amt(){

        NationCode[] nationCodes = {NationCode.TW, NationCode.JP, NationCode.US};

        for(NationCode nationCode : nationCodes){

            String stateCode = null;

            if(nationCode.equals(NationCode.US)){
                stateCode = StateTaxCodeRate.NY.getStateCode();
            }

            invoicePrinter.procInvoiceBasicInfo(nationCode, stateCode);

            BigDecimal[] invalidPreTaxAmts = {null, zeroPreTaxAmt};

            for (BigDecimal invalidPreTaxAmt : invalidPreTaxAmts){
                invalidPreTaxAmtCheckHelper(invoicePrinter.invoice, invalidPreTaxAmt);
            }
        }
    }

    private void invalidPreTaxAmtCheckHelper(Invoice invoice, BigDecimal preTaxAmt){
        try {
            invoice.procAmtInfo(preTaxAmt);
        } catch (Exception e){
            boolean isKeyErrMsgIncluded = e.getMessage().contains(ErrorCode.E04.getErrorDescription());
            Assert.assertTrue(isKeyErrMsgIncluded);
            if(!isKeyErrMsgIncluded){
                Assert.assertEquals("log", e.getMessage());
            }
        }
    }
}
