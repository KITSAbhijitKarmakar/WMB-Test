/*
 * TRFFileTotals.java
 *
 * Created on 02 November 2004, 18:13
 */

package com.bq.mb.utils.fileconversions.trf;
import com.bq.mb.utils.fileconversions.Field;
/**
 *
 * @author  paulma02
 */
public class TRFFileTotals {
    public Field type = new Field("\n\nType",0,1,"EBCDIC");
    public Field storeCodeAlpha = new Field("\nStoreCode-Alpha",1, 3, "EBCDIC");
    public Field storeCodeNum = new Field("\nStoreCode-Num",4,3,"");
    public Field debitTXCount = new Field("\nDebitTXCount",7, 8, "SIGNPACKED");
    public Field debitTXValue = new Field("\nDebitTXValue",15,14,"SIGNPACKED");
    public Field creditTXCount = new Field("\nCreditTXCount",29,8,"SIGNPACKED");
    public Field creditTXValue = new Field("\nCreditTXValue",37,14,"SIGNPACKED");
    public Field filler = new Field("\nFiller",51, 76, "EBCDIC");
    /** Creates a new instance of TRFFileTotals */
    public TRFFileTotals() {
    }
    
}
