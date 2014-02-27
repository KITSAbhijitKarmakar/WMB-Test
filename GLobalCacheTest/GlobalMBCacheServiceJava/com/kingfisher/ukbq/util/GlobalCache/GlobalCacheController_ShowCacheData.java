package com.kingfisher.ukbq.util.GlobalCache;
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
import com.ibm.broker.javacompute.MbJavaComputeNode;
import com.ibm.broker.plugin.MbElement;
import com.ibm.broker.plugin.MbException;
import com.ibm.broker.plugin.MbMessage;
import com.ibm.broker.plugin.MbMessageAssembly;
import com.ibm.broker.plugin.MbOutputTerminal;
import com.ibm.broker.plugin.MbUserException;
import com.kingfisher.ukbq.util.*;

public class GlobalCacheController_ShowCacheData extends MbJavaComputeNode {

	public void evaluate(MbMessageAssembly inAssembly) throws MbException {
		MbOutputTerminal out = getOutputTerminal("out");
		//MbOutputTerminal alt = getOutputTerminal("alternate");

		MbMessage inMessage = inAssembly.getMessage();
		MbMessageAssembly outAssembly = null;		
		
		MbElement[] refEle=new MbElement[]{inAssembly.getGlobalEnvironment().getRootElement().createElementAsLastChild(MbElement.TYPE_NAME_VALUE, "DISCOUNTTYPE", null)};
		DISCOUNTTYPEAccesser.getsALL(null,refEle);
		
		refEle=new MbElement[]{inAssembly.getGlobalEnvironment().getRootElement().createElementAsLastChild(MbElement.TYPE_NAME_VALUE, "MBCONFIG",null)};
		MBCONFIGAccesser.getsALL(null,refEle);
		
		refEle=new MbElement[]{inAssembly.getGlobalEnvironment().getRootElement().createElementAsLastChild(MbElement.TYPE_NAME_VALUE,"MBSTRCD",null)};
		MBSTRCDAccesser.getsALL(null,refEle);
		
		refEle=new MbElement[]{inAssembly.getGlobalEnvironment().getRootElement().createElementAsLastChild(MbElement.TYPE_NAME_VALUE, "MBTOPICS",null)};
		MBTOPICSAccesser.getsALL(null,refEle);
		
		refEle=new MbElement[]{inAssembly.getGlobalEnvironment().getRootElement().createElementAsLastChild(MbElement.TYPE_NAME_VALUE, "PRICEMODIFIER",null)};
		PRICEMODIFIERAccesser.getsALL(null,refEle);
		
		refEle=new MbElement[]{inAssembly.getGlobalEnvironment().getRootElement().createElementAsLastChild(MbElement.TYPE_NAME_VALUE, "TENDERDATA",null)};
		TENDERDATAAccesser.getsALL(null,refEle);
		
		refEle=new MbElement[]{inAssembly.getGlobalEnvironment().getRootElement().createElementAsLastChild(MbElement.TYPE_NAME_VALUE, "VATDATA",null)};
		VATDATAAccesser.getsALL(null,refEle);
		
		try {
			// create new message as a copy of the input
			MbMessage outMessage = new MbMessage(inMessage);
			outAssembly = new MbMessageAssembly(inAssembly, outMessage);
			// ----------------------------------------------------------
			// Add user code below

			// End of user code
			// ----------------------------------------------------------
		} catch (MbException e) {
			// Re-throw to allow Broker handling of MbException
			throw e;
		} catch (RuntimeException e) {
			// Re-throw to allow Broker handling of RuntimeException
			throw e;
		} catch (Exception e) {
			// Consider replacing Exception with type(s) thrown by user code
			// Example handling ensures all exceptions are re-thrown to be handled in the flow
			throw new MbUserException(this, "evaluate()", "", "", e.toString(),
					null);
		}
		// The following should only be changed
		// if not propagating message to the 'out' terminal
		out.propagate(outAssembly);

	}

}
