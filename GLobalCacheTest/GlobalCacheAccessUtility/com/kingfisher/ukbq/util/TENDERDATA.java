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

public class TENDERDATA implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public String RETAILJCARDTYPE=null;
	public String PEDTENDERID=null;
	public String VERIFONETENDERID=null;
	public String SAPCARDTYPE=null;
	public String TENDERID=null;
	public String CARDSCHEMEPROVIDER=null;
	public String BANKINGDESCRIPTION=null;
	public String TENDERTYPE=null;
	public String ACQUIRERNAME=null;
	public String GLACCOUNT=null;
	public String APPSTENDERDESCRIPTION=null;
	public String ACQUIRERIDNONUKCANDP=null;
	public String PANDORATENDERTYPE=null;
	public String RECONCILIATIONFLAG=null;
	public String GLTENDERTYPE=null;
	public String CCCPTENDERDESCRIPTION=null;
	public String APPSTENDERTYPE=null;
	public String ACQUIRERNAMENONUK=null;
	public String TENDERDESCRIPTION=null;
	public String ACQUIRERIDNONUK=null;
	public String CCCPTENDERTYPE=null;
	public String ACQUIRERIDCANDP=null;
	public String ACCOUNTNUMBER=null;
	public String SETTLEMENTDESCRIPTION=null;
	public String FINANCECODE=null;
	public String HEXFORMAT=null;
	public String ACQUIRERID=null;
	
	public TENDERDATA(String rETAILJCARDTYPE, String pEDTENDERID,
			String vERIFONETENDERID, String sAPCARDTYPE, String tENDERID,
			String cARDSCHEMEPROVIDER, String bANKINGDESCRIPTION,
			String tENDERTYPE, String aCQUIRERNAME, String gLACCOUNT,
			String aPPSTENDERDESCRIPTION, String aCQUIRERIDNONUKCANDP,
			String pANDORATENDERTYPE, String rECONCILIATIONFLAG,
			String gLTENDERTYPE, String cCCPTENDERDESCRIPTION,
			String aPPSTENDERTYPE, String aCQUIRERNAMENONUK,
			String tENDERDESCRIPTION, String aCQUIRERIDNONUK,
			String cCCPTENDERTYPE, String aCQUIRERIDCANDP,
			String aCCOUNTNUMBER, String sETTLEMENTDESCRIPTION,
			String fINANCECODE, String hEXFORMAT, String aCQUIRERID) {
		super();
		RETAILJCARDTYPE = rETAILJCARDTYPE;
		PEDTENDERID = pEDTENDERID;
		VERIFONETENDERID = vERIFONETENDERID;
		SAPCARDTYPE = sAPCARDTYPE;
		TENDERID = tENDERID;
		CARDSCHEMEPROVIDER = cARDSCHEMEPROVIDER;
		BANKINGDESCRIPTION = bANKINGDESCRIPTION;
		TENDERTYPE = tENDERTYPE;
		ACQUIRERNAME = aCQUIRERNAME;
		GLACCOUNT = gLACCOUNT;
		APPSTENDERDESCRIPTION = aPPSTENDERDESCRIPTION;
		ACQUIRERIDNONUKCANDP = aCQUIRERIDNONUKCANDP;
		PANDORATENDERTYPE = pANDORATENDERTYPE;
		RECONCILIATIONFLAG = rECONCILIATIONFLAG;
		GLTENDERTYPE = gLTENDERTYPE;
		CCCPTENDERDESCRIPTION = cCCPTENDERDESCRIPTION;
		APPSTENDERTYPE = aPPSTENDERTYPE;
		ACQUIRERNAMENONUK = aCQUIRERNAMENONUK;
		TENDERDESCRIPTION = tENDERDESCRIPTION;
		ACQUIRERIDNONUK = aCQUIRERIDNONUK;
		CCCPTENDERTYPE = cCCPTENDERTYPE;
		ACQUIRERIDCANDP = aCQUIRERIDCANDP;
		ACCOUNTNUMBER = aCCOUNTNUMBER;
		SETTLEMENTDESCRIPTION = sETTLEMENTDESCRIPTION;
		FINANCECODE = fINANCECODE;
		HEXFORMAT = hEXFORMAT;
		ACQUIRERID = aCQUIRERID;
	}
	
	public String getRETAILJCARDTYPE() {
		return RETAILJCARDTYPE;
	}
	public void setRETAILJCARDTYPE(String rETAILJCARDTYPE) {
		RETAILJCARDTYPE = rETAILJCARDTYPE;
	}
	public String getPEDTENDERID() {
		return PEDTENDERID;
	}
	public void setPEDTENDERID(String pEDTENDERID) {
		PEDTENDERID = pEDTENDERID;
	}
	public String getVERIFONETENDERID() {
		return VERIFONETENDERID;
	}
	public void setVERIFONETENDERID(String vERIFONETENDERID) {
		VERIFONETENDERID = vERIFONETENDERID;
	}
	public String getSAPCARDTYPE() {
		return SAPCARDTYPE;
	}
	public void setSAPCARDTYPE(String sAPCARDTYPE) {
		SAPCARDTYPE = sAPCARDTYPE;
	}
	public String getTENDERID() {
		return TENDERID;
	}
	public void setTENDERID(String tENDERID) {
		TENDERID = tENDERID;
	}
	public String getCARDSCHEMEPROVIDER() {
		return CARDSCHEMEPROVIDER;
	}
	public void setCARDSCHEMEPROVIDER(String cARDSCHEMEPROVIDER) {
		CARDSCHEMEPROVIDER = cARDSCHEMEPROVIDER;
	}
	public String getBANKINGDESCRIPTION() {
		return BANKINGDESCRIPTION;
	}
	public void setBANKINGDESCRIPTION(String bANKINGDESCRIPTION) {
		BANKINGDESCRIPTION = bANKINGDESCRIPTION;
	}
	public String getTENDERTYPE() {
		return TENDERTYPE;
	}
	public void setTENDERTYPE(String tENDERTYPE) {
		TENDERTYPE = tENDERTYPE;
	}
	public String getACQUIRERNAME() {
		return ACQUIRERNAME;
	}
	public void setACQUIRERNAME(String aCQUIRERNAME) {
		ACQUIRERNAME = aCQUIRERNAME;
	}
	public String getGLACCOUNT() {
		return GLACCOUNT;
	}
	public void setGLACCOUNT(String gLACCOUNT) {
		GLACCOUNT = gLACCOUNT;
	}
	public String getAPPSTENDERDESCRIPTION() {
		return APPSTENDERDESCRIPTION;
	}
	public void setAPPSTENDERDESCRIPTION(String aPPSTENDERDESCRIPTION) {
		APPSTENDERDESCRIPTION = aPPSTENDERDESCRIPTION;
	}
	public String getACQUIRERIDNONUKCANDP() {
		return ACQUIRERIDNONUKCANDP;
	}
	public void setACQUIRERIDNONUKCANDP(String aCQUIRERIDNONUKCANDP) {
		ACQUIRERIDNONUKCANDP = aCQUIRERIDNONUKCANDP;
	}
	public String getPANDORATENDERTYPE() {
		return PANDORATENDERTYPE;
	}
	public void setPANDORATENDERTYPE(String pANDORATENDERTYPE) {
		PANDORATENDERTYPE = pANDORATENDERTYPE;
	}
	public String getRECONCILIATIONFLAG() {
		return RECONCILIATIONFLAG;
	}
	public void setRECONCILIATIONFLAG(String rECONCILIATIONFLAG) {
		RECONCILIATIONFLAG = rECONCILIATIONFLAG;
	}
	public String getGLTENDERTYPE() {
		return GLTENDERTYPE;
	}
	public void setGLTENDERTYPE(String gLTENDERTYPE) {
		GLTENDERTYPE = gLTENDERTYPE;
	}
	public String getCCCPTENDERDESCRIPTION() {
		return CCCPTENDERDESCRIPTION;
	}
	public void setCCCPTENDERDESCRIPTION(String cCCPTENDERDESCRIPTION) {
		CCCPTENDERDESCRIPTION = cCCPTENDERDESCRIPTION;
	}
	public String getAPPSTENDERTYPE() {
		return APPSTENDERTYPE;
	}
	public void setAPPSTENDERTYPE(String aPPSTENDERTYPE) {
		APPSTENDERTYPE = aPPSTENDERTYPE;
	}
	public String getACQUIRERNAMENONUK() {
		return ACQUIRERNAMENONUK;
	}
	public void setACQUIRERNAMENONUK(String aCQUIRERNAMENONUK) {
		ACQUIRERNAMENONUK = aCQUIRERNAMENONUK;
	}
	public String getTENDERDESCRIPTION() {
		return TENDERDESCRIPTION;
	}
	public void setTENDERDESCRIPTION(String tENDERDESCRIPTION) {
		TENDERDESCRIPTION = tENDERDESCRIPTION;
	}
	public String getACQUIRERIDNONUK() {
		return ACQUIRERIDNONUK;
	}
	public void setACQUIRERIDNONUK(String aCQUIRERIDNONUK) {
		ACQUIRERIDNONUK = aCQUIRERIDNONUK;
	}
	public String getCCCPTENDERTYPE() {
		return CCCPTENDERTYPE;
	}
	public void setCCCPTENDERTYPE(String cCCPTENDERTYPE) {
		CCCPTENDERTYPE = cCCPTENDERTYPE;
	}
	public String getACQUIRERIDCANDP() {
		return ACQUIRERIDCANDP;
	}
	public void setACQUIRERIDCANDP(String aCQUIRERIDCANDP) {
		ACQUIRERIDCANDP = aCQUIRERIDCANDP;
	}
	public String getACCOUNTNUMBER() {
		return ACCOUNTNUMBER;
	}
	public void setACCOUNTNUMBER(String aCCOUNTNUMBER) {
		ACCOUNTNUMBER = aCCOUNTNUMBER;
	}
	public String getSETTLEMENTDESCRIPTION() {
		return SETTLEMENTDESCRIPTION;
	}
	public void setSETTLEMENTDESCRIPTION(String sETTLEMENTDESCRIPTION) {
		SETTLEMENTDESCRIPTION = sETTLEMENTDESCRIPTION;
	}
	public String getFINANCECODE() {
		return FINANCECODE;
	}
	public void setFINANCECODE(String fINANCECODE) {
		FINANCECODE = fINANCECODE;
	}
	public String getHEXFORMAT() {
		return HEXFORMAT;
	}
	public void setHEXFORMAT(String hEXFORMAT) {
		HEXFORMAT = hEXFORMAT;
	}
	public String getACQUIRERID() {
		return ACQUIRERID;
	}
	public void setACQUIRERID(String aCQUIRERID) {
		ACQUIRERID = aCQUIRERID;
	}
	
	
	public static String sRETAILJCARDTYPE="RETAILJCARDTYPE";
	public static String sPEDTENDERID="PEDTENDERID";
	public static String sVERIFONETENDERID="VERIFONETENDERID";
	public static String sSAPCARDTYPE="SAPCARDTYPE";
	public static String sTENDERID="TENDERID";
	public static String sCARDSCHEMEPROVIDER="CARDSCHEMEPROVIDER";
	public static String sBANKINGDESCRIPTION="BANKINGDESCRIPTION";
	public static String sTENDERTYPE="TENDERTYPE";
	public static String sACQUIRERNAME="ACQUIRERNAME";
	public static String sGLACCOUNT="GLACCOUNT";
	public static String sAPPSTENDERDESCRIPTION="APPSTENDERDESCRIPTION";
	public static String sACQUIRERIDNONUKCANDP="ACQUIRERIDNONUKCANDP";
	public static String sPANDORATENDERTYPE="PANDORATENDERTYPE";
	public static String sRECONCILIATIONFLAG="RECONCILIATIONFLAG";
	public static String sGLTENDERTYPE="GLTENDERTYPE";
	public static String sCCCPTENDERDESCRIPTION="CCCPTENDERDESCRIPTION";
	public static String sAPPSTENDERTYPE="APPSTENDERTYPE";
	public static String sACQUIRERNAMENONUK="ACQUIRERNAMENONUK";
	public static String sTENDERDESCRIPTION="TENDERDESCRIPTION";
	public static String sACQUIRERIDNONUK="ACQUIRERIDNONUK";
	public static String sCCCPTENDERTYPE="CCCPTENDERTYPE";
	public static String sACQUIRERIDCANDP="ACQUIRERIDCANDP";
	public static String sACCOUNTNUMBER="ACCOUNTNUMBER";
	public static String sSETTLEMENTDESCRIPTION="SETTLEMENTDESCRIPTION";
	public static String sFINANCECODE="FINANCECODE";
	public static String sHEXFORMAT="HEXFORMAT";
	public static String sACQUIRERID="ACQUIRERID";
	
	
	
	

}
