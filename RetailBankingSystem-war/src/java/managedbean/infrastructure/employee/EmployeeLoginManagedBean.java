package managedbean.infrastructure.employee;

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
import ejb.infrastructure.session.EmployeeAdminSessionBeanLocal;
import javax.faces.context.ExternalContext;
import ejb.infrastructure.session.LoggingSessionBeanLocal;

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

    @EJB
    private LoggingSessionBeanLocal loggingSessionBeanLocal;

    private String employeeAccountNum;
    private String employeePassword;
    private Employee employee;
    private boolean loggedIn = false;
    private boolean ArchiveStatus = false;
    private String logInStatus;

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
                    String logInStatus = getEmployee().getLogInStatus();
                    if (logInStatus.equals("true")) {
                        context.getExternalContext().redirect(context.getExternalContext().getRequestContextPath() + "/web/internalSystem/infrastructure/employeeChangePassword.xhtml?faces-redirect=true");
                    } else {
                        context.getExternalContext().redirect(context.getExternalContext().getRequestContextPath() + "/web/internalSystem/infrastructure/employeeMainPage.xhtml?faces-redirect=true");
                        message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Welcome " + employee.getEmployeeName() + " !", "Welcome message");
                        context.addMessage(null, message);
                        System.out.println("*** LoginManagedBean: welcome message");
                    }

                } catch (IOException ex) {
                    Logger.getLogger(EmployeeLoginManagedBean.class.getName()).log(Level.SEVERE, null, ex);
                }
                break;
            case "invalidPassword":
                message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Invalid employee Password.", "Invalid employee Password.");
                context.addMessage(null, message);
                System.out.println("*** LoginManagedBean: invalid password");
                break;
            case "invalidUser":
                message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Invalid employee Account.", "Invalid employee Account.");
                context.addMessage(null, message);
                System.out.println("*** LoginManagedBean: invalid user account");
                break;
            default:
                message = new FacesMessage(FacesMessage.SEVERITY_INFO, status, "Please check your account number.");
                context.addMessage(null, message);
                System.out.println("*** LoginManagedBean: invalid account");
                break;
        }
        employeeAccountNum = null;
        employeePassword = null;
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

    public Long getEmployeeId() {
        FacesContext context = FacesContext.getCurrentInstance();
        Employee employee = (Employee) context.getExternalContext().getSessionMap().get("employee");
        Long employeeId = employee.getEmployeeId();
        return employeeId;
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

    public void redirectToViewCustomer() throws IOException {
        FacesContext context = FacesContext.getCurrentInstance();
        ExternalContext ec = context.getExternalContext();
        ec.redirect(ec.getRequestContextPath() + "/web/internalSystem/CRM/RMViewCustomerList.xhtml");
    }

    public void redirectToSearchCustomer() throws IOException {
        FacesContext context = FacesContext.getCurrentInstance();
        ExternalContext ec = context.getExternalContext();
        ec.redirect(ec.getRequestContextPath() + "/web/internalSystem/CRM/RMSearchCustomer.xhtml");
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }

    public void setLoggedIn(boolean loggedIn) {
        this.loggedIn = loggedIn;
    }

    public String getLogInStatus() {
        return logInStatus;
    }

    public void setLogInStatus(String logInStatus) {
        this.logInStatus = logInStatus;
    }

}
