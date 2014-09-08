package com.abhijitk.uk.com;

import java.io.IOException;
import java.util.Arrays;

import com.ibm.mq.MQException;
import com.ibm.mq.constants.CMQC;
import com.ibm.mq.constants.MQConstants;
import com.ibm.mq.headers.MQDataException;
import com.ibm.mq.headers.pcf.PCFException;
import com.ibm.mq.headers.pcf.PCFMessageAgent;


public class PCF_CommonMethods {
  public static  String pcfChannel = "UNKNOWN";
  
  public static  String startCmd = "start";
  public static  String statusCmd = "status";
  public static  String stopCmd = "stop";
  public static  String deleteCmd = "delete";
  public static  String createCmd = "create";
  
  public String host = "127.0.0.1";
  public int port = 1430;
  public String queueManager = "UNKNOWN";
  public PCFMessageAgent agent = null;
  public boolean client = false;
  //public String channel_svrconn = "PRODSUP.SVRCONN";
  public String channel_svrconn = "SYSTEM.ADMIN.SVRCONN";
  public String padding = null;
  
  public PCF_CommonMethods() {
    char[] space = new char[64];
    Arrays.fill(space, 0, space.length, ' ');
    padding = new String(space);
    return;
  }
  
  public boolean ParseParameters(String[] args) {
        
    if(args.length == 3)  {
    	queueManager = args[0];
    	pcfChannel = args[1];    	
        return true;
     }
    else if (args.length == 5){
    	queueManager = args[0];
    	host = args[1];
    	port = new Integer(args[2]).intValue();
    	pcfChannel = args[3];
        return true;
    }

    System.out.println("The program must be used with the following parameters:-\n");   
    System.out.println("How to use Client mode:  ./MQChannelControl.sh [QmanagerName] [hostname] [port] [Channelname] <[start]/[stop]/[status]/[create]/[delete]>");
    System.out.println("How to use binding mode: ./MQChannelControl.sh [QmanagerName] [Channelname] <[start]/[stop]/[status]/[create]/[delete]>");    
    System.out.println("--------Client Mode-------------");
    System.out.println("Example: ./MQChannelControl QM1 localhost 1414 TEST.CHL start");
    System.out.println("Example: ./MQChannelControl QM1 localhost 1414 TEST.CHL stop");
    System.out.println("Example: ./MQChannelControl QM1 localhost 1414 TEST.CHL status");
    System.out.println("Example: ./MQChannelControl QM1 localhost 1414 TEST.CHL create");
    System.out.println("Example: ./MQChannelControl QM1 localhost 1414 TEST.CHL delete");
    System.out.println("--------Binding Mode-------------");
    System.out.println("Example: ./MQChannelControl QM1 TEST.CHL start");
    System.out.println("Example: ./MQChannelControl QM1 TEST.CHL stop");
    System.out.println("Example: ./MQChannelControl QM1 TEST.CHL status");
    System.out.println("Example: ./MQChannelControl QM1 TEST.CHL create");
    System.out.println("Example: ./MQChannelControl QM1 TEST.CHL delete");
    return false;
  }

 
  public void CreateAgent(int numOfArgs) throws MQDataException {
    try {
    	
      if (numOfArgs == 3) {
    	 System.out.println("Binding mode is being selected");
         client = false;         
         agent = new PCFMessageAgent(queueManager);
      }
      else if(numOfArgs == 5) {
    	  System.out.println("Client mode is being selected");
          client = true;         
          agent = new PCFMessageAgent(host, port, channel_svrconn);
      }
      else{
    	  
    	    System.out.println("The program must be used with the following parameters:-\n");   
    	    System.out.println("How to use Client mode:  ./MQChannelControl.sh [QmanagerName] [hostname] [port] [Channelname] <[start]/[stop]/[status]/[create]/[delete]>");
    	    System.out.println("How to use binding mode: ./MQChannelControl.sh [QmanagerName] [Channelname] <[start]/[stop]/[status]/[create]/[delete]>");    	    
    	    System.out.println("--------Client Mode-------------");
    	    System.out.println("Example: ./MQChannelControl QM1 localhost 1414 TEST.CHL start");
    	    System.out.println("Example: ./MQChannelControl QM1 localhost 1414 TEST.CHL stop");
    	    System.out.println("Example: ./MQChannelControl QM1 localhost 1414 TEST.CHL status");
    	    System.out.println("Example: ./MQChannelControl QM1 localhost 1414 TEST.CHL create");
    	    System.out.println("Example: ./MQChannelControl QM1 localhost 1414 TEST.CHL delete");
    	    System.out.println("--------Binding Mode-------------");
    	    System.out.println("Example: ./MQChannelControl QM1 TEST.CHL start");
    	    System.out.println("Example: ./MQChannelControl QM1 TEST.CHL stop");
    	    System.out.println("Example: ./MQChannelControl QM1 TEST.CHL status");
    	    System.out.println("Example: ./MQChannelControl QM1 TEST.CHL create");
    	    System.out.println("Example: ./MQChannelControl QM1 TEST.CHL delete");
    	    System.exit(0);
      } 
    }
    catch (MQDataException mqde) {
      if (mqde.reasonCode == CMQC.MQRC_Q_MGR_NAME_ERROR) {
        System.out.print("Either could not find the ");
        if (client) {
          System.out.print("default queue manager at \"" + host + "\", port \"" + port + "\"");
        }
        else {
          System.out.print("queue manager \"" + queueManager + "\"");
        }

        System.out.println(" or could not find the Server Conn channel \"" + channel_svrconn
            + "\" on the queue manager.");
      }
      else if (mqde.reasonCode == MQConstants.MQRC_Q_MGR_NAME_ERROR ){
    	  System.out.print("Either could not find the ");
    	  
    	  if (client) {
              System.out.print("default queue manager at \"" + host + "\", port \"" + port + "\"");
            }
            else {
              System.out.print("queue manager \"" + queueManager + "\"");
            }

            System.out.println(" or could not find the Server Conn channel \"" + channel_svrconn
                + "\" on the queue manager.");
      }
      else {
    	  throw mqde; 
      }      
    }
    return;
  }


  public void DestroyAgent() throws MQDataException {    
    agent.disconnect();
    return;
  }


  public void DisplayException(Exception exception) {
    if (exception.getClass().equals(PCFException.class)) {
      PCFException pcfe = (PCFException) exception;

      if (pcfe.reasonCode == MQConstants.MQRCCF_CHANNEL_NOT_FOUND) {
        if (client) {
          System.out.print("Either the queue manager \"" + queueManager + "\"");
        }
        else {
          System.out.print("Either the default queue manager");
        }

        System.out.println(" or the channel \"" + pcfChannel + "\" on the queue manager could not be found.");
      }
      else {
        System.err.println(pcfe + ": " + MQConstants.lookupReasonCode(pcfe.reasonCode));
      }
    }
    else if (exception.getClass().equals(IOException.class)) {
      IOException ioe = (IOException) exception;

      System.err.println(ioe);
    }
    else if (exception.getClass().equals(MQDataException.class)) {
      MQDataException de = (MQDataException) exception;

      System.err.println(de + ": " + MQConstants.lookupReasonCode(de.reasonCode));
    }
    else if (exception.getClass().equals(MQException.class)) {
      MQException mqe = (MQException) exception;

      if (mqe.reasonCode == CMQC.MQRC_UNKNOWN_OBJECT_NAME) {/*
        System.out.println("Cound not find the queue \"" + pcfQueue + "\" on the queue manager \""
            + queueManager + "\".");
      */}
      else {
        System.err.println(mqe + ": " + MQConstants.lookupReasonCode(mqe.reasonCode));
      }
    }
    return;
  }
}
