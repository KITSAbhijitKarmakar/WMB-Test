package com.kingfisher.kits.mb.crypto.httpssl;
import java.io.*;
import java.net.*;
import javax.net.ssl.*;

/**
 * Quick And Dirty HTTPS class!
 * @author stantr01
 *
 */
public class SSLConnection{
	
	private static final String KEYSTOREPATH="PCI-SSL-TrustStorePath";
	
	public void sendKey(String url, String key, String keyRef, String startDate, String endDate) throws Exception{
		try{
				doSSLConnection(url+"?key="+key+"&keyReference="+keyRef+"&startDate="+startDate+"&endDate="+endDate);
				System.out.print(url+"?key="+key+"&keyReference="+keyRef+"&startDate="+startDate+"&endDate="+endDate);
		}
		catch(Exception e){
			throw e;
		}
	}
	/**
	 * Provides HTTPS GET functionality.
	 * 
	 * @param url
	 * @param key
	 * @param keyRef
	 * @param keyStoreFileName
	 * @throws Exception
	 */
	//public void doSSLConnection(String url, String key, String keyRef, String keyStoreFileName) throws Exception{
	private void doSSLConnection(String url) throws Exception{
		HttpsURLConnection.setDefaultHostnameVerifier(new KITSHostNameVerifier());
		System.out.println(HttpsURLConnection.getDefaultSSLSocketFactory().getClass().getName());

		URL webUrl = new URL(url);
		
		HttpsURLConnection conn = (HttpsURLConnection)webUrl.openConnection();
		conn.setDoOutput(true);
		conn.setDoInput(true);

		InputStream is = conn.getInputStream();
		is.close();
		is = null;
		
	    conn.disconnect();
	}
}

/**
 * Inner class that provides HostnameVerifier functionality.
 * @author stantr01
 *
 */
class KITSHostNameVerifier implements HostnameVerifier{

	public boolean verify(String hostname, SSLSession session) {
		System.out.println("Verifying Host: "+hostname);
		return true;
	}
}
