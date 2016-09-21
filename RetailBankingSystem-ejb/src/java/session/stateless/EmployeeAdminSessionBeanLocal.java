/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package session.stateless;

import entity.Employee;
import entity.Role;
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
    
    public String createEmployeeAccount(String employeeName, String employeeDepartment,
    String employeePosition, String employeeNRIC, String employeeMobileNum, String employeeEmail,
    Set<String> roles);
    
    public void updateEmployeeAccount(String employeeName, String employeeDepartment,
    String employeePosition, Long employeeId, String employeeMobileNum, String employeeEmail,
    Set<String> roles);
    
    public Set<String> getSelectedRoles(Long employeeId);
    
    public void setSelectedRoles(Long employeeId,Set<String> selectedRoles);
    
    public List<Employee> filterAccountByDepartment(String employeeDepartment);
    
    public List<Employee> getEmployees();
    
    public Employee getEmployeeById(Long employeeId);
    
    public List<String> getEmployeeDepartments();
    
    public List<String> getEmployeePositions();
    
    public String deleteEmployee(Employee employee);
    
    public void editUserAccount(Long employeeId,String employeeName,String employeeDepartment,
            String employeePosition,String employeeMobileNum,String employeeEmail);
    
    public List<String> getRoles();
    
    public Role getRoleByName(String roleName);
    
}
