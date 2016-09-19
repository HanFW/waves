/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package session.stateless;

import entity.Employee;
import javax.ejb.Local;

/**
 *
 * @author Jingyuan
 */
@Local
public interface SendEmailSessionBeanLocal {
    public String resetPwd(String employeeEmail);
    
    public String changePwd(String currentPassword,String newPassword,Long employeeId);
}
