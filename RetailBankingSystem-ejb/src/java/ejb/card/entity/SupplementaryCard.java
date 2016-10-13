/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.card.entity;

import java.io.Serializable;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

/**
 *
 * @author aaa
 */
@Entity
public class SupplementaryCard implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long supplementaryCardId;
    
    @OneToOne(cascade = {CascadeType.ALL}, fetch = FetchType.EAGER)
    private CreditCard creditCard;
    
    private String creditCardNum;
    private String cardHolderName;
    private String creditCardExpiryDate;

    public Long getSupplementaryCardId() {
        return supplementaryCardId;
    }

    public void setSupplementaryCardId(Long supplementaryCardId) {
        this.supplementaryCardId = supplementaryCardId;
    }

    public CreditCard getCreditCard() {
        return creditCard;
    }

    public void setCreditCard(CreditCard creditCard) {
        this.creditCard = creditCard;
    }

    public String getCreditCardNum() {
        return creditCardNum;
    }

    public void setCreditCardNum(String creditCardNum) {
        this.creditCardNum = creditCardNum;
    }

    public String getCardHolderName() {
        return cardHolderName;
    }

    public void setCardHolderName(String cardHolderName) {
        this.cardHolderName = cardHolderName;
    }

    public String getCreditCardExpiryDate() {
        return creditCardExpiryDate;
    }

    public void setCreditCardExpiryDate(String creditCardExpiryDate) {
        this.creditCardExpiryDate = creditCardExpiryDate;
    }



    @Override
    public int hashCode() {
        int hash = 0;
        hash += (supplementaryCardId != null ? supplementaryCardId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SupplementaryCard)) {
            return false;
        }
        SupplementaryCard other = (SupplementaryCard) object;
        if ((this.supplementaryCardId == null && other.supplementaryCardId != null) || (this.supplementaryCardId != null && !this.supplementaryCardId.equals(other.supplementaryCardId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ejb.card.entity.SupplementaryCard[ id=" + supplementaryCardId + " ]";
    }
    
}
