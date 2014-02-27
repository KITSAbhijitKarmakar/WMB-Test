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

public class TENDERDATAAccesser {
	
	public static void getsALL(String TenderDataKey,MbElement[] mbEleTmp)  {		
		
			try {
				getTENDERDATADataFromGlobalCache(TenderDataKey, mbEleTmp);
			} catch (MbUserException e) {
				e.printStackTrace();
			}		
		
	}
	

	@SuppressWarnings("unchecked")
	static void getTENDERDATADataFromGlobalCache(String TenderDataKey,MbElement[] envTmp)throws MbUserException {		
		String detailException = "";		 
		
		try {
			// Recover Map from Global Cache
			detailException = detailException + "In Getting Global Map...";
			MbGlobalMap GlobalMBCacheMap = MbGlobalMap.getGlobalMap("GlobalMBCacheMap");
			

			if (GlobalMBCacheMap.containsKey("TENDERDATA") == true) {
				Map<String, TENDERDATA> TENDERDATAMapOut = new HashMap<String, TENDERDATA>();
				MbElement eleMb=null;
				TENDERDATAMapOut = new HashMap<String, TENDERDATA>();
				detailException = detailException+ "In Getting Table from Global Map...";
				byte[] TENDERDATAbyteIn = (byte[]) GlobalMBCacheMap.get("TENDERDATA");
				detailException = detailException + "Deserializing..."+ "TENDERDATA";
				TENDERDATAMapOut = (Map<String, TENDERDATA>) GenericMethods.deserialize(TENDERDATAbyteIn);
				
				if(TenderDataKey==null || TenderDataKey.equals("")){
					Set<Map.Entry<String, TENDERDATA>> TENDERDATAMapSet = TENDERDATAMapOut.entrySet();  
					Iterator<Map.Entry<String, TENDERDATA>> iter = TENDERDATAMapSet.iterator();
					
					while(iter.hasNext())  
					{  
						TENDERDATA tndrData = iter.next().getValue(); 					
						eleMb=envTmp[0].createElementAsFirstChild(MbElement.TYPE_NAME,"QueryResult",null);
						eleMb.createElementAsLastChild(MbElement.TYPE_NAME,"RETAILJCARDTYPE",tndrData.RETAILJCARDTYPE       );
						eleMb.createElementAsLastChild(MbElement.TYPE_NAME,"PEDTENDERID",tndrData.PEDTENDERID           );
						eleMb.createElementAsLastChild(MbElement.TYPE_NAME,"VERIFONETENDERID",tndrData.VERIFONETENDERID      );
						eleMb.createElementAsLastChild(MbElement.TYPE_NAME,"SAPCARDTYPE",tndrData.SAPCARDTYPE           );
						eleMb.createElementAsLastChild(MbElement.TYPE_NAME,"TENDERID",tndrData.TENDERID              );
						eleMb.createElementAsLastChild(MbElement.TYPE_NAME,"CARDSCHEMEPROVIDER",tndrData.CARDSCHEMEPROVIDER    );
						eleMb.createElementAsLastChild(MbElement.TYPE_NAME,"BANKINGDESCRIPTION",tndrData.BANKINGDESCRIPTION    );
						eleMb.createElementAsLastChild(MbElement.TYPE_NAME,"TENDERTYPE",tndrData.TENDERTYPE            );
						eleMb.createElementAsLastChild(MbElement.TYPE_NAME,"ACQUIRERNAME",tndrData.ACQUIRERNAME          );
						eleMb.createElementAsLastChild(MbElement.TYPE_NAME,"GLACCOUNT",tndrData.GLACCOUNT             );
						eleMb.createElementAsLastChild(MbElement.TYPE_NAME,"APPSTENDERDESCRIPTION",tndrData.APPSTENDERDESCRIPTION );
						eleMb.createElementAsLastChild(MbElement.TYPE_NAME,"ACQUIRERIDNONUKCANDP",tndrData.ACQUIRERIDNONUKCANDP  );
						eleMb.createElementAsLastChild(MbElement.TYPE_NAME,"PANDORATENDERTYPE",tndrData.PANDORATENDERTYPE     );
						eleMb.createElementAsLastChild(MbElement.TYPE_NAME,"RECONCILIATIONFLAG",tndrData.RECONCILIATIONFLAG    );
						eleMb.createElementAsLastChild(MbElement.TYPE_NAME,"GLTENDERTYPE",tndrData.GLTENDERTYPE          );
						eleMb.createElementAsLastChild(MbElement.TYPE_NAME,"CCCPTENDERDESCRIPTION",tndrData.CCCPTENDERDESCRIPTION );
						eleMb.createElementAsLastChild(MbElement.TYPE_NAME,"APPSTENDERTYPE",tndrData.APPSTENDERTYPE        );
						eleMb.createElementAsLastChild(MbElement.TYPE_NAME,"ACQUIRERNAMENONUK",tndrData.ACQUIRERNAMENONUK     );
						eleMb.createElementAsLastChild(MbElement.TYPE_NAME,"TENDERDESCRIPTION",tndrData.TENDERDESCRIPTION     );
						eleMb.createElementAsLastChild(MbElement.TYPE_NAME,"ACQUIRERIDNONUK",tndrData.ACQUIRERIDNONUK       );
						eleMb.createElementAsLastChild(MbElement.TYPE_NAME,"CCCPTENDERTYPE",tndrData.CCCPTENDERTYPE        );
						eleMb.createElementAsLastChild(MbElement.TYPE_NAME,"ACQUIRERIDCANDP",tndrData.ACQUIRERIDCANDP       );
						eleMb.createElementAsLastChild(MbElement.TYPE_NAME,"ACCOUNTNUMBER",tndrData.ACCOUNTNUMBER         );
						eleMb.createElementAsLastChild(MbElement.TYPE_NAME,"SETTLEMENTDESCRIPTION",tndrData.SETTLEMENTDESCRIPTION );
						eleMb.createElementAsLastChild(MbElement.TYPE_NAME,"FINANCECODE",tndrData.FINANCECODE           );
						eleMb.createElementAsLastChild(MbElement.TYPE_NAME,"HEXFORMAT",tndrData.HEXFORMAT             );
						eleMb.createElementAsLastChild(MbElement.TYPE_NAME,"ACQUIRERID",tndrData.ACQUIRERID            );
					} 
				}
				else{
					TENDERDATA tndrData = TENDERDATAMapOut.get(TenderDataKey);
					eleMb=envTmp[0].createElementAsFirstChild(MbElement.TYPE_NAME,"QueryResult",null);
					eleMb.createElementAsLastChild(MbElement.TYPE_NAME,"RETAILJCARDTYPE",tndrData.RETAILJCARDTYPE       );
					eleMb.createElementAsLastChild(MbElement.TYPE_NAME,"PEDTENDERID",tndrData.PEDTENDERID           );
					eleMb.createElementAsLastChild(MbElement.TYPE_NAME,"VERIFONETENDERID",tndrData.VERIFONETENDERID      );
					eleMb.createElementAsLastChild(MbElement.TYPE_NAME,"SAPCARDTYPE",tndrData.SAPCARDTYPE           );
					eleMb.createElementAsLastChild(MbElement.TYPE_NAME,"TENDERID",tndrData.TENDERID              );
					eleMb.createElementAsLastChild(MbElement.TYPE_NAME,"CARDSCHEMEPROVIDER",tndrData.CARDSCHEMEPROVIDER    );
					eleMb.createElementAsLastChild(MbElement.TYPE_NAME,"BANKINGDESCRIPTION",tndrData.BANKINGDESCRIPTION    );
					eleMb.createElementAsLastChild(MbElement.TYPE_NAME,"TENDERTYPE",tndrData.TENDERTYPE            );
					eleMb.createElementAsLastChild(MbElement.TYPE_NAME,"ACQUIRERNAME",tndrData.ACQUIRERNAME          );
					eleMb.createElementAsLastChild(MbElement.TYPE_NAME,"GLACCOUNT",tndrData.GLACCOUNT             );
					eleMb.createElementAsLastChild(MbElement.TYPE_NAME,"APPSTENDERDESCRIPTION",tndrData.APPSTENDERDESCRIPTION );
					eleMb.createElementAsLastChild(MbElement.TYPE_NAME,"ACQUIRERIDNONUKCANDP",tndrData.ACQUIRERIDNONUKCANDP  );
					eleMb.createElementAsLastChild(MbElement.TYPE_NAME,"PANDORATENDERTYPE",tndrData.PANDORATENDERTYPE     );
					eleMb.createElementAsLastChild(MbElement.TYPE_NAME,"RECONCILIATIONFLAG",tndrData.RECONCILIATIONFLAG    );
					eleMb.createElementAsLastChild(MbElement.TYPE_NAME,"GLTENDERTYPE",tndrData.GLTENDERTYPE          );
					eleMb.createElementAsLastChild(MbElement.TYPE_NAME,"CCCPTENDERDESCRIPTION",tndrData.CCCPTENDERDESCRIPTION );
					eleMb.createElementAsLastChild(MbElement.TYPE_NAME,"APPSTENDERTYPE",tndrData.APPSTENDERTYPE        );
					eleMb.createElementAsLastChild(MbElement.TYPE_NAME,"ACQUIRERNAMENONUK",tndrData.ACQUIRERNAMENONUK     );
					eleMb.createElementAsLastChild(MbElement.TYPE_NAME,"TENDERDESCRIPTION",tndrData.TENDERDESCRIPTION     );
					eleMb.createElementAsLastChild(MbElement.TYPE_NAME,"ACQUIRERIDNONUK",tndrData.ACQUIRERIDNONUK       );
					eleMb.createElementAsLastChild(MbElement.TYPE_NAME,"CCCPTENDERTYPE",tndrData.CCCPTENDERTYPE        );
					eleMb.createElementAsLastChild(MbElement.TYPE_NAME,"ACQUIRERIDCANDP",tndrData.ACQUIRERIDCANDP       );
					eleMb.createElementAsLastChild(MbElement.TYPE_NAME,"ACCOUNTNUMBER",tndrData.ACCOUNTNUMBER         );
					eleMb.createElementAsLastChild(MbElement.TYPE_NAME,"SETTLEMENTDESCRIPTION",tndrData.SETTLEMENTDESCRIPTION );
					eleMb.createElementAsLastChild(MbElement.TYPE_NAME,"FINANCECODE",tndrData.FINANCECODE           );
					eleMb.createElementAsLastChild(MbElement.TYPE_NAME,"HEXFORMAT",tndrData.HEXFORMAT             );
					eleMb.createElementAsLastChild(MbElement.TYPE_NAME,"ACQUIRERID",tndrData.ACQUIRERID            );
				}
				
				
				
			}

		} catch (Exception e) {
			throw new MbUserException("Exception:-", detailException, ":-",":", e.toString(), null);
		}
		
	}	

}
