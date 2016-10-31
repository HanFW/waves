/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.loan.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Entity;

/**
 *
 * @author hanfengwei
 */
@Entity
public class CarLoanApplication extends LoanApplication implements Serializable {
    private static final long serialVersionUID = 1L;
    
    
    public void create(double amountRequired, int periodRequired){
        this.setAmountRequired(amountRequired);
        this.setPeriodRequired(periodRequired);
        this.setLoanType("Car Loan");
        this.setApplicationDate(new Date());
        this.setApplicationStatus("pending");
    }
}
