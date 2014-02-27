/*
 * TRFFileHeader.java
 *
 * Created on 02 November 2004, 16:55
 */

package com.bq.mb.utils.fileconversions.trf;
import com.bq.mb.utils.fileconversions.Field;
/**
 *
 * @author  paulma02
 */
public class TRFFileHeader 
{
    public Field type = new Field("type",0,1,"EBCDIC");
    public Field retailedID = new Field("\nRetailerID",1,7,"EBCDIC");
    public Field creationDate = new Field("\nCreationDate",8,8,"");
    public Field transactionFrom = new Field("\nTransactionFromDate",16,8,"");
    public Field transactionTo = new Field("\nTransactionToDate",24,8,"");
    public Field SequenceNumber = new Field("\nSequenceNumber",32,8,"");
    public Field filler = new Field("\nFiller",40,65,"");
    
    /** Creates a new instance of TRFFileHeader */
    public TRFFileHeader() 
    {
    }
    
}
