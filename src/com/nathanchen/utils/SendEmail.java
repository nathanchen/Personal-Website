package com.nathanchen.utils;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.log4j.Logger;


public class SendEmail
{

	Logger	logger	= Logger.getLogger(SendEmail.class);


	public void sendEmail(String codeLevel, String content, String adminName)
			throws MessagingException
	{
		String host = "smtp.gmail.com";
		String from = "iamnathanchen";
		String pass = "cq081200";
		Properties props = System.getProperties();
		props.put("mail.smtp.starttls.enable", "true"); 
		props.put("mail.smtp.host", host);
		props.put("mail.smtp.user", from);
		props.put("mail.smtp.password", pass);
		props.put("mail.smtp.port", "587");
		props.put("mail.smtp.auth", "true");

		String[] to = { "natechen@me.com" }; 

		Session session = Session.getDefaultInstance(props, null);
		MimeMessage message = new MimeMessage(session);
		message.setFrom(new InternetAddress(from));

		InternetAddress[] toAddress = new InternetAddress[to.length];

		for (int i = 0; i < to.length; i++)
		{ 
			toAddress[i] = new InternetAddress(to[i]);
		}

		String recipient = "";
		if (to.length > 0)
		{
			for (String str : to)
				recipient = recipient + " " + str;
		}
		logger.info("发送邮件到 " + recipient.trim() + " ---------- sendEmail()");

		for (int i = 0; i < toAddress.length; i++)
		{ // changed from a while loop
			message.addRecipient(Message.RecipientType.TO, toAddress[i]);
		}
		message.setSubject("++++++++++ 网站出现异常/错误 " + codeLevel + "!!!");
		message.setText("错误等级： " + codeLevel + "\n" + "错误描述： " + content);
		Transport transport = session.getTransport("smtp");
		transport.connect(host, from, pass);
		transport.sendMessage(message, message.getAllRecipients());
		transport.close();
	}

}
