/*
 * TRFStoreTotal.java
 *
 * Created on 02 November 2004, 18:08
 */

package com.bq.mb.utils.fileconversions.trf;
import com.bq.mb.utils.fileconversions.Field;
/**
 *
 * @author  stantr01
 */
public class TRFStoreTotal {
    public Field type = new Field("\n\nType",0,1,"EBCDIC");
    public Field storeCodeAlpha = new Field("\nStoreCode-Alpha",1,3,"EBCDIC");
    public Field storeCodeNum = new Field("\nStoreCode-Num",4,3,"");
    public Field debitCount = new Field("\nDebitCount",7, 8, "SIGNPACKED");
    public Field debitValue = new Field("\nDebitValue",15, 14,"SIGNPACKED");
    public Field creditCount = new Field("\nCreditCount",29, 8, "SIGNPACKED");
    public Field creditValue = new Field("\nCreditValue",37, 14, "SIGNPACKED");
    public Field filler = new Field("\nFiller",51, 76,"EBCDIC");
    
    /** Creates a new instance of TRFStoreTotal */
    public TRFStoreTotal() {
    }
    
}
