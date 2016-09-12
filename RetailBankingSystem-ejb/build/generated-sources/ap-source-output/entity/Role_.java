package entity;

import entity.Employee;
import javax.annotation.Generated;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2016-09-12T13:44:23")
@StaticMetamodel(Role.class)
public class Role_ { 

    public static volatile SingularAttribute<Role, String> rolePermission;
    public static volatile SingularAttribute<Role, Long> roleId;
    public static volatile SingularAttribute<Role, String> roleName;
    public static volatile SetAttribute<Role, Employee> employee;
    public static volatile SingularAttribute<Role, String> roleDescription;

}