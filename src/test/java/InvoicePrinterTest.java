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
import java.util.HashSet;
import java.util.Set;

public class InvoicePrinterTest {

    private final String COMMON_NO = "0000";
    private final String INVOICE_NO = "5678";
    private final String INVOICE_TYPE = "電子發票";
    private final String SUPPLIER_NO = "1234";
    private final String TAX_FREE_FLAG = "Y";
    private final BigDecimal PRE_TAX_AMT = BigDecimal.valueOf(500);
    private final BigDecimal ZERO_PRE_TAX_AMT = BigDecimal.valueOf(0);
    private final String INVALID_STATE_CODE = "00";
    private final String[] ITEMS = {"apple","banana","orange"};

    public InvoicePrinter invoicePrinter;

    @Before
    public void initPrinter(){
        invoicePrinter = new InvoicePrinter();
    }
    @Test
    public void proc_invoice_basic_info_happy_path(){

        // TW
        invoicePrinter.procInvoiceBasicInfo(NationCode.TW, null);
        Assert.assertTrue(invoicePrinter.invoice instanceof TwInvoice);
        // JP
        invoicePrinter.procInvoiceBasicInfo(NationCode.JP, null);
        Assert.assertTrue(invoicePrinter.invoice instanceof JpInvoice);
        // US
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
            invoicePrinter.procInvoiceBasicInfo(NationCode.US, INVALID_STATE_CODE);
        } catch (Exception e){
            Assert.assertEquals(ErrorCode.E03.getErrorDescription(),e.getMessage());
        }
    }

    @Test
    public void set_other_invoice_info_happy_path() {

        // Taiwan
        InvoiceInfo invoiceInfoTw = invoiceInfoBuilder(NationCode.TW, true, null);
        Assert.assertEquals(COMMON_NO, invoiceInfoTw.getCommonNo());
        Assert.assertEquals(INVOICE_NO, invoiceInfoTw.getInvoiceNo());
        Assert.assertEquals(INVOICE_TYPE, invoiceInfoTw.getInvoiceType());
        // Jpaan
        InvoiceInfo invoiceInfoJp = invoiceInfoBuilder(NationCode.JP, true, null);
        Assert.assertEquals(SUPPLIER_NO, invoiceInfoJp.getSupplierNo());
        Assert.assertEquals(TAX_FREE_FLAG, invoiceInfoJp.getTaxFreeFlag());
        // US
        InvoiceInfo invoiceInfoUs = invoiceInfoBuilder(NationCode.US, true, null);
        Assert.assertArrayEquals(ITEMS, invoiceInfoUs.getItems().toArray(new String[ITEMS.length]));

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
            invoicePrinter.invoice.procAmtInfo(PRE_TAX_AMT);
            try {
                invoicePrinter.setOtherInvoiceInfo(null);
            } catch (Exception e){
                boolean isKeyErrMsgIncluded = e.getMessage().contains(ErrorCode.E02.getErrorDescription());
                Assert.assertTrue(isKeyErrMsgIncluded);
                if(!isKeyErrMsgIncluded){
                    Assert.assertEquals("錯誤發生在:" + nationCode.getClass(), "設定錯誤", e.getMessage());
                }
            }
        }
    }

    @Test
    public void print_invoice_tw_happy_path(){

        invoicePrinter.procInvoiceBasicInfo(NationCode.TW, null);
        invoicePrinter.invoice.procAmtInfo(PRE_TAX_AMT);

        InvoiceInfo invoiceInfo = invoiceInfoBuilder(NationCode.TW, true, null);
        invoicePrinter.setOtherInvoiceInfo(invoiceInfo);

        BigDecimal curPreTaxAmt = PRE_TAX_AMT.setScale(2);
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
        invoicePrinter.invoice.procAmtInfo(PRE_TAX_AMT);

        String[] nullData = {"common", "invoiceNo", "invoiceType"};

        for(String nullItem : nullData){
            InvoiceInfo invoiceInfoTw = invoiceInfoBuilder(NationCode.TW, false, new String[]{nullItem});
            invoicePrinter.setOtherInvoiceInfo(invoiceInfoTw);

            try {
                invoicePrinter.printInvoice();
            } catch (Exception e){
                Assert.assertEquals("錯誤發生在欄位: " + nullItem, ErrorCode.E02.getErrorDescription() ,e.getMessage());
            }
        }
    }

    @Test
    public void print_invoice_jp(){

        invoicePrinter.procInvoiceBasicInfo(NationCode.JP, null);
        invoicePrinter.invoice.procAmtInfo(PRE_TAX_AMT);
        InvoiceInfo invoiceInfoJp = invoiceInfoBuilder(NationCode.JP, true, null);
        invoicePrinter.setOtherInvoiceInfo(invoiceInfoJp);

        BigDecimal curPreTaxAmt = PRE_TAX_AMT.setScale(2);
        BigDecimal taxRate = invoicePrinter.invoice.getTaxRate().setScale(2);
        BigDecimal taxAmt = curPreTaxAmt.multiply(taxRate).setScale(2);

        StringBuilder sbJp = new StringBuilder();
        sbJp.append(InvoiceFormat.invoiceFormatJp[0] + "\n")
                .append(InvoiceFormat.invoiceFormatJp[1] + "\n")
                .append(String.format("%s: %s \n", InvoiceFormat.invoiceFormatJp[2], invoiceInfoJp.getSupplierNo()))
                .append(String.format("%s: %s \n", InvoiceFormat.invoiceFormatJp[3], curPreTaxAmt))
                .append(String.format("%s: %s \n", InvoiceFormat.invoiceFormatJp[4], taxRate))
                .append(String.format("%s: %s \n", InvoiceFormat.invoiceFormatJp[5], taxAmt))
                .append(String.format("%s: %s \n", InvoiceFormat.invoiceFormatJp[6], invoiceInfoJp.getTaxFreeFlag()));

        Assert.assertEquals(sbJp.toString(), invoicePrinter.printInvoice());
    }

    @Test
    public void print_invoice_jp_fail_path_insufficient_info(){

        invoicePrinter.procInvoiceBasicInfo(NationCode.JP, null);
        invoicePrinter.invoice.procAmtInfo(PRE_TAX_AMT);

        String[] nullData = {"taxFreeFlag", "supplierNo"};

        for(String nullItem : nullData){

            InvoiceInfo invoiceInfoJp = invoiceInfoBuilder(NationCode.JP, false, new String[]{nullItem});
            invoicePrinter.setOtherInvoiceInfo(invoiceInfoJp);

            try {
                invoicePrinter.printInvoice();
            } catch (Exception e){
                Assert.assertEquals("錯誤發生在欄位: " + nullItem, ErrorCode.E02.getErrorDescription(), e.getMessage());
            }
        }
    }

    @Test
    public void print_invoice_us(){
        //US
        invoicePrinter.procInvoiceBasicInfo(NationCode.US, StateTaxCodeRate.MA.getStateCode());
        invoicePrinter.invoice.procAmtInfo(BigDecimal.valueOf(500));

        InvoiceInfo invoiceInfoUs = invoiceInfoBuilder(NationCode.US, true, null);
        invoicePrinter.setOtherInvoiceInfo(invoiceInfoUs);

        BigDecimal curPreTaxAmt = PRE_TAX_AMT.setScale(2);

        StringBuilder sbUs = new StringBuilder();
        sbUs.append(InvoiceFormat.invoiceFormatUs[0] + "\n")
                .append(InvoiceFormat.invoiceFormatUs[1] + "\n")
                .append(String.format("%s: \n", InvoiceFormat.invoiceFormatUs[2]));

        for(String item : ITEMS){
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
        invoicePrinter.invoice.procAmtInfo(PRE_TAX_AMT);

        InvoiceInfo invoiceInfoUsItemsNull = invoiceInfoBuilder(NationCode.US, false, new String[]{"items"});
        invoicePrinter.setOtherInvoiceInfo(invoiceInfoUsItemsNull);

        try {
            invoicePrinter.printInvoice();
        } catch (Exception e){
            Assert.assertEquals(ErrorCode.E02.getErrorDescription(),e.getMessage());
        }

        invoiceInfoUsItemsNull.setItems(new ArrayList<String>());
        invoicePrinter.setOtherInvoiceInfo(invoiceInfoUsItemsNull);

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

            BigDecimal[] invalidPreTaxAmts = {null, ZERO_PRE_TAX_AMT};

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
                Assert.assertEquals("錯誤發生在金額:" + preTaxAmt, invoice.getClass(), e.getMessage());
            }
        }
    }

    private InvoiceInfo invoiceInfoBuilder(NationCode nationCode, boolean setAllInfo, String[] noNeedSetInfo){

        InvoiceInfo newInvoiceInfo = new InvoiceInfo();
        InvoiceInfoBuilder invoiceInfoBuilder = new InvoiceInfoBuilder();

        Set<String> nullSet = new HashSet<>();
        if(!setAllInfo){
            if(noNeedSetInfo!=null){
                nullSet.addAll(Arrays.asList(noNeedSetInfo));
            }
        }

        if (nationCode.equals(NationCode.TW)){

            newInvoiceInfo = invoiceInfoBuilder
                    .withCommonNo(nullSet.contains("commonNo")? null : COMMON_NO)
                    .withInvoiceNo(nullSet.contains("invoiceNo")? null : INVOICE_NO)
                    .withInvoiceType(nullSet.contains("invoiceType")? null : INVOICE_TYPE)
                    .build();

        } else if(nationCode.equals(NationCode.JP)){

            newInvoiceInfo = invoiceInfoBuilder
                    .withSupplierNo(nullSet.contains("supplierNo")? null : SUPPLIER_NO)
                    .withTaxFreeFlag(nullSet.contains("taxFreeFlag")? null : TAX_FREE_FLAG)
                    .build();

        } else if(nationCode.equals(NationCode.US)){

            newInvoiceInfo = invoiceInfoBuilder
                    .withItems(nullSet.contains("items")? null : Arrays.asList(ITEMS))
                    .build();

        }

        return newInvoiceInfo;
    }
}
