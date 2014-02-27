package com.kingfisher.ukbq.Ledger;

/**********************************************************************************************************************
* COPYRIGHT. <KINGFISHER INFORMATION TECHNOLOGY SERVICES 2012> ALL RIGHTS RESERVED. NO PART OF THIS 				  *
* SOURCE CODE MAY BE REPRODUCED, STORED IN A RETRIEVAL SYSTEM, OR TRANSMITTED,IN ANY								  *
* FORM BY ANY MEANS, ELECTRONIC, MECHANICAL, PHOTOCOPYING, RECORDING OR OTHERWISE,									  *
* WITHOUT THE PRIOR WRITTEN PERMISSION OF <KINGFISHER INFORMATION TECHNOLOGY SERVICES>.             				  *                           	
**********************************************************************************************************************/

/**********************************************************************************************************************
* Node Name      			:	GetDecryptedAccountId									  	  			  	      	  *									
* Interface Id 	 			:																		  		   		  *
* Interface Name 			:	Ledger_LoadLedger                                                              		  *															
* Message Flow 	 			: 	RegisterToken															              *
* Message Flow Description 	: 	The primary function of this message flow is to create RegisterToken				  *  						
* 							    request structure to hit CommIdea to exchange decrypted AccountId					  *
* 							    with Token.																			  *
* Module Name  	 			:	LoadLedger_RegisterToken_JavaCompute										  	      *            				  	  
* Module Description  		:	Gets the decrypted AccountId from the encrypted AccountId							  *                                         
*																									  				  *
* Version   Date	   		Author				Description                     				 				      *
* ======= 	=========== 	=========== 		========================================				  			  *
*  0.1   	22-MAY-2013 	Ashwin Pai 			The initial version. 		  		  				                  *
**********************************************************************************************************************/

import com.ibm.broker.javacompute.MbJavaComputeNode;
import java.sql.Connection;
import org.apache.commons.codec.binary.Base64;
import com.ibm.broker.plugin.MbElement;
import com.ibm.broker.plugin.MbException;
import com.ibm.broker.plugin.MbMessage;
import com.ibm.broker.plugin.MbMessageAssembly;
import com.ibm.broker.plugin.MbOutputTerminal;
import com.ibm.broker.plugin.MbUserException;
import com.kingfisher.kits.mb.crypto.wics.WICSEncryption;

public class LoadLedger_RegisterToken_JavaCompute extends MbJavaComputeNode {
	
	public void evaluate(MbMessageAssembly inAssembly) throws MbException {
		
		MbOutputTerminal out 					= getOutputTerminal("out");
		@SuppressWarnings("unused")
		MbOutputTerminal alt 					= getOutputTerminal("alternate");
		MbMessage inMessage 					= inAssembly.getMessage();

		// create new message
		MbMessage outMessage 					= new MbMessage(inMessage);
		MbMessageAssembly outAssembly 			= new MbMessageAssembly(inAssembly,outMessage);				
			
		//Get the Environment variable
		MbElement environment 					= inAssembly.getGlobalEnvironment().getRootElement();
		
		  
		String decryptedCardNo 					= "";
		String encryptedCrd 					= "";
		String storeCode						= "";
		String bdd								= "";
		String ivDataDecrypted 					= "";	
		String transactionID					= "";
		String tenderID							= "";
		String stage							= "Init";
		String allException						="Exception in : ";
		
		//Create database connections
		Connection ODSConnection 				= null;
		Connection MBREPOSConnection 			= null;		
		
		ODSConnection 							= getJDBCType4Connection("MBODS",JDBC_TransactionType.MB_TRANSACTION_AUTO); 
		stage	= "After ODS Connection";
		
		MBREPOSConnection 						= getJDBCType4Connection("MBREPOS",JDBC_TransactionType.MB_TRANSACTION_AUTO); 
		
		stage	= "After MBREPOS Connection";
			
		
		bdd 									= environment.getFirstElementByPath("Variables/EncryptDecrypt/BusinessDayID").getValueAsString();
		transactionID							= environment.getFirstElementByPath("Variables/TransactionID").getValueAsString();
		storeCode								= environment.getFirstElementByPath("Variables/EncryptDecrypt/StoreCode").getValueAsString();
		tenderID								= environment.getFirstElementByPath("Variables/EncryptDecrypt/Type").getValueAsString();
		ivDataDecrypted							= environment.getFirstElementByPath("Variables/EncryptDecrypt/IVData").getValueAsString();
		encryptedCrd							= environment.getFirstElementByPath("Variables/EncryptDecrypt/EncryptedNumber").getValueAsString();
				
	    try 
	       {
	          byte [] encCardByteArray 		= new Base64().decode(encryptedCrd.getBytes());
	          byte [] ivDatabyteArray 		= new Base64().decode(ivDataDecrypted.getBytes());

	                //Get the Decrypted Card Number and pass it as Pan
	          stage = "decryptData Method";
	          decryptedCardNo 				= new WICSEncryption().decryptData(encCardByteArray, storeCode, bdd, ODSConnection, MBREPOSConnection, ivDatabyteArray);
	          environment.createElementAsFirstChild(MbElement.TYPE_NAME_VALUE,"PAN",decryptedCardNo);

	      } catch (Exception e) {
	          e.printStackTrace();
	          
	          allException=allException+": "+ stage;
	          allException = allException+e.toString();
	          throw new MbUserException(this,"REGISTER TOKEN",stage,"TRANSACTION ID : "+transactionID+" and TENDER ID : "+tenderID,allException,null);
	     }
		out.propagate(outAssembly);
	}	
}
