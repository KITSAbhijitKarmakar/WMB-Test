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

import java.io.Serializable;


public class MBSTRCD implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public MBSTRCD(String cANVAL, String nUMSTORECODE, String cHARSTORECODE,
			String fULLSTORECODE, String sTOREDIVISION, String nONMFSTORE,
			String gLREGIONCOLUMN, String sTORENAME, String mCSTORE,
			String tRADECOUNTERFLAG, String vPCTRADINGREGION,
			String sTOREOPENDATETIME, String tESTORE, String sAPCOSTORE,
			String iCCSTORE, String sTORETYPE, String gLREGION,
			String oLASOURCEID, String nONWMSRCC, String aTGMIGRATED,
			String gENERICSTORECODE, String pRICEFORSTORECODE) {
		super();
		CANVAL = cANVAL;
		NUMSTORECODE = nUMSTORECODE;
		CHARSTORECODE = cHARSTORECODE;
		FULLSTORECODE = fULLSTORECODE;
		STOREDIVISION = sTOREDIVISION;
		NONMFSTORE = nONMFSTORE;
		GLREGIONCOLUMN = gLREGIONCOLUMN;
		STORENAME = sTORENAME;
		MCSTORE = mCSTORE;
		TRADECOUNTERFLAG = tRADECOUNTERFLAG;
		VPCTRADINGREGION = vPCTRADINGREGION;
		STOREOPENDATETIME = sTOREOPENDATETIME;
		TESTORE = tESTORE;
		SAPCOSTORE = sAPCOSTORE;
		ICCSTORE = iCCSTORE;
		STORETYPE = sTORETYPE;
		GLREGION = gLREGION;
		OLASOURCEID = oLASOURCEID;
		NONWMSRCC = nONWMSRCC;
		ATGMIGRATED = aTGMIGRATED;
		GENERICSTORECODE = gENERICSTORECODE;
		PRICEFORSTORECODE = pRICEFORSTORECODE;
	}
	public String getCANVAL() {
		return CANVAL;
	}
	public void setCANVAL(String cANVAL) {
		CANVAL = cANVAL;
	}
	public String getNUMSTORECODE() {
		return NUMSTORECODE;
	}
	public void setNUMSTORECODE(String nUMSTORECODE) {
		NUMSTORECODE = nUMSTORECODE;
	}
	public String getCHARSTORECODE() {
		return CHARSTORECODE;
	}
	public void setCHARSTORECODE(String cHARSTORECODE) {
		CHARSTORECODE = cHARSTORECODE;
	}
	public String getFULLSTORECODE() {
		return FULLSTORECODE;
	}
	public void setFULLSTORECODE(String fULLSTORECODE) {
		FULLSTORECODE = fULLSTORECODE;
	}
	public String getSTOREDIVISION() {
		return STOREDIVISION;
	}
	public void setSTOREDIVISION(String sTOREDIVISION) {
		STOREDIVISION = sTOREDIVISION;
	}
	public String getNONMFSTORE() {
		return NONMFSTORE;
	}
	public void setNONMFSTORE(String nONMFSTORE) {
		NONMFSTORE = nONMFSTORE;
	}
	public String getGLREGIONCOLUMN() {
		return GLREGIONCOLUMN;
	}
	public void setGLREGIONCOLUMN(String gLREGIONCOLUMN) {
		GLREGIONCOLUMN = gLREGIONCOLUMN;
	}
	public String getSTORENAME() {
		return STORENAME;
	}
	public void setSTORENAME(String sTORENAME) {
		STORENAME = sTORENAME;
	}
	public String getMCSTORE() {
		return MCSTORE;
	}
	public void setMCSTORE(String mCSTORE) {
		MCSTORE = mCSTORE;
	}
	public String getTRADECOUNTERFLAG() {
		return TRADECOUNTERFLAG;
	}
	public void setTRADECOUNTERFLAG(String tRADECOUNTERFLAG) {
		TRADECOUNTERFLAG = tRADECOUNTERFLAG;
	}
	public String getVPCTRADINGREGION() {
		return VPCTRADINGREGION;
	}
	public void setVPCTRADINGREGION(String vPCTRADINGREGION) {
		VPCTRADINGREGION = vPCTRADINGREGION;
	}
	public String getSTOREOPENDATETIME() {
		return STOREOPENDATETIME;
	}
	public void setSTOREOPENDATETIME(String sTOREOPENDATETIME) {
		STOREOPENDATETIME = sTOREOPENDATETIME;
	}
	public String getTESTORE() {
		return TESTORE;
	}
	public void setTESTORE(String tESTORE) {
		TESTORE = tESTORE;
	}
	public String getSAPCOSTORE() {
		return SAPCOSTORE;
	}
	public void setSAPCOSTORE(String sAPCOSTORE) {
		SAPCOSTORE = sAPCOSTORE;
	}
	public String getICCSTORE() {
		return ICCSTORE;
	}
	public void setICCSTORE(String iCCSTORE) {
		ICCSTORE = iCCSTORE;
	}
	public String getSTORETYPE() {
		return STORETYPE;
	}
	public void setSTORETYPE(String sTORETYPE) {
		STORETYPE = sTORETYPE;
	}
	public String getGLREGION() {
		return GLREGION;
	}
	public void setGLREGION(String gLREGION) {
		GLREGION = gLREGION;
	}
	public String getOLASOURCEID() {
		return OLASOURCEID;
	}
	public void setOLASOURCEID(String oLASOURCEID) {
		OLASOURCEID = oLASOURCEID;
	}
	public String getNONWMSRCC() {
		return NONWMSRCC;
	}
	public void setNONWMSRCC(String nONWMSRCC) {
		NONWMSRCC = nONWMSRCC;
	}
	public String getATGMIGRATED() {
		return ATGMIGRATED;
	}
	public void setATGMIGRATED(String aTGMIGRATED) {
		ATGMIGRATED = aTGMIGRATED;
	}
	public String getGENERICSTORECODE() {
		return GENERICSTORECODE;
	}
	public void setGENERICSTORECODE(String gENERICSTORECODE) {
		GENERICSTORECODE = gENERICSTORECODE;
	}
	public String getPRICEFORSTORECODE() {
		return PRICEFORSTORECODE;
	}
	public void setPRICEFORSTORECODE(String pRICEFORSTORECODE) {
		PRICEFORSTORECODE = pRICEFORSTORECODE;
	}
	
	public String CANVAL	=null;
	public String NUMSTORECODE	=null;
	public String CHARSTORECODE	=null;
	public String FULLSTORECODE	=null;
	public String STOREDIVISION	=null;
	public String NONMFSTORE	=null;
	public String GLREGIONCOLUMN	=null;
	public String STORENAME	=null;
	public String MCSTORE	=null;
	public String TRADECOUNTERFLAG	=null;
	public String VPCTRADINGREGION	=null;
	public String STOREOPENDATETIME	=null;
	public String TESTORE	=null;
	public String SAPCOSTORE	=null;
	public String ICCSTORE	=null;
	public String STORETYPE	=null;
	public String GLREGION	=null;
	public String OLASOURCEID	=null;
	public String NONWMSRCC	=null;
	public String ATGMIGRATED	=null;
	public String GENERICSTORECODE	=null;
	public String PRICEFORSTORECODE	=null;
	
	public static String sCANVAL	="CANVAL";
	public static String sNUMSTORECODE	="NUMSTORECODE";
	public static String sCHARSTORECODE	="CHARSTORECODE";
	public static String sFULLSTORECODE	="FULLSTORECODE";
	public static String sSTOREDIVISION	="STOREDIVISION";
	public static String sNONMFSTORE	="NONMFSTORE";
	public static String sGLREGIONCOLUMN	="GLREGIONCOLUMN";
	public static String sSTORENAME	="STORENAME";
	public static String sMCSTORE	="MCSTORE";
	public static String sTRADECOUNTERFLAG	="TRADECOUNTERFLAG";
	public static String sVPCTRADINGREGION	="VPCTRADINGREGION";
	public static String sSTOREOPENDATETIME	="STOREOPENDATETIME";
	public static String sTESTORE	="TESTORE";
	public static String sSAPCOSTORE	="SAPCOSTORE";
	public static String sICCSTORE	="ICCSTORE";
	public static String sSTORETYPE	="STORETYPE";
	public static String sGLREGION	="GLREGION";
	public static String sOLASOURCEID	="OLASOURCEID";
	public static String sNONWMSRCC	="NONWMSRCC";
	public static String sATGMIGRATED	="ATGMIGRATED";
	public static String sGENERICSTORECODE	="GENERICSTORECODE";
	public static String sPRICEFORSTORECODE	="PRICEFORSTORECODE";
	

}
