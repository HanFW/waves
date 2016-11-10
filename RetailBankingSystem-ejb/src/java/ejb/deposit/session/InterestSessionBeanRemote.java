/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.deposit.session;

import ejb.deposit.entity.Interest;
import javax.ejb.Remote;

/**
 *
 * @author Nicole
 */
@Remote
public interface InterestSessionBeanRemote {

    public Long addNewInterest(String dailyInterest, String monthlyInterest, String isTransfer,
            String isWithdraw);

    public Interest retrieveInterestById(Long interestId);

    public Interest retrieveInterestByAccountNum(String bankAccountNum);

    public String deleteInterest(Long interestId);
}
