/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package session.stateless;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import javax.ejb.Stateless;
import org.apache.commons.codec.binary.Base32;
import org.apache.commons.codec.binary.Base64;

/**
 *
 * @author hanfengwei
 */
@Stateless
public class SMSSessionBean implements SMSSessionBeanLocal {

    @Override
    public void sendOTP(String target) {
        
    }

    private void sendSMS() {
        System.out.println("*** infrastructure/SMSSessionBean: sendSMS() ***");
        try {
            String phoneNumber = "83114121";
            String appKey = "454b4f6f-f65d-47ac-8d9a-162b46e5f82c";
            String appSecret = "g6socSVvYk6wNa7Bz6PBVA==";
            String message = "Hello, world!";

            URL url = new URL("https://messagingapi.sinch.com/v1/sms/" + phoneNumber);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");

            String userCredentials = "application\\" + appKey + ":" + appSecret;
            byte[] encoded = Base64.encodeBase64(userCredentials.getBytes());
            String basicAuth = "Basic " + new String(encoded);

            connection.setRequestProperty("Authorization", basicAuth);
            String postData = "{\"Message\":\"" + message + "\"}";
            OutputStream os = connection.getOutputStream();

            os.write(postData.getBytes());
            StringBuilder response = new StringBuilder();
            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            while ((line = br.readLine()) != null) {
                response.append(line);
            }

            br.close();
            os.close();
            System.out.println(response.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
