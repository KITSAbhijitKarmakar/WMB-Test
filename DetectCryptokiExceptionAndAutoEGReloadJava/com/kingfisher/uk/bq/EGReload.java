package com.kingfisher.uk.bq;
/*
 * * Version      Date	   		   Author				Description                    							    				*
	* ======= 	=========== 	=========== 	================================	
	*  1.00  	16-Feb-2014   Abhijit Karmakar	Initial Design to reload execution group    
	************************************************************************************
 */

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Logger;

public class EGReload {
	
	public static Long EGReloadbyName(String EGName)  {
		Long exitVal=(long) 0;
		Logger LOGGER = Logger.getLogger("InfoLogging");
		File file = new File("/support/home/wmbadmin/test/reloadEG.sh");

		ArrayList<String> AppArgList = new ArrayList<String>();
		AppArgList.add("/support/home/wmbadmin/test/reloadEG.sh");
		AppArgList.add(EGName); 
		
		String[] arguments = AppArgList.toArray(new String[AppArgList.size()]);

		if (file.exists() && file.canExecute()) {
			Process proc = null;
			Runtime rt = null;
			Date currDate = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("ddmmyyyy-HH:mma");
			
			try {
				FileOutputStream fos = new FileOutputStream("/support/home/wmbadmin/test/KITS_ReloadEGStatus_"+sdf.format(currDate)+".log");
				rt = Runtime.getRuntime();
				rt.exec(arguments[0]);
				proc = rt.exec(arguments);
				
				StreamGobbler errorGobbler = new 
				StreamGobbler(proc.getErrorStream(), "ERROR");            
				StreamGobbler outputGobbler = new 
				StreamGobbler(proc.getInputStream(), "OUTPUT", fos);
				
				errorGobbler.start();
				outputGobbler.start();
				
				exitVal=(long) proc.waitFor();	
				LOGGER.info("Process exitValue: " + exitVal);
				
				fos.flush();
	            fos.close();
			} catch (IOException e) {
				exitVal=(long) 1;
				return exitVal;
			}
			catch (InterruptedException e) {
				exitVal=(long) 2;
				return exitVal;
			}
			catch (Exception e) {
				exitVal=(long) 3;
				return exitVal;
			}
			return exitVal;
		}
		else 
		 return exitVal;

	}
}
