package entity;

import entity.CustomerBasic;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2016-09-12T13:44:23")
@StaticMetamodel(Payee.class)
public class Payee_ { 

    public static volatile SingularAttribute<Payee, String> payeeName;
    public static volatile SingularAttribute<Payee, String> lastTransactionDate;
    public static volatile SingularAttribute<Payee, String> payeeAccountType;
    public static volatile SingularAttribute<Payee, Long> customerBasicId;
    public static volatile SingularAttribute<Payee, String> payeeAccountNum;
    public static volatile SingularAttribute<Payee, CustomerBasic> customerBasic;
    public static volatile SingularAttribute<Payee, Long> payeeId;

}