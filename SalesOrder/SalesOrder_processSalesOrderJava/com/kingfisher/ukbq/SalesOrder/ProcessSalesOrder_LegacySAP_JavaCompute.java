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
* Message Flow 	 		: 	ProcessSalesOrder_LegacySAP											                  	  *
* Description 	 		: 																							  *
* Module Name  	 		:	Java Compute																			  *            				  	  
* Module Description  	:																							  *                                         
*																									  				  *
* Version   Date	   		Author			Description                     				 				          *
* ======= 	=========== 	=========== 	========================================				  				  *
*  0.1   	19-MAY-2013 	Ashwin Pai 		The initial version. 		  		  				                  	  *
*  0.2   	15-NOV-2013 	Asif Hossain 	Code change for to handle SALT SALT encryption decryption             	  *
**********************************************************************************************************************/

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;

import org.apache.commons.codec.binary.Base64;

import com.ibm.broker.javacompute.MbJavaComputeNode;
import com.ibm.broker.plugin.*;
import com.kingfisher.kits.mb.crypto.wics.WICSEncryption;

public class ProcessSalesOrder_LegacySAP_JavaCompute extends MbJavaComputeNode {

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
			
		//Get the Environment variable
		MbElement environment 			= inAssembly.getGlobalEnvironment().getRootElement();
			
		//Get the rootElement
		MbElement rootElement 			= inMessage.getRootElement();
		
		try{
			String ccnum 					= null;
			String cckeyRef 				= null;
			String ccivData 				= null;
			String ccencrypt				= null;
			String ccmasked 				= null;
			String cchashed 				= null;
			String ccins					= null;
			String configval 				= null;
			String cardNo 					= null;
			String stage					= "init";
			
			//Instantiate WICSEncryption
			WICSEncryption wicsEncryption 	= new WICSEncryption();
		
			Connection ODSConnection 		= null;
			Connection MBREPOSConnection 	= null;		
			
			stage = "Before ODS Connection";
			ODSConnection 					= getJDBCType4Connection("MBODS",JDBC_TransactionType.MB_TRANSACTION_AUTO); 
			stage = "After ODS Connection and before REPOS connection";
			MBREPOSConnection 				= getJDBCType4Connection("MBREPOS",JDBC_TransactionType.MB_TRANSACTION_AUTO);
			stage = "After REPOS Connection";
			
			try 
			{	
				stage = "Before retrieveing data from input message";
				MbElement outBody 						= outRoot.getFirstElementByPath(MbXMLNSC.PARSER_NAME);
				MbElement sapZRetCcardPaymentsWrapper 	= outBody.getLastChild();				
				MbElement SapZRetCcardPayments 			= sapZRetCcardPaymentsWrapper.getFirstElementByPath("SapZRetCcardPayments");
				MbElement sapPtZpayments 				= SapZRetCcardPayments.getFirstElementByPath("SapPtZpayments");
			
				if (sapPtZpayments.getFirstElementByPath("CCNUM")!= null){
					ccnum 								= sapPtZpayments.getFirstElementByPath("CCNUM").getValueAsString();
				}
			
				if (sapPtZpayments.getFirstElementByPath("ZCC_KEYREF")!= null){
					cckeyRef 							= sapPtZpayments.getFirstElementByPath("ZCC_KEYREF").getValueAsString();
				}
			
				if (sapPtZpayments.getFirstElementByPath("ZCC_IVDATA")!= null){
					ccivData 							= sapPtZpayments.getFirstElementByPath("ZCC_IVDATA").getValueAsString();
				}
			
				if (sapPtZpayments.getFirstElementByPath("ZCC_ENCRYPT")!= null){
					ccencrypt 							= sapPtZpayments.getFirstElementByPath("ZCC_ENCRYPT").getValueAsString();
				}
			
				if (sapPtZpayments.getFirstElementByPath("ZCC_MASKED")!=null){
					ccmasked 							= sapPtZpayments.getFirstElementByPath("ZCC_MASKED").getValueAsString();
				}
			
				if (sapPtZpayments.getFirstElementByPath("ZCC_HASHED")!=null){
					cchashed 							= sapPtZpayments.getFirstElementByPath("ZCC_HASHED").getValueAsString();
				}
			
				if(sapPtZpayments.getFirstElementByPath("CCINS")!=null){
					ccins 								= sapPtZpayments.getFirstElementByPath("CCINS").getValueAsString();
				}
		
				//Setting the Environment
				environment.createElementAsFirstChild(MbElement.TYPE_NAME_VALUE,"AccountID",ccnum);
		
				if((cckeyRef == null || cckeyRef.length() == 0) && (ccivData == null || ccivData.length() == 0)){
					environment.createElementAsFirstChild(MbElement.TYPE_NAME_VALUE,"Token",ccencrypt);
					environment.createElementAsFirstChild(MbElement.TYPE_NAME_VALUE,"MaskedAccountId",ccmasked);
					environment.createElementAsFirstChild(MbElement.TYPE_NAME_VALUE,"HashedAccountId",cchashed);
				}
				
				else if((cckeyRef != null && cckeyRef.trim().length() != 0) && (ccivData != null && ccivData.trim().length() != 0)){
					
					byte[] ivData 			= new Base64().decode(ccivData.getBytes());
					byte[] encData 			= new Base64().decode(ccencrypt.getBytes());
					
					//Get the decrypted value
					try {
						stage 	= "decrypt data";
						cardNo 				= wicsEncryption.decryptData(encData, "SAP", cckeyRef, ODSConnection, MBREPOSConnection, ivData);
						Statement select 	= MBREPOSConnection.createStatement();
						stage 	= "Database lookup after card decyption";
						ResultSet result 	= select.executeQuery("SELECT CONFIGVALUE FROM MBCONFIG where CANVAL = 'FullPCi'");         
			 
						while(result.next()) { 
							configval 		= result.getString(1);
						}
						select.close();			      
					}catch (Exception e) {
						throw new Exception("Exception Caught whilst "+stage+" Exception was: "
										+ e.toString());
					}

					if(configval != null){
						if(ccnum != null && ccnum.trim().length() == 16)
							cardNo 			= ccnum;
						else
							cardNo 			= ccmasked;
					}
					
					//Set MaskedValue
					try {
						environment.createElementAsFirstChild(MbElement.TYPE_NAME_VALUE, "MaskedAccountId",WICSEncryption.getMaskedValue(cardNo, 6, cardNo.length()-4, "*"));
					}catch (Exception e) {
						throw new Exception("Exception Caught whilst setting MaskedValue. Exception was: "
										+ e.toString());
					}
				   
					//Set HashedValue
					try {
						
						//Code change starts for version 0.2 to handle SALT SALT encryption decryption
						//environment.createElementAsFirstChild(MbElement.TYPE_NAME_VALUE,"HashedAccountId",WICSEncryption.getHashedValue(cardNo));
						
						environment.createElementAsFirstChild(MbElement.TYPE_NAME_VALUE,"HashedAccountId",WICSEncryption.getHashedValue(cardNo,MBREPOSConnection));
						
						//Code change ends for version 0.2 to handle SALT SALT encryption decryption
						
					}catch (Exception e) {
						throw new Exception("Exception Caught whilst setting HashedValue. Exception was: "
										+ e.toString());
					}
				}
				
				//HCFS Card
				else if(ccins.equalsIgnoreCase("HCFS")){
					try{
						//Set the Masked value
						environment.createElementAsFirstChild(MbElement.TYPE_NAME_VALUE,"MaskedAccountId",WICSEncryption.getMaskedValue(ccnum, 6, ccnum.trim().length()-4, "*"));
						
						
						//Code change starts for version 0.2 to handle SALT SALT encryption decryption
						//Set the HasehedValue
						//environment.createElementAsFirstChild(MbElement.TYPE_NAME_VALUE,"HashedAccountId",WICSEncryption.getHashedValue(ccnum));
						
						environment.createElementAsFirstChild(MbElement.TYPE_NAME_VALUE,"HashedAccountId",WICSEncryption.getHashedValue(ccnum,MBREPOSConnection));
						
						//Code change ends for version 0.2 to handle SALT SALT encryption decryption
						
						//Set the IVData
						byte[] ivdata 			= WICSEncryption.createIVdata(ODSConnection,MBREPOSConnection);
						environment.createElementAsFirstChild(MbElement.TYPE_NAME_VALUE,"IVData",new Base64().encode(ivdata));
						
						//Set the encryptData
						String VKBUR 			= rootElement.getFirstElementByPath(MbXMLNSC.PARSER_NAME).getFirstElementByPath("SapZRetCcardPaymentsWrapper/SapZRetCcardPayments/SapPtZpayments/VKBUR").getValueAsString();
						String businessDayDate 	= MBREPOSConnection.prepareCall("MessageID.getNextBusinessDayDate").getResultSet().getString(1);
					
						environment.createElementAsFirstChild(MbElement.TYPE_NAME_VALUE,"EncryptedAccountId",wicsEncryption.encryptData(ccnum.getBytes(), VKBUR, businessDayDate, ODSConnection, MBREPOSConnection, ivdata));
					}catch (Exception e) {
						throw new Exception("Exception Caught whilst processing HCFS Cards. Exception was: "
										+ e.toString());
					}			   
				}
				if(ODSConnection != null)
					ODSConnection.close();
				if(MBREPOSConnection != null)
					MBREPOSConnection.close();
			
				out.propagate(outAssembly);
			
			}catch (Exception e) {
				throw new Exception("Exception Caught whilst processing Payment details. Exception was: "
								+ e.toString());
			}	
		}catch(Exception e)
		{
			e.printStackTrace();
			throw new MbUserException(this,"SAP LEGACY","SAP LEGACY","<TRANSACTION ID>/<LINE NUMBER>/<TENDER ID>",e.toString(),null);
		}
	} 	
}
