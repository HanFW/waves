/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.infrastructure.session;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;

/**
 *
 * @author Nicole
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class EmployeeRolePermissionManagementSessionBeanRemoteTest {

    EmployeeRolePermissionManagementSessionBeanRemote employeeRolePermissionManagementSessionBeanRemote = lookupEmployeeRolePermissionManagementSessionBeanRemote();

    public EmployeeRolePermissionManagementSessionBeanRemoteTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    
    @Test
    public void testGetPermissions() {
        System.out.println("getPermissions");
        String roleName = "Card Specialist";
        String[] expResult = new String[1];
        expResult[0] = "View Card Department Issue";
        String[] result = employeeRolePermissionManagementSessionBeanRemote.getPermissions(roleName);
        assertArrayEquals(expResult, result);
    }

   
    @Test
    public void testGetPermissionList() {
        System.out.println("getPermissionList");
        String[] expResult = new String[16];
        expResult[0] = "View Customer Accounts Information";
        expResult[1] = "Create Enquiry Case";
        expResult[2] = "Add Follow Up Questions";
        expResult[3] = "Search Enquiry";
        expResult[4] = "Edit Customer Basic Information";
        expResult[5] = "Add A New Account";
        expResult[6] = "Delete An Account";
        expResult[7] = "View Transaction History";
        expResult[8] = "View Bank Statement";
        expResult[9] = "Fund-transfer";
        expResult[10] = "Cash Deposit";
        expResult[11] = "Cash Withdraw";
        expResult[12] = "Change Deposit Account PassWord";
        expResult[13] = "Retrieve Deposit Account Password";
        expResult[14] = "Activate Fixed Deposit";
        expResult[15] = "Change Daily Transfer Limit";
        String[] result = employeeRolePermissionManagementSessionBeanRemote.getPermissionList();
        assertArrayEquals(expResult, result);
    }

    private EmployeeRolePermissionManagementSessionBeanRemote lookupEmployeeRolePermissionManagementSessionBeanRemote() {
        try {
            Context c = new InitialContext();
            return (EmployeeRolePermissionManagementSessionBeanRemote) c.lookup("java:global/RetailBankingSystem/RetailBankingSystem-ejb/EmployeeRolePermissionManagementSessionBean!ejb.infrastructure.session.EmployeeRolePermissionManagementSessionBeanRemote");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }

}
