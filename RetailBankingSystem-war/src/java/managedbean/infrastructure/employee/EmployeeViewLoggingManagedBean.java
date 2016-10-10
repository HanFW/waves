/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbean.infrastructure.employee;

import ejb.infrastructure.entity.Logging;
import ejb.infrastructure.session.LoggingSessionBeanLocal;
import java.util.List;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;

/**
 *
 * @author hanfengwei
 */
@Named(value = "employeeViewLoggingManagedBean")
@RequestScoped
public class EmployeeViewLoggingManagedBean {
    @EJB
    private LoggingSessionBeanLocal loggingSessionBeanLocal;
    
    private List<Logging> customerLogging;
    private List<Logging> employeeLogging;
    private List<Logging> systemLogging;

    /**
     * Creates a new instance of EmployeeViewLoggingManagedBean
     */
    public EmployeeViewLoggingManagedBean() {
    }

    public List<Logging> getCustomerLogging() {
        customerLogging = loggingSessionBeanLocal.retrieveAllCustomerLogging();
        return customerLogging;
    }

    public void setCustomerLogging(List<Logging> customerLogging) {
        this.customerLogging = customerLogging;
    }

    public List<Logging> getEmployeeLogging() {
        employeeLogging = loggingSessionBeanLocal.retrieveAllEmployeeLogging();
        return employeeLogging;
    }

    public void setEmployeeLogging(List<Logging> employeeLogging) {
        this.employeeLogging = employeeLogging;
    }

    public List<Logging> getSystemLogging() {
        systemLogging = loggingSessionBeanLocal.retrieveAllSystemLogging();
        return systemLogging;
    }

    public void setSystemLogging(List<Logging> systemLogging) {
        this.systemLogging = systemLogging;
    }
    
}
