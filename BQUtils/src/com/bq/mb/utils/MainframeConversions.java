package com.bq.mb.utils;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Hashtable;

public class MainframeConversions {
	
	//Need to default to codepage ISO8859_1/Cp850?
	//set through TRFCodePage system property
	public String CPTYPE;
	public static final int LEFT = 0;
	public static final int RIGHT = 1;
	protected final static String AlignmentBoth = "BOTH";
	protected final static String AlignmentLeft = "LEFT";
	protected final static String AlignmentRight = "RIGHT";
	//Change Markup notes
	//AG01 - Fix reverse binary padding to right not left.
	//RS01 - 21/9/2003 - Added asciiToHex
 	/**
 
Constructor
	 */
	public MainframeConversions(){
		//TCS - Malay: Changed system property from IBCodepage to TRFCodePage for IB Migration project
		String TRFCodePage=System.getProperty("TRFCodePage");
		if(TRFCodePage==null) {
			CPTYPE=System.getProperty("file.encoding");
		}
		else {
			CPTYPE=TRFCodePage;
		}
	}
	
	
	/**Converts a signed numeric ascii string to signed packed EBCDIC string, and removes '-' and '.' characters
	 *	
	 * @param Takes and ASCII coded string containing numeric, '-' and '.' characters
	 * @return Returns packed number without '-' and '.' characters
	 */	
	static public String asciiStringToPackedEbcdicString(String ascii)throws Exception{
		byte[] bytes = MainframeConversions.stringToEBCDIC(ascii);
		bytes = MainframeConversions.ebcdicStringToPackedString(bytes);
		String temp = new String(bytes);
		return temp;
	}
	/**Converts an ASCII String to and EBCDIC byte array.
	 * @param Takes an Ascii string
	 * @return EBCDIC character array
	 */	
	static public byte[] stringToEBCDIC(String s)throws Exception{
		byte[] bytes = s.getBytes();
		bytes=EbcdicAsciiConverter.ASCIIToEBCDIC(bytes);
		return bytes;
		
	}
	
	/**
	 * @param Takes a byte array that can contain the '-' and '.' characters
	 *			which are then removed whilst packing the output byte array
	 * @return Returns a byte array
	 */	
	static public byte[] ebcdicStringToPackedString(byte[] bytes){
		boolean negative = false;
		int removeChars = 0;
		//Check how many chars need removing
		for(int i=0; i<bytes.length; i++){
			if(((bytes[i]==0x60)||(bytes[i]==0x4b))){
				if(bytes[i]==0x60){
					negative = true;
				}
				removeChars++;
			}
		}
		byte[] trimmed = new byte[(bytes.length-removeChars)+1];//add sign byte
		int outputCounter = 0;
		//Remove the chars
		for(int i=0; i<bytes.length; i++){
			if(!((bytes[i]==0x60)||(bytes[i]==0x4b))){
				trimmed[outputCounter] = (byte)((bytes[i])&0x0f);
				outputCounter++;
			}
		}
		if(negative)
			trimmed[outputCounter] = 0x0d;
		else
			trimmed[outputCounter] = 0x0c;
		outputCounter++;
		int remainder = outputCounter % 2;
		int whole = outputCounter / 2;
		byte[] output = new byte[whole+remainder];
		if(remainder>0){
			int nib1 = ((trimmed[0])&0x0f);
			output[0]=(byte)(nib1);
		}
		else{
			int nib2 =((trimmed[0])<<4)&0xf0;
			int nib1 = ((trimmed[1])&0x0f);
			output[0]=(byte)(nib1|nib2);
		}
		for(int i=1; i<output.length; i++){
			int nib2 =((trimmed[(i*2)-remainder])<<4)&0xf0;
			int nib1 = ((trimmed[((i*2)-remainder)+1])&0x0f);
			output[i]=(byte)(nib1|nib2);
		}
		
		return output;
		
	}
	
	
	public String padString(String in, int length, int direction) throws Exception {
		try {
			while(in.length() < length) {
				if(direction==LEFT) {
					in = " "+in;
				}
				else if(direction==RIGHT) {
					in = in + " ";
				}
			}
			return in;
		}
		catch(Exception e) {
			throw e;
		}
	}
	public int hexToInt(String hex) throws Exception {
		Hashtable values = new Hashtable();
		values.put("A","10");
		values.put("B","11");
		values.put("C","12");
		values.put("D","13");
		values.put("E","14");
		values.put("F","15");
		try {
			String str="";
			
			//Reverse the String..
			for(int x=hex.length()-1; x>=0; x--) {
				str = str+hex.charAt(x);
			}
			
			double result=0;
			for(int x=0; x<str.length(); x++) {
				String val = ""+str.charAt(x);
				
				if(values.containsKey(val))
					val = (String)values.get(String.valueOf(str.charAt(x)));
				
				double multiplier = java.lang.Math.pow(16d,Double.parseDouble(String.valueOf(x)));
				result = result + multiplier*Double.parseDouble(val);
			}
			
			str = String.valueOf(result);
			return Integer.parseInt(str.substring(0,str.indexOf(".")));
		}
		catch(Exception e) {
			throw e;
		}
	}
	/**
	 * Convert ASCII to Hex. String "303030" would become "1E 1E 1E"
	 *
	 * @param asciiValue - the string to be converted
	 * @param length - The Length of the required Hex String, will be passed wtih spaces
	 * @return Returns a string containing the Hex Chars
	 * @exception   Throws Exception when an error occurs
	 */
	public String asciiToHex(String asciiValue, int length) throws Exception {
		try {
			char[] chars = asciiValue.toCharArray();
			String returnString = "";
			for(int x=0; x<chars.length; x++) {
				char c = chars[x];
				int val = c;
				String sVal = Integer.toHexString(c);
				returnString = returnString+""+Integer.parseInt(sVal);
			}
			
			int n=0;
			String temp = "";
			//Now Pack the field...
			while(n<returnString.length()) {
				String x = returnString.substring(n,n+2);
				temp = temp+asciiToPacked(x,1);
				n = n + 2;
			}
			
			returnString = temp;
			//System.out.println(returnString);
			while(returnString.length()<length)
				returnString = "0"+returnString;
			
			return returnString;
		}
		catch(Exception e) {
			throw e;
		}
	}
	/*************************
	 *     ASCII_to_EBCDIC
	 **************************/
	
	public String asciiToEBCDIC(String asciiValue) throws Exception {
		try {
			if (asciiValue != null) {
				
				//Convert this String into bytes according to the specified character encoding,
				//storing the result into a new byte array.
				byte[] byteArray = asciiValue.getBytes("Cp500"); //converts characters into bytes
				String postEBCDICvalue = new String(byteArray);
				return postEBCDICvalue;
			}
			else {
				return asciiValue; //don't try to convert a null
			}
		}
		catch (Exception me) {
			throw new Exception("EBCDIC conversion error for ASCII value: " + asciiValue);
		}
		
		
	}//method
	
	public String asciiToEBCDIC(String asciiValue, int length) throws Exception {
		try {
			if (!(asciiValue == null || asciiValue.equals(""))) {
				//if the string length is less than the specified length,
				//pad with spaces so that string is right justified
				while(asciiValue.length()<length) {
					asciiValue=" "+asciiValue;
				}
				
				byte[] byteArray = asciiValue.getBytes("Cp500");
				String postEBCDICvalue = new String(byteArray);
				
				return postEBCDICvalue;
			}
			else {
				String temp = this.EBCDICfiller("00",length);
				return temp;
			}
		}
		catch (Exception me) {
			me.printStackTrace();
			throw new Exception("EBCDIC conversion error for ASCII value: " + asciiValue);
		}
	}//method
	
	/**
	 * Converts ASCII to EBCDIC, but unlike the method above PADs on a specified end
	 * @param asciiValue The Value to convert
	 * @param length The Length to PAD to
	 * @param alignment The Alignment to be used
	 * @return Returns a String containing the EBCDIC value
	 */
	public String asciiToEBCDIC(String asciiValue, int length, String alignment) throws Exception {
		try {
			if (!(asciiValue == null || asciiValue.equals(""))) {
				//if the string length is less than the specified length,
				//pad with spaces so that string is right justified
				if(alignment.equalsIgnoreCase(AlignmentLeft) || alignment.equalsIgnoreCase(AlignmentBoth)) {
					while(asciiValue.length()<length) {
						asciiValue=asciiValue+" ";
					}
				}
				else if(alignment.equalsIgnoreCase(AlignmentRight)) {
					while(asciiValue.length()<length) {
						asciiValue=" "+asciiValue;
					}
				}
				
				byte[] byteArray = asciiValue.getBytes("Cp500");
				String postEBCDICvalue = new String(byteArray);
				
				return postEBCDICvalue;
			}
			else {
				String temp = this.EBCDICfiller("00",length);
				return temp;
			}
		}
		catch (Exception me) {
			throw new Exception("EBCDIC conversion error for ASCII value: " + asciiValue);
		}
	}
	
	/*************************
	 *     EBCDIC_to_ASCII
	 **************************/
	public String EBCDICBytes_to_ASCII(byte[] bEBCDICbyte) throws Exception {
		try {
			String sASCIIvalue = new String( bEBCDICbyte, "Cp500" );
			return sASCIIvalue;
		}
		catch(Exception e) {
			throw new Exception("Error Converting EBCDIC Bytes: "+e.toString());
		}
	}
	public String EBCDIC_to_ASCII(String sEBCDICvalue) throws Exception {
		
		// convert EBCDIC to ASCII using the same Cp500
		try{
			if( sEBCDICvalue != null ){
				
				byte[] bEBCDICbyte = sEBCDICvalue.getBytes();
				//Construct a new String by converting the specified array
				//of bytes using the specified character encoding.
				String sASCIIvalue = new String( bEBCDICbyte, "Cp500" );
				
				return sASCIIvalue;
			}
			else{
				return sEBCDICvalue; //don't attempt to convert a null
			}
		}
		catch (Exception me) {
			throw new Exception("ASCII conversion error for EBCDIC value: " + sEBCDICvalue);
		}
		
	}
	
	
	//-----------------------------end of EBCDIC conversion---------------------------
	
	
	/*******************************
	 *  Integers to Pack Decimals
	 ********************************/
	/**
	 * Creates a signed packed field
	 */
	public String asciiToPacked(String sUnPacked, int length, boolean signed) throws Exception {
		try{
			if ( sUnPacked != null ) {//if 1
				if(sUnPacked.indexOf(".")!=-1) {
					sUnPacked = sUnPacked.substring(0,sUnPacked.indexOf(".")) + sUnPacked.substring(sUnPacked.indexOf('.')+1,sUnPacked.length());
				}
				sUnPacked = sUnPacked.trim(); //remove whitespace
				
				if(signed) {
					// if rightmost character is a '-' then the number is negative
					if (sUnPacked.endsWith("-")) {
						sUnPacked = sUnPacked.substring(0,sUnPacked.length()-1); //remove the "-"
						sUnPacked = sUnPacked + "D"; //append hex negative sign
					}
					// if leftmost character is a '-' then the number is negative
					else if (sUnPacked.startsWith("-")) {
						sUnPacked = sUnPacked.substring(1,sUnPacked.length()); //remove the "-"
						sUnPacked = sUnPacked + "D"; //append hex negative sign
					}
					else if (sUnPacked.startsWith("+")) {
						sUnPacked = sUnPacked.substring(1,sUnPacked.length()); //remove the "+"
						sUnPacked = sUnPacked + "C"; //append hex negative sign
					}
					else if (sUnPacked.endsWith("+")) {
						sUnPacked = sUnPacked.substring(0,sUnPacked.length()-1); //remove the "+"
						sUnPacked = sUnPacked + "C"; //append hex negative sign
					}
					else {
						sUnPacked = sUnPacked + "C";    //append hex positive sign
					}
				}
				
				// if uneven number of chars, append a zero in front.
				if(sUnPacked.length() % 2 != 0) {
					sUnPacked = "0" + sUnPacked;
				}
				
				String sPacked = new String();
				char[] hexValue = new char[2];
				for(int i = sUnPacked.length()-2; i >= 0; i--) { //for 1
					if((i%2) == 0) {
						//even index element, proceed  //if 2
						hexValue[0] = sUnPacked.charAt(i);
						hexValue[1] = sUnPacked.charAt(i+1);
						String hexString = new String(hexValue);
						byte value[] = new byte[hexString.length() / 2];
						
						for (int j = 0; j < value.length; j++) {
							//the bytes are packed below
							value[j] = (byte) Integer.parseInt(hexString.substring(2 * j, 2 * j + 2), 16);
						}
						
						//Construct a new String by converting the specified
						//array of bytes using the specified character encoding. For mainframe conversion use 'ISO8859_1'
						String asciiValue = new String(value,CPTYPE);
						sPacked = asciiValue + sPacked;
						
					}//if 2
				}//for 1
				
				byte[] b = new byte[1];
				while(sPacked.length()<length) {
					b[0] = (byte)Integer.parseInt("0");
					String s = new String(b, CPTYPE);
					sPacked = s + sPacked;
				}
				return sPacked;
			}//if 1
			else {
				String temp = this.asciiToPacked("0",length,true);
				return temp;
				//return sUnPacked; // don't attempt to pack null field
			}
		}//try
		catch (Exception me) {
			me.printStackTrace();
			throw new Exception("Packed Decimal Error....Error is "+me);
		}
	}
	
	/**
	 * asciiToPacked takes an unpacked string and converts it to an unsigned packed field
	 */
	public String asciiToPacked(String sUnPacked, int length) throws Exception {
		//Converts an ASCII numeric string to a packed EBCDIC string
		try{
			if ( sUnPacked != null ) {//if 1
				if(sUnPacked.indexOf(".")!=-1) {
					sUnPacked = sUnPacked.substring(0,sUnPacked.indexOf(".")) + sUnPacked.substring(sUnPacked.indexOf('.')+1,sUnPacked.length());
				}
				sUnPacked = sUnPacked.trim(); //remove whitespace
				
				// if uneven number of chars, append a zero in front.
				if(sUnPacked.length() % 2 != 0) {
					sUnPacked = "0" + sUnPacked;
				}
				
				String sPacked = new String();
				char[] hexValue = new char[2];
				for(int i = sUnPacked.length()-2; i >= 0; i--) { //for 1
					if((i%2) == 0) {
						//even index element, proceed  //if 2
						hexValue[0] = sUnPacked.charAt(i);
						hexValue[1] = sUnPacked.charAt(i+1);
						String hexString = new String(hexValue);
						byte value[] = new byte[hexString.length() / 2];
						for (int j = 0; j < value.length; j++) {
							//the bytes are packed below
							value[j] = (byte) Integer.parseInt(hexString.substring(2 * j, 2 * j + 2), 16);
						}
						//Construct a new String by converting the specified
						//array of bytes using the specified character encoding.
						String asciiValue = new String(value, CPTYPE);
						sPacked = asciiValue + sPacked;
					}//if 2
				}//for 1
				
				byte[] b = new byte[1];
				while(sPacked.length()<length) {
					b[0] = (byte)Integer.parseInt("0");
					String s = new String(b, CPTYPE);
					sPacked = s + sPacked;
				}
				return sPacked;
			}//if 1
			else {
				String temp = this.asciiToPacked("0",length,true);
				return temp;
				//return sUnPacked; // don't attempt to pack null field
			}
		}//try
		catch (Exception me) {
			throw new Exception("Packed Decimal Error....Error is "+me);
		}
		
		
	}//method
	
	
	/*************************
	 *         UnPack
	 **************************/
	// convert packed decimals (comp-3) to integers
	public String convertPackedBytesToAscii(byte[] bytes) {
		String temp="";
		for(int i=0; i<bytes.length; i++) {
			int nib2 = ((bytes[i]&0xf0)>>4) + 0x30;
			int nib1 = (bytes[i]&0x0f) + 0x30;
			
			if(i==(bytes.length-1)) {
				temp = temp+(char)nib2;
				if(nib1==0x3D) {
					temp = "-"+temp;
				}
				else if(nib1!=0x3C) {
					temp = temp+(char)nib1;
				}
			}
			else {
				//Removed By Ross Stanton, 9/12/2003.
				//            if(nib1!='<')
				temp = temp +(char)nib2+(char)nib1;
			}
		}
		return temp;
	}
	public String convertPackedToAscii(String Packed) {
		String temp="";
		byte[] bytes = Packed.getBytes();
		
		for(int i=0; i<bytes.length; i++) {
			int nib2 = ((bytes[i]&0xf0)>>4) + 0x30;
			int nib1 = (bytes[i]&0x0f) + 0x30;
			
			if(i==(bytes.length-1)) {
				temp = temp+(char)nib2;
				if(nib1==0x3D) {
					temp = "-"+temp;
				}
				else if(nib1!=0x3C) {
					temp = temp+(char)nib1;
				}
			}
			else {
				//Ross Stanton, 9/12/2003 - Removed Check
				//            if(nib1!='<')
				temp = temp +(char)nib2+(char)nib1;
			}
		}
		return temp;
	}
	
	public String UnPack(String sPacked) throws Exception {
		try{
			if (sPacked != null) {
				byte[] b = sPacked.getBytes(); // First, create a byte array
				int i0 = (int) b[ b.length - 1 ] >>> 4; //shift right 4 to get high order digit
				int i1 = (int) b[ b.length - 1 ] & ( 15 ); //  AND with hex 0F to get low order sign bit
				boolean plus = ( i1 == 12 ) | ( i1 == 15 ); // determine sign digit
				int answer = i0;
				int digit = 10; // Power of 10 - each digit's  place value
				
				for ( int i = b.length - 2; i > -1; i-- ) {
					byte x = b[ i ];
					i0 = (int) x >>> 4; // shift right 4 to get high // order digit
					i1 = (int) x & ( 15 ); // AND with X'0F' to get low // order digit
					answer += i1 * digit;
					digit *= 10;
					answer += i0 * digit;
					digit *= 10;
				}
				
				answer = (plus ? answer : -1 * answer); //if negative, multiply by -1
				
				String sUnPacked = null;
				sUnPacked = sUnPacked.valueOf(answer);
				return sUnPacked;
				
			}
			else{
				return sPacked; //don't attempt to unpack a null
			}
			
		}
		catch (Exception me) {
			throw new Exception("Error unpacking the value: " + sPacked);
		}
		
	}// UnPack
	
	
	
	//-------------------------end of PACKED conversions-------------------
	
	/*******************************
	 * ASCII to MainFrame Binary
	 ********************************/
	public String asciiToBinary(String value, int length) throws Exception {
		try {
			if (value != null) {
				String sPlay = new String();
				value = value.trim(); //remove whitespace
				//if rightmost character is a '-' then the number is negative
				if (value.endsWith("-")) {
					value = value.substring(0,value.length()-1); //remove the "-"
					
					//-->value = value + "D"; //append hex negative sign
				}
				else{
					//-->value = value + "C";    //append hex positive sign
				}
				
				//if a decimal exists, remove it
				int decIndex=value.indexOf(".");
				if(decIndex!=-1) {
					if(decIndex<value.length()){
						value=value.substring(0,decIndex)+value.substring(decIndex+1);
					}
					else {
						value=value.substring(0,decIndex);
					}
				}
				
				sPlay = value;
				int iNumber;
				//convert string to int
				iNumber = Integer.parseInt(sPlay);
				//String sResult=new String(new byte[1],CPTYPE);
				String sResult = Integer.toHexString(iNumber);
				if(sResult.length() % 2 != 0) {
					sResult = "0" + sResult;
				}
				
				String sResultEBCDIC=new String(new byte[length],CPTYPE);
				sResultEBCDIC = this.hexToEBCDIC(sResult);
				
				byte[] b = new byte[1];
				while(sResultEBCDIC.length()<length) {
					b[0] = (byte)Integer.parseInt("0");
					String s = new String(b, CPTYPE);
					sResultEBCDIC = s + sResultEBCDIC;
				}
				
				if(sResultEBCDIC.length()>length) {
					sResultEBCDIC = sResultEBCDIC.substring(sResultEBCDIC.length()-length,sResultEBCDIC.length());
				}
				
				return sResultEBCDIC;
				
			}
			else {
				return value; //don't try to convert a null
			}
		}
		catch (Exception me) {
			throw new Exception("Binary conversion error for ASCII value: " + value+"\n"+me.toString());
		}
		
	}//method
	
	
	
	/*************************************
	 * ASCII to MainFrame ReverseBinary
	 **************************************/
	
	
	public String asciiToReverseBinary(String value, int length) throws Exception {
		//Converts an ASCII numeric string to a Reverse Binary EBCDIC string with the bytes in reverse order
		//Converts an ASCII numeric string to a binary EBCDIC string
		try {
			if (value != null) {
				//AG01 - remove next two lines - what does it do?
				//StringBuffer stringbuffer = new StringBuffer(value);
				//int i = stringbuffer.length();
				
				String sPlay = new String();
				value = value.trim(); //remove whitespace
				//if rightmost character is a '-' then the number is negative
				if (value.endsWith("-")) {
					value = value.substring(0,value.length()-1); //remove the "-"
					//-->value = value + "D"; //append hex negative sign
				}
				else {
					//-->value = value + "C";    //append hex positive sign
				}
				
				//if a decimal exists, remove it
				int decIndex=value.indexOf(".");
				if(decIndex!=-1) {
					if(decIndex<value.length()) {
						value=value.substring(0,decIndex)+value.substring(decIndex+1);
					}
					else {
						value=value.substring(0,decIndex);
					}
				}
				
				//OK, now we have the decimal removed, convert to binary
				sPlay = value;
				int iNumber;
				//convert string to int
				iNumber = Integer.parseInt(sPlay);
				
				//Now convert the integer to Hex
				String sResult = Integer.toHexString(iNumber);
				
				//If the resulting string is not an even length, then prepend '0'
				if(sResult.length() % 2 != 0) {
					sResult = "0" + sResult;
				}
				
				//Now we have the hex value, convert to EBCDIC bytes
				sResult = this.hexToEBCDIC(sResult);
				//Now we have the result in EBCDIC, pad to end with zeros
				
				StringBuffer resultbuffer = new StringBuffer(sResult);
				String returnString = resultbuffer.reverse().toString();
				
				byte[] b = new byte[1];
				while(returnString.length()<length) {
					b[0] = (byte)Integer.parseInt("0");
					String s = new String(b, CPTYPE);
					//AG01 - Previously used to pad from left, this is reverse binary, pad from right
					//AG01d returnString = s +returnString;
					returnString = returnString + s; //AG01a
				}
				return returnString;
			}
			else {
				return asciiToReverseBinary("0",length);
				//return value; //don't try to convert a null
			}
		}
		catch (Exception me) {
			throw new Exception("ReverseBinary conversion error for ASCII value: " + value);
		}
		
	}//method
	
	
	//------------------------end of BINARY conversions----------------------
	
	
/*public String hexToEBCDIC(String value) throws Exception
{
	//Converts an ASCII hex string to an EBCDIC string
 
	StringBuffer out=new StringBuffer("");
	if (value.length()%2==1) throw new NumberFormatException();
	char [] valueArray=value.toCharArray();
	for (int x=0;x<value.length();x+=2)
	{
	  char c1=(char)((hexDigit(valueArray[x]))<<4);
	  char c2=(char)hexDigit(valueArray[x+1]);
	  out.append(c1+c2);
	}
	return out.toString();
}//method
 */
	
	public String hexToEBCDIC(String value) throws Exception {
		if(value!=null) {
			/**
			 * //Converts an ASCII hex string to an EBCDIC string
			 * StringBuffer out=new StringBuffer("");
			 * if (value.length()%2==1) throw new NumberFormatException();
			 * for (int x=0;x<value.length();x+=2)
			 * {
			 * String c1=(hexToChar(value.substring(x,x+2)));
			 * out.append(c1);
			 * }
			 * return out.toString();
			 */
			//Converts an ASCII hex string to an EBCDIC string
			byte [] bytes= new byte[value.length()/2];
			int i=0;
			if (value.length()%2==1) throw new NumberFormatException();
			for (int x=0;x<value.length();x+=2) {
				bytes[i]=hexToCharByte(value.substring(x,x+2));
				i++;
			}
			String out = new String(bytes, CPTYPE);
			
			//return resultString;
			return out;
		}
		else {
			throw new Exception("ERROR: Cannot do a HEX conversion for a NULL value="+value);
		}
	}//method
	
	
	public String hexToEBCDIC(String value, int length) throws Exception {
		//Converts an ASCII hex string to an EBCDIC string
		String out = new String(new byte[0],CPTYPE);
		//StringBuffer out=new StringBuffer("");
		if (value.length()%2==1) throw new NumberFormatException();
		for (int x=0;x<value.length();x+=2) {
			out=out+hexToChar(value.substring(x,x+2));
		}
		//String resultString=out.toString();
		
		//return resultString;
		return out;
	}//method
	
	
	public String EBCDICfiller(int length) throws Exception {
		//Returns a string of length occurrences of filler.
		//If filler is not specified, 00h will be used.
		return null;
	}//method
	
	
	public String EBCDICfiller(String sFiller, int ilength) throws Exception {
		//Returns a string of length occurrences of filler.
		//If filler is not specified, 00h will be used.
		try{
			
			StringBuffer sFill = new StringBuffer(ilength);
			if (sFiller.length()%2==1) throw new NumberFormatException();
			if(sFiller !=null || sFiller.equals("CxIgnore")|| sFiller.equals("CxBlank")) {
				String cFill =hexToChar(sFiller);
				for(int i=0; i<ilength;i++) {
					//append it
					sFill.append(cFill);
				}
			} else {
				for(int i=0; i<ilength;i++) {
					String cFill =hexToChar("FF");
					//append
					sFill.append(cFill);
				}
			}
			return sFill.toString();
		}
		catch (Exception me) {
			throw new Exception("Filler conversion error for HEX value: " + sFiller+"\nError:"+me);
		}
	}//method
	
	
	//ascii lookup
	private String asciiLookUp(char c) {
		char c1 = c;
		String s = Integer.toBinaryString(c1);
		if(c1 < '\200')
			s = "0" + s;
		if(c1 < '@')
			s = "0" + s;
		if(c1 < ' ')
			s = "0" + s;
		if(c1 < '\020')
			s = "0" + s;
		if(c1 < '\b')
			s = "0" + s;
		if(c1 < '\004')
			s = "0" + s;
		if(c1 < '\002')
			s = "0" + s;
		if(c1 < '\001')
			s = "0" + s;
		return s;
	}
	
	private int hexDigit(char c1) throws NumberFormatException {
		if (c1>='0' && c1<='9') return (c1-'0');
		else if (c1>='a' && c1<='f') return (c1-'a');
		else if (c1>='A' && c1<='F') return (c1-'A');
		else throw new NumberFormatException();
	}
	
	private char hexDigitC(char c1) throws NumberFormatException {
		if (c1>='0' && c1<='9') return (char)(c1-'0');
		else if (c1>='a' && c1<='f') return (char)(c1-'a');
		else if (c1>='A' && c1<='F') return (char)(c1-'A');
		else throw new NumberFormatException();
	}
	
	//use following to convert hex to chars in a set of 2
	public String hexToChar(String str) throws Exception {
		try{
			//char dude = (char) Integer.parseInt(str,16);
			byte [] b = new byte[1];
			b[0] = (byte)Integer.parseInt(str,16);
			String s = new String(b, CPTYPE);
			return s;
		}
		catch(Exception e){
			throw new Exception("@@Problem converting Hex!"+e.toString());
		}
	}
	
	//use following to convert hex to chars in a set of 2
	public byte hexToCharByte(String str) throws Exception {
		try{
			//char dude = (char) Integer.parseInt(str,16);
			byte b = (byte)Integer.parseInt(str,16);
			return b;
			
		}
		catch(Exception e){
			throw new Exception("@@Problem converting Hex!"+e.toString());
		}
	
	}
	
}