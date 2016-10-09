/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbean.infrastructure.employee;

import ejb.infrastructure.entity.Employee;
import ejb.infrastructure.session.EmployeeAdminSessionBeanLocal;
import ejb.infrastructure.session.EmployeeEmailSessionBeanLocal;
import java.io.IOException;
import java.util.List;
import java.util.Set;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

/**
 *
 * @author Jingyuan
 */
@Named(value = "employeeCreateUserAccountManagedBean")
@RequestScoped
public class EmployeeCreateUserAccountManagedBean {

    @EJB
    private EmployeeAdminSessionBeanLocal adminSessionBeanLocal;
    @EJB
    private EmployeeEmailSessionBeanLocal sendEmailSessionBeanLocal;

    private Employee employee;
    private String employeeName;
    private String employeeDepartment;
    private String employeePosition;
    private String employeeNRIC;
    private Integer employeeMobileNum;
    private String employeeEmail;
    private String logInStatus;
    private Set<String> selectedRoles;
    private List<String> roles;

    /**
     * Creates a new instance of EmployeeCreateUserAccountManagedBean
     */
    public EmployeeCreateUserAccountManagedBean() {
    }

    public void createAccount(ActionEvent event) throws IOException {

        FacesMessage message = null;
        FacesContext context = FacesContext.getCurrentInstance();

        String newEmployee = adminSessionBeanLocal.createEmployeeAccount(employeeName,
                employeeDepartment, employeePosition, employeeNRIC, employeeMobileNum.toString(),
                employeeEmail, selectedRoles);

        sendEmailSessionBeanLocal.initialPwd(employeeNRIC, employeeEmail);

        if (newEmployee.equals("existing account")) {

            message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Error! Account Existed", "Error!The employee account has already Existed");
            context.addMessage(null, message);
            System.out.println("*** AccountManagedBean: account existed");
        } else {

            message = new FacesMessage(FacesMessage.SEVERITY_INFO, "A new employee account has been successfully created", "Account created.");
            context.addMessage(null, message);
            System.out.println("*** AccountManagedBean: account created");

        }

    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
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

    public Integer getEmployeeMobileNum() {
        return employeeMobileNum;
    }

    public void setEmployeeMobileNum(Integer employeeMobileNum) {
        this.employeeMobileNum = employeeMobileNum;
    }

    public String getEmployeeEmail() {
        return employeeEmail;
    }

    public void setEmployeeEmail(String employeeEmail) {
        this.employeeEmail = employeeEmail;
    }

    public Set<String> getSelectedRoles() {
        return selectedRoles;
    }

    public void setSelectedRoles(Set<String> selectedRoles) {
        this.selectedRoles = selectedRoles;
    }
    
     public List<String> getRoles() {

        if (roles == null) {
            roles = adminSessionBeanLocal.getRoles();
        }
        return roles;
    }
    
    
}
