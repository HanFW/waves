/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.card.entity;

import java.io.Serializable;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

/**
 *
 * @author aaa
 */
@Entity
public class CreditCardType implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long creditCardTypeId;
    
    @OneToMany(cascade = {CascadeType.ALL}, fetch = FetchType.EAGER, mappedBy = "creditCardType")
    private List<CreditCard> creditCard;
    
    private String creditCardTypeName;
    private String cardNetwork;
    private String rebateType;
    private double rebate;
    private double annualFee;
    private double latePaymentCharge;
    private double interestCharge;
    private double minSum;
    private double maxLiability;

    public Long getCreditCardTypeId() {
        return creditCardTypeId;
    }

    public void setCreditCardTypeId(Long creditCardTypeId) {
        this.creditCardTypeId = creditCardTypeId;
    }

    public List<CreditCard> getCreditCard() {
        return creditCard;
    }

    public void setCreditCard(List<CreditCard> creditCard) {
        this.creditCard = creditCard;
    }

    public String getCreditCardTypeName() {
        return creditCardTypeName;
    }

    public void setCreditCardTypeName(String creditCardTypeName) {
        this.creditCardTypeName = creditCardTypeName;
    }

    public String getCardNetwork() {
        return cardNetwork;
    }

    public void setCardNetwork(String cardNetwork) {
        this.cardNetwork = cardNetwork;
    }

    public String getRebateType() {
        return rebateType;
    }

    public void setRebateType(String rebateType) {
        this.rebateType = rebateType;
    }

    public double getRebate() {
        return rebate;
    }

    public void setRebate(double rebate) {
        this.rebate = rebate;
    }

    public double getAnnualFee() {
        return annualFee;
    }

    public void setAnnualFee(double annualFee) {
        this.annualFee = annualFee;
    }

    public double getLatePaymentCharge() {
        return latePaymentCharge;
    }

    public void setLatePaymentCharge(double latePaymentCharge) {
        this.latePaymentCharge = latePaymentCharge;
    }

    public double getInterestCharge() {
        return interestCharge;
    }

    public void setInterestCharge(double interestCharge) {
        this.interestCharge = interestCharge;
    }

    public double getMinSum() {
        return minSum;
    }

    public void setMinSum(double minSum) {
        this.minSum = minSum;
    }

    public double getMaxLiability() {
        return maxLiability;
    }

    public void setMaxLiability(double maxLiability) {
        this.maxLiability = maxLiability;
    }
    
    

    

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (creditCardTypeId != null ? creditCardTypeId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CreditCardType)) {
            return false;
        }
        CreditCardType other = (CreditCardType) object;
        if ((this.creditCardTypeId == null && other.creditCardTypeId != null) || (this.creditCardTypeId != null && !this.creditCardTypeId.equals(other.creditCardTypeId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ejb.card.entity.CreditCardType[ id=" + creditCardTypeId + " ]";
    }
    
}
