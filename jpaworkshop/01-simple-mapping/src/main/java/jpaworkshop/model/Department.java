package jpaworkshop.model;

import java.util.logging.Logger;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Department {
	
	private static Logger logger = Logger.getLogger(Department.class.toString());

    @Id
    @GeneratedValue
    protected Integer id;
	
    private String name;

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
    	logger.info("Department - setName called!");
        this.name = name;
    }
}
