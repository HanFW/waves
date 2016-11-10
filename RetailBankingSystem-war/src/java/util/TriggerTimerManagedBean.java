package util;

import ejb.common.util.EjbTimerSessionBeanLocal;
import ejb.common.util.demoSessionBean;
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
    private demoSessionBean demoSessionBean;

    @EJB
    private EjbTimerSessionBeanLocal ejbTimerSessionBeanLocal;

    private ExternalContext ec;

    public TriggerTimerManagedBean() {
    }

    public void create10000msTimer() {
        ec = FacesContext.getCurrentInstance().getExternalContext();
        demoSessionBean.create10000Timer();
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Create 10000ms Timer Successfully", "Successfully"));
    }

    public void cancel10000msTimer() {
        ec = FacesContext.getCurrentInstance().getExternalContext();
        demoSessionBean.cancel10000Timer();
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Cancel 10000ms Timer Successfully", "Successfully"));
    }

    public void create300000Timer() {
        ec = FacesContext.getCurrentInstance().getExternalContext();
        demoSessionBean.create300000Timer();
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Create 300000ms Timer Successfully", "Successfully"));
    }

    public void cancel300000Timer() {
        ec = FacesContext.getCurrentInstance().getExternalContext();
        demoSessionBean.cancel300000Timer();
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Cancel 300000ms Timer Successfully", "Successfully"));
    }

    public void create25000Timer() {
        ec = FacesContext.getCurrentInstance().getExternalContext();
        demoSessionBean.create25000Timer();
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Create 25000ms Timer Successfully", "Successfully"));
    }

    public void cancel25000Timer() {
        ec = FacesContext.getCurrentInstance().getExternalContext();
        demoSessionBean.cancel25000Timer();
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Cancel 25000ms Timer Successfully", "Successfully"));
    }
    
    public void create70000Timer() {
        ec = FacesContext.getCurrentInstance().getExternalContext();
        demoSessionBean.create70000Timer();
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Create 70000ms Timer Successfully", "Successfully"));
    }

    public void cancel70000Timer() {
        ec = FacesContext.getCurrentInstance().getExternalContext();
        demoSessionBean.cancel70000Timer();
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Cancel 70000ms Timer Successfully", "Successfully"));
    }
    
    public void create2000Timer() {
        ec = FacesContext.getCurrentInstance().getExternalContext();
        demoSessionBean.create2000Timer();
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Create 2000ms Timer Successfully", "Successfully"));
    }

    public void cancel2000Timer() {
        ec = FacesContext.getCurrentInstance().getExternalContext();
        demoSessionBean.cancel2000Timer();
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Cancel 2000ms Timer Successfully", "Successfully"));
    }
    
    public void create5000Timer() {
        ec = FacesContext.getCurrentInstance().getExternalContext();
        demoSessionBean.create5000Timer();
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Create 5000ms Timer Successfully", "Successfully"));
    }

    public void cancel5000Timer() {
        ec = FacesContext.getCurrentInstance().getExternalContext();
        demoSessionBean.cancel5000Timer();
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Cancel 5000ms Timer Successfully", "Successfully"));
    }
    
    public void create30000Timer() {
        ec = FacesContext.getCurrentInstance().getExternalContext();
        demoSessionBean.create30000Timer();
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Create 30000ms Timer Successfully", "Successfully"));
    }

    public void cancel30000Timer() {
        ec = FacesContext.getCurrentInstance().getExternalContext();
        demoSessionBean.cancel30000Timer();
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Cancel 30000ms Timer Successfully", "Successfully"));
    }
    
    public void create300000DashboardTimer() {
        ec = FacesContext.getCurrentInstance().getExternalContext();
        demoSessionBean.create300000DashboardTimer();
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Create 300000Dashboard Timer Successfully", "Successfully"));
    }

    public void cancel300000DashboardTimer() {
        ec = FacesContext.getCurrentInstance().getExternalContext();
        demoSessionBean.cancel300000DashboardTimer();
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Cancel 300000Dashboard Timer Successfully", "Successfully"));
    }
}
