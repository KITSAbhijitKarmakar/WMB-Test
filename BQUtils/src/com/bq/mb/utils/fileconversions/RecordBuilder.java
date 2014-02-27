/*
 * RecordBuilder.java
 *
 * Created on 02 November 2004, 17:16
 */

package com.bq.mb.utils.fileconversions;
import com.bq.mb.utils.MainframeConversions;
import java.lang.StringBuffer;
import java.util.Arrays;
import java.io.*;

/**
 *
 * @author  stantr01
 */
public class RecordBuilder 
{
    Object obj;
    char[] record;
    MainframeConversions mfc = new MainframeConversions();
    String rec;
    //String outputFile;
    
    /** Creates a new instance of RecordBuilder */
    public RecordBuilder() {
    }
    
    public RecordBuilder(Object obj, char[] record, String outputFile)
    {
        this.obj = obj;
        this.record = record;
        rec = new String(record);
        //this.outputFile = outputFile;
        
        //String response = buildRecord();
        //System.out.println("File Write: "+response);
        //writeFile(response, outputFile);
    }
    
    
    
    public void writeFile(String output, String file)
    {
        try
        {
            FileWriter fw = new FileWriter(file,false);
            BufferedWriter bw = new BufferedWriter(fw);
            
            bw.write(output);
            
            bw.close();
            fw.close();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    
    public StringBuffer buildRecord()
    {
        try
        {
            StringBuffer sb = new StringBuffer();
            
            java.lang.reflect.Field[] fields = obj.getClass().getDeclaredFields();
            //int offset = 0;
            Field customField[] = new Field[fields.length];
            for(int n=0; n<fields.length; n++)
            {
                java.lang.reflect.Field field = fields[n];
                customField[n] = (Field)field.get(obj);
            }
            Arrays.sort(customField);
            for(int n=0; n<customField.length; n++)
            {
                //java.lang.reflect.Field field = fields[n];
                //Field f = (Field)field.get(obj);
            	Field f = customField [n];
                String type = f.type;
                String name = f.name;
                int length = f.length;
                int startPosition = f.startPosition;
                //System.out.println(name+", "+type+", "+startPosition+" , "+length);

                String temp = rec.substring(startPosition,startPosition+length);
                //System.out.println("Record value ASCII: "+temp+" Length: "+length);
                //offset = offset+length;
                
                //System.out.println("Offset now = "+offset);
                
                if(type.equals("EBCDIC"))
                {
                    //temp = mfc.EBCDIC_to_ASCII(temp);
                	temp = mfc.asciiToEBCDIC(temp,length);
                    //System.out.println("Record value EBCDIC: "+temp);
                    f.value=temp;
                    //sb.append(f.name+"="+temp);
                }
                else if(type.equals("SIGNPACKED"))
                {
                    //temp = mfc.convertPackedBytesToAscii(temp.getBytes());
                    //temp = mfc.convertPackedToAscii(temp);
                	temp = mfc.asciiToPacked(temp, length/2, true);
                	//System.out.println("Record value SIGNPACKED: "+temp);
                    f.value=temp;
                    //sb.append(f.name+"="+temp);
                }
                else if(type.equals("PACKED"))
                {
                    //temp = mfc.convertPackedBytesToAscii(temp.getBytes());
                    //temp = mfc.convertPackedToAscii(temp);
                	temp = mfc.asciiToPacked(temp, length, false);
                	//System.out.println("Record value PACKED: "+temp);
                    f.value=temp;
                    //sb.append(f.name+"="+temp);
                }
                else if(type.equals(""))
                {
                    //temp = mfc.EBCDIC_to_ASCII(temp);
                	temp = mfc.asciiToEBCDIC(temp, length);
                	//System.out.println("Record value EBCDIC: "+temp);
                    //sb.append(f.name+"="+temp);
                }
                sb.append(temp);
            }
            //System.out.print("String: "+sb.toString());
            return sb;
        }
        catch(Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }
}
