/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.infrastructure.session;

import ejb.common.util.SMTPAuthenticator;
import ejb.infrastructure.entity.Employee;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Date;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
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

/**
 *
 * @author Jingyuan
 */
@Stateless
public class EmployeeEmailSessionBean implements EmployeeEmailSessionBeanLocal {

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    @PersistenceContext
    private EntityManager em;

    @Override
    public String initialPwd(String employeeNRIC, String employeeEmail) {

        try {
            System.out.println("***SendEmailSessionBean: send initial password to " + employeeEmail);
            Employee findEmployee = findEmployeeByNRIC(employeeNRIC);
            String emailCase = "initialPwd";
            sendPwdEmail(findEmployee, emailCase, employeeEmail);
            return "valid";
        } catch (NoResultException e) {
            return "invalid";
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(EmployeeEmailSessionBean.class.getName()).log(Level.SEVERE, null, ex);
            return "";
        }
    }

    @Override
    public String resetPwd(String employeeNRIC, String employeeEmail) {

        try {
            System.out.println("***SendEmailSessionBean: send resetPwd to " + employeeEmail);
            Employee findEmployee = findEmployeeByNRIC(employeeNRIC);
            if (!employeeEmail.equals(findEmployee.getEmployeeEmail())) {
                return "emailInvalid";
            }
            findEmployee.setLogInStatus("true");
            String emailCase = "resetPwd";
            sendPwdEmail(findEmployee, emailCase, employeeEmail);
            return "valid";
        } catch (NoResultException e) {
            return "invalid";
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(EmployeeEmailSessionBean.class.getName()).log(Level.SEVERE, null, ex);
            return "";
        }
    }

    @Override
    public String changePwd(String currentPassword, String newPassword, Long employeeId) {

        try {

            System.out.println("***SendEmailSessionBean: current password" + currentPassword);
            Employee employee = em.find(Employee.class, employeeId);
            System.out.println("employee: " + employee);
            String hashedPassword = employee.getEmployeePassword();
            System.out.println("hashedpassword" + hashedPassword);
            String hashedCurrentPassword = md5Hashing(currentPassword + employee.getEmployeeNRIC().substring(0, 3));
            System.out.println("hashedCurrentPassword" + hashedCurrentPassword);

            if (!hashedCurrentPassword.equals(hashedPassword)) {
                return "invalid";
            } else {
                if (currentPassword.equals(newPassword)) {
                    return "equal";
                } else {
                    String hashedNewPassword = md5Hashing(newPassword + employee.getEmployeeNRIC().substring(0, 3));
                    employee.setEmployeePassword(hashedNewPassword);
                    employee.setLogInStatus("false");
                    System.out.println("***SendEmailSessionBean: " + employee.getEmployeeId());
                    System.out.println("***SendEmailSessionBean: newPassword " + newPassword);
                    em.flush();
                    return "success";
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new EJBException(e.getMessage());
        }

    }

    private void sendPwdEmail(Employee findEmployee, String emailCase, String employeeEmail) throws NoSuchAlgorithmException {
        try {
            Employee employee = findEmployee;
            String password = generatePwd();
            String hashedPassword = password;
            hashedPassword = md5Hashing(password + employee.getEmployeeNRIC().substring(0, 3));
            findEmployee.setEmployeePassword(hashedPassword);
            em.flush();
            String emailServerName = "smtp.gmail.com";
            String emailFromAddress = "Han Fengwei Test Send<merlionbankes05@gmail.com>";
            String toEmailAddress = "Han Fengwei Test Receive<" + employeeEmail + ">";
            String mailer = "JavaMailer";
            String emailText = "Dear " + employee.getEmployeeName() + ",\n";
            if (emailCase.equals("initialPwd")) {
                emailText += "Your user account has been successfully created.\n";
                emailText += "Your account number is: " + employee.getEmployeeAccountNum() + "\n";
                emailText += "Your account password is: " + password + "\n";
                emailText += "Please go to Merlion Bank Internal System and log in with your account number and password. \n";
                emailText += "For security reason,you may change your password after logging in to Merlion Bank Internal System. \n";
            } else {
                emailText += "Your user account password has been reset.\n";
                emailText += "Your new password is: " + password + "\n";
                emailText += "Please go to Merlion Bank Internal System and log in with your new password.";
            }

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
                msg.setSubject("Merlion Bank Internal System Service");;
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

    private Employee findEmployeeByNRIC(String employeeNRIC) {
        Query query = em.createQuery("SELECT e FROM Employee e WHERE e.employeeNRIC= :NRIC");
        query.setParameter("NRIC", employeeNRIC);
        return (Employee) query.getSingleResult();
    }

    private String generatePwd() {
        SecureRandom random = new SecureRandom();
        return new BigInteger(40, random).toString(32);
    }

    private String md5Hashing(String stringToHash) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        return Arrays.toString(md.digest(stringToHash.getBytes()));
    }
}
