package managedbean.payment.customer;

import javax.inject.Named;
import javax.enterprise.context.RequestScoped;

@Named(value = "standingInstructionManagedBean")
@RequestScoped

public class StandingInstructionManagedBean {

    private boolean visible1=false;
    
    public StandingInstructionManagedBean() {
    }

    public boolean isVisible1() {
        return visible1;
    }

    public void setVisible1(boolean visible1) {
        this.visible1 = visible1;
    }
    
}
