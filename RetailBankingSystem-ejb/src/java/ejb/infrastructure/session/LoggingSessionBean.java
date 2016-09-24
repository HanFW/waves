/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.infrastructure.session;

import ejb.infrastructure.entity.Logging;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author hanfengwei
 */
@Stateless
public class LoggingSessionBean implements LoggingSessionBeanLocal {
    
    @PersistenceContext
    private EntityManager em;

    @Override
    public void createNewLogging(String trigger, Long person, String activity, String result, String remarks){
        System.out.println("#");
        System.out.println("##### infrastructure/LoggingSessionBean: createNewLogging() ######");
        Logging log = new Logging();
        log.create(trigger, person, activity, result, remarks);
        em.persist(log);
        em.flush();
    }
}
