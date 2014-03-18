package com.kingfisher.ukbq.Communication.utils;
/*
 * * Version      Date	   		   Author				Description                    							    				*
	* ======= 	=========== 	=========== 	================================	
	*  1.00  	31-May-2013   Abhijit Karmakar	Incident#INC0049749: Design change to handle error scenerio with Orange Service    
	************************************************************************************
 */
import java.util.regex.*;
public class ValidatePhone {	
	
	
	public static String getFormattedPhoneNum(String deviceMsidsn){
		
			
			String number = deviceMsidsn;
			
			
			if (number.startsWith("0")) {
				number = "+44" + number.substring(1);
			}
			if (number.startsWith("+")) {
				//do nothing
			}
			else {
				number = "+44" + number;
			}
			
			StringBuilder sbText = new StringBuilder(number);
			int spaceIndexNum = sbText.indexOf(" ");
			int tabIndexNum = sbText.indexOf("\t");
			
			//Remove the white spaces in the string
			if (spaceIndexNum != -1) {
				number=number.replaceAll("\\s","");
			}
			if(tabIndexNum != -1){
				number=number.replaceAll("\\s","");
			}
			
			//Validate Format of Phone number in UK standard
			String expression = "(\\+)44(7)\\d{9}";
			CharSequence inputStr = number;			
			Pattern pattern = Pattern.compile(expression);
			Matcher matcher = pattern.matcher(inputStr);		
		    
		   
		    if(!matcher.matches()){
		    	return "Invalid MSISDN";
		    	
		    } else
		    	return number; //valid number
	}

	
}
