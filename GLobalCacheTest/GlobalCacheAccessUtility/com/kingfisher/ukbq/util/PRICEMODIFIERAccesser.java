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

public class PRICEMODIFIERAccesser {
	
	public static void getsALL(String PriceModfierKey,MbElement[] mbEleTmp)  {		
		
			try {
				getProceModifierDataFromGlobalCache(PriceModfierKey,mbEleTmp);
			} catch (MbUserException e) {
				e.printStackTrace();
			}		
		
	}	
	
	@SuppressWarnings("unchecked")
	static void getProceModifierDataFromGlobalCache(String PriceModfierKey,MbElement[] envTmp)throws MbUserException {		
		String detailException = "";		 
		
		try {
			// Recover Map from Global Cache
			detailException = detailException + "In Getting Global Map...";
			MbGlobalMap GlobalMBCacheMap = MbGlobalMap.getGlobalMap("GlobalMBCacheMap");
			

			if (GlobalMBCacheMap.containsKey("PRICEMODIFIER") == true) {
				Map<String, PRICEMODIFIER> PRICEMODIFIERMapOut = new HashMap<String, PRICEMODIFIER>();
				MbElement eleMb=null;
				detailException = detailException+ "In Getting Table from Global Map...";
				byte[] PRICEMODIFIERbyteIn = (byte[]) GlobalMBCacheMap.get("PRICEMODIFIER");
				detailException = detailException + "Deserializing..."+ "PRICEMODIFIER";
				PRICEMODIFIERMapOut = (Map<String, PRICEMODIFIER>) GenericMethods.deserialize(PRICEMODIFIERbyteIn);
				
				//without using key
				if( PriceModfierKey==null || PriceModfierKey.equals("")){
					Set<Map.Entry<String, PRICEMODIFIER>> PRICEMODIFIERMapSet = PRICEMODIFIERMapOut.entrySet();  
					Iterator<Map.Entry<String, PRICEMODIFIER>> iter = PRICEMODIFIERMapSet.iterator();
					
					while(iter.hasNext())  
					{  
						PRICEMODIFIER priceModfierData = iter.next().getValue(); 					
						eleMb=envTmp[0].createElementAsLastChild(MbElement.TYPE_NAME,"QueryResult",null);
						eleMb.createElementAsLastChild(MbElement.TYPE_NAME,"CCCPDESCRIPTION",priceModfierData.CCCPDESCRIPTION);
						eleMb.createElementAsLastChild(MbElement.TYPE_NAME,"RETAILJMODIFIER",priceModfierData.RETAILJMODIFIER);
						eleMb.createElementAsLastChild(MbElement.TYPE_NAME,"MODIFIERID",priceModfierData.MODIFIERID);
						
					} 
				}
				else {
					//using Key
					PRICEMODIFIER priceModfierData = PRICEMODIFIERMapOut.get(PriceModfierKey);
					eleMb=envTmp[0].createElementAsLastChild(MbElement.TYPE_NAME,"QueryResult",null);
					eleMb.createElementAsLastChild(MbElement.TYPE_NAME,"CCCPDESCRIPTION",priceModfierData.CCCPDESCRIPTION);
					eleMb.createElementAsLastChild(MbElement.TYPE_NAME,"RETAILJMODIFIER",priceModfierData.RETAILJMODIFIER);
					eleMb.createElementAsLastChild(MbElement.TYPE_NAME,"MODIFIERID",priceModfierData.MODIFIERID);
				}
								
				
			}

		} catch (Exception e) {
			throw new MbUserException("Exception:-", detailException, ":-",":", e.toString(), null);
		}
		
	}	

}
