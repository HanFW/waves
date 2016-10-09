/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbean.infrastructure.employee;

import ejb.infrastructure.entity.Permission;
import ejb.infrastructure.entity.Role;
import ejb.infrastructure.session.EmployeeAdminSessionBeanLocal;
import ejb.infrastructure.session.EmployeeRolePermissionManagementSessionBeanLocal;
import ejb.infrastructure.session.LoggingSessionBeanLocal;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Named;

/**
 *
 * @author Jingyuan
 */
@Named(value = "employeeAssignPermissionManagedBean")
@SessionScoped
public class EmployeeAssignPermissionManagedBean implements Serializable {

    /**
     * Creates a new instance of EmployeeAssignPermissionManagedBean
     */
    @EJB
    private EmployeeAdminSessionBeanLocal employeeAdminSessionBeanLocal;
    @EJB
    private LoggingSessionBeanLocal loggingSessionBeanLocal;
    @EJB
    private EmployeeRolePermissionManagementSessionBeanLocal rolePermissionManagementSessionBeanLocal;

    private String roleName;
    private String[] permissionList1;
    private Long roleId;
    private List<Role> roles;
    private List<Permission> permissions;
    private boolean visible = false;
    private Permission permission;
    private String permissionName;

    public List<Role> getRoles() {
        if (roles == null) {
            employeeAdminSessionBeanLocal.getAllRoles();
        }
        return roles;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        System.err.println("******  employeeAssignPermissionManagedBean: " + roleName);
        this.roleName = roleName;

    }

    public String[] getPermissionList1() {
        if (roleName == null) {
            permissionList1 = rolePermissionManagementSessionBeanLocal.getPermissionList();
            System.out.println("~~~~~~~~I am null");
        } else {
            System.out.println("****** employeeAssignPermissionManagedBean: get permission list1" + roleName);
            permissionList1 = rolePermissionManagementSessionBeanLocal.getPermissions(roleName);
            System.out.println("****** employeeAssignPermissionManagedBean: get permission list1 " + permissionList1);
        }
        return permissionList1;
    }

    public void setPermissionList1(String[] permissionList1) {
        this.permissionList1 = permissionList1;
    }

    public List<Permission> getListOfPermissions(ActionEvent event) {
//        if (permissions == null) {
//            permissions = employeeAdminSessionBeanLocal.getPermissionList(roleName);
//        }
        visible = true;
        permissions = null;
//        roleName=null;
        return permissions;

    }

    public List<Permission> getPermissions() {
        System.out.println("*** employeeAssignPermissionManagedBena:  get permissions" + roleName);
//        return permissions;
        if (permissions == null) {
            permissions = employeeAdminSessionBeanLocal.getPermissionList(roleName);
        }
//        visible = true;
//        roleName=null;
//        permissions = null;
        return permissions;

    }

    public void setPermissions(List<Permission> permissions) {
        this.permissions = permissions;
    }

    public Permission getPermission() {
        return permission;
    }

    public void setPermission(Permission permission) {
        this.permission = permission;
        System.out.println("*** employeeAssignPermissionManagedBena: setPermission check permission name" + permission.getPermissionName());
    }

    public void deletePermission(Permission permission) throws IOException {

        FacesMessage message = null;
        FacesContext context = FacesContext.getCurrentInstance();
        if (roleName == null) {
            System.out.println("*** employeeAssignPermissionManagedBena: permission deleted" + roleName);
        }
        if (permission == null) {
            System.out.println("*** employeeAssignPermissionManagedBena: permission is null");
        }
        employeeAdminSessionBeanLocal.deletePermission(roleName, permission.getPermissionName());

        context.getExternalContext().redirect(context.getExternalContext().getRequestContextPath() + "/web/internalSystem/infrastructure/employeeSystemAdminAssignPermission.xhtml?faces-redirect=true");

        message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Permission is deleted for the role!", "permission disabled");
        context.addMessage(null, message);
        System.out.println("*** employeeAssignPermissionManagedBena: permission deleted");

        permissions = null;

    }

    public void deletePermissionCancel() throws IOException {
        System.out.println("===== AssignPermissionManagedBean: deleteCancel =====");
        FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Delete Action Cancelled", "Delete Action Cancelled");
        FacesContext.getCurrentInstance().addMessage(null, msg);

    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public String getPermissionName() {

        return permissionName;
    }

    public void setPermissionName(String permissionName) {
        System.out.println("===== AssignPermissionManagedBean: set permission name =====" + permissionName);
        this.permissionName = permissionName;
    }

    public void addNewPermissionToRole() throws IOException {
        System.out.println("===== AssignPermissionManagedBean: permission name =====" + permissionName);
        System.out.println("===== AssignPermissionManagedBean: role name =====" + roleName);
        employeeAdminSessionBeanLocal.addPermissionToRole(roleName, permissionName);
        FacesContext context = FacesContext.getCurrentInstance();
        context.getExternalContext().redirect(context.getExternalContext().getRequestContextPath() + "/web/internalSystem/infrastructure/employeeSystemAdminAssignPermission.xhtml?faces-redirect=true");
        System.out.println("===== AssignPermissionManagedBean: add new permission to role =====");

        permissions = null;
    }

    public void AssignPermission(ActionEvent event) throws IOException {
        FacesContext context = FacesContext.getCurrentInstance();
        context.getExternalContext().redirect(context.getExternalContext().getRequestContextPath() + "/web/internalSystem/infrastructure/employeeSystemAdminAssignPermission.xhtml?faces-redirect=true");
    }

}
