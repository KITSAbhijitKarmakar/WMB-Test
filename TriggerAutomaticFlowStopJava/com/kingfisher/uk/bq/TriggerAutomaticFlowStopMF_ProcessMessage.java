package com.kingfisher.uk.bq;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.logging.Logger;


public class TriggerAutomaticFlowStopMF_ProcessMessage {

	public static Long CallSTEPSubscriptionScript()  {
		Long exitVal=(long) 0;
		Logger LOGGER = Logger.getLogger("InfoLogging");
		File file = new File("/var/mqm/scripts/WMB/stopProductSubscriptionSTEP.sh");

		ArrayList<String> AppArgList = new ArrayList<String>();
		AppArgList.add("/var/mqm/scripts/WMB/stopProductSubscriptionSTEP.sh");
		//AppArgList.add("java");
		//AppArgList.add("-version");
		String[] arguments = AppArgList.toArray(new String[AppArgList.size()]);

		if (file.exists() && file.canExecute()) {
			Process proc = null;
			Runtime rt = null;
			
			try {
				FileOutputStream fos = new FileOutputStream("/support/home/wmbadmin/STEP/op.log");
				rt = Runtime.getRuntime();
				//rt.exec(arguments[0]);
				proc = rt.exec(arguments[0]);
				////////////////////////////////
				InputStream stderr = proc.getErrorStream();
				InputStreamReader isr = new InputStreamReader(stderr);
				BufferedReader br = new BufferedReader(isr);
				String line = null;
				/*LOGGER.info("=======<ERROR>=========");
				
				while ( (line = br.readLine()) != null)
					LOGGER.info(line);
				LOGGER.info("=======</ERROR>=========");*/
				//////////////////////////////
				StreamGobbler errorGobbler = new 
				StreamGobbler(proc.getErrorStream(), "ERROR");            
				StreamGobbler outputGobbler = new 
				StreamGobbler(proc.getInputStream(), "OUTPUT", fos);
				// kick them off
				errorGobbler.start();
				outputGobbler.start();
				
				exitVal=(long) proc.waitFor();	
				LOGGER.info("Process exitValue: " + exitVal);
				fos.flush();
	            fos.close();
			} catch (IOException e) {
				exitVal=(long) 1;
				return exitVal;
				//throw new MbUserException(e.getClass(), "CallAppScript () -- IOException while getting the runtime", "", "","", new Object[] { e.getMessage() });
			}
			catch (InterruptedException e) {
				exitVal=(long) 2;
				return exitVal;
				//throw new MbUserException(e.getClass(), "CallAppScript () -- InterruptedException while executing shell", "", "","", new Object[] { e.getMessage() });
			}
			catch (Exception e) {
				exitVal=(long) 3;
				return exitVal;
				//throw new MbUserException(e.getClass(), "CallAppScript () -- GenericException while executing shell", "", "","", new Object[] { e.getMessage() });
			}
			return exitVal;
		}
		else 
		 return exitVal;

	}

}
