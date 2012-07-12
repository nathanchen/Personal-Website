package com.nathanchen.utils;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;


public class SendEmail
{
	public static void sendEmail() throws MessagingException
	{
		String host = "smtp.gmail.com";
		String from = "iamnathanchen";
		String pass = "cq081200";
		Properties props = System.getProperties();
		props.put("mail.smtp.starttls.enable", "true"); // added this line
		props.put("mail.smtp.host", host);
		props.put("mail.smtp.user", from);
		props.put("mail.smtp.password", pass);
		props.put("mail.smtp.port", "587");
		props.put("mail.smtp.auth", "true");

		String[] to = { "natechen@me.com" }; // added this line

		Session session = Session.getDefaultInstance(props, null);
		MimeMessage message = new MimeMessage(session);
		message.setFrom(new InternetAddress(from));

		InternetAddress[] toAddress = new InternetAddress[to.length];

		// To get the array of addresses
		for (int i = 0; i < to.length; i++)
		{ // changed from a while loop
			toAddress[i] = new InternetAddress(to[i]);
		}
		System.out.println(Message.RecipientType.TO);

		for (int i = 0; i < toAddress.length; i++)
		{ // changed from a while loop
			message.addRecipient(Message.RecipientType.TO, toAddress[i]);
		}
		message.setSubject("sending in a group");
		message.setText("Welcome to JavaMail");
		Transport transport = session.getTransport("smtp");
		transport.connect(host, from, pass);
		transport.sendMessage(message, message.getAllRecipients());
		transport.close();
	}
	
	public static void main(String args[]) throws MessagingException
	{
		sendEmail();
	}
	
}
