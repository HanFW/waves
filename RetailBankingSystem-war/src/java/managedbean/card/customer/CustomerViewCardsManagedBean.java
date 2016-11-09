/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbean.card.customer;

import ejb.card.entity.DebitCard;
import ejb.card.entity.PrincipalCard;
import ejb.card.entity.SupplementaryCard;
import ejb.card.session.CreditCardSessionBeanLocal;
import ejb.card.session.DebitCardSessionBeanLocal;
import ejb.customer.entity.CustomerBasic;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

/**
 *
 * @author Jingyuan
 */
@Named(value = "customerViewCardsManagedBean")
@RequestScoped
public class CustomerViewCardsManagedBean implements Serializable{

    /**
     * Creates a new instance of CustomerViewCardsManagedBean
     */
    @EJB
    DebitCardSessionBeanLocal debitCardSessionBeanLocal;

    @EJB
    CreditCardSessionBeanLocal creditCardSessionBeanLocal;

    private List<DebitCard> debitCards = new ArrayList<>();
    private List<DebitCard> filteredDebitCards = new ArrayList<>();

    private List<PrincipalCard> creditCards = new ArrayList<>();
    private List<PrincipalCard> filteredCreditCards = new ArrayList<>();
    
    private List<SupplementaryCard> supplementaryCards = new ArrayList<> ();
    private List<SupplementaryCard> filteredSupplementaryCard = new ArrayList<> ();

    private CustomerBasic customer;


    public CustomerViewCardsManagedBean() {
    }

    public List<DebitCard> getDebitCards() {
        Long id = getCustomerViaSessionMap().getCustomerBasicId();
        debitCards = debitCardSessionBeanLocal.viewDebitCards(id);
        System.out.println("getDebitCards"+debitCards);
        return debitCards;
    }

    public void setDebitCards(List<DebitCard> debitCards) {
        this.debitCards = debitCards;
    }


    public List<DebitCard> getFilteredDebitCards() {
        return filteredDebitCards;
    }

    public void setFilteredDebitCards(List<DebitCard> filteredDebitCards) {
        this.filteredDebitCards = filteredDebitCards;
    }


    public CustomerBasic getCustomerViaSessionMap() {
        FacesContext context = FacesContext.getCurrentInstance();
        customer = (CustomerBasic) context.getExternalContext().getSessionMap().get("customer");

        return customer;

    }

    public List<PrincipalCard> getCreditCards() {
        customer = getCustomerViaSessionMap();
        creditCards = creditCardSessionBeanLocal.getAllPrincipalCardByCustomer(customer);
        System.out.println(" getCreditCards: "+creditCards);
        return creditCards;
    }

    public void setCreditCards(List<PrincipalCard> creditCards) {
        this.creditCards = creditCards;
    }

    public List<PrincipalCard> getFilteredCreditCards() {
        return filteredCreditCards;
    }

    public void setFilteredCreditCards(List<PrincipalCard> filteredCreditCards) {
        this.filteredCreditCards = filteredCreditCards;
    }

    public List<SupplementaryCard> getSupplementaryCards() {
        customer = getCustomerViaSessionMap();
        supplementaryCards = creditCardSessionBeanLocal.getAllSupplementaryCardByCustomer(customer);
        System.out.println(" getSupplementaryCards"+supplementaryCards);
        return supplementaryCards;
    }

    public void setSupplementaryCards(List<SupplementaryCard> supplementaryCards) {
        this.supplementaryCards = supplementaryCards;
    }

    public List<SupplementaryCard> getFilteredSupplementaryCard() {
        return filteredSupplementaryCard;
    }

    public void setFilteredSupplementaryCard(List<SupplementaryCard> filteredSupplementaryCard) {
        this.filteredSupplementaryCard = filteredSupplementaryCard;
    }
    
    public void activateCard(ActionEvent event){
        
    }

    public boolean activateCardAction(String status){
        String cardStatus=status.toLowerCase();
        if(!cardStatus.equals("pending") && !cardStatus.equals("activated"))
            return true;
        else
            return false;
    }
      

}
