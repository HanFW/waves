/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.infrastructure.session;

import ejb.customer.entity.CustomerBasic;
import javax.ejb.Local;

/**
 *
 * @author hanfengwei
 */
@Local
public interface SMSSessionBeanLocal {
    public void sendOTP(String target, CustomerBasic customer);
}
