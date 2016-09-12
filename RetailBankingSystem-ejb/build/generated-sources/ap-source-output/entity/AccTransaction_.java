package entity;

import entity.BankAccount;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2016-09-12T13:44:23")
@StaticMetamodel(AccTransaction.class)
public class AccTransaction_ { 

    public static volatile SingularAttribute<AccTransaction, BankAccount> bankAccount;
    public static volatile SingularAttribute<AccTransaction, String> accountCredit;
    public static volatile SingularAttribute<AccTransaction, String> accountDebit;
    public static volatile SingularAttribute<AccTransaction, String> transactionRef;
    public static volatile SingularAttribute<AccTransaction, String> transactionCode;
    public static volatile SingularAttribute<AccTransaction, String> transactionDate;
    public static volatile SingularAttribute<AccTransaction, Long> transactionId;

}