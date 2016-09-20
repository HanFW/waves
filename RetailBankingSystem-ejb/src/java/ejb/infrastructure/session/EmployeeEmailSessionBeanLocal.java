/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.infrastructure.session;

import ejb.infrastructure.entity.Employee;
import javax.ejb.Local;

/**
 *
 * @author Jingyuan
 */
@Local
public interface EmployeeEmailSessionBeanLocal {
    public String resetPwd(String employeeEmail);
    
    public String changePwd(String currentPassword,String newPassword,Long employeeId);
    
    public String initialPwd(String employeeNRIC,String employeeEmail);
}
