package com.abhijitk.uk.com;
import java.io.IOException;

import com.ibm.mq.MQMessage;
import com.ibm.mq.constants.MQConstants;
import com.ibm.mq.headers.MQDataException;
import com.ibm.mq.headers.pcf.MQCFST;
import com.ibm.mq.headers.pcf.PCFException;
import com.ibm.mq.headers.pcf.PCFMessage;
import com.ibm.mq.headers.pcf.PCFParameter;
import com.ibm.mq.pcf.MQCFH;

public class MQChannelControl {
/**
 * <td>MQChannelControl QueueManager Host Port</td>
 * <td>MQChannelControl UNXS389.MB.QM1 unxs0389.uk.b-and-q.com 1430</td>
 * <td>Use this prototype when connecting to a queue manager using client bindings.</td>
 * </tr>
 * </table>
 * 
 * @param args Input parameters.
 */
public static void main(String[] args) {
  PCF_CommonMethods pcfCM = new PCF_CommonMethods();

  try {
    if (pcfCM.ParseParameters(args)) {
      System.out.println("Creating agent.....");
      System.out.println("PCF_CommonMethods:"+pcfCM.queueManager+":"+pcfCM.client+":"+pcfCM.host+":"+pcfCM.port+":"+pcfCM.pcfChannel);
      pcfCM.CreateAgent(args.length);
      
      /** 
        How to use client: MQChannelControl <QmanagerName> <hostname> <port> <Channelname to start/stop/status> <[start]/[stop]/[status]>
        How to use binding: MQChannelControl <QmanagerName> <Channelname to start/stop/status> <[start]/[stop]/[status]>
       ***/
       if(args.length == 3){
	      if (args[2].toString().equalsIgnoreCase(PCF_CommonMethods.startCmd)) {
	    	  System.out.println("Executing : ChannelStart(pcfCM)");
	    	  ChannelStart(pcfCM);
	      }
	      
	      if (args[2].toString().equalsIgnoreCase(PCF_CommonMethods.stopCmd)) { 
	    	  System.out.println("Executing : ChannelStop(pcfCM)");
	          ChannelStop(pcfCM);
	       }
	      
	      if (args[2].toString().equalsIgnoreCase(PCF_CommonMethods.statusCmd)) {
	    	  System.out.println("Executing : ChannelStatus(pcfCM)");
	          ChannelStatus(pcfCM);     	  
	       }
       }
       else if(args.length == 5){
    	   if (args[4].toString().equalsIgnoreCase(PCF_CommonMethods.startCmd)) {
    		   System.out.println("Executing : ChannelStart(pcfCM)");
 	    	  ChannelStart(pcfCM);
 	      }
 	      
 	      if (args[4].toString().equalsIgnoreCase(PCF_CommonMethods.stopCmd)) {  
 	    	 System.out.println("Executing : ChannelStop(pcfCM)");
 	          ChannelStop(pcfCM);
 	       }
 	      
 	      if (args[4].toString().equalsIgnoreCase(PCF_CommonMethods.statusCmd)) {
 	    	 System.out.println("Executing : ChannelStatus(pcfCM)");
 	          ChannelStatus(pcfCM);     	  
 	       }
    	   
       }
       else{
    	   System.out.println("No suitable argument found to process your command.");
       }
       
       System.out.println("Destroying agent.....");
      pcfCM.DestroyAgent();
    }
  }
  catch (Exception e) {
    pcfCM.DisplayException(e);
  }
  return;
}


public static void ChannelStatus(PCF_CommonMethods pcfCM) throws MQDataException, IOException {
  // Create the PCF message type for the inquire channel.
  PCFMessage pcfCmd = new PCFMessage(MQConstants.MQCMD_INQUIRE_CHANNEL_STATUS);

  // Add the start channel mandatory parameters.
  // Channel name.
  pcfCmd.addParameter(MQConstants.MQCACH_CHANNEL_NAME, PCF_CommonMethods.pcfChannel);
  System.out.println("Channel enquery is bening sent...");
  
  try {
    PCFMessage[] pcfResponse = pcfCM.agent.send(pcfCmd);
	System.out.println("CHANNEL NAME="+pcfResponse[0].getStringParameterValue(3501));
	System.out.println("CHANNEL TYPE="+pcfResponse[0].getIntParameterValue(1511));
	System.out.println("BATCHES="+pcfResponse[0].getIntParameterValue(1537));
	System.out.println("BATCH SIZE="+pcfResponse[0].getIntParameterValue(1502));
	System.out.println("BUFFERS RECEIVED="+pcfResponse[0].getIntParameterValue(1539));
	System.out.println("BUFFERS SENT="+pcfResponse[0].getIntParameterValue(1538));
	System.out.println("BYTES_RECEIVED="+pcfResponse[0].getIntParameterValue(1536));
	System.out.println("BYTES SENT="+pcfResponse[0].getIntParameterValue(1535));
	System.out.println("CHANNEL START DATE="+pcfResponse[0].getStringParameterValue(3529));
	System.out.println("CHANNEL START TIME="+pcfResponse[0].getStringParameterValue(3528));
	System.out.println("CONNECTION_NAME="+pcfResponse[0].getStringParameterValue(3506));
	System.out.println("INDOUBT_STATUS="+pcfResponse[0].getIntParameterValue(1528));
	System.out.println("LOCAL ADDRESS="+pcfResponse[0].getStringParameterValue(3520));
	System.out.println("LAST MSG DATE="+pcfResponse[0].getStringParameterValue(3525));
	System.out.println("LAST MSG TIME="+pcfResponse[0].getStringParameterValue(3524));
	System.out.println("BYTES SENT="+pcfResponse[0].getIntParameterValue(1535));
	System.out.println("RESPONSE REASON="+pcfResponse[0].getReason());	

    int chStatus = ((Integer) (pcfResponse[0].getParameterValue(MQConstants.MQIACH_CHANNEL_STATUS))).intValue();

    String[] chStatusText = {"", "MQCHS_BINDING", "MQCHS_STARTING", "MQCHS_RUNNING",
        "MQCHS_STOPPING", "MQCHS_RETRYING", "MQCHS_STOPPED", "MQCHS_REQUESTING", "MQCHS_PAUSED",
        "", "", "", "", "MQCHS_INITIALIZING"};

    System.out.println("Channel "+ PCF_CommonMethods.pcfChannel +" is in "+"\"" + chStatusText[chStatus].substring(6)+"\" state" );
      
  }
  catch (PCFException pcfe) {
    if (pcfe.reasonCode == MQConstants.MQRCCF_CHL_STATUS_NOT_FOUND) {
    	System.out.println("Channel "+"\"" +PCF_CommonMethods.pcfChannel+ "\""+" might be in inactive state or does not exist" );
       // throw pcfe;
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
         }
         
    	 else if (pcfe.reasonCode == MQConstants.MQRCCF_CHANNEL_NOT_FOUND) {
             System.out.println("The Channel "+ PCF_CommonMethods.pcfChannel +"  does not exist.");
         }       
         
    	 else if (pcfCM.client) {
    		 System.out.println("\nQueue manager :" + pcfCM.queueManager + "\nListening on port:" + pcfCM.port+ "\nChannel:" + PCF_CommonMethods.pcfChannel);
 		    
 		    throw pcfe;
         }
         else {
           System.out.println("Either the queue manager \"" + pcfCM.queueManager
               + "\" does not exist or the channel \"" + PCF_CommonMethods.pcfChannel
               + "\" does not exist on the queue manager.");
         }
       }
       return;
	
}

public static void ChannelStop (PCF_CommonMethods pcfCM) throws PCFException, MQDataException, IOException{
	
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
		  }
		  
		  else if (pcfe.reasonCode == MQConstants.MQRCCF_CHANNEL_NOT_FOUND) {
		      System.out.println("The Channel "+ PCF_CommonMethods.pcfChannel +" does not exist.");
		  }
		  
		  else if (pcfe.reasonCode == MQConstants.MQRCCF_CHANNEL_NOT_ACTIVE) {
		      System.out.println("The Channel "+ PCF_CommonMethods.pcfChannel +" is already in stopped state.");
		  }  
		  
		  else if (pcfCM.client) {
		    System.out.println("\nQueue manager :" + pcfCM.queueManager + "\nListening on port:" + pcfCM.port+ "\nChannel:" + PCF_CommonMethods.pcfChannel);
		    
		    throw pcfe;
		  }
		  else {
		    System.out.println("Either the queue manager \"" + pcfCM.queueManager
		    		+ "\" does not exist or the channel \"" + PCF_CommonMethods.pcfChannel
		    		+ "\" does not exist on the queue manager.");
		  }
		}
       return;
	
}


}
