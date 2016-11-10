/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbean.infrastructure.employee;

import ejb.infrastructure.entity.Employee;
import ejb.infrastructure.session.EmployeeAdminSessionBeanLocal;
import ejb.infrastructure.session.EmployeeEmailSessionBeanLocal;
import ejb.infrastructure.session.LoggingSessionBeanLocal;
import java.io.IOException;
import java.util.List;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.Dependent;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

/**
 *
 * @author Jingyuan
 */
@Named(value = "employeeDeleteUserAccountManagedBean")
@RequestScoped
public class EmployeeDeleteUserAccountManagedBean {

    /**
     * Creates a new instance of EmployeeDeleteUserAccountManagedBean
     */
    @EJB
    private EmployeeAdminSessionBeanLocal adminSessionBeanLocal;
    @EJB
    private EmployeeEmailSessionBeanLocal sendEmailSessionBeanLocal;
    @EJB
    private LoggingSessionBeanLocal loggingSessionBeanLocal;

    private List<Employee> employees;

    public EmployeeDeleteUserAccountManagedBean() {

    }

    public void deleteAccount(Employee employee) throws IOException {
//        System.out.println("hi");
        System.out.println("delete account");
        System.out.println(employee);
        FacesMessage message = null;
        FacesContext context = FacesContext.getCurrentInstance();

        System.out.println("===== AcocuntManagedBean: deleteAccount - employee=====" + employee.getEmployeeName());
        String msg = adminSessionBeanLocal.deleteEmployee(employee);
        employees = adminSessionBeanLocal.getEmployees();
        System.out.println("===== AcocuntManagedBean: get employees =====" + employees);

        if (msg.equals("success")) {

            context.getExternalContext().redirect(context.getExternalContext().getRequestContextPath() + "/web/internalSystem/infrastructure/employeeUserAccountManagement.xhtml?faces-redirect=true");
            message = new FacesMessage(FacesMessage.SEVERITY_INFO, "User Account Archived!", "User account has been successfully archived");
            loggingSessionBeanLocal.createNewLogging("employee", getEmployeeViaSessionMap(),"employee archives user account", "success", employee.getEmployeeName());
            context.addMessage(null, message);
            System.out.println("*** AccountManagedBean: account deleted");

        }

    }

    private Long getEmployeeViaSessionMap() {
        Long employeeId;
        FacesContext context = FacesContext.getCurrentInstance();
        Employee employee = (Employee) context.getExternalContext().getSessionMap().get("employee");
        employeeId = employee.getEmployeeId();

        return employeeId;
    }

}
