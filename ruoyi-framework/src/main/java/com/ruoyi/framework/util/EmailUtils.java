package com.ruoyi.framework.util;

import com.ruoyi.common.config.EmailConfig;
import com.ruoyi.common.core.ApplicationContext;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.annotation.PostConstruct;
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
@Slf4j
@Component
public class EmailUtils {

    @Value("${mail.smtp.host}")
    private String mailSmtpHost;
    @Value("${mail.smtp.port}")
    private String mailSmtpPort;
    @Value("${mail.smtp.username}")
    private String mailSmtpUsername;
    @Value("${mail.smtp.password}")
    private String mailSmtpPassword;

    private static String staticMailSmtpHost;
    private static String staticMailSmtpPort;
    private static String staticMailSmtpUsername;
    private static String staticMailSmtpPassword;

    @PostConstruct
    public void init() {
        staticMailSmtpHost = this.mailSmtpHost;
        staticMailSmtpPort = this.mailSmtpPort;
        staticMailSmtpUsername = this.mailSmtpUsername;
        staticMailSmtpPassword = this.mailSmtpPassword;
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
        props.put("mail.smtp.host", staticMailSmtpHost);
        props.put("mail.smtp.socketFactory.port", staticMailSmtpPort);
        props.put("mail.smtp.auth", "true");
        Session session = Session.getDefaultInstance(props, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(staticMailSmtpUsername, staticMailSmtpPassword);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(staticMailSmtpUsername));
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
    public static void sendSimpleEmail(String recipientEmail, String subject, String body) {
        Properties props = new Properties();
        // 添加邮件配置（例如主机，端口，身份验证等）
        props.put("mail.smtp.host", staticMailSmtpHost);
        props.put("mail.smtp.socketFactory.port", staticMailSmtpPort);
        props.put("mail.smtp.auth", "true");
        Session session = Session.getDefaultInstance(props, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(staticMailSmtpUsername, staticMailSmtpPassword);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(staticMailSmtpUsername));
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
