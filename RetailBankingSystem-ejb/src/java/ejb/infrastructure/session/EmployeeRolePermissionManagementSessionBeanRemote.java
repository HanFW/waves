/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.infrastructure.session;

import javax.ejb.Remote;

/**
 *
 * @author Nicole
 */
@Remote
public interface EmployeeRolePermissionManagementSessionBeanRemote {

    public String[] getPermissions(String roleName);

    public String[] getPermissionList();
}
