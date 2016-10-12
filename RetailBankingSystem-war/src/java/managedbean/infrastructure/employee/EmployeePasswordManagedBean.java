/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbean.infrastructure.employee;

import ejb.infrastructure.entity.Employee;
import ejb.infrastructure.session.EmployeeAdminSessionBeanLocal;
import ejb.infrastructure.session.EmployeeEmailSessionBeanLocal;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.Dependent;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

/**
 *
 * @author Jingyuan
 */
@Named(value = "employeePasswordManagedBean")
@RequestScoped
public class EmployeePasswordManagedBean {

    @EJB
    private EmployeeAdminSessionBeanLocal adminSessionBeanLocal;
    @EJB
    private EmployeeEmailSessionBeanLocal sendEmailSessionBeanLocal;

    private Employee employee;
    private String currentPassword;
    private String newPassword;
    private String employeeEmail;
    private String employeeNRIC;

    /**
     * Creates a new instance of EmployeePasswordManagedBean
     */
    public EmployeePasswordManagedBean() {
    }

    public void resetPassword(ActionEvent event) {
        FacesMessage message = null;
        FacesContext context = FacesContext.getCurrentInstance();
        System.out.println("***PasswordManagedBean - email: " + employeeEmail);
        String msg = sendEmailSessionBeanLocal.resetPwd(employeeNRIC, employeeEmail);

        if (msg.equals("valid")) {
            message = new FacesMessage(FacesMessage.SEVERITY_INFO, "A new password has been sent to your email!", "A new password has been sent to your email!");
            context.addMessage(null, message);
            System.out.println("*** PasswordManagedBean: new password has been sent");

        } else if (msg.equals("emailInvalid")) {
            message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Email entered is not your registered Email, please check it and enter again!", "Email invalid!");
            context.addMessage(null, message);
            System.out.println("*** PasswordManagedBean: email invalid");
        } else {
            message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Account does not exist, please check your NRIC!", "Account not exist!");
            context.addMessage(null, message);
            System.out.println("*** PasswordManagedBean: account invalid");
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

    public String getEmployeeEmail() {
        return employeeEmail;
    }

    public void setEmployeeEmail(String employeeEmail) {
        this.employeeEmail = employeeEmail;
    }

    public String getEmployeeNRIC() {
        return employeeNRIC;
    }

    public void setEmployeeNRIC(String employeeNRIC) {
        this.employeeNRIC = employeeNRIC;
    }

}
