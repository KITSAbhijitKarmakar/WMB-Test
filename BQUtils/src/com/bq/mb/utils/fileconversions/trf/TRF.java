/*
 * TRF.java
 *
 * Created on 02 November 2004, 17:02
 */

package com.bq.mb.utils.fileconversions.trf;
import java.io.*;

import com.bq.mb.utils.MainframeConversions;
import com.bq.mb.utils.fileconversions.RecordBuilder;
/**
 *
 * @author  stantr01
 */
public class TRF {
    public MainframeConversions mfc = new MainframeConversions();
    /** Creates a new instance of TRF */
    public TRF() {
    }
    
    /**
     * @param args the command line arguments
     */
    public void encodeFiles(String inputFileName, String outputFileName) 
    {
        try
        {
            char[] record = new char[105];
            String recordLine = null;
            StringBuffer recordSB = new StringBuffer();
            RecordBuilder recordBuilder = new RecordBuilder();
            
            String filename = inputFileName;
            
            File input = new File(filename);
            FileReader fr = new FileReader(input);
            BufferedReader br = new BufferedReader(fr);
            
            while(br.ready())
            {
            	recordLine = br.readLine();
            	String rec = Character.toString(recordLine.charAt(0));
                //String rec = mfc.EBCDIC_to_ASCII(new String(type));
                
                //System.out.println("Record Type = "+rec);
                //System.out.println("Record  = "+recordLine);
                
                if(rec.equals("A"))
                {
                    //System.out.println("Found File Header");
                    TRFFileHeader header = new TRFFileHeader();
                    recordBuilder = new RecordBuilder(header, recordLine.toCharArray(), filename+"-OUTPUT");
                }
                else if(rec.equals("H"))
                {
                    //System.out.println("Found Invoice Header");
                    TRFInvoiceHeader invHeader = new TRFInvoiceHeader();
                    recordBuilder = new RecordBuilder(invHeader, recordLine.toCharArray(), filename+"-OUTPUT");
                    
                }
                else if(rec.equals("I"))
                {
                    //System.out.println("Found Invoice Item");
                    TRFInvoiceItem invItem = new TRFInvoiceItem();
                    recordBuilder = new RecordBuilder(invItem, recordLine.toCharArray(), filename+"-OUTPUT");
                    
                }
                else if(rec.equals("S"))
                {
                    //System.out.println("Found Store Total");
                    TRFStoreTotal storeTotal = new TRFStoreTotal();
                    recordBuilder = new RecordBuilder(storeTotal, recordLine.toCharArray(), filename+"-OUTPUT");
                }
                else if(rec.equals("F"))
                {
                    //System.out.println("Found File Total");
                    TRFFileTotals fileTotal = new TRFFileTotals();
                    recordBuilder = new RecordBuilder(fileTotal, recordLine.toCharArray(), filename+"-OUTPUT");
                }
                //System.out.println("Total SB before append = "+recordSB.toString());
                recordSB = recordSB.append(recordBuilder.buildRecord());
                //System.out.println("Total SB after append = "+recordSB.toString());
            }
            //System.out.println("Total SB = "+recordSB.toString());
            writeFile(recordSB.toString(), outputFileName);
            br.close();
            fr.close();
            
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }    
    }
    public void writeFile(String output, String file)
    {
        try
        {	
        	String TRFCodePage=System.getProperty("TRFCodePage");
        	if(TRFCodePage==null) {
        		TRFCodePage="ISO8859_1";
    		}
    		
        	// 'ISO8859_1' code page is used to retain the information of HEX bytes. Else all
        	// packed decimal fields gets corrupted when written into the o/p file using default windows codepage 'cp1252'
        	Writer fw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file),TRFCodePage));
            fw.write(output);
            fw.close();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
}
