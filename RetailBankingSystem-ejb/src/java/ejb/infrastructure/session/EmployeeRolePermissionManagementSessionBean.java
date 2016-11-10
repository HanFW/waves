/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.infrastructure.session;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Jingyuan
 */
@Stateless
public class EmployeeRolePermissionManagementSessionBean implements EmployeeRolePermissionManagementSessionBeanLocal, EmployeeRolePermissionManagementSessionBeanRemote {

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    @PersistenceContext
    private EntityManager em;

    private final static String[] permissionList1;
    private final static String[] permissionList2;
    private final static String[] permissionList3;
    private final static String[] permissionList4;
    private final static String[] permissionList5;
    private final static String[] permissionList6;
    private final static String[] permissionList7;
    private final static String[] permissionList8;

    static {

        permissionList1 = new String[16];
        permissionList1[0] = "View Customer Accounts Information";
        permissionList1[1] = "Create Enquiry Case";
        permissionList1[2] = "Add Follow Up Questions";
        permissionList1[3] = "Search Enquiry";
        permissionList1[4] = "Edit Customer Basic Information";
        permissionList1[5] = "Add A New Account";
        permissionList1[6] = "Delete An Account";
        permissionList1[7] = "View Transaction History";
        permissionList1[8] = "View Bank Statement";
        permissionList1[9] = "Fund-transfer";
        permissionList1[10] = "Cash Deposit";
        permissionList1[11] = "Cash Withdraw";
        permissionList1[12] = "Change Deposit Account PassWord";
        permissionList1[13] = "Retrieve Deposit Account Password";
        permissionList1[14] = "Activate Fixed Deposit";
        permissionList1[15] = "Change Daily Transfer Limit";
        
        permissionList2=new String[3];
        permissionList2[0]="Create Enquiry Case";
        permissionList2[1]="Add Follow Up Questions";
        permissionList2[2]="Search Enquiry";
        
        permissionList3=new String[3];
        permissionList3[0]="Search Enquiry";
        permissionList3[1]="Edit Customer Advanced Information";
        permissionList3[2]="View Customer Advanced Information";
        
        permissionList4=new String[1];
        permissionList4[0]="View Deposit Department Issue";
        
        permissionList5=new String[1];
        permissionList5[0]="View Card Department Issue";
        
        permissionList6=new String[1];
        permissionList6[0]="View Loan Department Issue";
               
        permissionList7=new String[1];
        permissionList7[0]="View Operation Department Issue";
        
        permissionList8=new String[1];
        permissionList8[0]="View Wealth Management Issue";
        
    }

    @Override
    public String[] getPermissions(String roleName) {
        System.out.println("*** adminSessionBean: Display available permissions of a role" + permissionList1);
        switch (roleName) {
            case "Counter Teller":
                return permissionList1;
            case "Call Center Staff":
                return permissionList2;
            case "Relationship Manager":
                return permissionList3;
            case "Deposit Specialist":
                return permissionList4;
            case "Card Specialist":
                return permissionList5;
            case "Loan Specialist":
                return permissionList6;
            case "Operation Specialist":
                return permissionList7;
            case "Wealth Management Specialist":
                return permissionList8;
            default:
                return permissionList1;
        }
    }
    
    @Override
    public String[] getPermissionList(){
         System.out.println("*** adminSessionBean: Display available permissions of a role" + permissionList1);
         return permissionList1;
    }
}
