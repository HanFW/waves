/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.wealth.session;

import javax.ejb.Local;

/**
 *
 * @author aaa
 */
@Local
public interface CreateCustomerRiskProfileSessionBeanLocal {
    public void createRiskProfile(Long id, int answer1,int answer2,int answer3,int answer4,int answer5,int answer6,int score, String level);
}
