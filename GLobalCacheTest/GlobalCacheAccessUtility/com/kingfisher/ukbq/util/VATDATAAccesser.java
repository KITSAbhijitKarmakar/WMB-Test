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

public class VATDATAAccesser {
	
	public static void getsALL(String VatDataKey,MbElement[] mbEleTmp)  {		
		
			try {
				getVATDATADataFromGlobalCache(VatDataKey, mbEleTmp);
			} catch (MbUserException e) {
				e.printStackTrace();
			}		
		
	}
	

	@SuppressWarnings("unchecked")
	static void getVATDATADataFromGlobalCache(String VatDataKey,MbElement[] envTmp)throws MbUserException {		
		String detailException = "";		 
		
		try {
			// Recover Map from Global Cache
			detailException = detailException + "In Getting Global Map...";
			MbGlobalMap GlobalMBCacheMap = MbGlobalMap.getGlobalMap("GlobalMBCacheMap");
			

			if (GlobalMBCacheMap.containsKey("VATDATA") == true) {
				Map<String, VATDATA> VATDATAMapOut = new HashMap<String, VATDATA>();
				detailException = detailException+ "In Getting Table from Global Map...";
				byte[] VATDATAbyteIn = (byte[]) GlobalMBCacheMap.get("VATDATA");
				detailException = detailException + "Deserializing..."+ "VATDATA";
				VATDATAMapOut = (Map<String, VATDATA>) GenericMethods.deserialize(VATDATAbyteIn);	
				
				if(VatDataKey==null || VatDataKey.equals("")){
					Set<Map.Entry<String, VATDATA>> VATDATAMapSet = VATDATAMapOut.entrySet();  
					Iterator<Map.Entry<String, VATDATA>> iter = VATDATAMapSet.iterator();  
					
					while(iter.hasNext())  
					{  
						VATDATA vatData = iter.next().getValue(); 
						MbElement eleMb=envTmp[0].createElementAsFirstChild(MbElement.TYPE_NAME,"QueryResult",null);
						eleMb.createElementAsLastChild(MbElement.TYPE_NAME,"VERIFONEVATCODE",  vatData.VERIFONEVATCODE ); 
						eleMb.createElementAsLastChild(MbElement.TYPE_NAME,"EPOSVATCODE",      vatData.EPOSVATCODE      );
						eleMb.createElementAsLastChild(MbElement.TYPE_NAME,"GENERICVATCODE",   vatData.GENERICVATCODE   );
						eleMb.createElementAsLastChild(MbElement.TYPE_NAME,"IBSDEALSVATCODE",  vatData.IBSDEALSVATCODE  );
						eleMb.createElementAsLastChild(MbElement.TYPE_NAME,"RJCOUNTRYCODE",    vatData.RJCOUNTRYCODE    );
						eleMb.createElementAsLastChild(MbElement.TYPE_NAME,"RJVATCATEGORY",    vatData.RJVATCATEGORY    );
						eleMb.createElementAsLastChild(MbElement.TYPE_NAME,"SAPCOUNTRYCODE",   vatData.SAPCOUNTRYCODE   );
						eleMb.createElementAsLastChild(MbElement.TYPE_NAME,"SAPVATCODE",       vatData.SAPVATCODE       );
						eleMb.createElementAsLastChild(MbElement.TYPE_NAME,"SAPARTCOUNTRYCODE",vatData.SAPARTCOUNTRYCODE);
						eleMb.createElementAsLastChild(MbElement.TYPE_NAME,"SAPARTVATCODE",    vatData.SAPARTVATCODE    );
						eleMb.createElementAsLastChild(MbElement.TYPE_NAME,"SAPMDVATCODE",     vatData.SAPMDVATCODE     );
						eleMb.createElementAsLastChild(MbElement.TYPE_NAME,"SAPMDVATCATEGORY", vatData.SAPMDVATCATEGORY );
						eleMb.createElementAsLastChild(MbElement.TYPE_NAME,"SOLVEVATCODE",     vatData.SOLVEVATCODE     );
						eleMb.createElementAsLastChild(MbElement.TYPE_NAME,"TRFVATCODE",       vatData.TRFVATCODE       );
						  
					}
					
				}
				else{
					VATDATA vatData = VATDATAMapOut.get(VatDataKey);
					MbElement eleMb=envTmp[0].createElementAsFirstChild(MbElement.TYPE_NAME,"QueryResult",null);
					eleMb.createElementAsLastChild(MbElement.TYPE_NAME,"VERIFONEVATCODE",  vatData.VERIFONEVATCODE ); 
					eleMb.createElementAsLastChild(MbElement.TYPE_NAME,"EPOSVATCODE",      vatData.EPOSVATCODE      );
					eleMb.createElementAsLastChild(MbElement.TYPE_NAME,"GENERICVATCODE",   vatData.GENERICVATCODE   );
					eleMb.createElementAsLastChild(MbElement.TYPE_NAME,"IBSDEALSVATCODE",  vatData.IBSDEALSVATCODE  );
					eleMb.createElementAsLastChild(MbElement.TYPE_NAME,"RJCOUNTRYCODE",    vatData.RJCOUNTRYCODE    );
					eleMb.createElementAsLastChild(MbElement.TYPE_NAME,"RJVATCATEGORY",    vatData.RJVATCATEGORY    );
					eleMb.createElementAsLastChild(MbElement.TYPE_NAME,"SAPCOUNTRYCODE",   vatData.SAPCOUNTRYCODE   );
					eleMb.createElementAsLastChild(MbElement.TYPE_NAME,"SAPVATCODE",       vatData.SAPVATCODE       );
					eleMb.createElementAsLastChild(MbElement.TYPE_NAME,"SAPARTCOUNTRYCODE",vatData.SAPARTCOUNTRYCODE);
					eleMb.createElementAsLastChild(MbElement.TYPE_NAME,"SAPARTVATCODE",    vatData.SAPARTVATCODE    );
					eleMb.createElementAsLastChild(MbElement.TYPE_NAME,"SAPMDVATCODE",     vatData.SAPMDVATCODE     );
					eleMb.createElementAsLastChild(MbElement.TYPE_NAME,"SAPMDVATCATEGORY", vatData.SAPMDVATCATEGORY );
					eleMb.createElementAsLastChild(MbElement.TYPE_NAME,"SOLVEVATCODE",     vatData.SOLVEVATCODE     );
					eleMb.createElementAsLastChild(MbElement.TYPE_NAME,"TRFVATCODE",       vatData.TRFVATCODE       );
				}
				
				 
				
				
				
				
			}

		} catch (Exception e) {
			throw new MbUserException("Exception:-", detailException, ":-",":", e.toString(), null);
		}
		
	}	

}
