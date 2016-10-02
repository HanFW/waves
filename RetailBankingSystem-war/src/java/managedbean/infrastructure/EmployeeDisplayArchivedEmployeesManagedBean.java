/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbean.infrastructure;

import ejb.infrastructure.entity.Employee;
import ejb.infrastructure.session.EmployeeAdminSessionBeanLocal;
import java.util.List;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.Dependent;
import javax.enterprise.context.RequestScoped;

/**
 *
 * @author Jingyuan
 */
@Named(value = "employeeDisplayArchivedEmployeesManagedBean")
@RequestScoped
public class EmployeeDisplayArchivedEmployeesManagedBean {

    @EJB
    private EmployeeAdminSessionBeanLocal adminSessionBeanLocal;

    /**
     * Creates a new instance of EmployeeDisplayArchivedEmployeesManagedBean
     */
    public EmployeeDisplayArchivedEmployeesManagedBean() {
    }

    public List<Employee> getArchivedEmployees() {

        List<Employee> employees = adminSessionBeanLocal.getArchivedEmployees();

        return employees;
    }

}
