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
 * @author Jingyuan
 */
@Entity
public class DebitCardType implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long debitCardTypeId;

    @OneToMany(cascade = {CascadeType.ALL}, fetch = FetchType.EAGER, mappedBy = "debitCardType")
    private List<DebitCard> debitCards;

    private String debitCardTypeName;
    private String cardNetwork;
    private String principal;
    private String annualFeeWaiver;
    private double cashBackRate;

    public Long getDebitCardTypeId() {
        return debitCardTypeId;
    }

    public void setDebitCardTypeId(Long debitCardTypeId) {
        this.debitCardTypeId = debitCardTypeId;
    }

    public String getDebitCardTypeName() {
        return debitCardTypeName;
    }

    public void setDebitCardTypeName(String debitCardTypeName) {
        this.debitCardTypeName = debitCardTypeName;
    }

    public String getCardNetwork() {
        return cardNetwork;
    }

    public void setCardNetwork(String cardNetwork) {
        this.cardNetwork = cardNetwork;
    }

    public String getPrincipal() {
        return principal;
    }

    public void setPrincipal(String principal) {
        this.principal = principal;
    }

    public String getAnnualFeeWaiver() {
        return annualFeeWaiver;
    }

    public void setAnnualFeeWaiver(String annualFeeWaiver) {
        this.annualFeeWaiver = annualFeeWaiver;
    }

    public double getCashBackRate() {
        return cashBackRate;
    }

    public void setCashBackRate(double cashBackRate) {
        this.cashBackRate = cashBackRate;
    }

    public List<DebitCard> getDebitCards() {
        return debitCards;
    }

    public void setDebitCards(List<DebitCard> debitCards) {
        this.debitCards = debitCards;
    }
    
    public void addDebitCard(DebitCard debitCard){
        this.debitCards.add(debitCard);
    }
    
    public void removeDebitCard(DebitCard debitCard){
        this.debitCards.remove(debitCard);
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (debitCardTypeId != null ? debitCardTypeId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DebitCardType)) {
            return false;
        }
        DebitCardType other = (DebitCardType) object;
        if ((this.debitCardTypeId == null && other.debitCardTypeId != null) || (this.debitCardTypeId != null && !this.debitCardTypeId.equals(other.debitCardTypeId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ejb.card.entity.DebitCardType[ id=" + debitCardTypeId + " ]";
    }

}
