/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.infrastructure.entity;

import ejb.customer.entity.CustomerAdvanced;
import java.io.Serializable;
import java.util.Set;
import java.util.HashSet;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

/**
 *
 * @author Nicole
 */
@Entity
public class Employee implements Serializable {

    //private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long employeeId;
    private String employeeName;
    private String employeeAccountNum;
    private String employeePassword;
    private String employeeDepartment;
    private String employeePosition;
    private String employeeNRIC;
    private String employeeMobileNum;
    private String employeeEmail;
    private String employeeStatus;
    private String logInStatus;
    
    @ManyToMany(cascade = {CascadeType.PERSIST})
    @JoinTable(name = "EMPLOYEE_ROLE")
    private Set<Role> role = new HashSet<Role>();
    
    @OneToMany(cascade={CascadeType.ALL},fetch=FetchType.EAGER,mappedBy="employee")
    private List<CustomerAdvanced> customerAdvanced;
    
    public boolean hasRole(Role checkRole){
        return role.contains(checkRole);
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

    public Set<Role> getRole() {
        return role;
    }

    public void setRole(Set<Role> role) {
        this.role = role;
    }

    public Long getEmployeeId() {
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

    public String getEmployeeAccountNum() {
        return employeeAccountNum;
    }

    public void setEmployeeAccountNum(String employeeAccountNum) {
        this.employeeAccountNum = employeeAccountNum;
    }

    public String getEmployeePassword() {
        return employeePassword;
    }

    public void setEmployeePassword(String employeePassword) {
        this.employeePassword = employeePassword;
    }

    public String getEmployeeNRIC() {
        return employeeNRIC;
    }

    public void setEmployeeNRIC(String employeeNRIC) {
        this.employeeNRIC = employeeNRIC;
    }

    public String getEmployeeMobileNum() {
        return employeeMobileNum;
    }

    public void setEmployeeMobileNum(String employeeMobileNum) {
        this.employeeMobileNum = employeeMobileNum;
    }

    public String getEmployeeEmail() {
        return employeeEmail;
    }

    public void setEmployeeEmail(String employeeEmail) {
        this.employeeEmail = employeeEmail;
    }

    public List<CustomerAdvanced> getCustomerAdvanced() {
        return customerAdvanced;
    }

    public void setCustomerAdvanced(List<CustomerAdvanced> customerAdvanced) {
        this.customerAdvanced = customerAdvanced;
    }
    
    public String getEmployeeStatus() {
        return employeeStatus;
    }

    public void setEmployeeStatus(String employeeStatus) {
        this.employeeStatus = employeeStatus;
    }

    public String getLogInStatus() {
        return logInStatus;
    }

    public void setLogInStatus(String logInStatus) {
        this.logInStatus = logInStatus;
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (employeeId != null ? employeeId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Employee)) {
            return false;
        }
        Employee other = (Employee) object;
        if ((this.employeeId == null && other.employeeId != null) || (this.employeeId != null && !this.employeeId.equals(other.employeeId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Employee[ id=" + employeeId + " ]";
    }

}
