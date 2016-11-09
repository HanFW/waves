/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.wealth.entity;

import java.io.Serializable;
import javax.persistence.Entity;

/**
 *
 * @author aaa
 */
@Entity
public class PortfolioTypeTwo extends Portfolio implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private double productMonthlyIncome;

    public double getProductMonthlyIncome() {
        return productMonthlyIncome;
    }

    public void setProductMonthlyIncome(double productMonthlyIncome) {
        this.productMonthlyIncome = productMonthlyIncome;
    }
    
    
    
}
