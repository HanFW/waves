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
import javax.faces.bean.ManagedProperty;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import org.primefaces.event.RowEditEvent;
import ejb.infrastructure.session.EmployeeAdminSessionBeanLocal;
import ejb.infrastructure.session.EmployeeEmailSessionBeanLocal;
import ejb.infrastructure.session.LoggingSessionBeanLocal;
import java.io.IOException;
import static java.time.Clock.system;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.ExternalContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

/**
 *
 * @author Jingyuan
 */
@Named(value = "employeeAccountManagedBean")
@RequestScoped

public class EmployeeAccountManagedBean implements Serializable {

    /**
     * Creates a new instance of LoginManagedBean
     */
    @EJB
    private EmployeeAdminSessionBeanLocal adminSessionBeanLocal;
    @EJB
    private EmployeeEmailSessionBeanLocal sendEmailSessionBeanLocal;
    @EJB
    private LoggingSessionBeanLocal loggingSessionBeanLocal;

    private Long employeeId;
    private String employeeName;
    private String employeeDepartment;
    private String employeePosition;
    private String employeeNRIC;
    private Integer employeeMobileNum;
    private String employeeEmail;
    private Employee employee;
   
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


    public List<Employee> getEmployees() {

        if (employees == null) {
            employees = adminSessionBeanLocal.getEmployees();;
            System.out.println(employees);
        }
        
        System.out.println("===== AcocuntManagedBean: get current employees =====" + employees);
        System.out.println(employees);
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




    public void deleteCancel(Employee employee) {

        System.out.println("===== AcocuntManagedBean: deleteCancel =====");
        FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Archive Action Cancelled", "Archive Action Cancelled");
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    

    public Long getEmployeeId() {
        System.out.println("*** AccountManagedBean: get employee Id!!!" + employeeId);
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        System.out.println("===== AcocuntManagedBean: set employee Id!! =====" + employeeId);
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

        System.out.println("*** AccountManagedBean: get employee by Id!!!" + employee.getEmployeeName());
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }


    public Set<Role> getRole() {
        return role;
    }

    public void setRole(Set<Role> role) {
        this.role = role;
    }

//    public Set<String> getSelectedRoles() {
//        System.out.println("------------ getSelectedRoles: " + selectedRoles);
//        return selectedRoles;
//    }

    public String getTargetEmployeeDepartment() {
        employee = adminSessionBeanLocal.getEmployeeById(employeeId);
        return employee.getEmployeeDepartment();
//        return targetEmployeeDepartment;
    }
//
//    public void setSelectedRoles(Set<String> selectedRoles) {
//        System.out.println("*** AccountManagedBean - setSelectedRoles");
//        this.selectedRoles = selectedRoles;
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

    public boolean hasRoleDepositSpecialist() {
        FacesContext context = FacesContext.getCurrentInstance();
        employee = (Employee) context.getExternalContext().getSessionMap().get("employee");

        Role hasRole = adminSessionBeanLocal.getRoleByName("Deposit Specialist");
        return employee.getRole().contains(hasRole);
    }

    public boolean hasRoleLoanSpecialist() {
        FacesContext context = FacesContext.getCurrentInstance();
        employee = (Employee) context.getExternalContext().getSessionMap().get("employee");

        Role hasRole = adminSessionBeanLocal.getRoleByName("Loan Specialist");
        return employee.getRole().contains(hasRole);
    }

    public boolean hasRoleCardSpecialist() {
        FacesContext context = FacesContext.getCurrentInstance();
        employee = (Employee) context.getExternalContext().getSessionMap().get("employee");

        Role hasRole = adminSessionBeanLocal.getRoleByName("Card Specialist");
        return employee.getRole().contains(hasRole);
    }

    public boolean hasRoleOperationSpecialist() {
        FacesContext context = FacesContext.getCurrentInstance();
        employee = (Employee) context.getExternalContext().getSessionMap().get("employee");

        Role hasRole = adminSessionBeanLocal.getRoleByName("Operation Specialist");
        return employee.getRole().contains(hasRole);
    }

    public boolean hasRoleWealthManagementSpecialist() {
        FacesContext context = FacesContext.getCurrentInstance();
        employee = (Employee) context.getExternalContext().getSessionMap().get("employee");

        Role hasRole = adminSessionBeanLocal.getRoleByName("Wealth Management Specialist");
        return employee.getRole().contains(hasRole);
    }

    public boolean hasRoleSystemAdmin() {

        FacesContext context = FacesContext.getCurrentInstance();
        employee = (Employee) context.getExternalContext().getSessionMap().get("employee");

        Role hasRole = adminSessionBeanLocal.getRoleByName("System Admin");
        return employee.getRole().contains(hasRole);
    }

    public boolean findSystemAdmins(Employee employee) {

        Role hasRole = adminSessionBeanLocal.getRoleByName("System Admin");
        return employee.getRole().contains(hasRole);
    }

    public String getUserName(){
    
        FacesContext context = FacesContext.getCurrentInstance();
        employee = (Employee) context.getExternalContext().getSessionMap().get("employee");
        return employee.getEmployeeName();
}
    
     public String getUserDepartment(){
    
        FacesContext context = FacesContext.getCurrentInstance();
        employee = (Employee) context.getExternalContext().getSessionMap().get("employee");
        return employee.getEmployeeDepartment();
}
    
      public String getUserPosition(){
    
        FacesContext context = FacesContext.getCurrentInstance();
        employee = (Employee) context.getExternalContext().getSessionMap().get("employee");
        return employee.getEmployeePosition();
}
 

}
