/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbean.card.customer;

import ejb.card.entity.PrincipalCard;
import ejb.card.session.CreditCardManagementSessionBeanLocal;
import ejb.card.session.CreditCardSessionBeanLocal;
import ejb.customer.entity.CustomerBasic;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.FlowEvent;
import org.primefaces.model.UploadedFile;

/**
 *
 * @author aaa
 */
@Named(value = "customerAddSupplementaryManagedBean")
@ViewScoped
public class CustomerAddSupplementaryManagedBean implements Serializable{

    @EJB
    private CreditCardManagementSessionBeanLocal creditCardManagementSessionBeanLocal;

    @EJB
    private CreditCardSessionBeanLocal creditCardSessionBeanLocal;

    private String principalCardName;
    private String cardHolderName;
    private String identificationNum;
    private Date dateOfBirth;
    private String relationship;

    private boolean show;

    //documents
    private UploadedFile file;
    private HashMap uploads = new HashMap();

    public CustomerAddSupplementaryManagedBean() {
    }

    public void addSupplementaryCard() {
        FacesContext context = FacesContext.getCurrentInstance();
        ExternalContext ec = context.getExternalContext();
        FacesMessage message = null;
        CustomerBasic customer = getCustomerViaSessionMap();
        String[] creditCardInfo = principalCardName.split("-");
        String principalCardNum = creditCardInfo[2];
        PrincipalCard pc = creditCardManagementSessionBeanLocal.getPrincipalByCardNum(principalCardNum);
        if (pc.getCreditCardType().getCreditCardTypeId() != 3) {
            message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Only Merlion Platinum Card can apply for supplementary cards", null);
            context.addMessage(null, message);
        } else if (getAge(dateOfBirth) < 18) {
            System.out.println("@@@@@@@@@@@@supplementary age " + getAge(dateOfBirth));
            message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Supplementary card holder must be at least 18 years old", null);
            context.addMessage(null, message);
        } else if (pc.getSupplementaryCards().size() >= 2) {
            message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "A Merlion Platinum Card can only have 2 supplementary cards. You have reached the maximum limit", null);
            context.addMessage(null, message);
        } else {
            String dob = changeDateFormat(dateOfBirth);
            System.out.println("@@@@@@@@@@@@@@@@cad holder name" + cardHolderName + "@@@@@@@ID num " + identificationNum);

            creditCardSessionBeanLocal.addSupplementaryCard(pc.getCardId(), cardHolderName, dob, relationship, identificationNum);
            message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Supplementary card added successfully!", null);
            context.addMessage(null, message);
        }
    }

    public void identificationUpload(FileUploadEvent event) throws FileNotFoundException, IOException {
        this.file = event.getFile();

        if (file != null) {
            String newFilePath = System.getProperty("user.dir").replace("config", "docroot") + System.getProperty("file.separator");
            System.out.println("?????????????????????supp id " + identificationNum);
            System.out.println("???//////////////////?supp id " + getIdentificationNum());
            String filename = "Supplementary-" + getIdentificationNum() + ".pdf";
            System.out.println("!!!!!!!!!!supp id " + identificationNum);
            File newFile = new File(newFilePath, filename);
            FileOutputStream fileOutputStream = new FileOutputStream(newFile);

            int a;
            int BUFFER_SIZE = 8192;
            byte[] buffer = new byte[BUFFER_SIZE];

            InputStream inputStream = file.getInputstream();

            while (true) {
                a = inputStream.read(buffer);

                if (a < 0) {
                    break;
                }

                fileOutputStream.write(buffer, 0, a);
                fileOutputStream.flush();
            }

            fileOutputStream.close();
            inputStream.close();

            uploads.replace("identification", true);
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, file.getFileName() + " uploaded successfully.", "");
            FacesContext.getCurrentInstance().addMessage("identificationUpload", message);
        } else {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Cannot find the file, please upload again.", "");
            FacesContext.getCurrentInstance().addMessage("identificationUpload", message);
        }
    }

    public void displayUpload() {
        show = true;
    }

    public String onFlowProcess(FlowEvent event) {
        return event.getNewStep();
    }

    public void checkAllInfo() {
        FacesContext context = FacesContext.getCurrentInstance();
        ExternalContext ec = context.getExternalContext();
        FacesMessage message = null;
        CustomerBasic customer = getCustomerViaSessionMap();
        String[] creditCardInfo = principalCardName.split("-");
        String principalCardNum = creditCardInfo[2];
        PrincipalCard pc = creditCardManagementSessionBeanLocal.getPrincipalByCardNum(principalCardNum);
        if (pc.getCreditCardType().getCreditCardTypeId() != 3) {
            message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Only Merlion Platinum Card can apply for supplementary cards", null);
            context.addMessage(null, message);
        } else if (getAge(dateOfBirth) < 18) {
            System.out.println("@@@@@@@@@@@@supplementary age " + getAge(dateOfBirth));
            message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Supplementary card holder must be at least 18 years old", null);
            context.addMessage(null, message);
        } else if (pc.getSupplementaryCards().size() >= 2) {
            message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "A Merlion Platinum Card can only have 2 supplementary cards. You have reached the maximum limit", null);
            context.addMessage(null, message);
        } else {
            RequestContext rc = RequestContext.getCurrentInstance();
            rc.execute("PF('addSupplementaryCardWizard').next();");
        }
    }

    private int getAge(Date customerDateOfBirth) {
        Date now = new Date();
        int yearDif = now.getYear() - customerDateOfBirth.getYear();
        int monthDif = now.getMonth() - customerDateOfBirth.getMonth();
        int dayDif = now.getDate() - customerDateOfBirth.getDate();
        if (monthDif < 0) {
            yearDif--;
        }
        if (monthDif == 0) {
            if (dayDif < 0) {
                yearDif--;
            }
        }
        return yearDif;
    }

    private String changeDateFormat(Date inputDate) {
        String dateString = inputDate.toString();
        String[] dateSplit = dateString.split(" ");
        String outputDate = dateSplit[2] + "/" + dateSplit[1] + "/" + dateSplit[5];
        return outputDate;
    }

    public List<String> getAllPrincipalCards() {
        List<String> principalCards = creditCardSessionBeanLocal.getAllPlatinumCards(getCustomerViaSessionMap().getCustomerBasicId());

        System.out.println("#################drop down menu contents " + principalCards);
        return principalCards;

    }

    public String getCardHolderName() {
        return cardHolderName;
    }

    public void setCardHolderName(String cardHolderName) {
        this.cardHolderName = cardHolderName;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getRelationship() {
        return relationship;
    }

    public void setRelationship(String relationship) {
        this.relationship = relationship;
    }

    public String getPrincipalCardName() {
        return principalCardName;
    }

    public void setPrincipalCardName(String principalCardName) {
        this.principalCardName = principalCardName;
    }

    public String getIdentificationNum() {
        return identificationNum;
    }

    public void setIdentificationNum(String identificationNum) {
        this.identificationNum = identificationNum;
    }

    public UploadedFile getFile() {
        return file;
    }

    public void setFile(UploadedFile file) {
        this.file = file;
    }

    public HashMap getUploads() {
        return uploads;
    }

    public void setUploads(HashMap uploads) {
        this.uploads = uploads;
    }

    public boolean isShow() {
        return show;
    }

    public void setShow(boolean show) {
        this.show = show;
    }

    public CustomerBasic getCustomerViaSessionMap() {
        FacesContext context = FacesContext.getCurrentInstance();
        CustomerBasic customer = (CustomerBasic) context.getExternalContext().getSessionMap().get("customer");

        return customer;
    }

}
