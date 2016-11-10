/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.testSuite;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 *
 * @author Nicole
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({ejb.customer.session.CRMCustomerSessionBeanRemoteTest.class, ejb.customer.session.EnquirySessionBeanRemoteTest.class,
    ejb.customer.session.FollowUpSessionBeanRemoteTest.class, ejb.customer.session.IssueSessionBeanRemoteTest.class,
    ejb.deposit.session.BankAccountSessionBeanRemoteTest.class, ejb.deposit.session.InterestSessionBeanRemoteTest.class,
    ejb.deposit.session.PayeeSessionBeanRemoteTest.class, ejb.deposit.session.RegularPayeeSessionBeanRemoteTest.class,
    ejb.deposit.session.StatementSessionBeanRemoteTest.class, ejb.deposit.session.TransactionSessionBeanRemoteTest.class,
    ejb.infrastructure.session.CustomerAdminSessionBeanRemoteTest.class, ejb.infrastructure.session.EmployeeAdminSessionBeanRemoteTest.class,
    ejb.infrastructure.session.EmployeeEmailSessionBeanRemoteTest.class, ejb.infrastructure.session.EmployeeRolePermissionManagementSessionBeanRemoteTest.class,
    ejb.infrastructure.session.MessageSessionBeanRemoteTest.class})
public class RetailBankingSystemTestSuite {

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }
}
