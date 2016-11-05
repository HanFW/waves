/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbean.card.customer;

import ejb.card.session.DebitCardSessionBeanLocal;
import ejb.card.session.DebitCardTransactionSessionBeanLocal;
import ejb.customer.entity.CustomerBasic;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import org.primefaces.context.RequestContext;
import org.primefaces.event.FlowEvent;

/**
 *
 * @author Jingyuan
 */
@Named(value = "customerSetTransactionLimitForDebitCard")
@ViewScoped
public class CustomerSetTransactionLimitForDebitCardManagedBean implements Serializable {

    /**
     * Creates a new instance of CustomerSetTransactionLimitForDebitCard
     */
    @EJB
    private DebitCardTransactionSessionBeanLocal debitCardTransactionSessionBeanLocal;

    @EJB
    private DebitCardSessionBeanLocal debitCardSessionBeanLocal;

    private CustomerBasic customer;

    private List<String> debitCards = new ArrayList<String>();
    private String selectedDebitCard;

    private String existingTransactionLimit = "";
    private int newTransactionLimit;

    public CustomerSetTransactionLimitForDebitCardManagedBean() {
    }

    public void updateTransactionLimit(ActionEvent event) {

        System.out.println("debug - updatetransactionlimit selectedDebitCard " + selectedDebitCard);
        System.out.println("debug - updatetransactionlimit newTransactionLimit " + newTransactionLimit);

        debitCardTransactionSessionBeanLocal.setTransactionLimit(selectedDebitCard, newTransactionLimit);

        RequestContext rc = RequestContext.getCurrentInstance();
        rc.execute("PF('changeTransactionLimitWizard').next();");

    }

    public void cancelUpdateTransactionLimit(ActionEvent event) throws IOException {

        System.out.println("CustomerSetTransactionLimitForDebitCardManagedBean - update transaction limit for debit card action cancelled");

        FacesContext context = FacesContext.getCurrentInstance();
        ExternalContext ec = context.getExternalContext();

        ec.redirect(ec.getRequestContextPath() + "/web/onlineBanking/deposit/customerDepositIndex.xhtml?faces-redirect=true");
    }

    public String onFlowProcess(FlowEvent event) {
        return event.getNewStep();
    }

    public List<String> getDebitCards() {
        System.out.println("test " + debitCards);
        if (debitCards.isEmpty()) {

            customer = getCustomerViaSessionMap();
            Long id = customer.getCustomerBasicId();
            debitCards = debitCardSessionBeanLocal.getAllActivatedDebitCards(id);

        }
        return debitCards;
    }

    public void setDebitCards(List<String> debitCards) {
        this.debitCards = debitCards;
    }

    public String getSelectedDebitCard() {
        return selectedDebitCard;
    }

    public void setSelectedDebitCard(String selectedDebitCard) {
        this.selectedDebitCard = selectedDebitCard;
        System.out.println("debug: set selectedDebitCard " + selectedDebitCard);
    }

    public String getExistingTransactionLimit() {

        System.out.println("debug: get existingTransactionLimit " + existingTransactionLimit);
        return existingTransactionLimit;
    }

    public void setExistingTransactionLimit(String existingTransactionLimit) {
        this.existingTransactionLimit = existingTransactionLimit;
    }

    public int getNewTransactionLimit() {
        return newTransactionLimit;
    }

    public void setNewTransactionLimit(int newTransactionLimit) {
        this.newTransactionLimit = newTransactionLimit;

    }

    public CustomerBasic getCustomer() {
        return customer;
    }

    public void setCustomer(CustomerBasic customer) {
        this.customer = customer;
    }

    public CustomerBasic getCustomerViaSessionMap() {
        FacesContext context = FacesContext.getCurrentInstance();
        customer = (CustomerBasic) context.getExternalContext().getSessionMap().get("customer");

        return customer;

    }

    public void findExistingLimit() {
        System.out.println("findExistingLimit - selctedDebitCard " + selectedDebitCard);
        int transactionLimit = debitCardTransactionSessionBeanLocal.getTransactionLimitByDebitCardNum(selectedDebitCard);
        existingTransactionLimit = String.valueOf(transactionLimit);
        System.out.println("findExistingLimit " + existingTransactionLimit);
    }

}
