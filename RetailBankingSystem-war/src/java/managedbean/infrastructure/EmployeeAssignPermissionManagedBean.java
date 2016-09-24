/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbean.infrastructure;

import ejb.infrastructure.entity.Permission;
import ejb.infrastructure.entity.Role;
import ejb.infrastructure.session.EmployeeAdminSessionBeanLocal;
import java.io.Serializable;
import java.util.List;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
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

    private String roleName;
    private String[] permissionList1;
    private String[] selectedPermissionList1;
    private Long roleId;
    private List<Role> roles;
    private String[] selectedPermissionList2;
    private List<Permission> permissions;
    
    public List<Role> getRoles(){
        if(roles==null){
            employeeAdminSessionBeanLocal.getAllRoles();
        }
        return roles;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

//    public String[] getPermissionList1() {
//        if (permissionList1 == null) {
//            permissionList1 = employeeAdminSessionBeanLocal.getPermissionList1();
//        }
//        
//        System.out.println("****** employeeAssignPermissionManagedBean: "+permissionList1);
//        return permissionList1;
//    }

    public void setPermissionList1(String[] permissionList1) {
        this.permissionList1 = permissionList1;
    }

    public String[] getSelectedPermissionList1() {
        
        Integer i=6;
        if (selectedPermissionList1 == null) {
            selectedPermissionList1 = employeeAdminSessionBeanLocal.getSelectedPermissionList(i.longValue());
            if (selectedPermissionList1 == null) {
                selectedPermissionList1[0] = "";
            }
        }
        System.out.println("****** employeeAssignPermissionManagedBean: "+selectedPermissionList1);
        return selectedPermissionList1;
    }

    public void setSelectedPermissionList1(String[] selectedPermissionList1) {
        System.out.println("****** employeeAssignPermissionManagedBean: setSelectedPermissionList1");
        this.selectedPermissionList1 = selectedPermissionList1;
    }
    
        public String[] getSelectedPermissionList2() {
        
        Integer i=5;
        if (selectedPermissionList1 == null) {
            selectedPermissionList1 = employeeAdminSessionBeanLocal.getSelectedPermissionList(i.longValue());
            if (selectedPermissionList1 == null) {
                selectedPermissionList1[0] = "";
            }
        }
        
        System.out.println("****** employeeAssignPermissionManagedBean: "+selectedPermissionList2);
        return selectedPermissionList1;
    }

    public void setSelectedPermissionList2(String[] selectedPermissionList2) {
        System.out.println("****** employeeAssignPermissionManagedBean: setSelectedPermissionList1");
        this.selectedPermissionList1 = selectedPermissionList2;
    }
    
    

    public void assignPermissionToCounterTeller(String roleName) {
        FacesMessage message = null;
        FacesContext context = FacesContext.getCurrentInstance();

        employeeAdminSessionBeanLocal.setSelectedPermissionList(roleName, selectedPermissionList1);
        System.err.println("****** employeeAssignPermissionManagedBean: "+roleId+ selectedPermissionList1);

        message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Permissions of the role has been succesfully updated!", "permissions updated");
        context.addMessage(null, message);
        System.out.println("***** employeeAssignPermissionManagedBean: permissions updated!");
     

    }

    public List<Permission> getPermissions() {
//        if(permissions==null)
//            permissions=employeeAssignPermissionManagedBean
        return permissions;
    }

    public void setPermissions(List<Permission> permissions) {
        this.permissions = permissions;
    }
    
    

}
