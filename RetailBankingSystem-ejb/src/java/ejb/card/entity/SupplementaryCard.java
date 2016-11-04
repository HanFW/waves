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
import javax.persistence.ManyToOne;

/**
 *
 * @author aaa
 */
@Entity
public class SupplementaryCard extends CreditCard implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String relationship;
    private String dateOfBirth;
    private String identificationNum;
    
    @ManyToOne(cascade = {CascadeType.PERSIST}, fetch = FetchType.EAGER)
    private PrincipalCard principalCard;

    public PrincipalCard getPrincipalCard() {
        return principalCard;
    }

    public void setPrincipalCard(PrincipalCard principalCard) {
        this.principalCard = principalCard;
    }

    public String getRelationship() {
        return relationship;
    }

    public void setRelationship(String relationship) {
        this.relationship = relationship;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getIdentificationNum() {
        return identificationNum;
    }

    public void setIdentificationNum(String identificationNum) {
        this.identificationNum = identificationNum;
    }
    




    
}
