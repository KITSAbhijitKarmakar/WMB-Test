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


public class PRICEMODIFIER implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	public String CCCPDESCRIPTION	=null;
	public String RETAILJMODIFIER	=null;
	public String MODIFIERID	=null;
	
	public static String sCCCPDESCRIPTION	="CCCPDESCRIPTION";
	public static String sRETAILJMODIFIER	="RETAILJMODIFIER";
	public static String sMODIFIERID		="MODIFIERID";
	
	public PRICEMODIFIER(String cCCPDESCRIPTION, String rETAILJMODIFIER,
			String mODIFIERID) {
		super();
		CCCPDESCRIPTION = cCCPDESCRIPTION;
		RETAILJMODIFIER = rETAILJMODIFIER;
		MODIFIERID = mODIFIERID;
	}
	public String getCCCPDESCRIPTION() {
		return CCCPDESCRIPTION;
	}
	public void setCCCPDESCRIPTION(String cCCPDESCRIPTION) {
		CCCPDESCRIPTION = cCCPDESCRIPTION;
	}
	public String getRETAILJMODIFIER() {
		return RETAILJMODIFIER;
	}
	public void setRETAILJMODIFIER(String rETAILJMODIFIER) {
		RETAILJMODIFIER = rETAILJMODIFIER;
	}
	public String getMODIFIERID() {
		return MODIFIERID;
	}
	public void setMODIFIERID(String mODIFIERID) {
		MODIFIERID = mODIFIERID;
	}
	

	
	
	

}
