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

public class MBTOPICS implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static String sCANVAL="CANVAL";
	public static String sTOPICNAME="TOPICNAME";
	public static String sTOPICSTRING="TOPICSTRING";
	public MBTOPICS(String cANVAL, String tOPICNAME, String tOPICSTRING) {
		super();
		CANVAL = cANVAL;
		TOPICNAME = tOPICNAME;
		TOPICSTRING = tOPICSTRING;
	}
	public String CANVAL=null;
	public String TOPICNAME=null;
	public String TOPICSTRING=null;
	
	public void setCANVAL(String cANVAL) {
		CANVAL = cANVAL;
	}
	public String getCANVAL() {
		return CANVAL;
	}
	public void setTOPICNAME(String tOPICNAME) {
		TOPICNAME = tOPICNAME;
	}
	public String getTOPICNAME() {
		return TOPICNAME;
	}
	public void setTOPICSTRING(String tOPICSTRING) {
		TOPICSTRING = tOPICSTRING;
	}
	public String getTOPICSTRING() {
		return TOPICSTRING;
	}
	

}
