package managedbean.infrastructure;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import ejb.infrastructure.entity.Employee;
import java.io.IOException;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.persistence.Query;
import ejb.infrastructure.session.CustomerAdminSessionBeanLocal;
import ejb.infrastructure.session.EmployeeAdminSessionBeanLocal;

/**
 *
 * @author Jingyuan
 */
@ManagedBean(name = "employeeLoginManagedBean")
@SessionScoped
public class EmployeeLoginManagedBean implements Serializable {

    /**
     * Creates a new instance of EmployeeLoginManagedBean
     */
    @EJB
    private EmployeeAdminSessionBeanLocal adminSessionBeanLocal;

    private String employeeAccountNum;
    private String employeePassword;
    private Employee employee;
    private String currentPassword;
    private String newPassword;
    private boolean loggedIn = false;
    private boolean ArchiveStatus = false;

    /**
     * Creates a new instance of loginManagedBean
     */
    public EmployeeLoginManagedBean() {
    }

    /**
     *
     * @param event
     */
    public void doLogin(ActionEvent event) {

        FacesMessage message = null;
        FacesContext context = FacesContext.getCurrentInstance();

        String status = adminSessionBeanLocal.login(employeeAccountNum, employeePassword);
        switch (status) {
            case "loggedIn":
//                message = new FacesMessage(FacesMessage.SEVERITY_INFO, status, "Welcome back!");
                System.out.println("***LoginManagedBean: loggedIn");
                context.getExternalContext().getSessionMap().put("employee", getEmployee());
                loggedIn = true;
                try {
                    context.getExternalContext().redirect(context.getExternalContext().getRequestContextPath()+"/web/internalSystem/infrastructure/employeeMainPage.xhtml");
                    message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Welcome " + employee.getEmployeeName() + " !", "Welcome message");
                    context.addMessage(null, message);
                    System.out.println("*** LoginManagedBean: welcome message");
                } catch (IOException ex) {
                    Logger.getLogger(EmployeeLoginManagedBean.class.getName()).log(Level.SEVERE, null, ex);
                }
                break;
            case "invalidPassword":
                message = new FacesMessage(FacesMessage.SEVERITY_INFO, status, "Invalid employee Password/account.");
                context.addMessage(null, message);
                System.out.println("*** LoginManagedBean: invalid password");
                break;
            default:
                message = new FacesMessage(FacesMessage.SEVERITY_INFO, status, "Please check your account number.");
                context.addMessage(null, message);
                System.out.println("*** LoginManagedBean: invalid account");
                break;
        }
    }

    public void doLogOut(ActionEvent event) throws IOException {

        FacesContext context = FacesContext.getCurrentInstance();
        context.getExternalContext().invalidateSession();
        loggedIn = false;
        System.out.println("***LoginManagedBean: session invalidated");
        try {
            context.getExternalContext().redirect(context.getExternalContext().getRequestContextPath() + "/web/internalSystem/infrastructure/employeeLogout.xhtml");
        } catch (IOException ex) {
            Logger.getLogger(EmployeeLoginManagedBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void forgetPassword(ActionEvent event) throws IOException {

        FacesContext context = FacesContext.getCurrentInstance();

        context.getExternalContext().redirect(context.getExternalContext().getRequestContextPath() + "/web/internalSystem/infrastructure/employeeForgetPassword.xhtml");
        
    }

    public void changePassword(ActionEvent event) throws IOException {
        FacesContext context = FacesContext.getCurrentInstance();

        context.getExternalContext().redirect(context.getExternalContext().getRequestContextPath() + "/web/internalSystem/infrastructure/employeeChangePassword.xhtml");
    }

    public void setEmployeeAccountNum(String employeeAccountNum) {
        this.employeeAccountNum = employeeAccountNum;
    }

    public void setEmployeePassword(String employeePassword) {
        this.employeePassword = employeePassword;
    }

    public String getEmployeeAccountNum() {
        return employeeAccountNum;
    }

    public String getEmployeePassword() {
        return employeePassword;
    }

    public Employee getEmployee() {
        employee = adminSessionBeanLocal.getEmployeeByAccountNum(employeeAccountNum);
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }

    public void setLoggedIn(boolean loggedIn) {
        this.loggedIn = loggedIn;
    }

}
