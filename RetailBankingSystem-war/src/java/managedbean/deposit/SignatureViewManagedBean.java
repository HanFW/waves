package managedbean.deposit;

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
}
