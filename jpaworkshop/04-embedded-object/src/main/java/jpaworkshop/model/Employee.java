package jpaworkshop.model;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Employee {

    @Id
    @GeneratedValue
    protected Integer id;
    
    private String name;
    private long salary;

    @Embedded
    private Address address;

//    @Embedded
//    @AttributeOverrides({
//    	@AttributeOverride(name = "street", column = @Column(name = "billing_street")),
//    	@AttributeOverride(name = "city", column = @Column(name = "billing_city")),
//    	@AttributeOverride(name = "state", column = @Column(name = "billing_state")),
//    	@AttributeOverride(name = "zip", column = @Column(name = "billing_zicode")),
//    })
//    private Address billingAddress;
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getSalary() {
        return salary;
    }

    public void setSalary(long salary) {
        this.salary = salary;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append(this.getClass().getName());
        sb.append(" id: ");
        sb.append(this.id);
        sb.append(" name: ");
        sb.append(this.name);
        sb.append(" salary: ");
        sb.append(this.salary);
        return sb.toString();
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

//    public Address getBillingAddress() {
//    	return billingAddress;
//    }
//    
//    public void setBillingAddress(Address address) {
//    	this.billingAddress = address;
//    }

}
