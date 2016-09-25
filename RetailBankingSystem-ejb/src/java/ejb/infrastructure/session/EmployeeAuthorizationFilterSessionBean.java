/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.infrastructure.session;

import ejb.infrastructure.entity.Employee;
import ejb.infrastructure.entity.Permission;
import ejb.infrastructure.entity.Role;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Jingyuan
 */
@Stateless
public class EmployeeAuthorizationFilterSessionBean implements EmployeeAuthorizationFilterSessionBeanLocal {

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    @PersistenceContext
    private EntityManager em;

    private Set<Role> findAuthorizedRoles(String path) {
        Query q = em.createQuery("Select p from Permission p");
        List<Permission> permissions = q.getResultList();
        Set<Role> authorizedRoles = new HashSet();

        int i = 0;
        while (i<permissions.size()) {
            Permission permission = permissions.get(i);
            String urlList = permission.getUrlList();
            String[] urlArray = urlList.split(",");
            System.err.println("filterSessionBean: " + i + " "+urlArray);
            System.err.println("filterSessionBean: "+urlArray.length);
            for (int j = 0; j < urlArray.length; j++) {
                if (path.equals(urlArray[j])) {
                    System.err.println("filterSessionBean: find!" + i + " "+urlArray[j]);
                    Permission findPermission = permissions.get(i);
                    authorizedRoles = findPermission.getRoles();

                }
            }//end for loop                      
            i++;
        }
        return authorizedRoles;
    }

    @Override
    public boolean authorizationCheck(Employee user, String path) {

        boolean isAuthorized = false;

        Set<Role> authorizedRoles = new HashSet();
        authorizedRoles = findAuthorizedRoles(path);
     
        Set<Role> userRolesSet=new HashSet();
        userRolesSet=user.getRole();
        List<Role> userRoles = new ArrayList<Role>(userRolesSet);

        int i = 0;
        while (i<userRoles.size()) {
            Role userRole = userRoles.get(i);
            if (authorizedRoles.contains(userRole)) {
                isAuthorized = true;
            }
            i++;
        }
        return isAuthorized;
    }

}
