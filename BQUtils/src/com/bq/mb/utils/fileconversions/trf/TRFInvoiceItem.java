/*
 * TRFInvoiceItem.java
 *
 * Created on 02 November 2004, 18:01
 */

package com.bq.mb.utils.fileconversions.trf;
import com.bq.mb.utils.fileconversions.Field;
/**
 *
 * @author  paulma02
 */
public class TRFInvoiceItem {
    public Field type = new Field("\n\nType",0,1,"EBCDIC");
    public Field ean = new Field("\nEAN",1,13,"EBCDIC");
    public Field productDesc = new Field("\nProduct Desc",14, 70, "EBCDIC");
    public Field qty = new Field("\nQuantity Sold",84, 6, "SIGNPACKED");
    public Field netPrice = new Field("\nNet Price",90, 12, "SIGNPACKED");
    public Field vatRate = new Field("\nVAT Rate",102, 6,"SIGNPACKED");
    public Field saleValue = new Field("\nSale Value",108, 12, "SIGNPACKED");
    public Field filler4 = new Field("\nFiller4",120, 3, "EBCDIC");
    /** Creates a new instance of TRFInvoiceItem */
    public TRFInvoiceItem() {
    }
    
}
