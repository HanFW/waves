package managedbean.common;

import java.io.Serializable;
import java.util.Map;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Named;

@Named(value = "signatureViewManagedBean")
@RequestScoped

public class SignatureViewManagedBean implements Serializable {

    private String customerSignature;
    private String guarantorSignature;
    private String jointApplicantSignature;

    public SignatureViewManagedBean() {
    }

    public String getCustomerSignature() {
        
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        Map<String, Object> sessionMap = externalContext.getSessionMap();
        sessionMap.put("customerSignature", customerSignature);
        
        return customerSignature;
    }

    public void setCustomerSignature(String customerSignature) {
        this.customerSignature = customerSignature;
    }

    public String getGuarantorSignature() {
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        Map<String, Object> sessionMap = externalContext.getSessionMap();
        sessionMap.put("guarantorSignature", guarantorSignature);
        
        return guarantorSignature;
    }

    public void setGuarantorSignature(String guarantorSignature) {
        this.guarantorSignature = guarantorSignature;
    }

    public String getJointApplicantSignature() {
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        Map<String, Object> sessionMap = externalContext.getSessionMap();
        sessionMap.put("jointApplicantSignature", jointApplicantSignature);
        
        return jointApplicantSignature;
    }

    public void setJointApplicantSignature(String jointApplicantSignature) {
        this.jointApplicantSignature = jointApplicantSignature;
    }
    
    
}
