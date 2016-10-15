/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbean.card.customer;

import ejb.card.session.DebitCardManagementSessionBeanLocal;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
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
@Named(value = "customerReportLossManagedBean")
@RequestScoped
public class CustomerReportLossManagedBean implements Serializable {

    /**
     * Creates a new instance of CustomerReportLossManagedBean
     */
    @EJB
    private DebitCardManagementSessionBeanLocal debitCardManagementSessionBeanLocal;

    private String cardType;
    private String debitCardNum;
    private String debitCardPwd;

    public CustomerReportLossManagedBean() {
    }

    public void reportCardLoss(ActionEvent event) {

        System.out.println("debug: cancel card");
        if (cardType.equals("debit")) {
            reportDebitCardLoss();
        }
    }

    public void reportDebitCardLoss() {
        FacesContext context = FacesContext.getCurrentInstance();
        ExternalContext ec = context.getExternalContext();
        FacesMessage message = null;
        
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        Date reportLossDate1 = new Date();
        String reportLossDate = df.format(reportLossDate1);


        System.out.println("debug: ReportDebitCardLoss- debit card num " + debitCardNum);
        System.out.println("debug: ReportDebitCardLoss- debit card Pwd " + debitCardPwd);
        System.out.println("debug: ReportDebitCardLoss- report loss date " + reportLossDate);
        String result = debitCardManagementSessionBeanLocal.reportDebitCardLoss(debitCardNum, debitCardPwd,reportLossDate);

        switch (result) {
            case "success":
                message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Your card has been suceesfully reported as loss, we will send a new card to your mailing address in 2-3 working days", null);
                context.addMessage(null, message);
                System.out.println("debit card report loss");
                break;
            case "debit card not exist":
                message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Card not exist! Please check the card number input", null);
                context.addMessage(null, message);
                break;
            case "wrong pwd":
                message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Password is wrong! Please check the card password input", null);
                context.addMessage(null, message);
                break;
        }
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public String getDebitCardNum() {
        return debitCardNum;
    }

    public void setDebitCardNum(String debitCardNum) {
        this.debitCardNum = debitCardNum;
    }

    public String getDebitCardPwd() {
        return debitCardPwd;
    }

    public void setDebitCardPwd(String debitCardPwd) {
        this.debitCardPwd = debitCardPwd;
    }

}
