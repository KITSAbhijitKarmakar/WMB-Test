package com.kits.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class FixDPMI {

	private static String HOME_DIR_LOCATION="C://DATA//DPMI//";
	private static String SOURCE_DIR="source";
	private static String DUSTBIN_DIR="dustin";
	private static String FINAL_DIR="final";
	private static String DUPLICATE_DIR="duplicate";
	
	private static boolean isDuplicateOrdersPresent;
	
	private static ArrayList<String> orderList;
	private static ArrayList<File> duplicateFileList;
	
	
	private static String ORDER_LIST_FILE_NAME="orders.txt";
	private static String DUPLICATE_FILE_LIST_FILE_NAME="duplicateFiles.txt";
	private static String UNATTENDED_FILE_LIST_FILE_NAME="unattened.txt";
	private static String FAULT_FILE_NAME="fault.txt";
	
	private static ArrayList<String> faultStringList;
	private static ArrayList<String> correctStringList;
	private static ArrayList<String> reportStringList;
	private static ArrayList<String> fileWithKnownFaultStringList;
	private static ArrayList<String> allFileList;
	private static ArrayList<File> filesModified;
	
	public static void main(String[] args) {
		
		loadFaultAndCorrectList();
		recordAllOrderNumber();
		listDuplicateFiles();
		
		if(isDuplicateOrdersPresent){
			removeDuplicateFiles();
			recordAllOrderNumber();
		}
		
		reportFailureCategory();
		reportUnattendedFiles();
		
		replaceFaultString();
		
		//removeReplacedFiles();
	}
	
	private static void removeReplacedFiles(){
		try{
			for(File file : filesModified){
				if(!file.renameTo(new File(HOME_DIR_LOCATION+"//"+DUPLICATE_DIR+"//" + file.getName()))){
					System.out.println(file.getName() + " is not moved to dustbin");
				}
				
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	private static void replaceFaultString(){
		try{
			if(null != filesModified && filesModified.size() > 0){
				for(File file : filesModified){
					BufferedReader br1 = null;
					FileInputStream fis1 = null;
					String strLine1 = null;
					StringBuilder bld = new StringBuilder();
					fis1 = new FileInputStream(file);
					br1 = new BufferedReader(new InputStreamReader(fis1));
					String tempStr = null;
					String strLine = null;
					boolean isDone = false;
					while ((strLine = br1.readLine()) != null){
						for(String fault : faultStringList){
							if(strLine.contains(fault)){
								tempStr = strLine.replaceAll(fault, correctStringList.get(faultStringList.indexOf(fault)));
								bld.append(tempStr);
								isDone = true;
							}
						}
						if(!isDone){
							bld.append(strLine);
						}
					}
					
					FileWriter fw = new FileWriter(new File(HOME_DIR_LOCATION+"//" +FINAL_DIR+"//" +file.getName()));
					BufferedWriter bw = new BufferedWriter(fw);
					bw.write(bld.toString());
					bw.close();
					
					//System.out.println(file.delete());
				}
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	private static void reportUnattendedFiles(){
 		try{
 			if(null != fileWithKnownFaultStringList && fileWithKnownFaultStringList.size() > 0){
 				allFileList.removeAll(fileWithKnownFaultStringList);
 				if(allFileList.size() > 0){
 					StringBuilder tmp = new StringBuilder();
 					for(String fileName : allFileList){
 						tmp.append(fileName);
 						tmp.append("\n");
 					}
 					if(allFileList.size() > 0){				
 						FileWriter fw = new FileWriter(new File(HOME_DIR_LOCATION+UNATTENDED_FILE_LIST_FILE_NAME));
 						BufferedWriter bw = new BufferedWriter(fw);
 						bw.write(tmp.toString());
 						bw.close();
 					}
 				}
 			}else{
 				System.out.println("No unattended file present");
 			}
			
 		}catch(Exception e){
 			e.printStackTrace();
 		}
	}
	
	private static void reportFailureCategory(){
		File directory = new File(HOME_DIR_LOCATION+SOURCE_DIR);
		ArrayList<String> uniqueOrderList = new ArrayList<String>();
		duplicateFileList = new ArrayList<File>();
		int currentIndex ;
		StringBuilder faultRecords = new StringBuilder();
		allFileList = new ArrayList<String>();
		fileWithKnownFaultStringList = new ArrayList<String>();
		filesModified = new ArrayList<File>();
 		try{
			if(directory.exists()){
				if(directory.isDirectory()){
					BufferedReader br = null;
					FileInputStream fis = null;
					File[] fList = directory.listFiles();
					String strLine = null;
					for (int i=0 ; i < fList.length; i++){
						allFileList.add(fList[i].getName());
						fis = new FileInputStream(fList[i]);
						br = new BufferedReader(new InputStreamReader(fis));
						while ((strLine = br.readLine()) != null){
							for(String fault : faultStringList){
								if(strLine.contains(fault)){
									faultRecords.append(fList[i].getName() + " : " + reportStringList.get(faultStringList.indexOf(fault)));
									faultRecords.append("\n");
									fileWithKnownFaultStringList.add(fList[i].getName());
									filesModified.add(fList[i]);
								}
							}
						}
						fis.close();
						br.close();
					}
				}
				
				
				if(faultRecords.length() > 0){				
					FileWriter fw = new FileWriter(new File(HOME_DIR_LOCATION+FAULT_FILE_NAME));
					BufferedWriter bw = new BufferedWriter(fw);
					bw.write(faultRecords.toString());
					bw.close();
				}
				
			}else{
				System.out.println("---------Directory does not exists");
			}
			
		}catch(Exception e){
			System.out.println("Error while preparing list");
			e.printStackTrace();
		}
	}
	
	private static void removeDuplicateFiles(){
		StringBuilder tmp = new StringBuilder();
		try{
			for(File file : duplicateFileList){
				tmp.append(file.getName());
				tmp.append("\n");
			}
			if(duplicateFileList.size() > 0){				
				FileWriter fw = new FileWriter(new File(HOME_DIR_LOCATION+DUPLICATE_FILE_LIST_FILE_NAME));
				BufferedWriter bw = new BufferedWriter(fw);
				bw.write(tmp.toString());
				bw.close();
			}
			
			for(File duplicateFile : duplicateFileList){
				if(!duplicateFile.renameTo(new File(HOME_DIR_LOCATION +"//"+ DUPLICATE_DIR +"//"+ duplicateFile.getName()))){
					System.out.println("File moving failed for : " + duplicateFile.getName());
				}
				
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	
	private static void listDuplicateFiles(){
		File directory = new File(HOME_DIR_LOCATION+SOURCE_DIR);
		String orderString = "<PurchaseOrderHeader><oa:DocumentID><oa:ID>";
		ArrayList<String> uniqueOrderList = new ArrayList<String>();
		duplicateFileList = new ArrayList<File>();
 		try{
			if(directory.exists()){
				if(directory.isDirectory()){
					BufferedReader br = null;
					FileInputStream fis = null;
					String strLine = null;
					File[] fList = directory.listFiles();  
					String orderNumber = null;
					for (int i=0 ; i < fList.length; i++){
						fis = new FileInputStream(fList[i]);
						br = new BufferedReader(new InputStreamReader(fis));
						while ((strLine = br.readLine()) != null){
							if(strLine.contains(orderString)){
								orderNumber = strLine.substring(strLine.indexOf(orderString)+43,strLine.indexOf(orderString)+51);
								if(uniqueOrderList.contains(orderNumber)){
									duplicateFileList.add(fList[i]);
								}else{
									uniqueOrderList.add(orderNumber);
								}
							}
						}
						fis.close();
						br.close();
					}
				}
				if(orderList.size() > uniqueOrderList.size()){
					isDuplicateOrdersPresent = true;
					System.out.println("There are : " + String.valueOf(orderList.size() - uniqueOrderList.size()) + " : extra orders");
				}else{
					System.out.println("No duplicate orders");
				}
			}else{
				System.out.println("---------Directory does not exists");
			}
			
		}catch(Exception e){
			System.out.println("Error while preparing list");
			e.printStackTrace();
		}
	}
	
	private static void recordAllOrderNumber(){
		File directory = new File(HOME_DIR_LOCATION+SOURCE_DIR);
		StringBuilder orderBld = new StringBuilder();
		String orderString = "<PurchaseOrderHeader><oa:DocumentID><oa:ID>";
		orderList = new ArrayList<String>();
 		try{
			if(directory.exists()){
				if(directory.isDirectory()){
					BufferedReader br = null;
					FileInputStream fis = null;
					String strLine = null;
					File[] fList = directory.listFiles();  
					String orderNumber = null;
					for (int i=0 ; i < fList.length; i++){
						fis = new FileInputStream(fList[i]);
						br = new BufferedReader(new InputStreamReader(fis));
						while ((strLine = br.readLine()) != null){
							if(strLine.contains(orderString)){
								orderNumber = strLine.substring(strLine.indexOf(orderString)+43,strLine.indexOf(orderString)+51);
								orderList.add(orderNumber);
								orderBld.append(orderNumber);
								orderBld.append("\n");
							}
						}
						fis.close();
						br.close();
					}
				}
			}else{
				System.out.println("---------Directory does not exists");
			}
			if(orderList.size() > 0){
				System.out.println("Total Order Count  : " + orderList.size());
				
				FileWriter fw = new FileWriter(new File(HOME_DIR_LOCATION+ORDER_LIST_FILE_NAME));
				BufferedWriter bw = new BufferedWriter(fw);
				bw.write(orderBld.toString());
				bw.close();
			}
		}catch(Exception e){
			System.out.println("Error while preparing list");
			e.printStackTrace();
		}
			
	}
	
	private static void loadFaultAndCorrectList(){
		faultStringList = new ArrayList<String>();
		correctStringList = new ArrayList<String>();
		reportStringList = new ArrayList<String>();
		try{
			faultStringList.add("KÄRCHER");
			faultStringList.add("DECK²");
			faultStringList.add("º");
			faultStringList.add(" – ");
			faultStringList.add("£");
			faultStringList.add("®");
			faultStringList.add("Nüssli");
			faultStringList.add("n’t");
			
			correctStringList.add("KARCHER");		
			correctStringList.add("DECK");
			correctStringList.add(" ");
			correctStringList.add(" ");
			correctStringList.add("Pound ");
			correctStringList.add(" ");
			correctStringList.add("Nussli");
			correctStringList.add("not");
			
			reportStringList.add("KARCHER");		
			reportStringList.add("DECK");
			reportStringList.add("Power");
			reportStringList.add("Hyphen");
			reportStringList.add("Pound");
			reportStringList.add("RoundR");
			reportStringList.add("Nussli");
			reportStringList.add("not");
			/*BufferedReader br = null;
 			FileInputStream fis = null;
 			String tmp = null;
 			fis = new FileInputStream(new File(HOME_DIR_LOCATION+FAULT_FILE_NAME));
 			br = new BufferedReader(new InputStreamReader(fis));
 			while ((tmp = br.readLine()) != null){
 				StringTokenizer tkn = new StringTokenizer(tmp, ";");
				while(tkn.hasMoreElements()){
					System.out.println(tkn.nextElement().toString());
					System.out.println(tkn.nextElement().toString());
				}
 			}*/
		}catch(Exception e){
			
		}
	}
}




