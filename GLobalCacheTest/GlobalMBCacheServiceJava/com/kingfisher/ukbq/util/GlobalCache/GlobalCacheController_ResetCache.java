package com.kingfisher.ukbq.util.GlobalCache;
/**********************************************************************************************************************
* COPYRIGHT. <KINGFISHER INFORMATION TECHNOLOGY SERVICES 2012> ALL RIGHTS RESERVED. NO PART OF THIS 				  *
* SOURCE CODE MAY BE REPRODUCED, STORED IN A RETRIEVAL SYSTEM, OR TRANSMITTED,IN ANY								  *
* FORM BY ANY MEANS, ELECTRONIC, MECHANICAL, PHOTOCOPYING, RECORDING OR OTHERWISE,									  *
* WITHOUT THE PRIOR WRITTEN PERMISSION OF <KINGFISHER INFORMATION TECHNOLOGY SERVICES>.             				  *                           	
**********************************************************************************************************************/

/**********************************************************************************************************************
* Node Name      		:										  	  			  	              		  			  *									
* Interface Id 	 		:																		  		   			  *
* Interface Name 		:	                                                              *															
* Message Flow 	 		: 											                  *
* Description 	 		: 																							  *
* Module Name  	 		:	Java Compute																			  *            				  	  
* Module Description  	:																							  *                                         
*																									  				  *
* Version   Date	   		Author				Description                     				 				      *
* ======= 	=========== 	=========== 		========================================				  			  *
*  0.1   	15-Aug-2013 	Abhijit Karmakar	The initial version. 		  		  				                  *
**********************************************************************************************************************/

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.ibm.broker.javacompute.MbJavaComputeNode;
import com.kingfisher.ukbq.util.*;

import com.ibm.broker.plugin.MbElement;
import com.ibm.broker.plugin.MbException;
import com.ibm.broker.plugin.MbGlobalMap;
import com.ibm.broker.plugin.MbMessage;
import com.ibm.broker.plugin.MbMessageAssembly;
import com.ibm.broker.plugin.MbXMLNSC;
import com.ibm.broker.plugin.MbXPath;

public class GlobalCacheController_ResetCache extends MbJavaComputeNode {
	
	public void evaluate(MbMessageAssembly inAssembly) throws MbException {
		
		Connection  MBREPOSConn= null;
		String 	CacheCmd=null;		
		MbGlobalMap GlobalMBCacheMap=null;
		List<MbElement> TableList = new ArrayList<MbElement>(); 
		
		MbMessage message = inAssembly.getMessage();
		MbElement inBody= message.getRootElement().getFirstElementByPath(MbXMLNSC.PARSER_NAME);
		CacheCmd= inBody.getFirstElementByPath("MB_GLOBAL_CACHE_CMD").getFirstElementByPath("CMD").getValueAsString();		
		TableList= (List<MbElement>)inBody.evaluateXPath("MB_GLOBAL_CACHE_CMD/TAB");				
		String tabName=""; 
		
		//Removing Table from Cache
		if(CacheCmd.equals("REMOVE") && TableList.size()!=0){
			GlobalMBCacheMap = MbGlobalMap.getGlobalMap("GlobalMBCacheMap");
			
			Iterator<MbElement> iter = TableList.iterator();
			if (iter.hasNext()) {
				tabName=iter.next().getValueAsString().trim();
				if(tabName.equals("ALL")){
					//get the list of Tables from property & remove all of them from cache				
					Properties prop = new Properties();
					ArrayList<String> tableList = new ArrayList<String>();
					try {
						prop.load(new FileInputStream("/support/home/wmbadmin/Proptest/DBconfig.properties"));
						tableList.addAll(Arrays.asList(prop.getProperty("TableList").split(",")));
						for (String table : tableList) {
						    if(!table.equals(null)){
						    	if (GlobalMBCacheMap.containsKey(table)==true)
									GlobalMBCacheMap.remove(table);
						    }
						}
					}catch (IOException e) {						
							e.printStackTrace();						
					}
				}
				else{								
					if (GlobalMBCacheMap.containsKey(tabName)==true)
							GlobalMBCacheMap.remove(tabName);
				}				
				
			}			
				
			return;	
		}
		
		if(CacheCmd.equals("REFRESH") && TableList.size()!=0)
		{	
			if(MBREPOSConn == null){ 
				MBREPOSConn =  getJDBCType4Connection("MBREPOS",JDBC_TransactionType.MB_TRANSACTION_AUTO);
			}
			
			GlobalMBCacheMap = MbGlobalMap.getGlobalMap("GlobalMBCacheMap");
			Iterator<MbElement> iter = TableList.iterator();
			
			if (iter.hasNext()) {
				tabName=iter.next().getValueAsString().trim();
					if(!tabName.equals("ALL"))
						UpdateCache(tabName, GlobalMBCacheMap, MBREPOSConn);
					if(tabName.equals("ALL")){
						Properties prop = new Properties();
						ArrayList<String> tableList = new ArrayList<String>();
						try {
							prop.load(new FileInputStream("/support/home/wmbadmin/Proptest/DBconfig.properties"));
							tableList.addAll(Arrays.asList(prop.getProperty("TableList").split(",")));
							for (String table : tableList) {
							    if(!table.equals(null)){
							    	UpdateCache(table, GlobalMBCacheMap, MBREPOSConn);
							    }
							}
							
							if (MBREPOSConn != null)
								try {
									MBREPOSConn.close();
								} catch (SQLException e) {
									e.printStackTrace();
								}
							return;
							
						} catch (IOException e) {						
							e.printStackTrace();						
						}					
					}
				
			}

			if (MBREPOSConn != null)
				try {
					MBREPOSConn.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			return;	
		}
		else
			return;  // Need to throw Exception saying invalid command

	}
	
	
	private void UpdateCache(String tabName, MbGlobalMap globalMBCacheMap,Connection mBREPOSConn) {
		ResultSet result=null;
		Statement stmt=null;

		if(mBREPOSConn != null) {
			
			try {
				stmt = ((Connection) mBREPOSConn).createStatement();				
				result = stmt.executeQuery("SELECT * FROM "+tabName);				
				globalMBCacheMap.remove(tabName);
				
				if( tabName.equals("MBTOPICS")){
					Map<String, MBTOPICS> MBTOPICSMapIn = new HashMap<String, MBTOPICS>(0);
					MBTOPICS mbtopicRow=null; 
					
					while(result.next()) {
						mbtopicRow = new MBTOPICS(
								result.getString(MBTOPICS.sCANVAL), 
								result.getString(MBTOPICS.sTOPICNAME),
								result.getString(MBTOPICS.sTOPICSTRING)
								);
						MBTOPICSMapIn.put(result.getString("CANVAL"), mbtopicRow);
					}				
				
					try {
						globalMBCacheMap.put("MBTOPICS",GenericMethods.serialize((Object)MBTOPICSMapIn));
					} catch (ClassNotFoundException e) {						
						e.printStackTrace();
					}  
					
				}//end of uploading MBTOPICS Table to Cache
				
				if( tabName.equals("MBSTRCD")){
					Map<String, MBSTRCD> MBSTRCDMapIn = new HashMap<String, MBSTRCD>(0);
					MBSTRCD MBSTRCDRow=null; 				
					
					while(result.next()) {
						MBSTRCDRow = new MBSTRCD(
								result.getString(MBSTRCD.sCANVAL), 
								result.getString(MBSTRCD.sNUMSTORECODE),
								result.getString(MBSTRCD.sCHARSTORECODE), 
								result.getString(MBSTRCD.sFULLSTORECODE), 
								result.getString(MBSTRCD.sSTOREDIVISION), 
								result.getString(MBSTRCD.sNONMFSTORE), 
								result.getString(MBSTRCD.sGLREGIONCOLUMN), 
								result.getString(MBSTRCD.sSTORENAME), 
								result.getString(MBSTRCD.sMCSTORE), 
								result.getString(MBSTRCD.sTRADECOUNTERFLAG), 
								result.getString(MBSTRCD.sVPCTRADINGREGION), 
								result.getString(MBSTRCD.sSTOREOPENDATETIME), 
								result.getString(MBSTRCD.sTESTORE), 
								result.getString(MBSTRCD.sSAPCOSTORE), 
								result.getString(MBSTRCD.sICCSTORE), 
								result.getString(MBSTRCD.sSTORETYPE), 
								result.getString(MBSTRCD.sGLREGION), 
								result.getString(MBSTRCD.sOLASOURCEID), 
								result.getString(MBSTRCD.sNONWMSRCC), 
								result.getString(MBSTRCD.sATGMIGRATED), 
								result.getString(MBSTRCD.sGENERICSTORECODE), 
								result.getString(MBSTRCD.sPRICEFORSTORECODE)
								);
						MBSTRCDMapIn.put(result.getString("CANVAL"), MBSTRCDRow);
					}				
				
					try {
						globalMBCacheMap.put("MBSTRCD",GenericMethods.serialize((Object)MBSTRCDMapIn));
					} catch (ClassNotFoundException e) {
						e.printStackTrace();
					}
				}//end of uploading MBSTRCD Table to Cache
				
				if( tabName.equals("TENDERDATA")){
					Map<String, TENDERDATA> TENDERDATAMapIn = new HashMap<String, TENDERDATA>(0);
					TENDERDATA TENDERDATARow=null; 				
					
					while(result.next()) {
						TENDERDATARow = new TENDERDATA(
								result.getString(TENDERDATA.sRETAILJCARDTYPE), 
								result.getString(TENDERDATA.sPEDTENDERID),
								result.getString(TENDERDATA.sVERIFONETENDERID),
								result.getString(TENDERDATA.sSAPCARDTYPE),
								result.getString(TENDERDATA.sTENDERID),
								result.getString(TENDERDATA.sCARDSCHEMEPROVIDER),
								result.getString(TENDERDATA.sBANKINGDESCRIPTION),
								result.getString(TENDERDATA.sTENDERTYPE),
								result.getString(TENDERDATA.sACQUIRERNAME),
								result.getString(TENDERDATA.sGLACCOUNT),
								result.getString(TENDERDATA.sAPPSTENDERDESCRIPTION),
								result.getString(TENDERDATA.sACQUIRERIDNONUKCANDP),
								result.getString(TENDERDATA.sPANDORATENDERTYPE),
								result.getString(TENDERDATA.sRECONCILIATIONFLAG),
								result.getString(TENDERDATA.sGLTENDERTYPE),
								result.getString(TENDERDATA.sCCCPTENDERDESCRIPTION),
								result.getString(TENDERDATA.sAPPSTENDERTYPE),
								result.getString(TENDERDATA.sACQUIRERNAMENONUK),
								result.getString(TENDERDATA.sTENDERDESCRIPTION),
								result.getString(TENDERDATA.sACQUIRERIDNONUK),
								result.getString(TENDERDATA.sCCCPTENDERTYPE),
								result.getString(TENDERDATA.sACQUIRERIDCANDP),
								result.getString(TENDERDATA.sACCOUNTNUMBER),
								result.getString(TENDERDATA.sSETTLEMENTDESCRIPTION),
								result.getString(TENDERDATA.sFINANCECODE),
								result.getString(TENDERDATA.sHEXFORMAT),
								result.getString(TENDERDATA.sACQUIRERID)								
								);
						TENDERDATAMapIn.put(result.getString("TENDERID"), TENDERDATARow);
					}				
				
					try {
						globalMBCacheMap.put("TENDERDATA",GenericMethods.serialize((Object)TENDERDATAMapIn));
					} catch (ClassNotFoundException e) {
						e.printStackTrace();
					}
				} //end of uploading TENDERDATA Table to Cache
				
				if( tabName.equals("VATDATA")){
					Map<String, VATDATA> VATDATAMapIn = new HashMap<String, VATDATA>(0);
					VATDATA VATDATARow=null; 				
					
					while(result.next()) {
						VATDATARow = new VATDATA(
								result.getString(VATDATA.sVERIFONEVATCODE  ),
								result.getString(VATDATA.sEPOSVATCODE      ),
								result.getString(VATDATA.sGENERICVATCODE   ),
								result.getString(VATDATA.sIBSDEALSVATCODE  ),
								result.getString(VATDATA.sRJCOUNTRYCODE    ),
								result.getString(VATDATA.sRJVATCATEGORY    ),
								result.getString(VATDATA.sSAPCOUNTRYCODE   ),
								result.getString(VATDATA.sSAPVATCODE       ),
								result.getString(VATDATA.sSAPARTCOUNTRYCODE),
								result.getString(VATDATA.sSAPARTVATCODE    ),
								result.getString(VATDATA.sSAPMDVATCODE     ),
								result.getString(VATDATA.sSAPMDVATCATEGORY ),
								result.getString(VATDATA.sSOLVEVATCODE     ),
								result.getString(VATDATA.sTRFVATCODE   )																
								);
						VATDATAMapIn.put(result.getString("test"), VATDATARow);
					}				
				
					try {
						globalMBCacheMap.put("VATDATA",GenericMethods.serialize((Object)VATDATAMapIn));
					} catch (ClassNotFoundException e) {
						e.printStackTrace();
					}
				} //end of uploading VATDATA Table to Cache
				
				if( tabName.equals("MBCONFIG")){
					Map<String, MBCONFIG> MBCONFIGMapIn = new HashMap<String, MBCONFIG>(0);
					MBCONFIG mbcfgRow=null; 
					
					while(result.next()) {
						mbcfgRow = new MBCONFIG(
								result.getString(MBCONFIG.sCANVAL), 
								result.getString(MBCONFIG.sCONFIGVALUE)
								);
						MBCONFIGMapIn.put(result.getString("CANVAL"), mbcfgRow);
					}				
				
					try {
						globalMBCacheMap.put("MBCONFIG",GenericMethods.serialize((Object)MBCONFIGMapIn));
					} catch (ClassNotFoundException e) {						
						e.printStackTrace();
					}  
					
				}//end of uploading MBCONFIG Table to Cache
				
				if( tabName.equals("DISCOUNTTYPE")){
					Map<String, DISCOUNTTYPE> DISCOUNTTYPEMapIn = new HashMap<String, DISCOUNTTYPE>(0);
					DISCOUNTTYPE discountTypRow=null; 
					
					while(result.next()) {
						discountTypRow = new DISCOUNTTYPE(
								result.getString(DISCOUNTTYPE.sRJTYPE), 
								result.getString(DISCOUNTTYPE.sSAPTYPE)
								);
						DISCOUNTTYPEMapIn.put(result.getString("CANVAL"), discountTypRow);
					}				
				
					try {
						globalMBCacheMap.put("DISCOUNTTYPE",GenericMethods.serialize((Object)DISCOUNTTYPEMapIn));
					} catch (ClassNotFoundException e) {						
						e.printStackTrace();
					}  
					
				}//end of uploading DISCOUNTTYPE Table to Cache
				
				stmt.close();	
				
			} catch (MbException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			  catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		
		}
		
	}
	
	
	

}


