package com.project.user_service.service.imp;

import com.project.user_service.exception.ExceptionType.MailSendingErrorException;
import com.project.user_service.service.EmailSendService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class EmailSendServiceImp implements EmailSendService {
    private final JavaMailSender javaMailSender;
    @Value("${frontend.url}")
    private String frontendUrl;

    @Override
    public void sendVerificationEmail(String toEmail, String token) {
        String url = frontendUrl + "/verify?token=" + token + "&email=" + toEmail;

        String subject = "Verify Your Life-Flow Account";
        String body = "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <title>Verify Your Life-Flow Account</title>\n" +
                "    <style>\n" +
                "        body {\n" +
                "            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;\n" +
                "            line-height: 1.6;\n" +
                "            color: #333;\n" +
                "            max-width: 600px;\n" +
                "            margin: 0 auto;\n" +
                "            padding: 20px;\n" +
                "        }\n" +
                "        .header {\n" +
                "            text-align: center;\n" +
                "            padding: 20px 0;\n" +
                "            background-color: #4CAF50;\n" +
                "            color: white;\n" +
                "            border-radius: 5px 5px 0 0;\n" +
                "        }\n" +
                "        .content {\n" +
                "            padding: 20px;\n" +
                "            background-color: #f9f9f9;\n" +
                "            border-radius: 0 0 5px 5px;\n" +
                "        }\n" +
                "        .button {\n" +
                "            display: inline-block;\n" +
                "            padding: 12px 24px;\n" +
                "            background-color: #4CAF50;\n" +
                "            color: white;\n" +
                "            text-decoration: none;\n" +
                "            border-radius: 4px;\n" +
                "            margin: 20px 0;\n" +
                "        }\n" +
                "        .footer {\n" +
                "            margin-top: 20px;\n" +
                "            font-size: 12px;\n" +
                "            color: #777;\n" +
                "            text-align: center;\n" +
                "        }\n" +
                "    </style>\n" +
                "</head>\n" +
                "<body>\n" +
                "    <div class=\"header\">\n" +
                "        <h1>Welcome to Life-Flow</h1>\n" +
                "    </div>\n" +
                "    <div class=\"content\">\n" +
                "        <h2>Verify Your Email Address</h2>\n" +
                "        <p>Thank you for signing up with Life-Flow! We're excited to have you on board.</p>\n" +
                "        <p>To complete your registration and start using Life-Flow, please verify your email address by clicking the button below:</p>\n" +
                "        <p style=\"text-align: center;\">\n" +
                "            <a href=\"" + url + "\" class=\"button\">Verify Email Address</a>\n" +
                "        </p>\n" +
                "        <p>Or copy and paste this link into your browser:</p>\n" +
                "        <p><a href=\"" + url + "\">" + url + "</a></p>\n" +
                "        <p>If you did not create an account with Life-Flow, you can safely ignore this email.</p>\n" +
                "        <p>Best regards,<br>The Life-Flow Team</p>\n" +
                "    </div>\n" +
                "    <div class=\"footer\">\n" +
                "        <p> 2025 Life-Flow. All rights reserved.</p>\n" +
                "        <p>This is an automated message, please do not reply to this email.</p>\n" +
                "    </div>\n" +
                "</body>\n" +
                "</html>";
        try {

            SimpleMailMessage simpleMailMessage = new SimpleMailMessage();

            simpleMailMessage.setTo(toEmail);
            simpleMailMessage.setSubject(subject);
            simpleMailMessage.setText(body);

            javaMailSender.send(simpleMailMessage);
        } catch (Exception e) {
//            throw new MailSendingErrorException(e.getMessage());
            throw new MailSendingErrorException("Mail Sending Error");
        }


    }


    @Override
    public void sendForgetPasswordEmail(String toEmail, String token) {
        String url = frontendUrl + "/reset-password?token=" + token + "&email=" + toEmail;
        String subject = "Reset Your Life-Flow Password";
        String body = "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <title>Reset Your Password - Life-Flow</title>\n" +
                "    <style>\n" +
                "        body {\n" +
                "            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;\n" +
                "            line-height: 1.6;\n" +
                "            color: #333;\n" +
                "            max-width: 600px;\n" +
                "            margin: 0 auto;\n" +
                "            padding: 20px;\n" +
                "        }\n" +
                "        .header {\n" +
                "            text-align: center;\n" +
                "            padding: 20px 0;\n" +
                "            background-color: #4CAF50;\n" +
                "            color: white;\n" +
                "            border-radius: 5px 5px 0 0;\n" +
                "        }\n" +
                "        .content {\n" +
                "            padding: 20px;\n" +
                "            background-color: #f9f9f9;\n" +
                "            border-radius: 0 0 5px 5px;\n" +
                "        }\n" +
                "        .button {\n" +
                "            display: inline-block;\n" +
                "            padding: 12px 24px;\n" +
                "            background-color: #4CAF50;\n" +
                "            color: white;\n" +
                "            text-decoration: none;\n" +
                "            border-radius: 4px;\n" +
                "            margin: 20px 0;\n" +
                "        }\n" +
                "        .footer {\n" +
                "            margin-top: 20px;\n" +
                "            font-size: 12px;\n" +
                "            color: #777;\n" +
                "            text-align: center;\n" +
                "        }\n" +
                "        .warning {\n" +
                "            color: #d32f2f;\n" +
                "            font-weight: bold;\n" +
                "        }\n" +
                "    </style>\n" +
                "</head>\n" +
                "<body>\n" +
                "    <div class=\"header\">\n" +
                "        <h1>Life-Flow Password Reset</h1>\n" +
                "    </div>\n" +
                "    <div class=\"content\">\n" +
                "        <h2>Reset Your Password</h2>\n" +
                "        <p>We received a request to reset the password for your Life-Flow account.</p>\n" +
                "        <p>To reset your password, please click the button below:</p>\n" +
                "        <p style=\"text-align: center;\">\n" +
                "            <a href=\"" + url + "\" class=\"button\">Reset Password</a>\n" +
                "        </p>\n" +
                "        <p>Or copy and paste this link into your browser:</p>\n" +
                "        <p><a href=\"" + url + "\">" + url + "</a></p>\n" +
                "        <p class=\"warning\">This link will expire in 1 hour for security reasons.</p>\n" +
                "        <p>If you didn't request this password reset, please ignore this email or contact support if you have concerns.</p>\n" +
                "        <p>Best regards,<br>The Life-Flow Team</p>\n" +
                "    </div>\n" +
                "    <div class=\"footer\">\n" +
                "        <p>© 2025 Life-Flow. All rights reserved.</p>\n" +
                "        <p>This is an automated message, please do not reply to this email.</p>\n" +
                "    </div>\n" +
                "</body>\n" +
                "</html>";
        try {
            SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
            simpleMailMessage.setTo(toEmail);
            simpleMailMessage.setSubject(subject);
            simpleMailMessage.setText(body);
            javaMailSender.send(simpleMailMessage);
        } catch (Exception e) {
            throw new MailSendingErrorException("Mail Sending Error");
        }
    }

}
