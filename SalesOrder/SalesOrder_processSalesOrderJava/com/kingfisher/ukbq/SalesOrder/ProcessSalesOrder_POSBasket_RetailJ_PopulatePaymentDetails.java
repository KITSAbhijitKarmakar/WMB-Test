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
*  0.1   	19-MAY-2013 	Ashwin Pai 			The initial version. 		  		  				                  *
*  0.2   	16-NOV-2013 	Asif Hossain		Code change for SALT encryption/decryption			                  *
**********************************************************************************************************************/

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import com.ibm.broker.javacompute.MbJavaComputeNode;
import com.ibm.broker.plugin.*;
import com.kingfisher.kits.mb.crypto.wics.WICSEncryption;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.List;
import javax.management.MBeanException;

public class ProcessSalesOrder_POSBasket_RetailJ_PopulatePaymentDetails extends	MbJavaComputeNode {

	public List<MbElement> getElementsByXPath(MbElement parentElement, String strXPath, String defaultNameSpaceURI) throws MbException
	{
		MbXPath XPath = new MbXPath(strXPath,parentElement);
		XPath.setDefaultNamespace(defaultNameSpaceURI);
		
		List<MbElement> nodeList = (List<MbElement>)parentElement.evaluateXPath(XPath); 
		return nodeList;
	}
	
	public void evaluate(MbMessageAssembly inAssembly) throws MbException {
		
		MbOutputTerminal out 			= getOutputTerminal("out");
		@SuppressWarnings("unused")
		MbOutputTerminal alt 			= getOutputTerminal("alternate");
		MbMessage inMessage 			= inAssembly.getMessage();

		// create new message
		MbMessage outMessage 			= new MbMessage(inMessage);
		MbElement outRoot 				= outMessage.getRootElement();
		MbMessageAssembly outAssembly 	= new MbMessageAssembly(inAssembly,outMessage);
		
		byte[] baPan 					= null;
		String transactionID 			= "";
		String tenderId					= "";
		String decryptedPan 			= "";
		String accountId 				= "";
		String hashedCardNumber 		= "";
		String encryptedPan 			= "";
		String defaultNameSpaceURI 		= "http://www.kingfisher.com/oagis/9";
		String oaNameSpaceURI 			= "http://www.openapplications.org/oagis/9";
		String number 					= "";
		String[] values 				= null;
        String pan 						= "";
        tenderId						= "";
        String keyRef 					= "";
        String encryptedFlag			= "";
        String store 					= "";
        String bdd 						= "";
        String stage					= "Init";
        
        String allException= "Exception in Calling method:-";
        
        //Code change starts for version 0.2
        String maskedCardNumber			= "";
		//Code change ends for version 0.2
        
        
		//Instantiate WICSEncryption
		WICSEncryption wicsEncryption 	= new WICSEncryption();
		
		try 
		{
			Connection ODSConnection 	= null;
			Connection MBREPOSConnection= null;		
		
			ODSConnection 				= getJDBCType4Connection("MBODS",JDBC_TransactionType.MB_TRANSACTION_AUTO); 
			
			stage="After ODS connection";
			MBREPOSConnection 			= getJDBCType4Connection("MBREPOS",JDBC_TransactionType.MB_TRANSACTION_AUTO); 
			stage="After MBREPOS connection";
			
			MbElement outBody 			= outRoot.getFirstElementByPath(MbXMLNSC.PARSER_NAME);
			MbElement processSalesOrder = outBody.getLastChild();
			
			List<MbElement> payments 			= getElementsByXPath(processSalesOrder, "DataArea/SalesOrder/SalesOrderHeader/Payment", defaultNameSpaceURI);
			List<MbElement> salesTransaction 	= getElementsByXPath(processSalesOrder, "DataArea/SalesOrder/SalesOrderHeader/SalesTransaction", defaultNameSpaceURI);
			List<MbElement> id 					= getElementsByXPath(salesTransaction.get(0), "ID", oaNameSpaceURI);
			
			transactionID						= id.get(0).getValueAsString();
			
			int i=0;
			
			//Looping for each 'Payment' segment
			while(i<payments.size())
			{
				MbElement currentPayment 			= (MbElement)payments.get(i);
				MbElement paymentCard 				= currentPayment.getFirstElementByPath("PaymentCard"); 
				
				if(paymentCard!=null)
				{
					MbElement paymentAuthorization 		= paymentCard.getFirstElementByPath("PaymentAuthorization"); 
					MbElement token 					= paymentAuthorization.getFirstElementByPath("Token");
					
					if(token == null)
					{	
						//Code comments starts for version 0.2 
						//MbElement mbeNumber 			= paymentCard.getFirstElementByPath("Number");
						//Code comments ends for version 0.2
						
						
						MbElement mbeNumber 			= paymentCard.getFirstElementByPath("CleartextNumber");
						
						MbElement mbeHashedNumber 		= paymentCard.getFirstElementByPath("HashedNumber"); 
						MbElement mbeEncryptedNumber 	= paymentCard.getFirstElementByPath("EncryptedNumber"); 
						MbElement mbeIVData 			= paymentCard.getFirstElementByPath("IVData");
						
						//Code change starts for version 0.2
						MbElement mbeMaskedCardNumber	= paymentCard.getFirstElementByPath("Number");
						//Code change ends for version 0.2
						
						stage = "Retriving concatinated data for before decryption";
						
						number 							= mbeNumber.getValueAsString();
						values 							= number.split("\\|");
			            pan 							= values[0];
			            tenderId						= values[1];
			            keyRef 							= values[2];
			            encryptedFlag					= values[3];
			            store 							= values[4];
			            bdd 							= values[5];
			            stage							= "Init";
			            hashedCardNumber 				= "" ;
			            encryptedPan					= "" ;
			            
		            
			            try
			            {
			            	stage 						= "createIVdata";
							byte [] IVData 				= WICSEncryption.createIVdata(ODSConnection,MBREPOSConnection);
						    baPan 						= new Hex().decode(pan.getBytes());
			            	stage 						= "decryptData";
							decryptedPan 				= wicsEncryption.decryptData(baPan, "EPOS", keyRef, ODSConnection, MBREPOSConnection, IVData);
				            if(!isValidNumber(decryptedPan))
				            {
								throw new Exception("Card Number Provided By EPOS does not pass LUHN validation.");
							} 
				            if(encryptedFlag.equalsIgnoreCase("true"))
				            {
				            	if(tenderId.equalsIgnoreCase("TRAD") || tenderId.startsWith("HFC"))
				            	{
				            		accountId 			= decryptedPan;
				            	}
				            	else
				            	{
				            		accountId 			= "ENCRYPTED";
				            	}
				            	
				            	stage 					= "getHashedValue";
				            	
				            	//Code change starts for version 0.2 for SALT changes 
				            	
				            	//hashedCardNumber 		= WICSEncryption.getHashedValue(decryptedPan);
				            	
				            	hashedCardNumber 		= WICSEncryption.getHashedValue(decryptedPan,MBREPOSConnection);
				            	//Code change ends for version 0.2 for SALT changes
				            	
				            	stage 					= "encryptData";
								encryptedPan 			= wicsEncryption.encryptData(decryptedPan.getBytes(), store, bdd, ODSConnection, MBREPOSConnection, IVData);
								
								//Code change starts for version 0.2
								stage 					= "getMaskedValue";
								maskedCardNumber		= WICSEncryption.getMaskedValue(decryptedPan, 6, decryptedPan.length()-4, "*");
								//Code change ends for version 0.2
								
				            }
				            else
				            {
				            	if(tenderId.equalsIgnoreCase("TRAD") || tenderId.startsWith("HFC"))
				            	{
				            		accountId 	= pan;
				            	}
								else
								{
									throw new Exception("Received clear text card from number from EPOS. This is not Supported");
								}
				            }
				            
				            //Code comments starts for version 0.2
				            //mbeNumber.setValue(accountId);
				            //Code comments ends for version 0.2
				            
				            //Code change starts for version 0.2
				            mbeNumber.setValue(accountId);
				            //Code change ends for version 0.2
				            
				            
				            //Code change starts for version 0.2
				            mbeMaskedCardNumber.setValue(maskedCardNumber);
				            //Code change ends for version 0.2
				            
				            
				            mbeHashedNumber.setValue(hashedCardNumber);
				            mbeEncryptedNumber.setValue(encryptedPan);
				            mbeIVData.setValue(new String(new Base64().encode(IVData)));
			            }
			            catch(Exception ex)
			            {	
			            	allException=allException+":"+ stage;
			            	allException=allException+ex.toString();
			            	//throw new MbUserException(this,stage,store,"TRANSACTION ID:"+ transactionID+" and TENDER ID : "+tenderId,allException,null);
			            	throw new MbUserException(this,null,"STORE_CODE:"+store," TRANSACTION ID:"+ transactionID+" and TENDER ID:"+tenderId," :"+allException,null);
			            }					
					}
				}
				i++;
			}
			if(ODSConnection != null)
				ODSConnection.close();
			if(MBREPOSConnection != null)
				MBREPOSConnection.close();
			out.propagate(outAssembly);
		}catch(Exception e){
			e.printStackTrace();
			allException=allException+":"+ stage;
			allException=allException+e.toString();
			throw new MbUserException(this,"POS BASKET","POS BASKET"," TRANSACTION ID:"+ transactionID+" and TENDER ID:"+tenderId," :"+allException,null);
			//throw new MbUserException(this,null,null," TRANSACTION ID:"+ transactionID+" and TENDER ID:"+tenderId," :"+allException,null);
		}
	}

	private boolean isValidNumber(String decryptedPan) {
		int i = 0;
	    int j = 0;
	    for (int k = decryptedPan.length() - 1; k >= 0; --k){
	      int l = Integer.parseInt(decryptedPan.substring(k, k + 1));
	      if (j != 0){
	        l *= 2;
	        if (l > 9)
	          l = l % 10 + 1;
	      }
	      i += l;
	      j = (j == 0) ? 1 : 0;
	    }
	    return i % 10 == 0;
	}

}
