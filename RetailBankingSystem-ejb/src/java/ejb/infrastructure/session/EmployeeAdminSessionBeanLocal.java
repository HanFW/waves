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
public interface EmployeeAdminSessionBeanLocal {

    public String login(String accountNum, String password);
    
    public Employee getEmployeeByAccountNum(String accountNum);
    
    public Role findRole(Long RoleId);
    
    public String createEmployeeAccount(String employeeName, String employeeGender,String employeeDepartment,
    String employeePosition, String employeeNRIC, String employeeMobileNum, String employeeEmail,
    Set<String> roles); 
    
    public void updateEmployeeAccount(Long id, String name, String department, String position, String mobile, String email, Set<String> roles);
    
    public Set<String> getSelectedRoles(Long employeeId);
    
    public void setSelectedRoles(Long employeeId,Set<String> selectedRoles);
    
    public List<Employee> filterAccountByDepartment(String employeeDepartment);
    
    public List<Employee> getEmployees();
    
    public List<Role> getAllRoles();
    
    public List<Employee> getArchivedEmployees();
    
    public Employee getEmployeeById(Long employeeId);
    
    public List<String> getEmployeeDepartments();
    
    public List<String> getEmployeePositions();
    
    public List<String> getEmployeeGenders();
    
    public String deleteEmployee(Employee employee);
    
    public void editUserAccount(Long employeeId,String employeeName,String employeeDepartment,
            String employeePosition,String employeeMobileNum,String employeeEmail);
    
    public List<String> getPositionsByDepartment(String department);
    
    public List<String> getRolesByDepartment(String department);
    
    public List<Permission> getPermissionList(String roleName);
    
    public String[] getSelectedPermissionList(Long roleId);
    
    public void setSelectedPermissionList(String roleName,String[] selectedPermission);
    
    public Role getRoleByName(String roleName);
    
    public void deletePermission(String roleName,String permissionName);
    
    public String addPermissionToRole(String roleName, String permissionName);
    
    
}
