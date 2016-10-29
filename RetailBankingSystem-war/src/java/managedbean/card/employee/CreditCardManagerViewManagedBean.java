/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbean.card.employee;

import ejb.card.entity.CreditCard;
import ejb.card.session.CreditCardSessionBeanLocal;
import java.io.IOException;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

/**
 *
 * @author aaa
 */
@Named(value = "creditCardManagerViewManagedBean")
@RequestScoped
public class CreditCardManagerViewManagedBean {

    @EJB
    private CreditCardSessionBeanLocal creditCardSessionLocal;
    /**
     * Creates a new instance of CreditCardManagerViewManagedBean
     */
    private List<CreditCard> creditCards;

    public CreditCardManagerViewManagedBean() {
    }

    @PostConstruct
    public void init() {
        creditCards = creditCardSessionLocal.getAllPendingCreditCards();
    }

    public void viewApplication(Long creditCardId) throws IOException {
        System.out.println("====== creditCard/CreditCardMangerViewManagedBean: viewApplication() ======");
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        ec.getFlash().put("creditCardId", creditCardId);

        ec.redirect(ec.getRequestContextPath() + "/web/internalSystem/card/creditCard/creditCardManagerProcessApplication.xhtml?faces-redirect=true");
    }

    public List<CreditCard> getCreditCards() {
        return creditCards;
    }

    public void setCreditCards(List<CreditCard> creditCards) {
        this.creditCards = creditCards;
    }

}
