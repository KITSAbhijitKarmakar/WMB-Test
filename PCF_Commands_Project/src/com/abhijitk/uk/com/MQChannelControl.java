package com.abhijitk.uk.com;
import java.io.Console;
import java.io.IOException;
import com.ibm.mq.constants.MQConstants;
import com.ibm.mq.headers.MQDataException;
import com.ibm.mq.headers.pcf.PCFException;
import com.ibm.mq.headers.pcf.PCFMessage;


public class MQChannelControl {
/**
 * <td>java -Djava.library.path=/usr/mqm/java/lib64 -Djava.ext.dirs=.:/usr/mqm/java/lib -jar MQChannelControl.jar $1 $2 $3 $4 $5</td>
 * </tr> 
 * </table>
 */
public static void main(String[] args) {
  PCF_CommonMethods pcfCM = new PCF_CommonMethods(); 
  
  
  try {
    if (pcfCM.ParseParameters(args)) {
      
    	System.out.println("Connection to Queue manager("+ pcfCM.queueManager + ") is being established....");
        pcfCM.CreateAgent(args.length);
        
        if(args.length == 3 && !PCF_CommonMethods.createCmd.equalsIgnoreCase(args[2].toString())){
    	  System.out.println("Command Used:-./MQChannelControl.sh "+pcfCM.queueManager+" "+PCF_CommonMethods.pcfChannel+" "+args[2]);
    	  

	      if (args[2].toString().equalsIgnoreCase(PCF_CommonMethods.startCmd)) {
	    	  System.out.println("The Specified Channel is being started....");
	    	  ChannelStart(pcfCM);
	      }
	      
	      else if (args[2].toString().equalsIgnoreCase(PCF_CommonMethods.stopCmd)) { 
	    	  System.out.println("The Specified Channel is being stopped....");
	          ChannelStop(pcfCM);
	       }
	      
	      else if (args[2].toString().equalsIgnoreCase(PCF_CommonMethods.statusCmd)) {
	    	  System.out.println("The Specified Channel is being status checked....");
	          ChannelStatus(pcfCM);     	  
	       }
	      
	      else if (args[2].toString().equalsIgnoreCase(PCF_CommonMethods.deleteCmd)) {
	    	  System.out.println("The Specified Channel is being deleted....");
	    	  DeleteChannel(pcfCM);    	  
	       }
	      
	      else {
   		   	   System.out.println("No suitable command found to process your request.");
   	   		}
       
        }
        else if(args.length == 5 && !PCF_CommonMethods.createCmd.equalsIgnoreCase(args[4].toString())){
    	  System.out.println("Command Used:-./MQChannelControl.sh "+pcfCM.queueManager+" "+pcfCM.host+" "+pcfCM.port+" "+PCF_CommonMethods.pcfChannel+" "+args[4]);
    	  
	   	   if (args[4].toString().equalsIgnoreCase(PCF_CommonMethods.startCmd)) {
	   		   System.out.println("The Specified Channel is being started....");
		    	   ChannelStart(pcfCM);
		      }
		      
	   	   else if (args[4].toString().equalsIgnoreCase(PCF_CommonMethods.stopCmd)) {  
		    	  System.out.println("The Specified Channel is being stopped....");
		          ChannelStop(pcfCM);
		       }
		      
	   	   else if (args[4].toString().equalsIgnoreCase(PCF_CommonMethods.statusCmd)) {
		    	 System.out.println("The Specified Channel is being status checked...");
		          ChannelStatus(pcfCM);     	  
		       }
	   	   else if (args[4].toString().equalsIgnoreCase(PCF_CommonMethods.deleteCmd)) {
		    	  System.out.println("The Specified Channel is being deleted....");
		    	  System.out.println("You could also delete the transmission queue....");
		    	  DeleteChannel(pcfCM);    	  
		       }
	   	   else {
	   		   System.out.println("No suitable command found to process your request.");
	   	   }
   	   
      
        }
        else if((args.length == 3 || args.length == 5) && (args[2].toString().equalsIgnoreCase(PCF_CommonMethods.createCmd)  || args[4].toString().equalsIgnoreCase(PCF_CommonMethods.createCmd))){
        	System.out.println("Prameters Used:-"+pcfCM.queueManager+"-:-"+pcfCM.client+"-:-"+pcfCM.host+"-:-"+pcfCM.port+"-:-"+PCF_CommonMethods.pcfChannel+"-:-"+PCF_CommonMethods.createCmd);
        	System.out.println("The Specified Channel is being created...");
        	CreateChannel(pcfCM);
        }
        
      
       /*if(args.length == 3){
	      if (args[2].toString().equalsIgnoreCase(PCF_CommonMethods.startCmd)) {
	    	  System.out.println("The Specified Channel is being started....");
	    	  ChannelStart(pcfCM);
	      }
	      
	      else if (args[2].toString().equalsIgnoreCase(PCF_CommonMethods.stopCmd)) { 
	    	  System.out.println("The Specified Channel is being stopped....");
	          ChannelStop(pcfCM);
	       }
	      
	      else if (args[2].toString().equalsIgnoreCase(PCF_CommonMethods.statusCmd)) {
	    	  System.out.println("The Specified Channel is being status checked....");
	          ChannelStatus(pcfCM);     	  
	       }
	      
	      else if (args[2].toString().equalsIgnoreCase(PCF_CommonMethods.deleteCmd)) {
	    	  System.out.println("The Specified Channel is being deleted....");
	    	  DeleteChannel(pcfCM);    	  
	       }
	      
	      else {
   		   	   System.out.println("No suitable command found to process your request.");
   	   		}
       }
       else if(args.length == 5){
    	   if (args[4].toString().equalsIgnoreCase(PCF_CommonMethods.startCmd)) {
    		   System.out.println("The Specified Channel is being started....");
 	    	   ChannelStart(pcfCM);
 	      }
 	      
    	   else if (args[4].toString().equalsIgnoreCase(PCF_CommonMethods.stopCmd)) {  
 	    	  System.out.println("The Specified Channel is being stopped....");
 	          ChannelStop(pcfCM);
 	       }
 	      
    	   else if (args[4].toString().equalsIgnoreCase(PCF_CommonMethods.statusCmd)) {
 	    	 System.out.println("The Specified Channel is being status checked...");
 	          ChannelStatus(pcfCM);     	  
 	       }
    	   else if (args[4].toString().equalsIgnoreCase(PCF_CommonMethods.deleteCmd)) {
	    	  System.out.println("The Specified Channel is being deleted....");
	    	  System.out.println("You could also delete the transmission queue....");
	    	  DeleteChannel(pcfCM);    	  
	       }
    	   else {
    		   System.out.println("No suitable command found to process your request.");
    	   }
    	   
       }*/
       else{
    	   System.out.println("No suitable argument found to process your command.");
       }
       
       System.out.println("Connection to Queue manager("+ pcfCM.queueManager + ") is being closed....");
       pcfCM.DestroyAgent();
    }
  }
  catch (Exception e) {
    pcfCM.DisplayException(e);
  }
  return;
}


public static void ChannelStatus(PCF_CommonMethods pcfCM) throws MQDataException, IOException {
 
  PCFMessage pcfCmd = new PCFMessage(MQConstants.MQCMD_INQUIRE_CHANNEL_STATUS);
  pcfCmd.addParameter(MQConstants.MQCACH_CHANNEL_NAME, PCF_CommonMethods.pcfChannel);
  System.out.println("Channel enquery is bening sent...");
  
  try {
    PCFMessage[] pcfResponse = pcfCM.agent.send(pcfCmd);
	System.out.println("CHANNEL NAME="+pcfResponse[0].getStringParameterValue(3501));
	System.out.println("CHANNEL TYPE="+pcfResponse[0].getIntParameterValue(1511));
	System.out.println("CONNECTION_NAME="+pcfResponse[0].getStringParameterValue(3506));
	if(pcfResponse[0].getIntParameterValue(1511)!= 7){
		System.out.println("BATCHES="+pcfResponse[0].getIntParameterValue(1537));
		System.out.println("BATCH SIZE="+pcfResponse[0].getIntParameterValue(1502));
		System.out.println("BUFFERS RECEIVED="+pcfResponse[0].getIntParameterValue(1539));
		System.out.println("BUFFERS SENT="+pcfResponse[0].getIntParameterValue(1538));
		System.out.println("BYTES_RECEIVED="+pcfResponse[0].getIntParameterValue(1536));
		System.out.println("BYTES SENT="+pcfResponse[0].getIntParameterValue(1535));
		System.out.println("CHANNEL START DATE="+pcfResponse[0].getStringParameterValue(3529));
		System.out.println("CHANNEL START TIME="+pcfResponse[0].getStringParameterValue(3528));	
		System.out.println("INDOUBT_STATUS="+pcfResponse[0].getIntParameterValue(1528));
		System.out.println("LOCAL ADDRESS="+pcfResponse[0].getStringParameterValue(3520));
		System.out.println("LAST MSG DATE="+pcfResponse[0].getStringParameterValue(3525));
		System.out.println("LAST MSG TIME="+pcfResponse[0].getStringParameterValue(3524));
		System.out.println("BYTES SENT="+pcfResponse[0].getIntParameterValue(1535));
		System.out.println("RESPONSE REASON="+pcfResponse[0].getReason());	
	}
    int chStatus = ((Integer) (pcfResponse[0].getParameterValue(MQConstants.MQIACH_CHANNEL_STATUS))).intValue();

    String[] chStatusText = {"", "MQCHS_BINDING", "MQCHS_STARTING", "MQCHS_RUNNING",
        "MQCHS_STOPPING", "MQCHS_RETRYING", "MQCHS_STOPPED", "MQCHS_REQUESTING", "MQCHS_PAUSED",
        "", "", "", "", "MQCHS_INITIALIZING"};

    System.out.println("Channel "+ PCF_CommonMethods.pcfChannel +" is in "+"\"" + chStatusText[chStatus].substring(6)+"\" state" );
      
  }
  catch (PCFException pcfe) {
    if (pcfe.reasonCode == MQConstants.MQRCCF_CHL_STATUS_NOT_FOUND) {
    	System.out.println("Channel "+"\"" +PCF_CommonMethods.pcfChannel+ "\""+" might be in inactive state or does not exist" );
    	return;
    }  
    else if (pcfe.reasonCode == MQConstants.MQRCCF_MQOPEN_FAILED) {
        System.out.println("The Channel "+ PCF_CommonMethods.pcfChannel +"  might be incorrectly configured.  But can be deleted only");
        return;
    }
    else if (pcfCM.client) {
      System.out.println("Either the queue manager \"" + pcfCM.queueManager
          + "\" does not exist or is not listening on port \"" + pcfCM.port
          + "\" or the channel \"" + PCF_CommonMethods.pcfChannel
          + "\" does not exist on the queue manager.");
    }
    else {
      System.out.println("Either the queue manager \"" + pcfCM.queueManager
          + "\" does not exist or the channel \"" + PCF_CommonMethods.pcfChannel
          + "\" does not exist on the queue manager.");
    }
  }
  return;
}

public static void ChannelStart (PCF_CommonMethods pcfCM) throws PCFException, MQDataException, IOException{
	
	   PCFMessage pcfCmd = new PCFMessage(MQConstants.MQCMD_START_CHANNEL);       
       pcfCmd.addParameter(MQConstants.MQCACH_CHANNEL_NAME, PCF_CommonMethods.pcfChannel);
       
       try {
    	   PCFMessage []   responses = pcfCM.agent.send (pcfCmd);
    	   System.out.println("The command to start the channel "+ PCF_CommonMethods.pcfChannel +"  issued successfully");
    	   System.out.println("responses"+responses.toString());
       }
       catch (PCFException pcfe) {         
         
    	 if (pcfe.reasonCode == MQConstants.MQRCCF_CHANNEL_IN_USE) {
           System.out.println("The Channel "+ PCF_CommonMethods.pcfChannel +"  is already in use and running.");
           return;
         }
         
    	 else if (pcfe.reasonCode == MQConstants.MQRCCF_CHANNEL_NOT_FOUND) {
             System.out.println("The Channel "+ PCF_CommonMethods.pcfChannel +"  does not exist.");
             return;
         }       
    	 else if (pcfe.reasonCode == MQConstants.MQRCCF_MQOPEN_FAILED) {
             System.out.println("The Channel "+ PCF_CommonMethods.pcfChannel +"  might be incorrectly configured. But can be deleted only");
             return;
         }
    	 /*else if (pcfCM.client) {
    		 System.out.println("\nQueue manager :" + pcfCM.queueManager + "\nListening on port:" + pcfCM.port+ "\nChannel:" + PCF_CommonMethods.pcfChannel);
 		    
 		    throw pcfe;
         }*/
         else {
           System.out.println("Either the queue manager \"" + pcfCM.queueManager
               + "\" does not exist or the channel \"" + PCF_CommonMethods.pcfChannel
               + "\" does not exist on the queue manager.");
         }
       }
       return;
	
}

public static void ChannelStop (PCF_CommonMethods pcfCM) throws PCFException, MQDataException, IOException{
	   System.out.println("The Specified Channel is being stopped....");
	   PCFMessage pcfCmd = new PCFMessage(MQConstants.MQCMD_STOP_CHANNEL);       
       pcfCmd.addParameter(MQConstants.MQCACH_CHANNEL_NAME, PCF_CommonMethods.pcfChannel);
       
       try {
	       PCFMessage []   responses = pcfCM.agent.send (pcfCmd);
	       System.out.println("The command to stop the channel "+ PCF_CommonMethods.pcfChannel +"  issued successfully");
	       System.out.println("responses"+responses.toString());	       
		}
		catch (PCFException pcfe) {         
		  
		  if (pcfe.reasonCode == MQConstants.MQRCCF_CHANNEL_IN_USE) {
		    System.out.println("The Channel "+ PCF_CommonMethods.pcfChannel +" is already in use and running.");
		    return;
		  }
		  
		  else if (pcfe.reasonCode == MQConstants.MQRCCF_CHANNEL_NOT_FOUND) {
		      System.out.println("The Channel "+ PCF_CommonMethods.pcfChannel +" does not exist.");
		      return;
		  }
		  
		  else if (pcfe.reasonCode == MQConstants.MQRCCF_CHANNEL_NOT_ACTIVE) {
		      System.out.println("The Channel "+ PCF_CommonMethods.pcfChannel +" is already in stopped state.");
		      return;
		  }  
		  else if (pcfe.reasonCode == MQConstants.MQRCCF_MQOPEN_FAILED) {
	             System.out.println("The Channel "+ PCF_CommonMethods.pcfChannel +"  might be incorrectly configured. But can be deleted only");
	             return;
	        }
		  /*else if (pcfCM.client) {
		    System.out.println("\nQueue manager :" + pcfCM.queueManager + "\nListening on port:" + pcfCM.port+ "\nChannel:" + PCF_CommonMethods.pcfChannel);
		    
		    throw pcfe;
		  }*/
		  else {
		    System.out.println("Either the queue manager \"" + pcfCM.queueManager
		    		+ "\" does not exist or the channel \"" + PCF_CommonMethods.pcfChannel
		    		+ "\" does not exist on the queue manager.");
		  }
		}
       return;
	
}

	public static void DeleteChannel(PCF_CommonMethods pcfCM) throws PCFException, MQDataException,IOException {
		
		ChannelStop(pcfCM);		
		PCFMessage pcfCmd = new PCFMessage(MQConstants.MQCMD_DELETE_CHANNEL);		
		pcfCmd.addParameter(MQConstants.MQCACH_CHANNEL_NAME, PCF_CommonMethods.pcfChannel);
		
		try {			
		    PCFMessage []  responses = pcfCM.agent.send(pcfCmd);
		    System.out.println("The command to delete the channel "+ PCF_CommonMethods.pcfChannel +"  issued successfully");
		    System.out.println("responses"+responses.toString());
		} catch(PCFException pcfe){
			if (pcfe.reasonCode == MQConstants.MQRCCF_CHANNEL_NOT_FOUND) {
			      System.out.println("The Channel "+ PCF_CommonMethods.pcfChannel +" does not exist.");
			      return;
			  }
			else {
				System.out.println("Either the queue manager \"" + pcfCM.queueManager
			    		+ "\" does not exist or the channel \"" + PCF_CommonMethods.pcfChannel
			    		+ "\" does not exist on the queue manager.");
				throw pcfe;
			}
		}	
		
		return;
	}
	
		
	public static void CreateChannel(PCF_CommonMethods pcfCM) throws MQDataException, IOException {    
	    PCFMessage pcfCmd = new PCFMessage(MQConstants.MQCMD_CREATE_CHANNEL);
	    pcfCmd.addParameter(MQConstants.MQCACH_CHANNEL_NAME, PCF_CommonMethods.pcfChannel);
	    String chlType = null;
	    String tgtHost = null;
	    String tgtPort = null;
	    String transmitQ = null;
	    String desc = null;
	    Console console = System.console();
 	    chlType = console.readLine("Channel Type? ");
	    
	    if(chlType.equalsIgnoreCase("sender")){
		    tgtHost = console.readLine("Target Host? ");
		    tgtPort = console.readLine("Target Port? ");
		    transmitQ = console.readLine("Transmission Queue? ");
		    desc = console.readLine("Channel Description? ");
	    	pcfCmd.addParameter(MQConstants.MQIACH_CHANNEL_TYPE, MQConstants.MQCHT_SENDER);
	    	pcfCmd.addParameter(MQConstants.MQCACH_CONNECTION_NAME, tgtHost + "(" + tgtPort + ")");
		    pcfCmd.addParameter(MQConstants.MQCACH_XMIT_Q_NAME, transmitQ);
		    pcfCmd.addParameter(MQConstants.MQCACH_DESC, desc);
	    }
	    else if(chlType.equalsIgnoreCase("server")){
	    	desc = console.readLine("Channel Description? ");
	    	pcfCmd.addParameter(MQConstants.MQIACH_CHANNEL_TYPE, MQConstants.MQCHT_SERVER);
	    	pcfCmd.addParameter(MQConstants.MQCACH_DESC, desc);
	    }
	    
	    else if(chlType.equalsIgnoreCase("svrcon")){
	    	desc = console.readLine("Channel Description? ");
	    	pcfCmd.addParameter(MQConstants.MQIACH_CHANNEL_TYPE, MQConstants.MQCHT_SVRCONN);
	    	pcfCmd.addParameter(MQConstants.MQCACH_DESC, desc);
	    }
	    
	    else if(chlType.equalsIgnoreCase("receiver")){
	    	desc = console.readLine("Channel Description? ");
	    	pcfCmd.addParameter(MQConstants.MQIACH_CHANNEL_TYPE, MQConstants.MQCHT_RECEIVER);
	    	pcfCmd.addParameter(MQConstants.MQCACH_DESC, desc);
	    }
	    else{
	    	System.out.println("Unknown channel type.program exiting without any action.");
	    	System.out.println("Valid Channel types:sender,server,svrcon,receiver.");
	    	return;
	    }
	    
	    
	    try {	      
	    	PCFMessage []  responses = pcfCM.agent.send(pcfCmd);
	    	System.out.println("The command to create the channel "+ PCF_CommonMethods.pcfChannel +"  issued successfully");
		    System.out.println("responses"+responses.toString());
	    }
	    catch (PCFException pcfe) {
	      if (pcfe.reasonCode == MQConstants.MQRCCF_OBJECT_ALREADY_EXISTS) {
	    	  System.out.println("The channel "+ PCF_CommonMethods.pcfChannel +"  already exist");
	    	  return;
	      }
	      else{
	    	  throw pcfe; 
	      }
	    }
	    System.out.println("Starting the channel after creation.....");
	    ChannelStart(pcfCM);
	    System.out.println("Checking status of the channel after creation.....");
	    ChannelStatus(pcfCM);
	    return;
	  }

}
