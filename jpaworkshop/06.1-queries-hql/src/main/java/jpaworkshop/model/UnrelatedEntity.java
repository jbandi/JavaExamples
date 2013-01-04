package jpaworkshop.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class UnrelatedEntity {

    @Id @GeneratedValue
    protected Integer id;

    private long numberAttribute;
    private String stringAttribute;

    public long getNumberAttribute() {
        return numberAttribute;
    }

    public void setNumberAttribute(long numberAttribute) {
        this.numberAttribute = numberAttribute;
    }

    public String getStringAttribute() {
        return stringAttribute;
    }

    public void setStringAttribute(String stringAttribute) {
        this.stringAttribute = stringAttribute;
    }

}
