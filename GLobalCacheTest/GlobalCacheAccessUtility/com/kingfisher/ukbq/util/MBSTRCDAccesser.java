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

public class MBSTRCDAccesser {
	
	
	public static void getsALL(String MbstrcdKey,MbElement[] mbEleTmp)  {		
		
			try {
				getMBSTRCDDataFromGlobalCache(MbstrcdKey,mbEleTmp);
			} catch (MbUserException e) {
				e.printStackTrace();
			}		
		
	}

	

	@SuppressWarnings("unchecked")
	static void getMBSTRCDDataFromGlobalCache(String MbstrcdKey,MbElement[] envTmp)throws MbUserException {		
		String detailException = "";		 
		
		try {
			// Recover Map from Global Cache
			detailException = detailException + "In Getting Global Map...";
			MbGlobalMap GlobalMBCacheMap = MbGlobalMap.getGlobalMap("GlobalMBCacheMap");
			

			if (GlobalMBCacheMap.containsKey("MBSTRCD") == true) {
				Map<String, MBSTRCD> MBSTRCDMapOut = new HashMap<String, MBSTRCD>();
				MBSTRCDMapOut = new HashMap<String, MBSTRCD>();
				detailException = detailException+ "In Getting Table from Global Map...";
				byte[] MBSTRCDbyteIn = (byte[]) GlobalMBCacheMap.get("MBSTRCD");
				detailException = detailException + "Deserializing..."+ "MBSTRCD";
				MBSTRCDMapOut = (Map<String, MBSTRCD>) GenericMethods.deserialize(MBSTRCDbyteIn);
				MbElement eleMb=null;
				
				if(MbstrcdKey==null || MbstrcdKey.equals("")){
					Set<Map.Entry<String, MBSTRCD>> MBSTRCDMapSet = MBSTRCDMapOut.entrySet();  
					Iterator<Map.Entry<String, MBSTRCD>> iter = MBSTRCDMapSet.iterator();
					
					while(iter.hasNext())  
					{  
						MBSTRCD mbtrcdData = iter.next().getValue(); 					
						eleMb=envTmp[0].createElementAsLastChild(MbElement.TYPE_NAME,"QueryResult",null);
						eleMb.createElementAsLastChild(MbElement.TYPE_NAME,"CANVAL",mbtrcdData.CANVAL);
						eleMb.createElementAsLastChild(MbElement.TYPE_NAME,"ATGMIGRATED",mbtrcdData.ATGMIGRATED);
						eleMb.createElementAsLastChild(MbElement.TYPE_NAME,"CHARSTORECODE",mbtrcdData.CHARSTORECODE);				
						eleMb.createElementAsLastChild(MbElement.TYPE_NAME,"FULLSTORECODE",mbtrcdData.FULLSTORECODE);
						eleMb.createElementAsLastChild(MbElement.TYPE_NAME,"GENERICSTORECODE",mbtrcdData.GENERICSTORECODE);
						eleMb.createElementAsLastChild(MbElement.TYPE_NAME,"GLREGION",mbtrcdData.GLREGION);
						eleMb.createElementAsLastChild(MbElement.TYPE_NAME,"GLREGIONCOLUMN",mbtrcdData.GLREGIONCOLUMN);
						eleMb.createElementAsLastChild(MbElement.TYPE_NAME,"ICCSTORE",mbtrcdData.ICCSTORE);
						eleMb.createElementAsLastChild(MbElement.TYPE_NAME,"MCSTORE",mbtrcdData.MCSTORE);
						eleMb.createElementAsLastChild(MbElement.TYPE_NAME,"NONMFSTORE",mbtrcdData.NONMFSTORE);
						eleMb.createElementAsLastChild(MbElement.TYPE_NAME,"NONWMSRCC",mbtrcdData.NONWMSRCC);
						eleMb.createElementAsLastChild(MbElement.TYPE_NAME,"NUMSTORECODE",mbtrcdData.NUMSTORECODE);
						eleMb.createElementAsLastChild(MbElement.TYPE_NAME,"OLASOURCEID",mbtrcdData.OLASOURCEID);
						eleMb.createElementAsLastChild(MbElement.TYPE_NAME,"PRICEFORSTORECODE",mbtrcdData.PRICEFORSTORECODE);
						eleMb.createElementAsLastChild(MbElement.TYPE_NAME,"SAPCOSTORE",mbtrcdData.SAPCOSTORE);
						eleMb.createElementAsLastChild(MbElement.TYPE_NAME,"STOREDIVISION",mbtrcdData.STOREDIVISION);
						eleMb.createElementAsLastChild(MbElement.TYPE_NAME,"STORENAME",mbtrcdData.STORENAME);
						eleMb.createElementAsLastChild(MbElement.TYPE_NAME,"STOREOPENDATETIME",mbtrcdData.STOREOPENDATETIME);
						eleMb.createElementAsLastChild(MbElement.TYPE_NAME,"STORETYPE",mbtrcdData.STORETYPE);
						eleMb.createElementAsLastChild(MbElement.TYPE_NAME,"TESTORE",mbtrcdData.TESTORE);
						eleMb.createElementAsLastChild(MbElement.TYPE_NAME,"TRADECOUNTERFLAG",mbtrcdData.TRADECOUNTERFLAG);
						eleMb.createElementAsLastChild(MbElement.TYPE_NAME,"VPCTRADINGREGION",mbtrcdData.VPCTRADINGREGION);
					}
					
				}
				else{
					MBSTRCD mbsTrcd = MBSTRCDMapOut.get(MbstrcdKey);
					
					eleMb=envTmp[0].createElementAsFirstChild(MbElement.TYPE_NAME,"QueryResult",null);
					eleMb.createElementAsLastChild(MbElement.TYPE_NAME,"CANVAL",mbsTrcd.CANVAL);
					eleMb.createElementAsLastChild(MbElement.TYPE_NAME,"ATGMIGRATED",mbsTrcd.ATGMIGRATED);
					eleMb.createElementAsLastChild(MbElement.TYPE_NAME,"CHARSTORECODE",mbsTrcd.CHARSTORECODE);				
					eleMb.createElementAsLastChild(MbElement.TYPE_NAME,"FULLSTORECODE",mbsTrcd.FULLSTORECODE);
					eleMb.createElementAsLastChild(MbElement.TYPE_NAME,"GENERICSTORECODE",mbsTrcd.GENERICSTORECODE);
					eleMb.createElementAsLastChild(MbElement.TYPE_NAME,"GLREGION",mbsTrcd.GLREGION);
					eleMb.createElementAsLastChild(MbElement.TYPE_NAME,"GLREGIONCOLUMN",mbsTrcd.GLREGIONCOLUMN);
					eleMb.createElementAsLastChild(MbElement.TYPE_NAME,"ICCSTORE",mbsTrcd.ICCSTORE);
					eleMb.createElementAsLastChild(MbElement.TYPE_NAME,"MCSTORE",mbsTrcd.MCSTORE);
					eleMb.createElementAsLastChild(MbElement.TYPE_NAME,"NONMFSTORE",mbsTrcd.NONMFSTORE);
					eleMb.createElementAsLastChild(MbElement.TYPE_NAME,"NONWMSRCC",mbsTrcd.NONWMSRCC);
					eleMb.createElementAsLastChild(MbElement.TYPE_NAME,"NUMSTORECODE",mbsTrcd.NUMSTORECODE);
					eleMb.createElementAsLastChild(MbElement.TYPE_NAME,"OLASOURCEID",mbsTrcd.OLASOURCEID);
					eleMb.createElementAsLastChild(MbElement.TYPE_NAME,"PRICEFORSTORECODE",mbsTrcd.PRICEFORSTORECODE);
					eleMb.createElementAsLastChild(MbElement.TYPE_NAME,"SAPCOSTORE",mbsTrcd.SAPCOSTORE);
					eleMb.createElementAsLastChild(MbElement.TYPE_NAME,"STOREDIVISION",mbsTrcd.STOREDIVISION);
					eleMb.createElementAsLastChild(MbElement.TYPE_NAME,"STORENAME",mbsTrcd.STORENAME);
					eleMb.createElementAsLastChild(MbElement.TYPE_NAME,"STOREOPENDATETIME",mbsTrcd.STOREOPENDATETIME);
					eleMb.createElementAsLastChild(MbElement.TYPE_NAME,"STORETYPE",mbsTrcd.STORETYPE);
					eleMb.createElementAsLastChild(MbElement.TYPE_NAME,"TESTORE",mbsTrcd.TESTORE);
					eleMb.createElementAsLastChild(MbElement.TYPE_NAME,"TRADECOUNTERFLAG",mbsTrcd.TRADECOUNTERFLAG);
					eleMb.createElementAsLastChild(MbElement.TYPE_NAME,"VPCTRADINGREGION",mbsTrcd.VPCTRADINGREGION);
				}
				
								
			}

		} catch (Exception e) {
			throw new MbUserException("Exception:-", detailException, ":-",":", e.toString(), null);
		}
		
	}	

}
