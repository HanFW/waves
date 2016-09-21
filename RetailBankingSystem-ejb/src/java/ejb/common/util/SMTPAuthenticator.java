/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.common.util;

import javax.mail.PasswordAuthentication;

/**
 *
 * @author hanfengwei
 */
public class SMTPAuthenticator extends javax.mail.Authenticator{
    private static final String SMTP_AUTH_USER = "merlionbankes05";
    private static final String SMTP_AUTH_PWD = "waveswaves";
    
    public SMTPAuthenticator() {
    }
    
    @Override
    public PasswordAuthentication getPasswordAuthentication() {
        String username = SMTP_AUTH_USER;
        String password = SMTP_AUTH_PWD;
        return new PasswordAuthentication(username, password);
    }
}
