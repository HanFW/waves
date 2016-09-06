/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package session.stateless;

import entity.CustomerBasic;
import javax.ejb.Local;

/**
 *
 * @author hanfengwei
 */
@Local
public interface EmailSessionBeanLocal {
    public void sendEmail(CustomerBasic customer, String subject, Boolean createOnlineAccount, String password);
}
