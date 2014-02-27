package com.karmaa.mq;

import javax.jms.JMSException;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.filechooser.FileNameExtensionFilter;

import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.util.Scanner;
import javax.swing.*;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.Session;

import com.ibm.msg.client.jms.JmsConnectionFactory;
import com.ibm.msg.client.jms.JmsDestination;
import com.ibm.msg.client.jms.JmsFactoryFactory;
import com.ibm.msg.client.wmq.WMQConstants;


public class MQBulkReadPanel extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTextField QUEUE_MANAGER_NAME_Text;
	private JTextField TARGET_Q_NAME_Text;
	private JTextField MQ_CHANNEL_NAME_Text;
	private JTextField LISTENER_PORT_NUM_Text;
	private JTextField INPUT_FILE_Text;
	private JTextField HOST_NAME_Text;
	JPanel p;
	String dirName = "D:\\JavaProject\\";
	int count=0;
	
	// System exit status value (assume unset value to be 1)
	private static int status = 1;

	/**
	 * Create the panel.
	 */
	public MQBulkReadPanel() {
		//setLayout(null);
		GridLayout gl_p = new GridLayout(10,2);
				
		gl_p.setVgap(5);
		gl_p.setHgap(5);
		p=new JPanel(gl_p);
		System.out.println("Initalizing MQBulkReadPanel!");
		QUEUE_MANAGER_NAME_Text = new JTextField();
		QUEUE_MANAGER_NAME_Text.setBounds(55, 78, 114, 28);
		p.add(QUEUE_MANAGER_NAME_Text);
		QUEUE_MANAGER_NAME_Text.setColumns(10);
		
		JLabel QUEUE_MANAGER_NAME_Label = new JLabel("QUEUE MANAGER NAME");
		QUEUE_MANAGER_NAME_Label.setBounds(55, 53, 132, 14);
		p.add(QUEUE_MANAGER_NAME_Label);
		
		TARGET_Q_NAME_Text = new JTextField();
		TARGET_Q_NAME_Text.setBounds(55, 161, 114, 28);
		p.add(TARGET_Q_NAME_Text);
		TARGET_Q_NAME_Text.setColumns(10);
		
		JLabel TARGET_Q_NAME_Label = new JLabel("TARGET Q NAME");
		TARGET_Q_NAME_Label.setBounds(55, 136, 86, 14);
		p.add(TARGET_Q_NAME_Label);
		
		MQ_CHANNEL_NAME_Text = new JTextField();
		MQ_CHANNEL_NAME_Text.setBounds(55, 233, 114, 33);
		p.add(MQ_CHANNEL_NAME_Text);
		MQ_CHANNEL_NAME_Text.setColumns(10);
		
		JLabel MQ_CHANNEL_NAME_Label = new JLabel("MQ CHANNEL NAME");
		MQ_CHANNEL_NAME_Label.setBounds(55, 208, 132, 14);
		p.add(MQ_CHANNEL_NAME_Label);
		
		LISTENER_PORT_NUM_Text = new JTextField();
		LISTENER_PORT_NUM_Text.setBounds(55, 315, 114, 33);
		p.add(LISTENER_PORT_NUM_Text);
		LISTENER_PORT_NUM_Text.setColumns(10);
		
		JLabel LISTENER_PORT_NUM_Label = new JLabel("LISTENER PORT NUM");
		LISTENER_PORT_NUM_Label.setBounds(55, 290, 122, 14);
		p.add(LISTENER_PORT_NUM_Label);
		
		INPUT_FILE_Text = new JTextField();
		INPUT_FILE_Text.setBounds(204, 161, 114, 28);
		p.add(INPUT_FILE_Text);
		INPUT_FILE_Text.setColumns(10);
		
		JButton INPUT_FILE_Upload_Button = new JButton("Upload");
		INPUT_FILE_Upload_Button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			JFileChooser chooser = new JFileChooser();
			chooser.setCurrentDirectory(new File(dirName));
			chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

			FileNameExtensionFilter filter = new FileNameExtensionFilter(
			".txt and .java files", "txt", "java");
			chooser.setFileFilter(filter);
			
			//=============
			try {
				int code = chooser.showOpenDialog(null);
				if (code == JFileChooser.APPROVE_OPTION) {
				File selectedFile = chooser.getSelectedFile();
				Scanner input = new Scanner(selectedFile); 
				
				while (input.hasNext()) {
				++count;
				input.next();
				}
				String f=selectedFile.getName();
				long size=selectedFile.length();
				System.out.println("file size:"+size+"\n");
				System.out.println("Absolute Path:"+selectedFile.getAbsolutePath());
				INPUT_FILE_Text.setText(selectedFile.getAbsolutePath());
				//l1.setText("File Name is: "+f);
				//l2.setText("File Size is: "+Long.toString(size));
				//l3.setText("No of words : "+Integer.toString(count));
				//l1.setVisible(true);
				//l2.setVisible(true);
				//l3.setVisible(true);
				
				String qMgrTxt=QUEUE_MANAGER_NAME_Text.getText().toString();
				String tgtQName=TARGET_Q_NAME_Text.getText().toString();
				String mqChalName=MQ_CHANNEL_NAME_Text.getText().toString();
				String lsnrPortNum=LISTENER_PORT_NUM_Text.getText().toString();
				String inpFilePath=INPUT_FILE_Text.getText().toString();
				String hostName=HOST_NAME_Text.getText().toString();
				
				
				processSubmitRequest(qMgrTxt,tgtQName,mqChalName,lsnrPortNum,inpFilePath,hostName);

				}

				} catch (Exception f) {
				f.printStackTrace();
				}
				
				//=================
			
			}
		});
		
		INPUT_FILE_Upload_Button.setBounds(375, 160, 74, 23);
		p.add(INPUT_FILE_Upload_Button);
		
		JButton Submit_Button = new JButton("Submit");
		Submit_Button.setBounds(360, 243, 89, 23);
		p.add(Submit_Button);
		
		JLabel lblMqbulkread = new JLabel("MQBulkRead");
		lblMqbulkread.setBounds(217, 11, 68, 14);
		p.add(lblMqbulkread);
		
		HOST_NAME_Text = new JTextField();
		HOST_NAME_Text.setBounds(204, 78, 114, 28);
		p.add(HOST_NAME_Text);
		HOST_NAME_Text.setColumns(10);
		
		JLabel HOST_NAME_Label = new JLabel("HOST NAME");
		HOST_NAME_Label.setBounds(204, 53, 81, 14);
		p.add(HOST_NAME_Label);
		
		getContentPane().add(p);
		setVisible(true);
		pack();

	}
	
	public static void main(String[] a) {
		MQBulkReadPanel c=new MQBulkReadPanel();
		}
	
	private static void recordFailure(Exception ex) {
		if (ex != null) {
			if (ex instanceof JMSException) {
				processJMSException((JMSException) ex);
			} else {
				System.out.println(ex);
			}
		}
		System.out.println("FAILURE");
		status = -1;
		return;
	}
	
	private static void recordSuccess(int mCount) {
		System.out.println("SUCCESS IN READING MESSAGES - Read Count:" + mCount
				+ "\n");
		status = 0;
		return;
	}
	
	private static void processJMSException(JMSException jmsex) {
		System.out.println(jmsex);
		Throwable innerException = jmsex.getLinkedException();
		if (innerException != null) {
			System.out.println("Inner exception(s):");
		}
		while (innerException != null) {
			System.out.println(innerException);
			innerException = innerException.getCause();
		}
		return;
	}
	
	private static void processSubmitRequest(String qMgrTxt,String tgtQName,String mqChalName,String lsnrPortNum,String inpFilePath,String hostName) throws IOException{


		// Variables
		Connection connection = null;
		Session session = null;
		Destination destination = null;
		MessageConsumer consumer = null;

		// Input Arguments
		//int argLength = args.length;
		System.out.println("\nqMgrTxt:"+qMgrTxt);

		if ((qMgrTxt.compareTo("")==0)&& (tgtQName.compareTo("")==0) && (mqChalName.compareTo("")==0) && (lsnrPortNum.compareTo("")==0) && (inpFilePath.compareTo("")==0) &&  (hostName.compareTo("")==0)) {
		//if (qMgrTxt.compareTo("")==0) {
			
			System.exit(0);
		}

		try {
			// Create a connection factory
			JmsFactoryFactory ff = JmsFactoryFactory
					.getInstance(WMQConstants.WMQ_PROVIDER);
			JmsConnectionFactory cf = ff.createConnectionFactory();

			// Set the properties
			cf.setStringProperty(WMQConstants.WMQ_HOST_NAME, hostName);
			cf.setIntProperty(WMQConstants.WMQ_PORT, Integer.parseInt(lsnrPortNum));
			cf.setStringProperty(WMQConstants.WMQ_CHANNEL, mqChalName);
			cf.setIntProperty(WMQConstants.WMQ_CONNECTION_MODE,
					WMQConstants.WMQ_CM_CLIENT);
			cf.setStringProperty(WMQConstants.WMQ_QUEUE_MANAGER, qMgrTxt);

			// Insist on queue manager level to be v7 or above
			cf.setStringProperty(WMQConstants.WMQ_PROVIDER_VERSION, "7.0.0.0");

			// Create JMS objects
			System.out.println("<<-----Creating Connection.....----->>\n");
			connection = cf.createConnection();
			System.out.println("<<-----Creating Session........----->>\n");
			session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			System.out
					.println("<<-----Preparing for Target queue to be read...----->>\n");
			destination = session.createQueue("queue:///" + tgtQName);

			// Enable read of MQMD fields. See documentation for further
			// details.
			((JmsDestination) destination).setBooleanProperty(
					WMQConstants.WMQ_MQMD_READ_ENABLED, true);

			// Open the file
			FileInputStream fstream = new FileInputStream(inpFilePath);

			// Get the object of DataInputStream
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));

			String correlId;
			StringBuilder sb = new StringBuilder("");
			int msgCount = 0;

			// Read File InputStream By Line
			while ((correlId = br.readLine()) != null) {
				System.out.println("<<-----CorrelId is:"+correlId.trim()+"----->>\n");
				
				// Create a consumer
				int correlIdLen=correlId.getBytes().length;				
				if (correlIdLen <= 48 && correlIdLen > 0) {
					if (!correlId.trim().equals("")) {
						System.out
								.println("<<-----Message to be Read with CorrelID----->>"
										+ correlId + "\n");
						consumer = session.createConsumer(destination,
								"JMSCorrelationID='" + "ID:" + correlId.trim()
										+ "'");

						// Start the connection
						connection.start();
						Message returnedMsg = consumer.receive(1000);
						System.out
								.println("<<-----Message Start----->>\n"
										+ returnedMsg
										+ "\n<<-----Message End----->>\n");
						msgCount++;

						System.out
								.println("<<-----Message attempted to read with CorrelID----->>"
										+ correlId + "\n");

						if (returnedMsg == null) {
							msgCount--;
							System.out.println("!!Either already read by this program or no message found with such identifier!!\n");
							System.out.println("<<===============================Next Message===============================>>\n");
							continue;
						}						

					}
					
				}
				else{
					System.out.println("!!Invalid Correlation identifier of length greater than 24 byte or equal to 0!!");
					System.out.println("<<===============================Next Message===============================>>\n");
				}
				
				
			}

			recordSuccess(msgCount);

			// Close the input stream
			System.out.println("<<-----Closing InputStream........----->>\n");
			in.close();

		} catch (JMSException jmsex) {
			recordFailure(jmsex);
		} finally {
			if (consumer != null) {
				try {
					System.out.println("<<-----Closing JMS Consumer......----->>\n");
					consumer.close();
				} catch (JMSException jmsex) {
					System.out.println("<<-----Consumer could not be closed----->>\n");
					recordFailure(jmsex);
				}
			}

			if (session != null) {
				try {
					System.out.println("<<-----Closing Session.......----->>\n");
					session.close();
				} catch (JMSException jmsex) {
					System.out.println("<<-----Session could not be closed----->>\n");
					recordFailure(jmsex);
				}
			}

			if (connection != null) {
				try {
					System.out.println("<<-----Closing Connection........----->>\n");
					connection.close();
				} catch (JMSException jmsex) {
					System.out.println("<<-----Connection could not be closed----->>\n");
					recordFailure(jmsex);
				}
			}
		}
		System.exit(status);
		return;
	
		
	}
}
