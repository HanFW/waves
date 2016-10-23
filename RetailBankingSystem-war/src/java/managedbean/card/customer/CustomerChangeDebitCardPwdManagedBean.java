/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbean.card.customer;

import ejb.card.session.DebitCardPasswordSessionBeanLocal;
import ejb.card.session.DebitCardSessionBeanLocal;
import ejb.customer.entity.CustomerBasic;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

/**
 *
 * @author Jingyuan
 */
@Named(value = "customerChangeDebitCardPwdManagedBean")
@RequestScoped
public class CustomerChangeDebitCardPwdManagedBean {

    /**
     * Creates a new instance of CustomerChangeDebitCardPwdManagedBean
     */
    @EJB
    private DebitCardPasswordSessionBeanLocal debitCardPasswordSessionBeanLocal;

    @EJB
    DebitCardSessionBeanLocal debitCardSessionBeanLocal;

    private CustomerBasic customer;

    private List<String> debitCards = new ArrayList<String>();
    private String selectedDebitCard;

    private int currentDebitCardPwd;
    private int debitCardPwd;
    private int debitCardPwd1;

    public CustomerChangeDebitCardPwdManagedBean() {
    }

    public void changeDebitCardPassword(ActionEvent event) {
        FacesContext context = FacesContext.getCurrentInstance();
        ExternalContext ec = context.getExternalContext();
        FacesMessage message = null;

        String[] debitCardInfo = selectedDebitCard.split("-");
        String debitCardNum = debitCardInfo[1];

        System.out.println("current pwd " + currentDebitCardPwd);
        System.out.println("pwd1 " + debitCardPwd);
        System.out.println("pwd2 " + debitCardPwd1);

        if (debitCardPwd != debitCardPwd1) {
            message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Password does not match!", null);
            context.addMessage(null, message);
        } else {
            String currentDebitCardPwdToString = String.valueOf(currentDebitCardPwd);
            String debitCardPwdToString = String.valueOf(debitCardPwd);
            System.out.println("pwd " + debitCardPwdToString);
            System.out.println("debitCardNum " + debitCardNum);
            String result = debitCardPasswordSessionBeanLocal.changePassword(currentDebitCardPwdToString, debitCardPwdToString, debitCardNum);

            System.out.println("====== card/debitCard/CustomerChangeDebitCardPwdManagedBean: set password for debit Card");

            if (result.equals("success")) {
                System.out.println("1");
                message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Password has been successfully changed for your debit card!", null);
                context.addMessage(null, message);
            } else {
                System.out.println("2");
                message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Please check the current debit card password input!", null);
                context.addMessage(null, message);
            }

        }

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
    }

    public int getCurrentDebitCardPwd() {
        return currentDebitCardPwd;
    }

    public void setCurrentDebitCardPwd(int currentDebitCardPwd) {
        this.currentDebitCardPwd = currentDebitCardPwd;
    }

    public int getDebitCardPwd() {
        return debitCardPwd;
    }

    public void setDebitCardPwd(int debitCardPwd) {
        this.debitCardPwd = debitCardPwd;
    }

    public int getDebitCardPwd1() {
        return debitCardPwd1;
    }

    public void setDebitCardPwd1(int debitCardPwd1) {
        this.debitCardPwd1 = debitCardPwd1;
    }

    public CustomerBasic getCustomerViaSessionMap() {
        FacesContext context = FacesContext.getCurrentInstance();
        customer = (CustomerBasic) context.getExternalContext().getSessionMap().get("customer");

        return customer;

    }

}
