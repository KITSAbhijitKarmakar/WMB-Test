/*
 * ConfigData.java
 *
 * Created on February 23, 2004, 1:59 PM
 */

package com.bq.mb.utils;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Connection;


/**
 *
 * @author  stantr01
 * @version 
 */
public class ConfigData {
	
	Connection dbCon = null;
    /** Creates new ConfigData */
    public ConfigData(Connection repos) 
    {
    	dbCon = repos;
    }

   /**
     * Returns a value from the IBConfig Relationship that matches the specified key
     *
     * @param key A String which is the key to be found
     * @return Returns a String which is the value related to the key
     * @exception Throws Exception when nothing found or something goes wrong.
     */
    public String getConfigData(String key) throws Exception
    {
    	ResultSet rSet=null;
    	PreparedStatement pstatement=null;
        try
        {
        	String value = "";
        	//Code change done by Deep Bhattacharya (TCS) for WICS Migration to WMB 
        	//Using MBCONFIG in place of IBCONFIG_CONFIG_T
        	String sqlStmt = "Select CONFIGVALUE from MBCONFIG where CANVAL=?";
        	pstatement = dbCon.prepareStatement(sqlStmt);
        	pstatement.setString(1, key);
        	rSet = pstatement.executeQuery();
        	
        	if(rSet.next())
        	{
        		value = rSet.getString("CONFIGVALUE");
        	}
        	if(rSet!=null)
        	{
        		rSet.close();
        		pstatement.close();
        	}
        	return value;
        }
        catch(Exception e)
        {
            throw e;
        }
        finally
        {
        	if(rSet!=null)
        	{
        		rSet.close();
        	}
        	if(pstatement!=null)
        	{
        		pstatement.close();
        	}
       	
        }
    }
    
    
}
