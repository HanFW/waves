package util;

import ejb.common.util.EjbTimerSessionBeanLocal;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

@Named(value = "triggerTimerManagedBean")
@RequestScoped

public class TriggerTimerManagedBean {

    @EJB
    private EjbTimerSessionBeanLocal ejbTimerSessionBeanLocal;

    private ExternalContext ec;

    public TriggerTimerManagedBean() {
    }

    public void createOneDayTimer() {
        ec = FacesContext.getCurrentInstance().getExternalContext();
        ejbTimerSessionBeanLocal.createTimer10000MS();
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Create One Day Timer Successfully", "Successfully"));
    }

    public void cancelOneDayTimer() {
        ec = FacesContext.getCurrentInstance().getExternalContext();
        ejbTimerSessionBeanLocal.cancelTimer10000MS();
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Cancel One Day Timer Successfully", "Successfully"));
    }

    public void createOneMonthTimer() {
        ec = FacesContext.getCurrentInstance().getExternalContext();
        ejbTimerSessionBeanLocal.createTimer300000MS();
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Create One Month Timer Successfully", "Successfully"));
    }

    public void cancelOneMonthTimer() {
        ec = FacesContext.getCurrentInstance().getExternalContext();
        ejbTimerSessionBeanLocal.cancelTimer300000MS();
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Cancel One Month Timer Successfully", "Successfully"));
    }

    public void createDailyTransferTimer() {
        ec = FacesContext.getCurrentInstance().getExternalContext();
        ejbTimerSessionBeanLocal.createTimer15000MS();
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Create Daily Transfer Timer Successfully", "Successfully"));
    }

    public void cancelDailyTransferTimer() {
        ec = FacesContext.getCurrentInstance().getExternalContext();
        ejbTimerSessionBeanLocal.cancelTimer15000MS();
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Cancel Daily Transfer Timer Successfully", "Successfully"));
    }
}
