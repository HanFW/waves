/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.infrastructure.entity;

import java.io.Serializable;
import java.util.Set;
import java.util.HashSet;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

/**
 *
 * @author Nicole
 */
@Entity
public class Role implements Serializable {

    //private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roleId;
    private String roleName;

    @ManyToMany(cascade = {CascadeType.ALL}, mappedBy = "role")
    private Set<Employee> employee = new HashSet<Employee>();

    @ManyToMany(cascade = {CascadeType.PERSIST})
    @JoinTable(name = "ROLE_PERMISSION")
    private Set<Permission> permission = new HashSet<Permission>();

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public Set<Employee> getEmployee() {
        return employee;
    }

    public void setEmployee(Set<Employee> employee) {
        this.employee = employee;
    }

    public void setId(Long id) {
        this.roleId = id;
    }

    public Set<Permission> getPermissions() {
        return permission;
    }

    public void setPermissions(Set<Permission> permissions) {
        this.permission = permissions;
    }
    

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (roleId != null ? roleId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Role)) {
            return false;
        }
        Role other = (Role) object;
        if ((this.roleId == null && other.roleId != null) || (this.roleId != null && !this.roleId.equals(other.roleId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Role[ id=" + roleId + " ]";
    }

    public void addEmployeeToRole(Employee employee) {

        this.employee.add(employee);
    }

    public void deleteEmployeeFromRole(Employee employee) {
        this.employee.remove(employee);
    }
    
      public void addPermissionToRole(Permission permission) {

        this.permission.add(permission);
    }

    public void deletePermissionFromRole(Permission permission) {
        this.permission.remove(permission);
    }

}
