package ejb.card.entity;

import java.io.Serializable;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class CreditCardReport implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long creditCardReportId;
    private String statementDate;
    private String cardType;
    private Long startTime;
    private Long endTime;

    @ManyToOne(cascade = {CascadeType.PERSIST}, fetch = FetchType.EAGER)
    private PrincipalCard principalCard;
    
    public Long getCreditCardReportId() {
        return creditCardReportId;
    }

    public void setCreditCardReportId(Long creditCardReportId) {
        this.creditCardReportId = creditCardReportId;
    }

    public String getStatementDate() {
        return statementDate;
    }

    public void setStatementDate(String statementDate) {
        this.statementDate = statementDate;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public PrincipalCard getPrincipalCard() {
        return principalCard;
    }

    public void setPrincipalCard(PrincipalCard principalCard) {
        this.principalCard = principalCard;
    }

    public Long getStartTime() {
        return startTime;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    public Long getEndTime() {
        return endTime;
    }

    public void setEndTime(Long endTime) {
        this.endTime = endTime;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (creditCardReportId != null ? creditCardReportId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CreditCardReport)) {
            return false;
        }
        CreditCardReport other = (CreditCardReport) object;
        if ((this.creditCardReportId == null && other.creditCardReportId != null) || (this.creditCardReportId != null && !this.creditCardReportId.equals(other.creditCardReportId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ejb.card.entity.CreditCardReport[ id=" + creditCardReportId + " ]";
    }

}
