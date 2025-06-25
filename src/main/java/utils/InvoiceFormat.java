package utils;

public class InvoiceFormat {
    public static String[] invoiceFormatTw = {"Taiwan invoice", "======", "commonNo", "invoiceNo", "invoiceType", "taxRate", "taxAmt", "taxIncludedAmt"};
    public static String[] invoiceFormatJp = {"Japan invoice", "======", "supplierNo", "preTaxAmt", "taxRate", "taxAmt", "taxFree"};
    public static String[] invoiceFormatUs = {"US invoice", "======", "items", "preTaxAmt", "stateCode"};
}
