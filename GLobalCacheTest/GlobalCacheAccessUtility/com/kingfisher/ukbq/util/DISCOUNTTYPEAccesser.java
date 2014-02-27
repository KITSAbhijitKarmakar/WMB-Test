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

public class DISCOUNTTYPEAccesser {
	
	public static void getsALL(String DiscntTypKey,MbElement[] mbEleTmp)  {		
		
			try {
				getDisCountTypeDataFromGlobalCache(DiscntTypKey,mbEleTmp);
			} catch (MbUserException e) {
				e.printStackTrace();
			}		
		
	}

	
	@SuppressWarnings("unchecked")
	static void getDisCountTypeDataFromGlobalCache(String DiscntTypKey,MbElement[] envTmp)throws MbUserException {		
		String detailException = "";		 
		
		try {
			// Recover Map from Global Cache
			detailException = detailException + "In Getting Global Map...";
			MbGlobalMap GlobalMBCacheMap = MbGlobalMap.getGlobalMap("GlobalMBCacheMap");
			

			if (GlobalMBCacheMap.containsKey("DISCOUNTTYPE") == true) {
				Map<String, DISCOUNTTYPE> DISCOUNTTYPEMapOut = new HashMap<String, DISCOUNTTYPE>();
				MbElement eleMb=null;
				detailException = detailException+ "In Getting Table from Global Map...";
				byte[] DISCOUNTTYPEbyteIn = (byte[]) GlobalMBCacheMap.get("DISCOUNTTYPE");
				detailException = detailException + "Deserializing..."+ "DISCOUNTTYPE";
				DISCOUNTTYPEMapOut = (Map<String, DISCOUNTTYPE>) GenericMethods.deserialize(DISCOUNTTYPEbyteIn);
				
				//without using key
				if( DiscntTypKey==null || DiscntTypKey.equals("")){
					Set<Map.Entry<String, DISCOUNTTYPE>> DISCOUNTTYPEMapSet = DISCOUNTTYPEMapOut.entrySet();  
					Iterator<Map.Entry<String, DISCOUNTTYPE>> iter = DISCOUNTTYPEMapSet.iterator();
					
					while(iter.hasNext())  
					{  
						DISCOUNTTYPE discntData = iter.next().getValue(); 					
						eleMb=envTmp[0].createElementAsLastChild(MbElement.TYPE_NAME,"QueryResult",null);
						eleMb.createElementAsLastChild(MbElement.TYPE_NAME,"CANVAL",discntData.RJTYPE);
						eleMb.createElementAsLastChild(MbElement.TYPE_NAME,"CONFIGVALUE",discntData.SAPTYPE);
						
					} 
				}
				else {
					//using Key
					DISCOUNTTYPE discntData = DISCOUNTTYPEMapOut.get(DiscntTypKey);
					eleMb=envTmp[0].createElementAsLastChild(MbElement.TYPE_NAME,"QueryResult",null);
					eleMb.createElementAsLastChild(MbElement.TYPE_NAME,"CANVAL",discntData.RJTYPE);
					eleMb.createElementAsLastChild(MbElement.TYPE_NAME,"CONFIGVALUE",discntData.SAPTYPE);
				}
								
				
			}

		} catch (Exception e) {
			throw new MbUserException("Exception:-", detailException, ":-",":", e.toString(), null);
		}
		
	}	

}
