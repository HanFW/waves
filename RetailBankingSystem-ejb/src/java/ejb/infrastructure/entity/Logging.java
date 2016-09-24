/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.infrastructure.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 *
 * @author hanfengwei
 */
@Entity
public class Logging implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String logTime;
    private String logParty;
    private Long logPerson;
    private String logActivity;
    private String logResult;
    private String logRemarks;
    
    public void create(String party, Long person, String activity, String result, String remarks){
        this.setLogTime(new Date().toString());
        this.setLogParty(party);
        this.setLogPerson(person);
        this.setLogActivity(activity);
        this.setLogResult(result);
        this.setLogRemarks(remarks);
    }
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLogTime() {
        return logTime;
    }

    public void setLogTime(String logTime) {
        this.logTime = logTime;
    }

    public String getLogParty() {
        return logParty;
    }

    public void setLogParty(String logParty) {
        this.logParty = logParty;
    }

    public Long getLogPerson() {
        return logPerson;
    }

    public void setLogPerson(Long logPerson) {
        this.logPerson = logPerson;
    }

    public String getLogActivity() {
        return logActivity;
    }

    public void setLogActivity(String logActivity) {
        this.logActivity = logActivity;
    }

    public String getLogResult() {
        return logResult;
    }

    public void setLogResult(String logResult) {
        this.logResult = logResult;
    }

    public String getLogRemarks() {
        return logRemarks;
    }

    public void setLogRemarks(String logRemarks) {
        this.logRemarks = logRemarks;
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Logging)) {
            return false;
        }
        Logging other = (Logging) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ejb.infrastructure.entity.Logging[ id=" + id + " ]";
    }
    
}
