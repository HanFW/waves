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
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import org.primefaces.event.RowEditEvent;
import ejb.infrastructure.session.EmployeeAdminSessionBeanLocal;
import ejb.infrastructure.session.EmployeeEmailSessionBeanLocal;
import java.io.IOException;

/**
 *
 * @author Jingyuan
 */
@ManagedBean(name = "employeeAccountManagedBean")
@SessionScoped

public class EmployeeAccountManagedBean implements Serializable {

    /**
     * Creates a new instance of LoginManagedBean
     */
    @EJB
    private EmployeeAdminSessionBeanLocal adminSessionBeanLocal;
    @EJB
    private EmployeeEmailSessionBeanLocal sendEmailSessionBeanLocal;

    @ManagedProperty(value = "#{employeeLoginManagedBean}")
    private EmployeeLoginManagedBean login;

    private Long employeeId;
    private String employeeName;
    private String employeeDepartment;
    private String employeePosition;
    private String employeeNRIC;
    private Integer employeeMobileNum;
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
    private boolean loggedIn;
    private String employeeStatus;
    //    private Set<String> updatedRoles;

    /**
     * Creates a new instance of loginManagedBean
     */
    public EmployeeAccountManagedBean() {
    }

    /**
     *
     * @param event
     * @return
     */
    @PostConstruct
    public void init() {
        // User is available here for the case you'd like to work with it
////        // directly after bean's construction.
    }

    public boolean getLoggedIn() {
        if (login != null) {
            loggedIn = login.isLoggedIn();
        }
        return loggedIn;
    }

    public EmployeeLoginManagedBean getLogin() {
        return login;
    }

    public void setLogin(EmployeeLoginManagedBean login) {
        this.login = login;
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
    


    public void updateAccountInfo(ActionEvent event) {
        adminSessionBeanLocal.updateEmployeeAccount(employeeName,
                employeeDepartment, employeePosition, employeeId, employeeMobileNum.toString(),
                employeeEmail, selectedRoles);
    }

    public List<Employee> getEmployees() {

        if (employees == null) {
            employees = adminSessionBeanLocal.getEmployees();;
        }
        return employees;
    }
    
      public List<Employee> getArchivedEmployees() {

        if (employees == null) {
            employees = adminSessionBeanLocal.getArchivedEmployees();;
        }
        return employees;
    }

    public List<String> getDepartments() {
        if (departments == null) {
            departments = adminSessionBeanLocal.getEmployeeDepartments();
        }
        return departments;

    }

    public List<String> getPositions() {

        if (positions == null) {
            positions = adminSessionBeanLocal.getEmployeePositions();
        }
        return positions;
    }

    public List<String> getRoles() {

        if (roles == null) {
            roles = adminSessionBeanLocal.getRoles();
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
        adminSessionBeanLocal.editUserAccount(employee.getEmployeeId(), employee.getEmployeeName(), employee.getEmployeeDepartment(),
                employee.getEmployeePosition(), employee.getEmployeeMobileNum(), employee.getEmployeeEmail());
        employeeName = employee.getEmployeeName();
        System.out.println("employee name: " + employeeName);

        FacesMessage msg = new FacesMessage("User Account Edited", ((Employee) event.getObject()).getEmployeeName());

        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    public void onRowCancel(RowEditEvent event) {
        FacesMessage msg = new FacesMessage("Edit Action Cancelled");
        FacesContext.getCurrentInstance().addMessage(null, msg);

    }

//    public void editUserAccountInfo(ActionEvent event) throws IOException {
//        System.out.println("*** AccountManagedBean: employee id before redirecting: "+employeeId+" test");
//        FacesContext context = FacesContext.getCurrentInstance();
//        context.getExternalContext().redirect("editAccountInfo.xhtml");
//        System.out.println("*** AccountManagedBean: redirect to edit account page");
//    }
    public void deleteAccount(Employee employee) throws IOException {
//        System.out.println("hi");
        FacesMessage message = null;
        FacesContext context = FacesContext.getCurrentInstance();

        System.out.println("===== AcocuntManagedBean: deleteAccount =====");
        String msg = adminSessionBeanLocal.deleteEmployee(employee);

        if (msg.equals("success")) {

            context.getExternalContext().redirect(context.getExternalContext().getRequestContextPath() + "/web/internalSystem/infrastructure/employeeUserAccountManagement.xhtml");
            message = new FacesMessage(FacesMessage.SEVERITY_INFO, "User Account ARchived!", "User account has been successfully archived");
            context.addMessage(null, message);
            System.out.println("*** AccountManagedBean: account deleted");
        }

    }

    public void deleteCancel(Employee employee) {
//        System.out.println("hi jojo");
        System.out.println("===== AcocuntManagedBean: deleteCancel =====");
        FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Archive Action Cancelled", "Archive Action Cancelled");
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    public void resetPassword(ActionEvent event) {
        FacesMessage message = null;
        FacesContext context = FacesContext.getCurrentInstance();
        System.out.println("***AccountManagedBean - email: " + employeeEmail);
        String msg = sendEmailSessionBeanLocal.resetPwd(employeeNRIC, employeeEmail);

        if (msg.equals("valid")) {
            message = new FacesMessage(FacesMessage.SEVERITY_INFO, "A new password has been sent to your email!", "A new password has been sent to your email!");
            context.addMessage(null, message);
            System.out.println("*** AccountManagedBean: new password has been sent");

        } else {
            message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Account does not exist, please check your NRIC!", "Account not exist!");
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
        System.out.println("*** AccountManagedBean: get employee Id!!!" + employeeId);
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

    public Employee getEmployee() {
        employee = adminSessionBeanLocal.getEmployeeById(employeeId);
        System.out.println("*** AccountManagedBean: get employee!!!" + employee.getEmployeeName());
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
//        adminSessionBeanLocal.setSelectedRoles(employeeId,selectedRoles);
        System.out.println("*** AccountManagedBean - setSelectedRoles");
        this.selectedRoles = selectedRoles;
    }
//
//    public Set<String> getUpdatedRoles() {
//        updatedRoles = selectedRoles;
//        return updatedRoles;
//    }
//

//    public void setUpdatedRoles() {
//        System.out.println("====== updated roles: " + selectedRoles);
//        adminSessionBeanLocal.setSelectedRoles(employeeId, selectedRoles);
//        System.out.println("*** AccountManagedBean - setSelectedRoles");
//    }
    public boolean hasRoleCounterTeller() {

        FacesContext context = FacesContext.getCurrentInstance();
        employee = (Employee) context.getExternalContext().getSessionMap().get("employee");

        Role hasRole = adminSessionBeanLocal.getRoleByName("Counter Teller");
        return employee.getRole().contains(hasRole);
    }

    public boolean hasRoleEnquiryManager() {

        FacesContext context = FacesContext.getCurrentInstance();
        employee = (Employee) context.getExternalContext().getSessionMap().get("employee");

        Role hasRole = adminSessionBeanLocal.getRoleByName("Enquiry Manager");
        return employee.getRole().contains(hasRole);
    }

    public boolean hasRoleCallCenterStaff() {

        FacesContext context = FacesContext.getCurrentInstance();
        employee = (Employee) context.getExternalContext().getSessionMap().get("employee");

        Role hasRole = adminSessionBeanLocal.getRoleByName("Call Center Staff");
        return employee.getRole().contains(hasRole);
    }

    public boolean hasRoleRelationshipManager() {

        FacesContext context = FacesContext.getCurrentInstance();
        employee = (Employee) context.getExternalContext().getSessionMap().get("employee");

        Role hasRole = adminSessionBeanLocal.getRoleByName("Relationship Manager");
        return employee.getRole().contains(hasRole);
    }

    public boolean hasRoleCardDepartmentManager() {

        FacesContext context = FacesContext.getCurrentInstance();
        employee = (Employee) context.getExternalContext().getSessionMap().get("employee");

        Role hasRole = adminSessionBeanLocal.getRoleByName("Card Department Manager");
        return employee.getRole().contains(hasRole);
    }

    public boolean hasRoleLoanOfficer() {

        FacesContext context = FacesContext.getCurrentInstance();
        employee = (Employee) context.getExternalContext().getSessionMap().get("employee");

        Role hasRole = adminSessionBeanLocal.getRoleByName("Loan Officer");
        return employee.getRole().contains(hasRole);
    }

    public boolean hasRoleUnderwriter() {

        FacesContext context = FacesContext.getCurrentInstance();
        employee = (Employee) context.getExternalContext().getSessionMap().get("employee");

        Role hasRole = adminSessionBeanLocal.getRoleByName("Underwriter");
        return employee.getRole().contains(hasRole);
    }

    public boolean hasRoleSalesDepartmentManager() {

        FacesContext context = FacesContext.getCurrentInstance();
        employee = (Employee) context.getExternalContext().getSessionMap().get("employee");

        Role hasRole = adminSessionBeanLocal.getRoleByName("Sales Department Manager");
        return employee.getRole().contains(hasRole);
    }

    public boolean hasRoleMortgageAppraiser() {

        FacesContext context = FacesContext.getCurrentInstance();
        employee = (Employee) context.getExternalContext().getSessionMap().get("employee");

        Role hasRole = adminSessionBeanLocal.getRoleByName("Mortgage Appraiser");
        return employee.getRole().contains(hasRole);
    }

    public boolean hasRoleCEO() {

        FacesContext context = FacesContext.getCurrentInstance();
        employee = (Employee) context.getExternalContext().getSessionMap().get("employee");

        Role hasRole = adminSessionBeanLocal.getRoleByName("CEO");
        return employee.getRole().contains(hasRole);
    }

    public boolean hasRoleSystemAdmin() {

        FacesContext context = FacesContext.getCurrentInstance();
        employee = (Employee) context.getExternalContext().getSessionMap().get("employee");

        Role hasRole = adminSessionBeanLocal.getRoleByName("System Admin");
        return employee.getRole().contains(hasRole);
    }

}
