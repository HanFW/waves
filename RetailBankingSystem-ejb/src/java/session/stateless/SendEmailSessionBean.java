/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package session.stateless;

import entity.CustomerBasic;
import entity.Employee;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Date;
import java.util.Properties;
import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.SMTPAuthenticator;

/**
 *
 * @author Jingyuan
 */
@Stateless
public class SendEmailSessionBean implements SendEmailSessionBeanLocal {

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    @PersistenceContext
    private EntityManager em;

    @Override
    public String resetPwd(String employeeEmail) {
        System.out.println("***SendEmailSessionBean:" + employeeEmail);
        try {
            System.out.println("***SendEmailSessionBean:" + employeeEmail);
            Query query = em.createQuery("SELECT e FROM Employee e WHERE e.employeeEmail= :Email");
            query.setParameter("Email", employeeEmail);
            Employee findEmployee = (Employee) query.getSingleResult();
            sendResetPwdEmail(findEmployee);
            return "valid";
        } catch (NoResultException e) {
            return "invalid";
        }
    }

    @Override
    public String changePwd(String currentPassword, String newPassword, Long employeeId) {
        System.out.println("***SendEmailSessionBean:" + currentPassword);
        Employee employee = em.find(Employee.class, employeeId);
        System.out.println("employee: "+employee);
        String password = employee.getEmployeePassword();

        if (!currentPassword.equals(password)) {
            return "invalid";
        } else {
            if (currentPassword.equals(newPassword)) {
                return "equal";
            } else {
                employee.setEmployeePassword(newPassword);
                System.out.println("***SendEmailSessionBean: " +employee.getEmployeeId());
                System.out.println("***SendEmailSessionBean: newPassword " + newPassword);
                em.flush();
                return "success";
            }
        }

    }

    private void sendResetPwdEmail(Employee findEmployee) {
        Employee employee = findEmployee;

        String password = generatePwd();
        findEmployee.setEmployeePassword(password);
        em.flush();
        String emailServerName = "smtp.gmail.com";
        String emailFromAddress = "Han Fengwei Test Send<merlionbankes05@gmail.com>";
        String toEmailAddress = "Han Fengwei Test Receive<" + employee.getEmployeeEmail() + ">";
        String mailer = "JavaMailer";
        String emailText = "Dear Employee, \n";

        emailText += "Your user account password has been reset.\n";
        emailText += "Your new password is: " + password + "\n";
        emailText += "Please go to Merlion Bank Internal System and log in with your new password.";

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
        } catch (Exception e) {
            e.printStackTrace();
            throw new EJBException(e.getMessage());
        }
    }

    private String generatePwd() {
        SecureRandom random = new SecureRandom();
        return new BigInteger(40, random).toString(32);
    }
}
