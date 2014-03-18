package fp2014;

import java.io.UnsupportedEncodingException;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class Mail {

	String recipient;
	String sender;
	String senderName;
	String subject;
	String content;
	
	public Mail(String recipient, String sender, String senderName, String subject, String content) {
		this.recipient = recipient;
		this.sender = sender;
		this.senderName = senderName;
		this.subject = subject;
		this.content = content;
		
		sendMail();
	}
	
	private void sendMail(){
		try {
			// Set properties of the mail
	        Properties props = new Properties();
	        props.put("mail.smtp.host", "smtp.stud.ntnu.no");
	        props.put("mail.debug", "false"); // Set to true to get console output on the sending process 
	        props.put("mail.smtp.starttls.enable", "true");
	        props.put("mail.smtp.port", "25");
	        props.put("mail.smtp.socketFactory.port", "25");
	        props.put("mail.smtp.socketFactory.fallback", "false");
	        
	        // Create mail session
	        Session mailSession = Session.getInstance(props);
	        mailSession.setDebug(false); // Set to true to get console output on the sending process 
	
	        // Create the mail
	        Message msg = new MimeMessage( mailSession );
	        msg.setFrom(new InternetAddress(sender, senderName));
			msg.setRecipients( Message.RecipientType.TO,InternetAddress.parse(recipient) );
	        msg.setSubject(subject);
	        msg.setText(content);
	
	        // Send the mail
	        Transport.send( msg );
		} catch (MessagingException | UnsupportedEncodingException e) { e.printStackTrace(); }
	}
}
