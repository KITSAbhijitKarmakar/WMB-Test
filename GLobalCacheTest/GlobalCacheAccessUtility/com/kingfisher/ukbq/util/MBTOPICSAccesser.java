package com.kingfisher.ukbq.util;
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

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.ibm.broker.plugin.MbElement;
import com.ibm.broker.plugin.MbGlobalMap;
import com.ibm.broker.plugin.MbUserException;

public class MBTOPICSAccesser {
	private String sCANVAL = null;
	private static String sTOPICNAME = null;
	private static String sTOPICSTRING = null;

	String getsCANVAL(String arg) throws MbUserException {
		//sCANVAL = getMBtopicDataFromGlobalCache(arg);
		return sCANVAL;

	}

	public String getsTOPICNAME(String canValValue) throws MbUserException {
		//sTOPICNAME = getMBtopicDataFromGlobalCache(canValValue);
		return sTOPICNAME;
	}
	
	public static void getsALL(String MbtopicsKey,MbElement[] mbEleTmp)  {		
		
			try {
				getMBtopicDataFromGlobalCache(MbtopicsKey,mbEleTmp);
			} catch (MbUserException e) {
				e.printStackTrace();
			}		
		
	}

	public static String getsTOPICSTRING(String canValValue) {
		/*try {
			//sTOPICSTRING = getMBtopicDataFromGlobalCache(canValValue);			
		} catch (MbUserException e) {
			e.printStackTrace();
		}*/
		return sTOPICSTRING;
	}

	@SuppressWarnings("unchecked")
	static void getMBtopicDataFromGlobalCache(String MbtopicsKey,MbElement[] envTmp)throws MbUserException {		
		String detailException = "";		 
		
		try {
			// Recover Map from Global Cache
			detailException = detailException + "In Getting Global Map...";
			MbGlobalMap GlobalMBCacheMap = MbGlobalMap.getGlobalMap("GlobalMBCacheMap");
			

			if (GlobalMBCacheMap.containsKey("MBTOPICS") == true) {
				Map<String, MBTOPICS> MBTOPICSMapOut = new HashMap<String, MBTOPICS>();
				MbElement eleMb=null;
				detailException = detailException+ "In Getting Table from Global Map...";
				byte[] MBTOPICSbyteIn = (byte[]) GlobalMBCacheMap.get("MBTOPICS");
				detailException = detailException + "Deserializing..."+ "MBTOPICS";
				MBTOPICSMapOut = (Map<String, MBTOPICS>) GenericMethods.deserialize(MBTOPICSbyteIn);
				
				//without using key
				if( MbtopicsKey==null|| MbtopicsKey.equals("")){
					Set<Map.Entry<String, MBTOPICS>> MBTOPICSMapSet = MBTOPICSMapOut.entrySet();  
					Iterator<Map.Entry<String, MBTOPICS>> iter = MBTOPICSMapSet.iterator();
					
					while(iter.hasNext())  
					{  
						MBTOPICS mbtopData = iter.next().getValue(); 					
						eleMb=envTmp[0].createElementAsLastChild(MbElement.TYPE_NAME,"QueryResult",null);
						eleMb.createElementAsLastChild(MbElement.TYPE_NAME,"CANVAL",mbtopData.CANVAL);
						eleMb.createElementAsLastChild(MbElement.TYPE_NAME,"TOPICNAME",mbtopData.TOPICNAME);
						eleMb.createElementAsLastChild(MbElement.TYPE_NAME,"TOPICSTRING",mbtopData.TOPICSTRING);
					} 
				}
				else {
					//using Key
					MBTOPICS mbTop = MBTOPICSMapOut.get(MbtopicsKey);
					eleMb=envTmp[0].createElementAsLastChild(MbElement.TYPE_NAME,"QueryResult",null);
					eleMb.createElementAsLastChild(MbElement.TYPE_NAME,"CANVAL",mbTop.CANVAL);
					eleMb.createElementAsLastChild(MbElement.TYPE_NAME,"TOPICNAME",mbTop.TOPICNAME);
					eleMb.createElementAsLastChild(MbElement.TYPE_NAME,"TOPICSTRING",mbTop.TOPICSTRING);
				}
				
				
								
				
			}

		} catch (Exception e) {
			throw new MbUserException("Exception:-", detailException, ":-",":", e.toString(), null);
		}
		
	}	

}
