///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package managedbean.card.simulate;
//
//import ejb.card.session.MerlionBankAuthorizeTransactionSessionBeanLocal;
//import java.util.ArrayList;
//import java.util.List;
//import javax.ejb.EJB;
//import javax.inject.Named;
//import javax.enterprise.context.RequestScoped;
//import javax.xml.ws.WebServiceRef;
//import ws.client.merlionTransactionAuthorization.TransactionAuthorizationWebService_Service;
//import ws.client.merlionTransactionAuthorization.TransactionToBeAuthorized;
//
///**
// *
// * @author Jingyuan
// */
//@Named(value = "merlionBankTransactionAuthorizationManagedBean")
//@RequestScoped
//public class MerlionBankTransactionAuthorizationManagedBean {
//
//    @WebServiceRef(wsdlLocation = "WEB-INF/wsdl/localhost_8080/TransactionAuthorizationWebService/TransactionAuthorizationWebService.wsdl")
//    private TransactionAuthorizationWebService_Service service_merlionTransactionAuthorization;
//
//    @EJB
//    private MerlionBankAuthorizeTransactionSessionBeanLocal merlionBankAuthorizeTransactionSessionBeanLocal;
//
//    private List<TransactionToBeAuthorized> transactionList = new ArrayList<>();
//    
//    private TransactionToBeAuthorized transaction;
//
//    public List<TransactionToBeAuthorized> getTransactionList() {
//        transactionList = getAllPendingTransactionsToBeAuthorized();
//
//        return transactionList;
//    }
//
//    public MerlionBankTransactionAuthorizationManagedBean() {
//    }
//
//    public String checkTransactionAuthorization(Long id) {
//        String result = merlionBankAuthorizeTransactionSessionBeanLocal.checkTransactionAuthorizationById(id);
//        return result;
//    }
//
//    private java.util.List<ws.client.merlionTransactionAuthorization.TransactionToBeAuthorized> getAllPendingTransactionsToBeAuthorized() {
//        // Note that the injected javax.xml.ws.Service reference as well as port objects are not thread safe.
//        // If the calling of port operations may lead to race condition some synchronization is required.
//        ws.client.merlionTransactionAuthorization.TransactionAuthorizationWebService port = service_merlionTransactionAuthorization.getTransactionAuthorizationWebServicePort();
//        return port.getAllPendingTransactionsToBeAuthorized();
//    }
//
//    public TransactionToBeAuthorized getTransaction() {
//        return transaction;
//    }
//
//    public void setTransaction(TransactionToBeAuthorized transaction) {
//        this.transaction = transaction;
//    }
//    
//    
//
//}
