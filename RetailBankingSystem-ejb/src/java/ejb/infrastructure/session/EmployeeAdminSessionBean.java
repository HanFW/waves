/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.infrastructure.session;

import ejb.infrastructure.entity.Employee;
import ejb.infrastructure.entity.Role;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Jingyuan
 */
@Stateless
public class EmployeeAdminSessionBean implements EmployeeAdminSessionBeanLocal {

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    @PersistenceContext
    private EntityManager em;

    private final static String[] departments;
    private final static String[] positions;
    private final static String[] roles;

    static {
        departments = new String[5];
        departments[0] = "CEO";
        departments[1] = "Card Department";
        departments[2] = "Loan Department";
        departments[3] = "Sales Department";
        departments[4] = "Operation Department";

        positions = new String[8];
        positions[0] = "CEO";
        positions[1] = "Manager";
        positions[2] = "Officer";
        positions[3] = "Mortgage Appraiser";
        positions[4] = "Underwriter";
        positions[5] = "Relationship Manager";
        positions[6] = "Counter Teller";
        positions[7] = "Call Center Staff";

        roles = new String[10];
        roles[0] = "CEO";
        roles[1] = "Loan Officer";
        roles[2] = "Card Department Manager";
        roles[3] = "Mortgage Appraiser";
        roles[4] = "Underwriter";
        roles[5] = "Relationship Manager";
        roles[6] = "Sales Department Manager";
        roles[7] = "Counter Teller";
        roles[8] = "Call Center Staff";
        roles[9] = "Enquiry Manager";


    }

    @Override
    public Employee getEmployeeByAccountNum(String accountNum) {
        Query query = em.createQuery("SELECT e FROM Employee e WHERE e.employeeAccountNum= :account");
        query.setParameter("account", accountNum);

        Employee findEmployee = (Employee) query.getSingleResult();
        return findEmployee;
    }

    @Override
    public String createEmployeeAccount(String employeeName, String employeeDepartment,
            String employeePosition, String employeeNRIC, String employeeMobileNum, String employeeEmail,
            Set<String> selectedRoles) {

        String account = null;
        String password = null;
        int i;

        try {

            Query query = em.createQuery("SELECT e FROM Employee e WHERE e.employeeNRIC= :NRIC");
            query.setParameter("NRIC", employeeNRIC);
            Employee findEmployee = (Employee) query.getSingleResult();
            System.out.println("*** adminSessionBean: employee existed");
            return "existing account";
        } catch (NoResultException e) {
            Employee employee = new Employee();

            employee.setEmployeeName(employeeName);
            employee.setEmployeeDepartment(employeeDepartment);
            employee.setEmployeePosition(employeePosition);
            employee.setEmployeeNRIC(employeeNRIC);
            employee.setEmployeeMobileNum(employeeMobileNum);
            employee.setEmployeeEmail(employeeEmail);
            em.persist(employee);
            //generate employee account number
            account = generateAccountNumber(employeeNRIC);
            System.out.println("*** adminSessionBean: generateAccountNumber(): employee account number generated");

            //generate random password
            password = generatePassword();
            System.out.println("*** adminSessionBean: generatePassword(): employee password generated");

            //create employee account
            try {
                employee.setEmployeeAccountNum(account);
                String hashedPassword = password;
                hashedPassword = md5Hashing(password + employee.getEmployeeNRIC().substring(0, 3));
                employee.setEmployeePassword(hashedPassword);
                Set<Role> employeeRoles = new HashSet<Role>();
                String[] selectedRolesToArray = selectedRoles.toArray(new String[selectedRoles.size()]);
                for (i = 0; i < selectedRolesToArray.length; i++) {
                    System.out.println("*** adminSessionBean print employee role " + selectedRolesToArray[i]);
                    Query q = em.createQuery("SELECT r FROM Role r WHERE r.roleName= :name");
                    q.setParameter("name", selectedRolesToArray[i]);
                    Role findRole = (Role) q.getSingleResult();
                    employeeRoles.add(findRole);
                }
                //set selected roles to an employee
                employee.setRole(employeeRoles);
                em.flush();
            } catch (NoSuchAlgorithmException ex) {
                Logger.getLogger(EmployeeAdminSessionBean.class.getName()).log(Level.SEVERE, null, ex);
            }

            System.out.println("*** adminSessionBean: employee account created");
            return account + "," + password;
        }

    }

    //display all employee accounts
    @Override
    public List<Employee> getEmployees() {
        System.out.println("*** adminSessionBean: Display all employee accounts");
        Query query = em.createQuery("SELECT e FROM Employee e");
        List<Employee> employees = query.getResultList();
        return employees;
    }

    @Override
    public List<String> getEmployeeDepartments() {
        System.out.println("*** adminSessionBean: Display all employee departments");
        return Arrays.asList(departments);

    }

    @Override
    public List<String> getEmployeePositions() {
        System.out.println("*** adminSessionBean: Display all employee positions");
        return Arrays.asList(positions);
    }

    @Override
    public List<String> getRoles() {
        System.out.println("*** adminSessionBean: Display all roles");
        return Arrays.asList(roles);
    }

    //edit user account info
    @Override
    public void editUserAccount(Long employeeId, String employeeName, String employeeDepartment,
            String employeePosition, String employeeMobileNum, String employeeEmail) {
        Employee employee = em.find(Employee.class, employeeId);

        System.out.println("*** adminSessionBean: find the user account to be edited" + employee.getEmployeeNRIC());
        employee.setEmployeeName(employeeName);
        System.out.println(employee.getEmployeeName());
        employee.setEmployeeDepartment(employeeDepartment);
        System.out.println(employee.getEmployeeDepartment());
        employee.setEmployeePosition(employeePosition);
        System.out.println(employee.getEmployeePosition());
        employee.setEmployeeMobileNum(employeeMobileNum);
        System.out.println(employee.getEmployeeMobileNum());
        employee.setEmployeeEmail(employeeEmail);
        System.out.println(employee.getEmployeeEmail());

        em.flush();
        System.out.println("*** adminSessionBean: employee edited");
    }

    //filter employee user accounts by department
    @Override
    public List<Employee> filterAccountByDepartment(String employeeDepartment) {
        Query query = em.createQuery("SELECT e FROM Employee e WHERE e.employeeDepartment= :Department");
        query.setParameter("Department", employeeDepartment);
        System.out.println("*** adminSessionBean:" + employeeDepartment);
        System.out.println("*** adminSessionBean: filter user accounts by departement");
        List<Employee> employees = query.getResultList();
        return employees;

    }

    //delte employee account from database
    @Override
    public String deleteEmployee(Employee employee) {
        Long employeeId = employee.getEmployeeId();

        System.out.println("*** adminSessionBean:" + employeeId);
        System.out.println("*** adminSessionBean: delete employee account from database");

        Employee findEmployee = em.find(Employee.class, employeeId);
        em.remove(findEmployee);
        return "success";
    }

    private String generatePassword() {
        SecureRandom random = new SecureRandom();
        return new BigInteger(40, random).toString(32);
    }

    //Generate initial account number for employee account
    private String generateAccountNumber(String employeeNRIC) {
//        String hash = employeeNRIC;
//        System.out.println(hash.concat(employeeName.substring(0, 3)));
//        try {
//            return md5Hashing(hash);
//        } catch (NoSuchAlgorithmException ex) {
//            Logger.getLogger(EmployeeAdminSessionBean.class.getName()).log(Level.SEVERE, null, ex);
//            return Integer.toString(hash.hashCode());
//        }
        System.out.println("*** employeeAdminSessionBean: employeeNRIC"+employeeNRIC);
        Query query = em.createQuery("SELECT e FROM Employee e WHERE e.employeeNRIC= :NRIC");
        query.setParameter("NRIC", employeeNRIC);
        Employee employee=new Employee();
        List resultList = query.getResultList();
        if(resultList.isEmpty()){
            System.out.println("*** employeeAdminSessionBean: employee cannt be found");
            return "";
        }
        else{
        employee=(Employee)resultList.get(0);
        System.out.println("*** employeeAdminSessionBean: employee id"+employee.getEmployeeId());
        Integer userId = (employee.getEmployeeId()).intValue()+1000;
        System.out.println("*** employeeAdminSessionBean: employee id"+userId);
        String accountNum = userId.toString();
        System.out.println("*** employeeAdminSessionBean: account number created" + accountNum);
        return accountNum;
        }
    }

    //Do customer login
    @Override
    public String login(String employeeAccount, String password) {
        System.out.println("*** adminSessionBean: login(accountNum,password)");

        Query query = em.createQuery("SELECT e FROM Employee e WHERE e.employeeAccountNum = :accountNum");
        query.setParameter("accountNum", employeeAccount);

        try {
            Employee thisEmployee = (Employee) query.getSingleResult();
            password = md5Hashing(password + thisEmployee.getEmployeeNRIC().substring(0, 3));
            System.out.println("?????????" + password);
            if (thisEmployee.getEmployeePassword().equals(password)) {
                System.out.println("*** adminSessionBean: login(): valid account and password" + ": account " + thisEmployee.getEmployeeAccountNum());
                return "loggedIn";
            } else {
                System.out.println("*** adminSessionBean: login(): invalid password");
                return "invalidPassword";
            }
        } catch (NoResultException ex) {
            System.out.println("*** adminSessionBean: login(): invalid account");
            return "invalidAccount";
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(EmployeeAdminSessionBean.class.getName()).log(Level.SEVERE, null, ex);
            return "PasswordNoSuchAlgorithmException";
        }
    }

    private String md5Hashing(String stringToHash) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        return Arrays.toString(md.digest(stringToHash.getBytes()));
    }

}
