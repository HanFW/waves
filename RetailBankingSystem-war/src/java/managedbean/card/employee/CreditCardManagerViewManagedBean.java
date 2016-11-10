/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbean.card.employee;

import ejb.card.entity.PrincipalCard;
import ejb.card.entity.SupplementaryCard;
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
    private List<PrincipalCard> principalCards;
    private List<SupplementaryCard> supplementaryCards;

    public CreditCardManagerViewManagedBean() {
    }

    @PostConstruct
    public void init() {
        principalCards = creditCardSessionLocal.getAllPendingCreditCards();
        supplementaryCards = creditCardSessionLocal.getAllPendingSupplementaryCards();
    }

    public void viewApplication(Long creditCardId) throws IOException {
        System.out.println("====== creditCard/CreditCardMangerViewManagedBean: viewApplication() ======");
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        ec.getSessionMap().put("creditCardId", creditCardId);

        ec.redirect(ec.getRequestContextPath() + "/web/internalSystem/card/creditCard/creditCardManagerProcessApplication.xhtml?faces-redirect=true");
    }
    
    public void viewSupplementaryApplication (Long supplementaryCardId) throws IOException{
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        ec.getFlash().put("supplementaryCardId", supplementaryCardId);

        ec.redirect(ec.getRequestContextPath() + "/web/internalSystem/card/creditCard/creditCardManagerProcessSupplementaryApplication.xhtml?faces-redirect=true");
    }

    public List<PrincipalCard> getPrincipalCards() {
        return principalCards;
    }

    public void setPrincipalCards(List<PrincipalCard> principalCards) {
        this.principalCards = principalCards;
    }

    public List<SupplementaryCard> getSupplementaryCards() {
        return supplementaryCards;
    }

    public void setSupplementaryCards(List<SupplementaryCard> supplementaryCards) {
        this.supplementaryCards = supplementaryCards;
    }

    

}
