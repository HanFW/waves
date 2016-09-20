package managedbean.infrastructure;

//package util;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import ejb.infrastructure.entity.Employee;
import ejb.infrastructure.entity.Role;
import java.io.Serializable;
import java.util.List;
import java.util.Set;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import org.primefaces.event.RowEditEvent;
import ejb.infrastructure.session.CustomerAdminSessionBeanLocal;
import ejb.infrastructure.session.EmployeeAdminSessionBeanLocal;
import ejb.infrastructure.session.EmployeeEmailSessionBeanLocal;

/**
 *
 * @author Jingyuan
 */
@ManagedBean
@SessionScoped
public class EmployeeAccountManagedBean implements Serializable {

    /**
     * Creates a new instance of LoginManagedBean
     */
    @EJB
    private EmployeeAdminSessionBeanLocal adminSessionBeanLocal;
    @EJB
    private EmployeeEmailSessionBeanLocal sendEmailSessionBeanLocal;

    private Long employeeId;
    private String employeeName;
    private String employeeDepartment;
    private String employeePosition;
    private String employeeNRIC;
    private String employeeMobileNum;
    private String employeeEmail;
    private Employee employee;
    private String currentPassword;
    private String newPassword;
    private List<Employee> employees;
    private List<String> departments;
    private List<String> positions;
    private Set<Role> role;
    private Set<String> selectedRoles;
    private List<String> roles;

    /**
     * Creates a new instance of loginManagedBean
     */
    public EmployeeAccountManagedBean() {
    }

    /**
     *
     * @param event
     */
    public void createAccount(ActionEvent event) {

        FacesMessage message = null;
        FacesContext context = FacesContext.getCurrentInstance();
        
        String newEmployee = adminSessionBeanLocal.createEmployeeAccount(employeeName,
                employeeDepartment, employeePosition, employeeNRIC, employeeMobileNum,
                employeeEmail,selectedRoles);
        
        sendEmailSessionBeanLocal.initialPwd(employeeNRIC,employeeEmail);

        if (newEmployee.equals("existing account")) {
            message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Error! Account Existed", "Error!The employee account has already Existed");
            context.addMessage(null, message);
            System.out.println("*** AccountManagedBean: account existed");
        } else {
            message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Account Created", "A new employee account has been successfully created.");
            context.addMessage(null, message);
            System.out.println("*** AccountManagedBean: account created");
        }

    }

    public List<Employee> getEmployees() {
  
        if(employees==null){
            employees=adminSessionBeanLocal.getEmployees();;
        }
        return employees;
    }
    
    
     public List<String> getDepartments() {
       if(departments==null){
       departments=adminSessionBeanLocal.getEmployeeDepartments();
       }
       return departments;
    
     }
     
      public List<String> getPositions() {
  
        if(positions==null){
        positions= adminSessionBeanLocal.getEmployeePositions();
        }
        return positions;
    }
      
       public List<String> getRoles() {
  
        if(roles==null){
        roles= adminSessionBeanLocal.getRoles();
        }
        return roles;
    }

//    public List<Employee> getEmployeeByDepartment(String employeeDepartment) {
//
//        List<Employee> employees = adminSessionBeanLocal.filterAccountByDepartment(employeeDepartment);
//        return employees;
//    }

    public void onRowEdit(RowEditEvent event) {

        employee = (Employee) event.getObject();
        adminSessionBeanLocal.editUserAccount(employee.getEmployeeId(),employee.getEmployeeName(),employee.getEmployeeDepartment(),
                employee.getEmployeePosition(),employee.getEmployeeMobileNum(),employee.getEmployeeEmail());
        employeeName = employee.getEmployeeName();
        System.out.println("employee name: " + employeeName);

        FacesMessage msg = new FacesMessage("User Account Edited", ((Employee) event.getObject()).getEmployeeName());

        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    public void onRowCancel(RowEditEvent event) {
        FacesMessage msg = new FacesMessage("Edit Cancelled");
        FacesContext.getCurrentInstance().addMessage(null, msg);

    }

    public void deleteAccount(Employee employee) {
        FacesMessage message = null;
        FacesContext context = FacesContext.getCurrentInstance();

        String msg = adminSessionBeanLocal.deleteEmployee(employee);

        if (msg.equals("success")) {
            message = new FacesMessage(FacesMessage.SEVERITY_INFO, "User Account Deleted!", "User account has been successfully deleted");
            context.addMessage(null, message);
            System.out.println("*** AccountManagedBean: account deleted");
        }

    }

    public void resetPassword(ActionEvent event) {
        FacesMessage message = null;
        FacesContext context = FacesContext.getCurrentInstance();
        System.out.println("***AccountManagedBean - email: " + employeeEmail);
        String msg = sendEmailSessionBeanLocal.resetPwd(employeeEmail);

        if (msg.equals("valid")) {
            message = new FacesMessage(FacesMessage.SEVERITY_INFO, "A new password has been sent to your email!", "A new password has been sent to your email!");
            context.addMessage(null, message);
            System.out.println("*** AccountManagedBean: new password has been sent");

        } else {
            message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Email account invalid!", "Email account invalid!");
            context.addMessage(null, message);
            System.out.println("*** AccountManagedBean: email account invalid");
        }

    }

    public void changePassword(ActionEvent event) {
        FacesMessage message = null;
        FacesContext context = FacesContext.getCurrentInstance();
        System.out.println("***AccountManagedBean - pwd: " + currentPassword);
        employee = (Employee) context.getExternalContext().getSessionMap().get("employee");
        System.out.println("***AccountManagedBean - employee: " + employee.getEmployeeName());
        String msg = sendEmailSessionBeanLocal.changePwd(currentPassword, newPassword, employee.getEmployeeId());

        if (msg.equals("success")) {
            message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Your password has been Successfully changed!", "Your password has been Successfully changed!");
            context.addMessage(null, message);
            System.out.println("*** AccountManagedBean: password has been changed");

        } else if (msg.equals("invalid")) {
            message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Password invalid!", "Password invalid!");
            context.addMessage(null, message);
            System.out.println("*** AccountManagedBean: Password invalid");
        } else {
            message = new FacesMessage(FacesMessage.SEVERITY_INFO, "New password cannot be the same as current password!", "New password cannot be the same as current password!");
            context.addMessage(null, message);
            System.out.println("*** AccountManagedBean: New password equals current password");
        }
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getEmployeeDepartment() {
        return employeeDepartment;
    }

    public void setEmployeeDepartment(String employeeDepartment) {
        this.employeeDepartment = employeeDepartment;
    }

    public String getEmployeePosition() {
        return employeePosition;
    }

    public void setEmployeePosition(String employeePosition) {
        this.employeePosition = employeePosition;
    }

    public String getEmployeeNRIC() {
        return employeeNRIC;
    }

    public void setEmployeeNRIC(String employeeNRIC) {
        this.employeeNRIC = employeeNRIC;
    }

    public String getEmployeeMobileNum() {
        return employeeMobileNum;
    }

    public void setEmployeeMobileNum(String employeeMobileNum) {
        this.employeeMobileNum = employeeMobileNum;
    }

    public String getEmployeeEmail() {
        return employeeEmail;
    }

    public void setEmployeeEmail(String employeeEmail) {
        this.employeeEmail = employeeEmail;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public String getCurrentPassword() {
        return currentPassword;
    }

    public void setCurrentPassword(String currentPassword) {
        this.currentPassword = currentPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public Set<Role> getRole() {
        return role;
    }

    public void setRole(Set<Role> role) {
        this.role = role;
    }

    public Set<String> getSelectedRoles() {
        return selectedRoles;
    }

    public void setSelectedRoles(Set<String> selectedRoles) {
        this.selectedRoles = selectedRoles;
    }
  
}
