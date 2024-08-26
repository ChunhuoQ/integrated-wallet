package com.ruoyi.common.utils;

import com.ruoyi.common.config.EmailConfig;
import com.ruoyi.common.core.ApplicationContext;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.File;
import java.util.Properties;


/**
 * 邮件发送工具类
 */
public class EmailUtils {

    static String mailSmtpHost;
    static String mailSmtpPort;
    static String mailSmtpUsername;
    static String mailSmtpPassword;
    static {
        EmailConfig config = ApplicationContext.getInstance().getBean(EmailConfig.class);
        mailSmtpHost = config.getMailSmtpHost();
        mailSmtpPort = config.getMailSmtpPort();
        mailSmtpUsername = config.getMailSmtpUsername();
        mailSmtpPassword = config.getMailSmtpPassword();
    }

    /**
     * 发送带附件的邮件
     * @param recipientEmail
     * @param subject
     * @param file
     * @param fileName
     */
    private static void sendEmailWithAttachment(String recipientEmail, String subject, File file, String fileName) {
        Properties props = new Properties();
        // Add your email configuration (e.g., host, port, authentication details)
        props.put("mail.smtp.host", mailSmtpHost);
        props.put("mail.smtp.socketFactory.port", mailSmtpPort);
        props.put("mail.smtp.auth", "true");
        Session session = Session.getDefaultInstance(props, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(mailSmtpUsername, mailSmtpPassword);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(mailSmtpUsername));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
            message.setSubject(subject);
            MimeBodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setText("请参见所附 CSV 文件。");
            MimeMultipart multipart = new MimeMultipart();
            multipart.addBodyPart(messageBodyPart);

            messageBodyPart = new MimeBodyPart();
            DataSource source = new FileDataSource(file);
            messageBodyPart.setDataHandler(new DataHandler(source));
            messageBodyPart.setFileName(fileName);
            multipart.addBodyPart(messageBodyPart);

            message.setContent(multipart);
            Transport.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 发送不带附件的邮件
     * @param recipientEmail 收件人邮箱
     * @param subject 邮件主题
     * @param body 邮件正文
     */
    private static void sendSimpleEmail(String recipientEmail, String subject, String body) {
        Properties props = new Properties();
        // 添加邮件配置（例如主机，端口，身份验证等）
        props.put("mail.smtp.host", mailSmtpHost);
        props.put("mail.smtp.socketFactory.port", mailSmtpPort);
        props.put("mail.smtp.auth", "true");
        Session session = Session.getDefaultInstance(props, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(mailSmtpUsername, mailSmtpPassword);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(mailSmtpUsername));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
            message.setSubject(subject);

            // 设置邮件正文  body= "您的验证码为88888.
            message.setText(body);

            // 发送邮件
            Transport.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }


}
