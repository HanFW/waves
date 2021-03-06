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
        String emailFromAddress = "Merlion Bank<merlionbankes05@gmail.com>";
        String toEmailAddress = "Merlion Bank Customer<" + customer.getCustomerEmail() + ">";
        String mailer = "JavaMailer";
        String emailText = "Dear " + customer.getCustomerName() + ", \n";

        switch (subject) {
            //deposit: open deposit account
            case "openAccount":
                emailText += "You have a deposit account opened. ";
                if (actions.get("onlineBanking").equals("yes")) {
                    emailText += "Your online banking account has been successfully created.\n";
                    emailText += "Initial User ID: " + customer.getCustomerOnlineBankingAccountNum() + "\n";
                    emailText += "Initial PIN: " + actions.get("onlineBankingPassword") + "\n";
                    emailText += "Please login with your initial User ID and PIN. \n\n";
                    emailText += "If your bank account type is Monthly Saving Account, please remember minimum monthly saving is S$50.";
                    emailText += "Otherwise, you are not able to get bonus interest.\n\n";
                    emailText += "If your bank account type is not Monthly Saving Account, please check your bank account status under 'My Account' after you login.\n";
                    emailText += "Please deposit/transfer sufficient amount to your account to activate your bank account, if your bank account status is inactive.\n";
                    emailText += "Please activate your bank account within one week.\n";
                    emailText += "Otherwise, our system will automatically close your account after one week.\n";
                }
                break;
            case "resetOnlineBankingPassword":
                emailText += "Your online banking account PIN has been reset as follow: \n";
                emailText += "Initial PIN: " + actions.get("onlineBankingPassword") + "\n";
                emailText += "Please proceed to change your PIN after login.";
                break;
            case "recreateIBAccount":
                emailText += "Your online banking account has been successfully reopened. \n";
                emailText += "Initial User ID: " + actions.get("userId") + "\n";
                emailText += "Initial PIN: " + actions.get("pin") + "\n";
                emailText += "Please login with your initial User ID and PIN to activate your online banking account.";
                break;
            case "rejectOpenAccount":
                emailText += "I regret to inform that your open account application have not been approved. \n";
                emailText += "Please verify your identification number with your IC/Passport. \n";
                emailText += "If you have any enquiry, please contact us at 800 820 8820. \n";
                emailText += "We look forward to serving you again. \n";
                break;
            case "enquiryReplied":
                emailText += "Your recent enquiry (Case ID: " + actions.get("caseId") + ") has been replied by us. \n";
                emailText += "Please login to your online banking account to view our reply in detail. \n";
                emailText += "We look forward to serving you again. \n";
                break;
            case "cardToBeExpired":
                emailText += "Your " + actions.get("cardTypeName") + " " + actions.get("cardNumber") + " is going to be expired in " + actions.get("remainingMonths") + " months. \n";
                emailText += "Please log in to your online banking account to replace your current card. \n";
                emailText += "If you have any enquiry, please contact us at 800 820 8820. \n";
                emailText += "We look forward to serving you again. \n";
                break;
            case "cardToBeActivated":
                emailText += "Your " + actions.get("cardTypeName") + " " + actions.get("cardNumber") + " is going to be no longer activated in " + actions.get("remainingDays") + " days. \n";
                emailText += "Please log in to your online banking account to activate your card. \n";
                emailText += "If you have any enquiry, please contact us at 800 820 8820. \n";
                emailText += "We look forward to serving you again. \n";
                break;
            case "mortgageLoanApplication":
                emailText += "Your " + actions.get("loanType") + " loan application has been submitted successfully. \n";
                emailText += "Mortgage loan application usually takes a few weeks to process. We will contact you via email once we have finished processing your request.\n";
                emailText += "Thank you for your cooperation. \n";
                break;
            case "cashlineApplication":
                emailText += "Your Cashline application has been submitted successfully. \n";
                emailText += "Cashline application usually takes a few weeks to process. We will contact you via email once we have finished processing your request.\n";
                emailText += "Thank you for your cooperation. \n";
                break;
            case "educationLoanApplication":
                emailText += "Your Education Loan application has been submitted successfully. \n";
                emailText += "Education Loan application usually takes a few weeks to process. We will contact you via email once we have finished processing your request.\n";
                emailText += "Thank you for your cooperation. \n";
                break;
            case "renovationLoanApplication":
                emailText += "Your Renovation Loan application has been submitted successfully. \n";
                emailText += "Renovation Loan application usually takes a few weeks to process. We will contact you via email once we have finished processing your request.\n";
                emailText += "Thank you for your cooperation. \n";
                break;
            case "approveContractLoanRequest":
                emailText += "Your loan request has been approved. We would like to arrange a meeting with you soon to sign the contracts.\n\n";
                break;
            case "approveRenovationLoanRequest":
                emailText += "Your Renovation Loan request has been approved. \n\n";
                if (actions.get("onlineBanking").equals("yes")) {
                    emailText += "Here are your online banking details: \n";
                    emailText += "Initial User ID: " + customer.getCustomerOnlineBankingAccountNum() + "\n";
                    emailText += "Initial PIN: " + actions.get("onlineBankingPassword") + "\n";
                    emailText += "Please login with your initial User ID and PIN. \n\n";
                }
                emailText += "We will inform you again once the loan has been disbursed. ";
                break;
            case "approveEducationLoanRequest":
                emailText += "Your Education Loan request has been approved. \n\n";
                if (actions.get("onlineBanking").equals("yes")) {
                    emailText += "Here are your online banking details: \n";
                    emailText += "Initial User ID: " + customer.getCustomerOnlineBankingAccountNum() + "\n";
                    emailText += "Initial PIN: " + actions.get("onlineBankingPassword") + "\n";
                    emailText += "Please login with your initial User ID and PIN. \n\n";
                }
                emailText += "Please log in to online banking to submit your loan disbursement forms. ";
                break;
            case "startMortgageLoan":
                emailText += "Your Mortgage Loan has commenced. \n";
                emailText += "Please log in to your online banking to view your loan details and make repayments. \n";
                emailText += "The repayment due day is on 1st every month and your first repayment will due on the second month after commencement. \n";
                if (actions.get("onlineBanking").equals("yes")) {
                    emailText += "Here are your online banking details: \n";
                    emailText += "Initial User ID: " + customer.getCustomerOnlineBankingAccountNum() + "\n";
                    emailText += "Initial PIN: " + actions.get("onlineBankingPassword") + "\n";
                    emailText += "Please login with your initial User ID and PIN. \n\n";
                }
                break;
            case "startCarLoan":
                emailText += "Your Car Loan has commenced. \n";
                emailText += "Please log in to your online banking to view your loan details and make repayments. \n";
                emailText += "The repayment due day is on 1st every month and your first repayment will due on the second month after commencement. \n";
                if (actions.get("onlineBanking").equals("yes")) {
                    emailText += "Here are your online banking details: \n";
                    emailText += "Initial User ID: " + customer.getCustomerOnlineBankingAccountNum() + "\n";
                    emailText += "Initial PIN: " + actions.get("onlineBankingPassword") + "\n";
                    emailText += "Please login with your initial User ID and PIN. \n\n";
                }
                break;
            case "startRenovationLoan":
                emailText += "Your Renovation Loan has commenced. \n";
                emailText += "Please log in to your online banking to view your loan details and make repayments. \n";
                emailText += "The repayment due day is on 1st every month and your first repayment will due on the second month after commencement. \n";
                if (actions.get("onlineBanking").equals("yes")) {
                    emailText += "Here are your online banking details: \n";
                    emailText += "Initial User ID: " + customer.getCustomerOnlineBankingAccountNum() + "\n";
                    emailText += "Initial PIN: " + actions.get("onlineBankingPassword") + "\n";
                    emailText += "Please login with your initial User ID and PIN. \n\n";
                }
                break;
            case "startEducationLoan":
                emailText += "Your Education Loan Repayment has commenced. \n";
                emailText += "Please log in to your online banking to view your loan details and make repayments. \n";
                break;
            case "gentelDefaultReminder":
                emailText += "Please be reminded to repay your loan. Default on loans will badly affect your credit scores. ";
                break;
            case "seriousDefaultReminder":
                emailText += "Please be reminded to repay your loan. Merlion Bank will take actions on your assets if you are not able to repay the loan in the required period.";
                break;
            case "bankruptReminder":
                emailText += "Please be reminded that Merlion Bank has sued your for bankrupt and will take actions on your assets to recover your default payments";
                break;
            case "recurringStopReminder":
                emailText += "Please be noted that your recurring loan repayment has been terminated due to insufficient balance in your account. Please make repayment manually or declare recurring account again after deposit.";
                break;
            case "approveCreditCard":
                emailText += "You have an online banking account opened. ";
                if (actions.get("onlineBanking").equals("yes")) {
                    emailText += "Your online banking account has been successfully created.\n";
                    emailText += "Initial User ID: " + customer.getCustomerOnlineBankingAccountNum() + "\n";
                    emailText += "Initial PIN: " + actions.get("onlineBankingPassword") + "\n";
                    emailText += "Please login with your initial User ID and PIN. \n\n";
                    
                    emailText += "Once your credit card has been approved, we will contact you immediately.\n";
                    emailText += "Thank you for your support with Merlion Bank.\n";
                }
                break;
            case "creditCardDefualt":
                emailText += "Please be reminded to make repayments for your credit card. ";
                break;
            case "creditCardDefualtAction":
                emailText += "Please be reminded that your Merlion Bank credit card limit has been set to 0. \n";
                emailText += "Please repay your credit card to continue your services";
                break;
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
                msg.setSubject("Thank you for choosing Merlion Bank! ");;
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
