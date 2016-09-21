/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.infrastructure.session;

import com.twilio.sdk.TwilioRestClient;
import com.twilio.sdk.TwilioRestException;
import com.twilio.sdk.resource.factory.MessageFactory;
import com.twilio.sdk.resource.instance.Account;
import com.twilio.sdk.resource.instance.Message;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.jboss.aerogear.security.otp.Totp;
import org.jboss.aerogear.security.otp.api.Base32;

/**
 *
 * @author hanfengwei
 */
@Stateless
public class SMSSessionBean implements SMSSessionBeanLocal {

    @Override
    public void sendOTP(String target, String phoneNum) {
        String secret = Base32.random();
        Totp totp = new Totp(secret);
        String strOtp = totp.now(); //427773
        System.out.println("*************** " + strOtp);
        String message = "Dear customer, thank you for choosing Merlion Bank! This is your one-time password: " + strOtp;
        sendSMS(message, phoneNum);
    }
    
    private void sendSMS(String message, String toPhoneNum){
        final String ACCOUNT_SID = "ACd2668f24db04f66798892a6a99af1680";
        final String AUTH_TOKEN = "c029686d79f83924599423aee622994c";
        
        TwilioRestClient client = new TwilioRestClient(ACCOUNT_SID, AUTH_TOKEN);

        Account account = client.getAccount();

        MessageFactory messageFactory = account.getMessageFactory();
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("To", "+65"+toPhoneNum)); // Replace with a valid phone number for your account.
        params.add(new BasicNameValuePair("From", "+16155412284 ")); // Replace with a valid phone number for your account.
        params.add(new BasicNameValuePair("Body", message));
        try {
            Message sms = messageFactory.create(params);
        } catch (TwilioRestException ex) {
            System.out.println(ex.getErrorMessage());
        }
    }
}
