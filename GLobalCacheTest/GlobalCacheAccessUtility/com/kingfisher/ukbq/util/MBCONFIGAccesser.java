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

public class MBCONFIGAccesser {
	
	public static void getsALL(String MbconfigKey,MbElement[] mbEleTmp)  {		
		
			try {
				getMBconfigDataFromGlobalCache(MbconfigKey,mbEleTmp);
			} catch (MbUserException e) {
				e.printStackTrace();
			}		
		
	}

	
	@SuppressWarnings("unchecked")
	static void getMBconfigDataFromGlobalCache(String MbconfigKey,MbElement[] envTmp)throws MbUserException {		
		String detailException = "";		 
		
		try {
			// Recover Map from Global Cache
			detailException = detailException + "In Getting Global Map...";
			MbGlobalMap GlobalMBCacheMap = MbGlobalMap.getGlobalMap("GlobalMBCacheMap");
			

			if (GlobalMBCacheMap.containsKey("MBCONFIG") == true) {
				Map<String, MBCONFIG> MBCONFIGMapOut = new HashMap<String, MBCONFIG>();
				MbElement eleMb=null;
				detailException = detailException+ "In Getting Table from Global Map...";
				byte[] MBCONFIGbyteIn = (byte[]) GlobalMBCacheMap.get("MBCONFIG");
				detailException = detailException + "Deserializing..."+ "MBCONFIG";
				MBCONFIGMapOut = (Map<String, MBCONFIG>) GenericMethods.deserialize(MBCONFIGbyteIn);
				
				//without using key
				if( MbconfigKey==null || MbconfigKey.equals("")){
					Set<Map.Entry<String, MBCONFIG>> MBCONFIGMapSet = MBCONFIGMapOut.entrySet();  
					Iterator<Map.Entry<String, MBCONFIG>> iter = MBCONFIGMapSet.iterator();
					
					while(iter.hasNext())  
					{  
						MBCONFIG mbcnfigData = iter.next().getValue(); 					
						eleMb=envTmp[0].createElementAsLastChild(MbElement.TYPE_NAME,"QueryResult",null);
						eleMb.createElementAsLastChild(MbElement.TYPE_NAME,"CANVAL",mbcnfigData.CANVAL);
						eleMb.createElementAsLastChild(MbElement.TYPE_NAME,"CONFIGVALUE",mbcnfigData.CONFIGVALUE);
						
					} 
				}
				else {
					//using Key
					MBCONFIG mbcnfigData = MBCONFIGMapOut.get(MbconfigKey);
					eleMb=envTmp[0].createElementAsLastChild(MbElement.TYPE_NAME,"QueryResult",null);
					eleMb.createElementAsLastChild(MbElement.TYPE_NAME,"CANVAL",mbcnfigData.CANVAL);
					eleMb.createElementAsLastChild(MbElement.TYPE_NAME,"CONFIGVALUE",mbcnfigData.CONFIGVALUE);
				}
								
				
			}

		} catch (Exception e) {
			throw new MbUserException("Exception:-", detailException, ":-",":", e.toString(), null);
		}
		
	}	

}
