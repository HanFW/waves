/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.infrastructure.session;

import ejb.infrastructure.entity.Employee;
import ejb.infrastructure.entity.Permission;
import ejb.infrastructure.entity.Role;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
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

    private final static String[] permissionList1;
//    private final static String[] permissionList2;
//    private final static String[] permissionList3;

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

        roles = new String[16];
        roles[0] = "CEO";
        roles[1] = "Loan Officer";
        roles[2] = "Card Department Manager";
        roles[3] = "Mortgage Appraiser";
        roles[4] = "Underwriter";
        roles[5] = "Relationship Manager";
        roles[6] = "Sales Department Manager";
        roles[7] = "Counter Teller";
        roles[8] = "Call Center Staff";
        roles[9] = "Card Specialist";
        roles[10] = "Loan Specialist";
        roles[11] = "Deposit Specialist";
        roles[12] = "Operation Specialist";
        roles[13] = "Wealth Management Specialist";
        roles[14] = "Enquiry Manager";
        roles[15]="System Admin";

        permissionList1 = new String[27];
        permissionList1[0] = "View Customer Accounts Information";
        permissionList1[1] = "Create Enquiry Case";
        permissionList1[2] = "Add Follow Up Questions";
        permissionList1[3] = "Search Enquiry";
        permissionList1[4] = "Edit Customer Basic Information";
        permissionList1[5] = "View Enquiry";
        permissionList1[6] = "Add A New Account";
        permissionList1[7] = "Delete An Account";
        permissionList1[8] = "View Transaction History";
        permissionList1[9] = "View Bank Statement";
        permissionList1[10] = "Fund-transfer";
        permissionList1[11] = "Cash Deposit";
        permissionList1[12] = "Cash Withdraw";
        permissionList1[13] = "Forward Enquiry To Specialist";
        permissionList1[14] = "Edit Customer Advanced Information";
        permissionList1[15] = "View Customer Advanced Information";
        permissionList1[16] = "User Account Management";
        permissionList1[17] = "Manage Permissions of Roles";
        permissionList1[18] = "View Deposit Department Issue";
        permissionList1[19] = "View Card Department Issue";
        permissionList1[20] = "View Loan Department Issue";
        permissionList1[21] = "View Operation Department Issue";
        permissionList1[22] = "View Wealth Management Issue";
        permissionList1[23] = "Change Deposit Account PassWord";
        permissionList1[24] = "Retrieve Deposit Account Password";
        permissionList1[25] = "Activate Fixed Deposit";
        permissionList1[26] = "Change Daily Transfer Limit";
    }

    @Override
    public Role findRole(Long roleId) {
        return em.find(Role.class, roleId);

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
            String employeePosition, String employeeNRIC, String employeeMobileNum, String employeeEmail, Set<String> selectedRoles) {

        String account = null;
        String password = null;
        int i;
        int j;

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
            employee.setEmployeeStatus("true");
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

                //set employee to selected roles
                for (j = 1; j < 17; j++) {
                    Integer k = j;
                    Long l = k.longValue();
                    Role role = em.find(Role.class, l);
                    if (role == null) {
                        System.err.println("~~~~~adminSessionBean print role " + role);
                    }

                    if (employeeRoles.contains(role)) {
                        role.addEmployeeToRole(employee);
                    }
                    em.flush();
//                    System.out.println("~~~~~adminSessionBean print role get employees " + role.getEmployee());

                }

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
        Query query = em.createQuery("SELECT e FROM Employee e where e.employeeStatus=:status");
        query.setParameter("status", "true");
        List<Employee> employees = query.getResultList();
        return employees;
    }

    @Override
    public List<Role> getAllRoles() {
        System.out.println("*** adminSessionBean: Display all roles");
        Query query = em.createQuery("SELECT r FROM Role r");
        List<Role> roles = query.getResultList();
        System.out.println("*** adminSessionBean: Display all roles" + roles);
        return roles;
    }

    @Override
    public List<Employee> getArchivedEmployees() {
        System.out.println("*** adminSessionBean: Display archived employee accounts");
        Query query = em.createQuery("SELECT e FROM Employee e where e.employeeStatus=:status");
        query.setParameter("status", "false");
        List<Employee> employees = query.getResultList();
        return employees;
    }

    @Override
    public Employee getEmployeeById(Long employeeId) {

        Employee employee = em.find(Employee.class, employeeId);
        System.out.println("*** adminSessionBean: get employee by id" + employee.getEmployeeName());
        return employee;

    }

    @Override
    public void updateEmployeeAccount(Long id, String name, String department, String position, String mobile, String email, Set<String> roles) {

        Set<Role> employeeRoles = new HashSet<Role>();
        String[] selectedRolesToArray = roles.toArray(new String[roles.size()]);
        int j;

        for (int i = 0; i < selectedRolesToArray.length; i++) {
            System.out.println("*** adminSessionBean print employee role " + selectedRolesToArray[i]);
            Query q = em.createQuery("SELECT r FROM Role r WHERE r.roleName= :name");
            q.setParameter("name", selectedRolesToArray[i]);
            Role findRole = (Role) q.getSingleResult();
            employeeRoles.add(findRole);
        }
        //set selected roles to an employee
        Employee thisEmployee = em.find(Employee.class, id);
        System.out.println("****** infrastructure/EmployeeAdminSessonBean: updateEmployeeAccount(): update this employee: " + thisEmployee.getEmployeeId());
        thisEmployee.setEmployeeName(name);
        thisEmployee.setEmployeeDepartment(department);
        thisEmployee.setEmployeePosition(position);
        thisEmployee.setEmployeeMobileNum(mobile);
        thisEmployee.setEmployeeEmail(email);
        thisEmployee.setRole(employeeRoles);
        System.out.println("****** infrastructure/EmployeeAdminSessonBean: updateEmployeeAccount(): employee account updated" + thisEmployee.getEmployeeId());

        //update employee of selected roles
        for (j = 1; j < 17; j++) {
            Integer k = j;
            Long l = k.longValue();
            Role role = em.find(Role.class, l);
            if (role == null) {
                System.err.println("~~~~~adminSessionBean print role " + role);
            }

            if (employeeRoles.contains(role) && !role.getEmployee().contains(thisEmployee)) {
                role.addEmployeeToRole(thisEmployee);
            } else if (!employeeRoles.contains(role) && role.getEmployee().contains(thisEmployee)) {
                role.deleteEmployeeFromRole(thisEmployee);
            }

            em.flush();
            System.out.println("~~~~~adminSessionBean print role get employees " + role.getEmployee());

        }

    }

    @Override
    public Set<String> getSelectedRoles(Long employeeId) {
        System.out.println("###### employeeAdminSessionBean: getSeletectedRoles: start");
        Employee employee = em.find(Employee.class, employeeId);

        System.out.println("*** adminSessionBean-getSelectedRoles: get employee by id" + employee.getEmployeeName());
        Set<Role> employeeRoles = employee.getRole();
        String[] roles = new String[16];
        Set<String> selectedRoles = new HashSet();

        if (!employeeRoles.isEmpty()) {
            int i = 0;

            Iterator iterator = employeeRoles.iterator();
            while (iterator.hasNext()) {
                Role employeeRole = (Role) iterator.next();
                String employeeRoleToString = employeeRole.getRoleName();
                roles[i] = employeeRoleToString;
                selectedRoles.add(roles[i]);
                i++;
            }
        }

        System.out.println(selectedRoles);

        return selectedRoles;
    }

    @Override
    public String[] getSelectedPermissionList(Long roleId) {
        System.out.println("###### employeeAdminSessionBean: getSeletectedPermissionList: start");
        Role role = em.find(Role.class, roleId);

        System.out.println("*** adminSessionBean-getSelectedRoles: getrole by id" + role.getRoleName());
        Set<Permission> permissions = role.getPermissions();
        String[] permissionList = new String[16];

        if (!permissions.isEmpty()) {
            int i = 0;

            Iterator iterator = permissions.iterator();
            while (iterator.hasNext()) {
                Permission permission = (Permission) iterator.next();
                String permissionToString = permission.getPermissionName();
                permissionList[i] = permissionToString;
                i++;
            }
        }

        System.out.println(permissionList);

        return permissionList;
    }

    @Override
    public void setSelectedPermissionList(String roleName, String[] selectedPermission) {
        System.out.println("====== internalSystem/employeAdminSessionBean: setSelectedPermission() ======");
        Query query = em.createQuery("Select r from Role r where r.roleName= :name");
        query.setParameter("name", roleName);
        Role role = (Role) query.getSingleResult();

        Set<Permission> permission = new HashSet<Permission>();

        for (int i = 0; i < selectedPermission.length; i++) {
            System.out.println("*** adminSessionBean print permission " + selectedPermission[i]);
            Query q = em.createQuery("SELECT p FROM Permission p WHERE p.permissionName= :name");
            q.setParameter("name", selectedPermission[i]);
            Permission findPermission = (Permission) q.getSingleResult();
            permission.add(findPermission);
        }

        role.setPermissions(permission);
        em.flush();

        //update roles of selected permissions
        for (int j = 1; j < 5; j++) {
            Integer k = j;
            Long l = k.longValue();
            Permission thisPermission = em.find(Permission.class, l);
            if (thisPermission == null) {
                System.err.println("~~~~~adminSessionBean print permission is null");
            }

            if (permission.contains(thisPermission) && !thisPermission.getRoles().contains(role)) {
                thisPermission.addRoleToPermission(role);
            } else if (!permission.contains(thisPermission) && thisPermission.getRoles().contains(role)) {
                thisPermission.deleteRoleFromPermission(role);
            }

            em.flush();
            System.out.println("~~~~~adminSessionBean print permission get roles " + thisPermission);

        }//end for loop

    }

    @Override
    public void setSelectedRoles(Long employeeId, Set<String> selectedRoles) {
        System.out.println("====== internalSystem/employeAdminSessionBean: setSelectedRoles() ======");
        Employee employee = em.find(Employee.class, employeeId);

        Set<Role> employeeRoles = new HashSet<Role>();
        String[] selectedRolesToArray = selectedRoles.toArray(new String[selectedRoles.size()]);
        for (int i = 0; i < selectedRolesToArray.length; i++) {
            System.out.println("*** adminSessionBean print employee role " + selectedRolesToArray[i]);
            Query q = em.createQuery("SELECT r FROM Role r WHERE r.roleName= :name");
            q.setParameter("name", selectedRolesToArray[i]);
            Role findRole = (Role) q.getSingleResult();
            employeeRoles.add(findRole);
        }
        //set selected roles to an employee
        employee.setRole(employeeRoles);
        em.flush();
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

    @Override
    public String[] getPermissionList1() {
        System.out.println("*** adminSessionBean: Display all permissions" + permissionList1);
        return permissionList1;
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
        System.out.println("*** adminSessionBean: Archive employee account");

        Employee findEmployee = em.find(Employee.class, employeeId);
        System.out.println("*** adminSessionBean: Archive employee account name: " + findEmployee.getEmployeeName());
        findEmployee.setEmployeeStatus("false");
        em.flush();
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
        System.out.println("*** employeeAdminSessionBean: employeeNRIC" + employeeNRIC);
        Query query = em.createQuery("SELECT e FROM Employee e WHERE e.employeeNRIC= :NRIC");
        query.setParameter("NRIC", employeeNRIC);
        Employee employee = new Employee();
        List resultList = query.getResultList();
        if (resultList.isEmpty()) {
            System.out.println("*** employeeAdminSessionBean: employee cannt be found");
            return "";
        } else {
            employee = (Employee) resultList.get(0);
            System.out.println("*** employeeAdminSessionBean: employee id" + employee.getEmployeeId());
            Integer userId = (employee.getEmployeeId()).intValue() + 1000;
            System.out.println("*** employeeAdminSessionBean: employee id" + userId);
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
            if (thisEmployee.getEmployeeStatus().equals("false")) {
                System.out.println("*** adminSessionBean: login(): invalid user");
                return "invalidAccount";
            } else {
                if (thisEmployee.getEmployeePassword().equals(password)) {
                    System.out.println("*** adminSessionBean: login(): valid account and password" + ": account " + thisEmployee.getEmployeeAccountNum());
                    return "loggedIn";
                } else {
                    System.out.println("*** adminSessionBean: login(): invalid password");
                    return "invalidPassword";
                }
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

    @Override
    public Role getRoleByName(String roleName) {
        Query query = em.createQuery("SELECT r FROM Role r WHERE r.roleName = :name");
        query.setParameter("name", roleName);
        Role role = (Role) query.getSingleResult();

        return role;

    }

    public Permission getPermissionByName(String permissionName) {
        Query query = em.createQuery("SELECT p FROM Permission p WHERE p.permissionName = :name");
        query.setParameter("name", permissionName);
        Permission permission = (Permission) query.getSingleResult();

        return permission;

    }

    @Override
    public List<Permission> getPermissionList(String roleName) {

        System.out.println("*** adminSessionBean: Display all permissions of " + roleName);
        Query query = em.createQuery("SELECT r FROM Role r WHERE r.roleName = :name");
        query.setParameter("name", roleName);
        Role role = (Role) query.getSingleResult();

        Set<Permission> permissions = new HashSet();
        permissions = role.getPermissions();
        List<Permission> permissionsList = new ArrayList<Permission>(permissions);

        System.out.println("***** adminSessionBean: all permissions of the role " + permissions);

        return permissionsList;
    }

    @Override
    public void deletePermission(String roleName, String permissionName) {
        System.out.println("*** adminSessionBean: Display all permissions of " + roleName);
        Role role = getRoleByName(roleName);
        Permission permission = getPermissionByName(permissionName);

        role.deletePermissionFromRole(permission);
        permission.deleteRoleFromPermission(role);
        em.flush();
    }

    @Override
    public void addPermissionToRole(String roleName, String permissionName) {
        Role role = getRoleByName(roleName);
        Permission permission = getPermissionByName(permissionName);

        role.addPermissionToRole(permission);
        permission.addRoleToPermission(role);
        em.flush();
    }

}
