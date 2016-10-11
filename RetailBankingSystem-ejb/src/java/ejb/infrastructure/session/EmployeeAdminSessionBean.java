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
    private final static String[] positions1;
    private final static String[] positions2;
    private final static String[] genders;
    private final static String[] roles1;
    private final static String[] roles2;
    private final static String[] roles3;
    private final static String[] roles4;
    private final static String[] roles5;

    static {
        departments = new String[5];
        departments[0] = "Board of Directors";
        departments[1] = "Card Department";
        departments[2] = "Loan Department";
        departments[3] = "Sales Department";
        departments[4] = "Operation Department";

        positions = new String[9];
        positions[0] = "Staff";
        positions[1] = "Officer";
        positions[2] = "Manager";
        positions[3] = "Chief Executive Officer";
        positions[4] = "Chief Operation Officer";
        positions[5] = "Chief Financial Officer";
        positions[6] = "Chief Marketing Officer";
        positions[7] = "Chief Information Officer";
        positions[8] = "Chief Technology Officer";

        positions1 = new String[3];
        positions1[0] = "Staff";
        positions1[1] = "Officer";
        positions1[2] = "Manager";

        positions2 = new String[6];
        positions2[0] = "Chief Executive Officer";
        positions2[1] = "Chief Operation Officer";
        positions2[2] = "Chief Financial Officer";
        positions2[3] = "Chief Marketing Officer";
        positions2[4] = "Chief Information Officer";
        positions2[5] = "Chief Technology Officer";

        genders = new String[2];
        genders[0] = "Female";
        genders[1] = "Male";

        roles1 = new String[1];
        roles1[0] = "Director";

        roles2 = new String[5];
        roles2[0] = "Credit Card Manager";
        roles2[1] = "Credit Card Verifier";
        roles2[2] = "Card Department Manager";
        roles2[3] = "Card Specialist";
        roles2[4] = "Deposit Specialist";

        roles3 = new String[5];
        roles3[0] = "Loan Officer";
        roles3[1] = "Mortgage Appraiser";
        roles3[2] = "Underwriter";
        roles3[3] = "Loan Specialist";
        roles3[4] = "Loan Department Manager";

        roles4 = new String[3];
        roles4[0] = "Relationship Manager";
        roles4[1] = "Sales Department Manager";
        roles4[2] = "Wealth Management Specialist";

        roles5 = new String[5];
        roles5[0] = "Counter Teller";
        roles5[1] = "Customer Service Agent";
        roles5[2] = "Enquiry Processor";
        roles5[3] = "Operation Specialist";
        roles5[4] = "Operation Department Manager";

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
    public String createEmployeeAccount(String employeeName, String employeeGender, String employeeDepartment,
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
            employee.setEmployeeGender(employeeGender);
            employee.setEmployeeDepartment(employeeDepartment);
            employee.setEmployeePosition(employeePosition);
            employee.setEmployeeNRIC(employeeNRIC);
            employee.setEmployeeMobileNum(employeeMobileNum);
            employee.setEmployeeEmail(employeeEmail);
            employee.setEmployeeStatus("true");
            employee.setLogInStatus("true");
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
                    System.out.println("~~~~~adminSessionBean print role get employees " + role.getEmployee());

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
    public List<String> getEmployeeGenders() {
        System.out.println("*** adminSessionBean: Display all employee genders");
        return Arrays.asList(genders);
    }

    @Override
    public List<String> getPositionsByDepartment(String department) {
        if (department.equals("Board of Directors")) {
            return Arrays.asList(positions2);
        } else {
            return Arrays.asList(positions1);
        }
    }

    @Override
    public List<String> getRolesByDepartment(String department) {
        switch (department) {
            case "Board of Directors":
                return Arrays.asList(roles1);
            case "Card Department":
                return Arrays.asList(roles2);
            case "Loan Department":
                return Arrays.asList(roles3);
            case "Sales Department":
                return Arrays.asList(roles4);
            default:
                return Arrays.asList(roles5);
        }

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
