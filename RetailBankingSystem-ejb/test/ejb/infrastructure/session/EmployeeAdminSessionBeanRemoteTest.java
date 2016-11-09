/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.infrastructure.session;

import ejb.infrastructure.entity.Employee;
import ejb.infrastructure.entity.Permission;
import ejb.infrastructure.entity.Role;
import java.util.List;
import java.util.Set;
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
public class EmployeeAdminSessionBeanRemoteTest {

    EmployeeAdminSessionBeanRemote employeeAdminSessionBeanRemote = lookupEmployeeAdminSessionBeanRemote();

    public EmployeeAdminSessionBeanRemoteTest() {
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
    public void testLogin1() {
        System.out.println("login");
        String accountNum = "1002";
        String password = "12345678";
        String expResult = "loggedIn";
        String result = employeeAdminSessionBeanRemote.login(accountNum, password);
        assertEquals(expResult, result);
    }

    @Test
    public void testLogin2() {
        System.out.println("login");
        String accountNum = "1002";
        String password = "11223344";
        String expResult = "invalidPassword";
        String result = employeeAdminSessionBeanRemote.login(accountNum, password);
        assertEquals(expResult, result);
    }

    @Test
    public void testFilterAccountByDepartment() {
        System.out.println("filterAccountByDepartment");
        String employeeDepartment = "Card Department";
        int expResult = 1;
        List<Employee> result = employeeAdminSessionBeanRemote.filterAccountByDepartment(employeeDepartment);
        assertEquals(expResult, result.size());
    }

    @Test
    public void testGetEmployees() {
        System.out.println("getEmployees");
        int expResult = 6;
        List<Employee> result = employeeAdminSessionBeanRemote.getEmployees();
        assertEquals(expResult, result.size());
    }

    @Test
    public void testGetAllRoles() {
        System.out.println("getAllRoles");
        int expResult = 22;
        List<Role> result = employeeAdminSessionBeanRemote.getAllRoles();
        assertEquals(expResult, result.size());
    }

    @Test
    public void testGetArchivedEmployees() {
        System.out.println("getArchivedEmployees");
        int expResult = 0;
        List<Employee> result = employeeAdminSessionBeanRemote.getArchivedEmployees();
        assertEquals(expResult, result.size());
    }

    @Test
    public void testGetEmployeeById() {
        System.out.println("getEmployeeById");
        Long employeeId = (long) 1;
        String expResult = "1001";
        Employee result = employeeAdminSessionBeanRemote.getEmployeeById(employeeId);
        assertEquals(expResult, result.getEmployeeAccountNum());
    }

    @Test
    public void testGetEmployeeDepartments() {
        System.out.println("getEmployeeDepartments");
        int expResult = 6;
        List<String> result = employeeAdminSessionBeanRemote.getEmployeeDepartments();
        assertEquals(expResult, result.size());
    }

    @Test
    public void testGetEmployeePositions() {
        System.out.println("getEmployeePositions");
        int expResult = 9;
        List<String> result = employeeAdminSessionBeanRemote.getEmployeePositions();
        assertEquals(expResult, result.size());
    }

    @Test
    public void testGetPositionsByDepartment() {
        System.out.println("getPositionsByDepartment");
        String department = "Board of Directors";
        int expResult = 6;
        List<String> result = employeeAdminSessionBeanRemote.getPositionsByDepartment(department);
        assertEquals(expResult, result.size());
    }

    @Test
    public void testGetRolesByDepartment() {
        System.out.println("getRolesByDepartment");
        String department = "Sales Department";
        int expResult = 3;
        List<String> result = employeeAdminSessionBeanRemote.getRolesByDepartment(department);
        assertEquals(expResult, result.size());
    }

    @Test
    public void testGetPermissionList() {
        System.out.println("getPermissionList");
        String roleName = "Loan Department Manager";
        int expResult = 2;
        List<Permission> result = employeeAdminSessionBeanRemote.getPermissionList(roleName);
        assertEquals(expResult, result.size());
    }

    @Test
    public void testGetRoleByName() {
        System.out.println("getRoleByName");
        String roleName = "Sales Department Manager";
        Long expResult = (long) 10;
        Role result = employeeAdminSessionBeanRemote.getRoleByName(roleName);
        assertEquals(expResult, result.getRoleId());
    }

    @Test
    public void testAddPermissionToRole() {
        System.out.println("addPermissionToRole");
        String roleName = "Director";
        String permissionName = "View Customer Accounts Information";
        String expResult = "Successfully Added!";
        String result = employeeAdminSessionBeanRemote.addPermissionToRole(roleName, permissionName);
        assertEquals(expResult, result);
    }

    private EmployeeAdminSessionBeanRemote lookupEmployeeAdminSessionBeanRemote() {
        try {
            Context c = new InitialContext();
            return (EmployeeAdminSessionBeanRemote) c.lookup("java:global/RetailBankingSystem/RetailBankingSystem-ejb/EmployeeAdminSessionBean!ejb.infrastructure.session.EmployeeAdminSessionBeanRemote");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }

}
