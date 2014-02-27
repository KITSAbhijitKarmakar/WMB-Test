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


public class DISCOUNTTYPE implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	public String RJTYPE	=null;
	public String SAPTYPE	=null;
	
	public static String sRJTYPE	="RJTYPE";
	public static String sSAPTYPE	="SAPTYPE";
	
	public DISCOUNTTYPE(String rJTYPE, String sAPTYPE) {
		super();
		RJTYPE = rJTYPE;
		SAPTYPE = sAPTYPE;
	}
	public String getRJTYPE() {
		return RJTYPE;
	}
	public void setRJTYPE(String rJTYPE) {
		RJTYPE = rJTYPE;
	}
	public String getSAPTYPE() {
		return SAPTYPE;
	}
	public void setSAPTYPE(String sAPTYPE) {
		SAPTYPE = sAPTYPE;
	}
	
	
	
	

}
