/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbean.infrastructure;

import ejb.infrastructure.entity.Employee;
import ejb.infrastructure.session.EmployeeAdminSessionBeanLocal;
import ejb.infrastructure.session.LoggingSessionBeanLocal;
import java.io.Serializable;
import java.util.List;
import java.util.Set;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.event.ActionEvent;

/**
 *
 * @author Jingyuan
 */
@Named(value = "systemAdminEditUserAccountManagedBean")
@SessionScoped
public class SystemAdminEditUserAccountManagedBean implements Serializable {

    @EJB
    private EmployeeAdminSessionBeanLocal employeeAdminSessionBeanLocal;   
    @EJB
    private LoggingSessionBeanLocal loggingSessionBeanLocal;


    private String targetEmployeeName;
    private String targetEmployeeDepartment;
    private String targetEmployeePosition;
    private String targetEmployeeMobile;
    private String targetEmployeeEmail;
    private Set<String> selectedRoles;
    private List<String> roles;
    private Employee employee;
    private Long employeeId;
    private List<String> departments;
    private List<String> positions;

    /**
     * Creates a new instance of SystemAdminEditUserAccountManagedBean
     */
    public void updateAccountInfo(ActionEvent event) {

        FacesMessage message = null;
        FacesContext context = FacesContext.getCurrentInstance();

        System.err.println("********** update Account info");
        for (String sr : selectedRoles) {
            System.err.println("********** sr: " + sr);
        }
        employeeAdminSessionBeanLocal.updateEmployeeAccount(employeeId, targetEmployeeName, targetEmployeeDepartment, targetEmployeePosition, targetEmployeeMobile, targetEmployeeEmail, selectedRoles);
        loggingSessionBeanLocal.createNewLogging("employee", getEmployeeIdViaSessionScope(),"System admin updates account information of employee "+ targetEmployeeName,
                "successful",null);
        
        System.out.println("clear");
        employee=null;
        employeeId=null;
        targetEmployeeName = null;
        targetEmployeeDepartment = null;
        targetEmployeePosition = null;
        targetEmployeeMobile = null;
        targetEmployeeEmail = null;
        selectedRoles = null;

        message = new FacesMessage(FacesMessage.SEVERITY_INFO, "User account has been successfully updated!", "account updated");
        context.addMessage(null, message);
        System.out.println("*** AccountManagedBean: account updated");
        
        

    }

    public SystemAdminEditUserAccountManagedBean() {
    }

    public String getTargetEmployeeName() {
        if (targetEmployeeName == null) {
            if (employee == null) {
                employee = employeeAdminSessionBeanLocal.getEmployeeById(employeeId);
            }
            targetEmployeeName = employee.getEmployeeName();
        }
        return targetEmployeeName;
    }

    public void setTargetEmployeeName(String targetEmployeeName) {
        this.targetEmployeeName = targetEmployeeName;
    }

    public String getTargetEmployeeDepartment() {
        if (targetEmployeeDepartment == null) {
            if (employee == null) {
                employee = employeeAdminSessionBeanLocal.getEmployeeById(employeeId);
            }
            targetEmployeeDepartment = employee.getEmployeeDepartment();
        }
        return targetEmployeeDepartment;
    }

    public void setTargetEmployeeDepartment(String targetEmployeeDepartment) {
        this.targetEmployeeDepartment = targetEmployeeDepartment;
    }

    public String getTargetEmployeePosition() {
        if (targetEmployeePosition == null) {
            if (employee == null) {
                employee = employeeAdminSessionBeanLocal.getEmployeeById(employeeId);
            }
            targetEmployeePosition = employee.getEmployeePosition();
        }
        return targetEmployeePosition;
    }

    public void setTargetEmployeePosition(String targetEmployeePosition) {
        this.targetEmployeePosition = targetEmployeePosition;
    }

    public String getTargetEmployeeMobile() {
        if (targetEmployeeMobile == null) {
            if (employee == null) {
                employee = employeeAdminSessionBeanLocal.getEmployeeById(employeeId);
            }
            targetEmployeeMobile = employee.getEmployeeMobileNum();
        }
        return targetEmployeeMobile;
    }

    public void setTargetEmployeeMobile(String targetEmployeeMobile) {
        this.targetEmployeeMobile = targetEmployeeMobile;
    }

    public String getTargetEmployeeEmail() {
        if (targetEmployeeEmail == null) {
            if (employee == null) {
                employee = employeeAdminSessionBeanLocal.getEmployeeById(employeeId);
            }
            targetEmployeeEmail = employee.getEmployeeEmail();
        }
        return targetEmployeeEmail;
    }

    public void setTargetEmployeeEmail(String targetEmployeeEmail) {
        this.targetEmployeeEmail = targetEmployeeEmail;
    }

    public Set<String> getSelectedRoles() {
        if (selectedRoles == null && employeeId != null) {
            selectedRoles = employeeAdminSessionBeanLocal.getSelectedRoles(employeeId);
        }
        return selectedRoles;
    }

    public void setSelectedRoles(Set<String> selectedRoles) {
        this.selectedRoles = selectedRoles;
    }

    public List<String> getRoles() {
        if (roles == null) {
            roles = employeeAdminSessionBeanLocal.getRoles();
        }
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public Long getEmployeeId() {
        System.out.println("***** SystemAdminEditAccountManagedBean get employee id"+employeeId);
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    public List<String> getDepartments() {
        if (departments == null) {
            departments = employeeAdminSessionBeanLocal.getEmployeeDepartments();
        }
        return departments;
    }

    public List<String> getPositions() {

        if (positions == null) {
            positions = employeeAdminSessionBeanLocal.getEmployeePositions();
        }
        return positions;
    }
    
     public Long getEmployeeIdViaSessionScope() {
        FacesContext context = FacesContext.getCurrentInstance();
        Employee employee = (Employee) context.getExternalContext().getSessionMap().get("employee");
        Long employeeId = employee.getEmployeeId();
        return employeeId;
    }
}
