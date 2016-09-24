/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.infrastructure.session;

import ejb.common.util.SMTPAuthenticator;
import ejb.customer.entity.CustomerBasic;
import java.util.Date;
import java.util.Map;
import java.util.Properties;
import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 *
 * @author hanfengwei
 */
@Stateless
public class CustomerEmailSessionBean implements CustomerEmailSessionBeanLocal {

    @Override
    public void sendEmail(CustomerBasic customer, String subject, Map actions) {
        System.out.println("@");
        System.out.println("@@@@@@ infrastructure/CustomerEmailSessionBean: sendEmail() @@@@@@");
        
        String emailServerName = "smtp.gmail.com";
        String emailFromAddress = "Han Fengwei Test Send<merlionbankes05@gmail.com>";
        String toEmailAddress = "Han Fengwei Test Receive<" + customer.getCustomerEmail() + ">";
        String mailer = "JavaMailer";
        String emailText = "Dear customer, \n";

        switch (subject) {
            //deposit: open deposit account
            case "openAccount":
                emailText += "You have an deposit account opened. ";
                if (actions.get("onlineBanking").equals("yes")) {
                    emailText += "Your online banking account has been successfully created.\n";
                    emailText += "Account number: " + customer.getCustomerOnlineBankingAccountNum() + "\n";
                    emailText += "Initial password: " + actions.get("onlineBankingPassword") + "\n";
                    emailText += "Please go to ??? and activate your online banking account. ";
                }
                break;
            case "resetOnlineBankingPassword":
                emailText += "Your online banking account password has been reset as follow: \n";
                emailText += "Initial password: " + actions.get("onlineBankingPassword") + "\n";
                emailText += "Please go to ??? and activate your online banking account. ";
        }

        try {
            Properties props = new Properties();
            props.put("mail.transport.protocol", "smtp");
            props.put("mail.smtp.host", emailServerName);
            props.put("mail.smtp.port", "587");
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.debug", "true");
            javax.mail.Authenticator auth = new SMTPAuthenticator();
            Session session = Session.getInstance(props, auth);
            session.setDebug(true);
            Message msg = new MimeMessage(session);
            if (msg != null) {
                msg.setFrom(InternetAddress.parse(emailFromAddress, false)[0]);
                msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmailAddress, false));
                msg.setSubject("Thank you for using our service! ");;
                msg.setText(emailText);
                msg.setHeader("X-Mailer", mailer);
                Date timeStamp = new Date();
                msg.setSentDate(timeStamp);
                Transport.send(msg);
            }
            System.out.println("@");
        } catch (Exception e) {
            e.printStackTrace();
            throw new EJBException(e.getMessage());
        }
    }
}
