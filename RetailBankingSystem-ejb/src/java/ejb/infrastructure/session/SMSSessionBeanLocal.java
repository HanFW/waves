/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.infrastructure.session;

import javax.ejb.Local;
import org.jboss.aerogear.security.otp.Totp;

/**
 *
 * @author hanfengwei
 */
@Local
public interface SMSSessionBeanLocal {
    public String sendOTP(String target, String phoneNum);
}
