package com.kingfisher.ukbq.SalesOrder;

/**********************************************************************************************************************
* COPYRIGHT. <KINGFISHER INFORMATION TECHNOLOGY SERVICES 2012> ALL RIGHTS RESERVED. NO PART OF THIS 				  *
* SOURCE CODE MAY BE REPRODUCED, STORED IN A RETRIEVAL SYSTEM, OR TRANSMITTED,IN ANY								  *
* FORM BY ANY MEANS, ELECTRONIC, MECHANICAL, PHOTOCOPYING, RECORDING OR OTHERWISE,									  *
* WITHOUT THE PRIOR WRITTEN PERMISSION OF <KINGFISHER INFORMATION TECHNOLOGY SERVICES>.             				  *                           	
**********************************************************************************************************************/

/**********************************************************************************************************************
* Node Name      		:										  	  			  	              		  			  *									
* Interface Id 	 		:																		  		   			  *
* Interface Name 		:	SalesOrder_processSalesOrder                                                              *															
* Message Flow 	 		: 	ProcessSalesOrder_POSBasket_RetailJ										                  *
* Description 	 		: 																							  *
* Module Name  	 		:	Java Compute																			  *            				  	  
* Module Description  	:																							  *                                         
*																									  				  *
* Version   Date	   		Author				Description                     				 				      *
* ======= 	=========== 	=========== 		========================================				  			  *
*  0.1   	18-JUL-2013 	Asif Hossain		The initial version. 		  		  				                  *
*  0.1		16-NOV-2013		Asif Hossain		Code change to handle SLAT encryption/decryption					  *
**********************************************************************************************************************/

import com.ibm.broker.javacompute.MbJavaComputeNode;
import com.ibm.broker.plugin.*;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import com.kingfisher.kits.mb.crypto.wics.WICSEncryption;
import java.sql.Connection;

public class ProcessSalesOrder_POSBasket_RetailJ_PopulateCardDetails_ProductReturn
		extends MbJavaComputeNode {

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
			
		//Create database connections
		Connection ODSConnection 				= null;
		Connection MBREPOSConnection 			= null;		
		
		String hexHashedPAN						= "";
		String decryptedPAN 					= "";
		String hashedPAN						= "";
		String stage							= "Init";
		String token							= null;
		String maskedPAN						= null;
		String cardPAN							= null;
		String base64HashedPAN					= null;
		String encrypted						= null;
		String keyRef							= null;
		String transactionID					= null;
		String allException						= "Exception in Calling method:-";
		try 
		{
		ODSConnection 							= getJDBCType4Connection("MBODS",JDBC_TransactionType.MB_TRANSACTION_AUTO); 
		
		stage="After ODS Connection";
		
		MBREPOSConnection 						= getJDBCType4Connection("MBREPOS",JDBC_TransactionType.MB_TRANSACTION_AUTO); 
		
		stage="After MBREPOS Connection";
		
		transactionID							= environment.getFirstElementByPath("Variables/TransactionID").getValueAsString();;
		
		if (environment.getFirstElementByPath("ProductReturn/CardTender/Token")!=null)
			token								= environment.getFirstElementByPath("ProductReturn/CardTender/Token").getValueAsString();
		
		if (environment.getFirstElementByPath("ProductReturn/CardTender/FormattedPAN")!=null)
			maskedPAN						= environment.getFirstElementByPath("ProductReturn/CardTender/FormattedPAN").getValueAsString();
		else
			maskedPAN						= "";
		
		if (environment.getFirstElementByPath("ProductReturn/CardTender/CardPAN")!=null)
			cardPAN							= environment.getFirstElementByPath("ProductReturn/CardTender/CardPAN").getValueAsString();
		else
			cardPAN							= "";
		
		if (environment.getFirstElementByPath("ProductReturn/CardTender/Hash")!=null)
			base64HashedPAN 					= environment.getFirstElementByPath("ProductReturn/CardTender/Hash").getValueAsString();
		else
			base64HashedPAN						= "";
		
		if (environment.getFirstElementByPath("ProductReturn/CardTender/encrypted")!=null)
			encrypted						= environment.getFirstElementByPath("ProductReturn/CardTender/encrypted").getValueAsString();
		else
			encrypted						= "";	
		
		if (environment.getFirstElementByPath("ProductReturn/CardTender/KeyID")!=null)
			keyRef							= environment.getFirstElementByPath("ProductReturn/CardTender/KeyID").getValueAsString();
		else
			keyRef							= "";
		
		try {
			if(token!=null)
			{					
				if (maskedPAN==""){
					throw new Exception("Received tokenised Original tender without Masked PAN");
				}
				
				if (base64HashedPAN==""){
					throw new Exception("Received tokenised Original tender without base64 Hash");
				}
				
				hexHashedPAN = new String(Hex.encodeHex(Base64.decodeBase64((base64HashedPAN).getBytes())));
				hexHashedPAN = hexHashedPAN.substring(hexHashedPAN.length()-40);
				
				environment.createElementAsFirstChild(MbElement.TYPE_NAME_VALUE,"HashedAccountId",hexHashedPAN.toUpperCase());
				//environment.createElementAsFirstChild(MbElement.TYPE_NAME_VALUE,"Token",token);
				//environment.createElementAsFirstChild(MbElement.TYPE_NAME_VALUE,"MaskedAccountId",maskedPAN);
			}
			else
				if(cardPAN!=null){
					if (encrypted.equalsIgnoreCase("true")){
						stage 				= "PAN is encrypted";
						byte[] ba		 	= new Hex().decode(cardPAN.getBytes());
						decryptedPAN 		= new WICSEncryption().decryptData(ba,"EPOS", keyRef, ODSConnection, MBREPOSConnection, null);
						
						//Code change starts for version 0.2to handle SALT changes
						//hashedPAN			= WICSEncryption.getHashedValue(decryptedPAN);
						hashedPAN			= WICSEncryption.getHashedValue(decryptedPAN,MBREPOSConnection);
						//Code change ends for version 0.2to handle SALT changes
					}
					else{
						stage 				= "PAN is NOT encrypted";
						//Code change starts for version 0.2to handle SALT changes
						//hashedPAN			= WICSEncryption.getHashedValue(cardPAN);
						hashedPAN			= WICSEncryption.getHashedValue(cardPAN,MBREPOSConnection);
						//Code change ends for version 0.2to handle SALT changes
					}
					 environment.createElementAsFirstChild(MbElement.TYPE_NAME_VALUE,"HashedAccountId",hashedPAN);
				}
				else
				{
					throw new Exception("Received Original Tender with no encrypted card info nor token.");
				}
		}
        catch(Exception ex)
        {	
        	allException=allException+":"+ stage;
        	allException=allException+ex.toString();
        	throw new MbUserException(this,stage,"TRANSACTION ID:"+ transactionID,allException,null, null);
        }
		
        if(ODSConnection != null)
			ODSConnection.close();
		if(MBREPOSConnection != null)
			MBREPOSConnection.close();
		out.propagate(outAssembly);
		
		}catch (Exception e) {
			allException=allException+":"+ stage;
			allException=allException+e.toString();
        	throw new MbUserException(this,stage,"TRANSACTION ID:"+ transactionID,allException,null, null);
		}
	}	
}

