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
public class EmployeeEmailSessionBeanRemoteTest {

    EmployeeEmailSessionBeanRemote employeeEmailSessionBeanRemote = lookupEmployeeEmailSessionBeanRemote();

    public EmployeeEmailSessionBeanRemoteTest() {
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
    public void testResetPwd() {
        System.out.println("resetPwd");
        String employeeNRIC = "G7654321Q";
        String employeeEmail = "zhoujingyuan1996@gmail.com";
        String expResult = "valid";
        String result = employeeEmailSessionBeanRemote.resetPwd(employeeNRIC, employeeEmail);
        assertEquals(expResult, result);
    }

   
    @Test
    public void testChangePwd1() {
        System.out.println("changePwd");
        String currentPassword = "12345678";
        String newPassword = "11223344";
        Long employeeId = (long) 2;
        String expResult = "success";
        String result = employeeEmailSessionBeanRemote.changePwd(currentPassword, newPassword, employeeId);
        assertEquals(expResult, result);
    }

    @Test
    public void testChangePwd2() {
        System.out.println("changePwd");
        String currentPassword = "12345678";
        String newPassword = "12345678";
        Long employeeId = (long) 3;
        String expResult = "equal";
        String result = employeeEmailSessionBeanRemote.changePwd(currentPassword, newPassword, employeeId);
        assertEquals(expResult, result);
    }

    @Test
    public void testChangePwd3() {
        System.out.println("changePwd");
        String currentPassword = "66666666";
        String newPassword = "11223344";
        Long employeeId = (long) 1;
        String expResult = "invalid";
        String result = employeeEmailSessionBeanRemote.changePwd(currentPassword, newPassword, employeeId);
        assertEquals(expResult, result);
    }

    
    @Test
    public void testInitialPwd1() {
        System.out.println("initialPwd");
        String employeeNRIC = "G46095795";
        String employeeEmail = "zhoujingyuan1996@gmail.com";
        String expResult = "valid";
        String result = employeeEmailSessionBeanRemote.initialPwd(employeeNRIC, employeeEmail);
        assertEquals(expResult, result);
    }

     @Test
    public void testInitialPwd2() {
        System.out.println("initialPwd");
        String employeeNRIC = "S6666666X";
        String employeeEmail = "tom@gmail.com";
        String expResult = "invalid";
        String result = employeeEmailSessionBeanRemote.initialPwd(employeeNRIC, employeeEmail);
        assertEquals(expResult, result);
    }
    
    private EmployeeEmailSessionBeanRemote lookupEmployeeEmailSessionBeanRemote() {
        try {
            Context c = new InitialContext();
            return (EmployeeEmailSessionBeanRemote) c.lookup("java:global/RetailBankingSystem/RetailBankingSystem-ejb/EmployeeEmailSessionBean!ejb.infrastructure.session.EmployeeEmailSessionBeanRemote");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }

}
