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


public class VATDATA implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public String VERIFONEVATCODE  =null;
	public String EPOSVATCODE      =null;
	public String GENERICVATCODE   =null;
	public String IBSDEALSVATCODE  =null;
	public String RJCOUNTRYCODE    =null;
	public String RJVATCATEGORY    =null;
	public String SAPCOUNTRYCODE   =null;
	public String SAPVATCODE       =null;
	public String SAPARTCOUNTRYCODE=null;
	public String SAPARTVATCODE    =null;
	public String SAPMDVATCODE     =null;
	public String SAPMDVATCATEGORY =null;
	public String SOLVEVATCODE     =null;
	public String TRFVATCODE       =null;
	
	public static String   sVERIFONEVATCODE  ="VERIFONEVATCODE"   ;
	public static String   sEPOSVATCODE      ="EPOSVATCODE"       ;
	public static String   sGENERICVATCODE   ="GENERICVATCODE"    ;
	public static String   sIBSDEALSVATCODE  ="IBSDEALSVATCODE"   ;
	public static String   sRJCOUNTRYCODE    ="RJCOUNTRYCODE"     ;
	public static String   sRJVATCATEGORY    ="RJVATCATEGORY"     ;
	public static String   sSAPCOUNTRYCODE   ="SAPCOUNTRYCODE"    ;
	public static String   sSAPVATCODE       ="SAPVATCODE"        ;
	public static String   sSAPARTCOUNTRYCODE="SAPARTCOUNTRYCODE" ;
	public static String   sSAPARTVATCODE    ="SAPARTVATCODE"     ;
	public static String   sSAPMDVATCODE     ="SAPMDVATCODE"      ;
	public static String   sSAPMDVATCATEGORY ="SAPMDVATCATEGORY"  ;
	public static String   sSOLVEVATCODE     ="SOLVEVATCODE"      ;
	public static String   sTRFVATCODE       ="TRFVATCODE"        ;


	
	public VATDATA(String vERIFONEVATCODE, String ePOSVATCODE,
			String gENERICVATCODE, String iBSDEALSVATCODE,
			String rJCOUNTRYCODE, String rJVATCATEGORY, String sAPCOUNTRYCODE,
			String sAPVATCODE, String sAPARTCOUNTRYCODE, String sAPARTVATCODE,
			String sAPMDVATCODE, String sAPMDVATCATEGORY, String sOLVEVATCODE,
			String tRFVATCODE) {
		super();
		VERIFONEVATCODE = vERIFONEVATCODE;
		EPOSVATCODE = ePOSVATCODE;
		GENERICVATCODE = gENERICVATCODE;
		IBSDEALSVATCODE = iBSDEALSVATCODE;
		RJCOUNTRYCODE = rJCOUNTRYCODE;
		RJVATCATEGORY = rJVATCATEGORY;
		SAPCOUNTRYCODE = sAPCOUNTRYCODE;
		SAPVATCODE = sAPVATCODE;
		SAPARTCOUNTRYCODE = sAPARTCOUNTRYCODE;
		SAPARTVATCODE = sAPARTVATCODE;
		SAPMDVATCODE = sAPMDVATCODE;
		SAPMDVATCATEGORY = sAPMDVATCATEGORY;
		SOLVEVATCODE = sOLVEVATCODE;
		TRFVATCODE = tRFVATCODE;
	}
	public String getVERIFONEVATCODE() {
		return VERIFONEVATCODE;
	}
	public void setVERIFONEVATCODE(String vERIFONEVATCODE) {
		VERIFONEVATCODE = vERIFONEVATCODE;
	}
	public String getEPOSVATCODE() {
		return EPOSVATCODE;
	}
	public void setEPOSVATCODE(String ePOSVATCODE) {
		EPOSVATCODE = ePOSVATCODE;
	}
	public String getGENERICVATCODE() {
		return GENERICVATCODE;
	}
	public void setGENERICVATCODE(String gENERICVATCODE) {
		GENERICVATCODE = gENERICVATCODE;
	}
	public String getIBSDEALSVATCODE() {
		return IBSDEALSVATCODE;
	}
	public void setIBSDEALSVATCODE(String iBSDEALSVATCODE) {
		IBSDEALSVATCODE = iBSDEALSVATCODE;
	}
	public String getRJCOUNTRYCODE() {
		return RJCOUNTRYCODE;
	}
	public void setRJCOUNTRYCODE(String rJCOUNTRYCODE) {
		RJCOUNTRYCODE = rJCOUNTRYCODE;
	}
	public String getRJVATCATEGORY() {
		return RJVATCATEGORY;
	}
	public void setRJVATCATEGORY(String rJVATCATEGORY) {
		RJVATCATEGORY = rJVATCATEGORY;
	}
	public String getSAPCOUNTRYCODE() {
		return SAPCOUNTRYCODE;
	}
	public void setSAPCOUNTRYCODE(String sAPCOUNTRYCODE) {
		SAPCOUNTRYCODE = sAPCOUNTRYCODE;
	}
	public String getSAPVATCODE() {
		return SAPVATCODE;
	}
	public void setSAPVATCODE(String sAPVATCODE) {
		SAPVATCODE = sAPVATCODE;
	}
	public String getSAPARTCOUNTRYCODE() {
		return SAPARTCOUNTRYCODE;
	}
	public void setSAPARTCOUNTRYCODE(String sAPARTCOUNTRYCODE) {
		SAPARTCOUNTRYCODE = sAPARTCOUNTRYCODE;
	}
	public String getSAPARTVATCODE() {
		return SAPARTVATCODE;
	}
	public void setSAPARTVATCODE(String sAPARTVATCODE) {
		SAPARTVATCODE = sAPARTVATCODE;
	}
	public String getSAPMDVATCODE() {
		return SAPMDVATCODE;
	}
	public void setSAPMDVATCODE(String sAPMDVATCODE) {
		SAPMDVATCODE = sAPMDVATCODE;
	}
	public String getSAPMDVATCATEGORY() {
		return SAPMDVATCATEGORY;
	}
	public void setSAPMDVATCATEGORY(String sAPMDVATCATEGORY) {
		SAPMDVATCATEGORY = sAPMDVATCATEGORY;
	}
	public String getSOLVEVATCODE() {
		return SOLVEVATCODE;
	}
	public void setSOLVEVATCODE(String sOLVEVATCODE) {
		SOLVEVATCODE = sOLVEVATCODE;
	}
	public String getTRFVATCODE() {
		return TRFVATCODE;
	}
	public void setTRFVATCODE(String tRFVATCODE) {
		TRFVATCODE = tRFVATCODE;
	}
	
	
	

}
