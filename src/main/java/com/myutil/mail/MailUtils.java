package com.myutil.mail;
import javax.mail.*;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;
import java.util.Properties;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MailUtils {
	// 激活过程中重置密码成功前台地址
	public static String ACTIVATION_RESET_PASSWORD_SUCCESS_URL;
	// 激活过程中重置密码失败前台地址
	public static String ACTIVATION_RESET_PASSWORD_FAILURE_URL;
	// 秘钥
	public static String AES_KEY;
	// 激活成功跳转地址
	public static String ACTIVATION_SUCCESS_URL;
	// 激活失败跳转地址
	public static String ACTIVATION_FAIL_URL;
	// 激活跳转地址
	public static String ACTIVATION_URL;
	// 前台访问地址
	public static String FRONTEND_URL;
	// 后台访问地址
	public static String BACKEND_URL;
	// 重置密码后台激活地址
	public static String RESET_PASSWORD_URL;
	// 重置密码前台激活成功地址
	public static String RESET_PASSWORD_SUCCESS_URL;
	// 重置密码前台激活失败地址
	public static String RESET_PASSWORD_FAIL_URL;
	// 注册激活主题
	public static String REGISTER_ACTIVATION_SUBJECT = "Verify Your DTEN Account";
	// 重置密码激活主题
	public static String RESET_PASSWORD_SUBJECT = "Reset Password";
	// 激活码主题
	public static String ACTIVATION_CODE_SUBJECT = "Your DTEN Activation Codes";
	// 激活码主题_个人
	public static String VERIFY_YOUR_EMAIL_ON_DTEN = "Verify your email address on DTEN";
	// 邀请邮件主题_个人
	public static String INVITE_YOUR_DTEN_ACCOUNT = "DTEN account invitation";

	public static String DEVICE_ACTIVATE_SUBJECT = "DTEN device activated - DTEN ID ";

	public static String YOUR_ACCOUNT_ON_DTEN = "Your account on DTEN";

	public static  String mailHost;
	public static  String mailAccount;
	public static  String mailPassword;
	/**
	 * 判断邮箱格式
	 * @param email
	 * @return
	 */
	public static boolean isEmail(String email){
		if (null==email || "".equals(email)){
			return false;
		}

//		String regEx1 = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
        String regEx1 = "^\\S+@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*\\.[a-zA-Z0-9]*$";
		Pattern p = Pattern.compile(regEx1);
		Matcher m = p.matcher(email);
		return m.matches();
	}

    public static void main(String[] args) {
        String email="44_pb0h1@linshiyouxiang.net";
        System.out.println(isEmail(email));
    }
	/**
	 * 生成验证码
	 * @return
	 */
	public static String randomCode() {
		StringBuilder str = new StringBuilder();
		Random random = new Random();
		for (int i = 0; i < 6; i++) {
			str.append(random.nextInt(10));
		}
		return str.toString();
	}

	/**
	 * 发送邮箱
	 * @param email
	 * @param emailMsg
	 * @throws AddressException
	 * @throws MessagingException
	 */
	public static void sendMail(String subject, String email, String emailMsg)
			throws MessagingException {
		// 1.创建一个程序与邮件服务器会话对象 Session

		Properties props = new Properties();
		//设置发送的协议
		props.setProperty("mail.transport.protocol", "SMTP");

		//设置发送邮件的服务器
		props.setProperty("mail.host", mailHost);
		props.put("mail.smtp.ssl.enable", "true");
		props.setProperty("mail.smtp.auth", "true");// 指定验证为true
		props.put("mail.smtp.port", "465");

		// 创建验证器
		Authenticator auth = new Authenticator() {
			@Override
			public PasswordAuthentication getPasswordAuthentication() {
				//设置发送人的帐号和密码
				return new PasswordAuthentication(mailAccount, mailPassword);
			}
		};

		Session session = Session.getInstance(props, auth);

		// 2.创建一个Message，它相当于是邮件内容

		Message message = new MimeMessage(session);

		//设置发送者
		message.setFrom(new InternetAddress(mailAccount));

		//设置发送方式与接收者
		message.setRecipient(RecipientType.TO, new InternetAddress(email));

		//设置邮件主题
		message.setSubject(subject);
		// message.setText("这是一封激活邮件，请<a href='#'>点击</a>");

		//设置邮件内容
		message.setContent(emailMsg, "text/html;charset=utf-8");

		// 3.创建 Transport用于将邮件发送
		Transport.send(message);
	}

}
