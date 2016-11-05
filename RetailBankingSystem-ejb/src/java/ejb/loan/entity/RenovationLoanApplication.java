/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.loan.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import javax.persistence.Entity;
import javax.persistence.Temporal;

/**
 *
 * @author hanfengwei
 */
@Entity
public class RenovationLoanApplication extends LoanApplication implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date renovationStartDate;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date renovationEndDate;
    private double renovationQuotation;
    
    public void create(double amountRequired, int periodRequired, String employmentStatus,
            Date startDate, Date endDate, double quotation){
        this.setAmountRequired(amountRequired);
        this.setPeriodRequired(periodRequired);
        this.setLoanType("Renovation Loan");
        this.setApplicationDate(new Date());
        this.setApplicationStatus("pending");
        
        this.setRenovationStartDate(startDate);
        this.setRenovationEndDate(endDate);
        this.setRenovationQuotation(quotation);
        
        HashMap docs = new HashMap();
        docs.put("identification", true);
        docs.put("quotation", true);
        docs.put("ownership", true);
        if (employmentStatus.equals("Self-Employed")) {
            docs.put("selfEmployedTax", true);
            docs.put("employeeTax", false);
        } else {
            docs.put("selfEmployedTax", false);
            docs.put("employeeTax", true);
        } 
        this.setUploads(docs);
    }

    public Date getRenovationStartDate() {
        return renovationStartDate;
    }

    public void setRenovationStartDate(Date renovationStartDate) {
        this.renovationStartDate = renovationStartDate;
    }

    public Date getRenovationEndDate() {
        return renovationEndDate;
    }

    public void setRenovationEndDate(Date renovationEndDate) {
        this.renovationEndDate = renovationEndDate;
    }

    public double getRenovationQuotation() {
        return renovationQuotation;
    }

    public void setRenovationQuotation(double renovationQuotation) {
        this.renovationQuotation = renovationQuotation;
    }
    
}
