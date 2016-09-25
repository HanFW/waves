/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.infrastructure.session;

import ejb.infrastructure.entity.Employee;
import ejb.infrastructure.entity.Permission;
import ejb.infrastructure.entity.Role;
import java.util.List;
import java.util.Set;
import javax.ejb.Local;

/**
 *
 * @author Jingyuan
 */
@Local
public interface EmployeeAuthorizationFilterSessionBeanLocal {
    
    public boolean authorizationCheck(Employee user, String path);
}
