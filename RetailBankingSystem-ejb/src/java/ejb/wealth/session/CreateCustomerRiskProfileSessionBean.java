/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.wealth.session;

import ejb.customer.entity.CustomerBasic;
import ejb.wealth.entity.RiskProfile;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author aaa
 */
@Stateless
public class CreateCustomerRiskProfileSessionBean implements CreateCustomerRiskProfileSessionBeanLocal {

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    @PersistenceContext
    private EntityManager em;

    @Override
    public void createRiskProfile(Long id, int answer1, int answer2, int answer3, int answer4, int answer5, int answer6, int score, String level) {
        RiskProfile riskProfile = new RiskProfile();

        riskProfile.setAnswer1(answer1);
        riskProfile.setAnswer2(answer2);
        riskProfile.setAnswer3(answer3);
        riskProfile.setAnswer4(answer4);
        riskProfile.setAnswer5(answer5);
        riskProfile.setAnswer6(answer6);
        riskProfile.setScore(score);
        riskProfile.setLevel(level);

        CustomerBasic customer = em.find(CustomerBasic.class, id);
        riskProfile.setCustomer(customer);
        customer.setRiskProfile(riskProfile);
        em.persist(riskProfile);
    }
}
