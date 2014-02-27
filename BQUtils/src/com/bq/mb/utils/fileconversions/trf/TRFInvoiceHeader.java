/*
 * TRFInvoiceHeader.java
 *
 * Created on 02 November 2004, 17:54
 */

package com.bq.mb.utils.fileconversions.trf;
import com.bq.mb.utils.fileconversions.Field;
/**
 *
 * @author  paulma02
 * 
 */
public class TRFInvoiceHeader 
{
    public Field type = new Field("\n\nType",0,1,"EBCDIC");
    public Field storeCode = new Field("\nStoreCode-Alpha",1,3,"EBCDIC");
    public Field storeCodeNum = new Field("\nStoreCode-Num",4,3,"");
    public Field invoiceType = new Field("\nInvoiceType",7,1,"EBCDIC");
    public Field tradeCard = new Field("\nTradeCard",8, 16, "");
    public Field date = new Field("\nDate",24,8,"");
    public Field time = new Field("\nTime",32,4,"");
    public Field vat = new Field("\nVAT",36,12,"SIGNPACKED");
    public Field amount = new Field("\nTotal Amount",48, 12, "SIGNPACKED");
    public Field itemCount = new Field("\nItem Count",60, 4, "SIGNPACKED");
    public Field orderNumber = new Field("\nOrder Number",64, 20,"EBCDIC");
    public Field orderDate = new Field("\nOrder Date",84, 8, "");
    public Field filler = new Field("\nFiller",92, 27, "");

    /** Creates a new instance of TRFInvoiceHeader */
    public TRFInvoiceHeader() {
    }
    
}
