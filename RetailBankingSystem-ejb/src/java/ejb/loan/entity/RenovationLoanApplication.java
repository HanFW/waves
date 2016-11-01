/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.loan.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.OneToOne;

/**
 *
 * @author hanfengwei
 */
@Entity
public class RenovationLoanApplication extends LoanApplication implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @OneToOne(mappedBy="renovationLoanApplication")
    private MortgageLoanApplication mortgageLoanApplication;
    
    public void create(double amountRequired, int periodRequired){
        this.setAmountRequired(amountRequired);
        this.setPeriodRequired(periodRequired);
        this.setLoanType("Renovation Loan");
        this.setApplicationDate(new Date());
        this.setApplicationStatus("pending");
    }

    public MortgageLoanApplication getMortgageLoanApplication() {
        return mortgageLoanApplication;
    }

    public void setMortgageLoanApplication(MortgageLoanApplication mortgageLoanApplication) {
        this.mortgageLoanApplication = mortgageLoanApplication;
    }
    
}
