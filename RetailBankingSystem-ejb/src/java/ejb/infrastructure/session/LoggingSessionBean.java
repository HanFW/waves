/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.infrastructure.session;

import ejb.infrastructure.entity.Logging;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author hanfengwei
 */
@Stateless
public class LoggingSessionBean implements LoggingSessionBeanLocal, LoggingSessionBeanRemote {
    
    @PersistenceContext
    private EntityManager em;

    @Override
    public void createNewLogging(String trigger, Long person, String activity, String result, String remarks){
        System.out.println("#");
        System.out.println("##### infrastructure/LoggingSessionBean: createNewLogging() ######");
        Logging log = new Logging();
        log.create(new Date().toString(), person, activity, result, remarks);
        em.persist(log);
        em.flush();
    }
    
    @Override
    public List<Logging> retrieveAllCustomerLogging(){
        Query query = em.createQuery("SELECT lg FROM Logging lg WHERE lg.logParty = :logParty");
        query.setParameter("logParty", "customer");
        return query.getResultList();
    }
    
    @Override
    public List<Logging> retrieveAllEmployeeLogging(){
        Query query = em.createQuery("SELECT lg FROM Logging lg WHERE lg.logParty = :logParty");
        query.setParameter("logParty", "employee");
        return query.getResultList();
    }
    
    @Override
    public List<Logging> retrieveAllSystemLogging(){
        Query query = em.createQuery("SELECT lg FROM Logging lg WHERE lg.logParty = :logParty");
        query.setParameter("logParty", "system");
        return query.getResultList();
    }
}
