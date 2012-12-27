package jpaworkshop.test;

import jpaworkshop.model.Employee;
import junit.framework.Assert;

import org.junit.Test;

public class PureUnitTest {
	
	@Test
	public void check_to_string(){
		
		Employee emp = new Employee();
		emp.setId(1);
		emp.setName("John");
		emp.setSalary(1000);
		
		Assert.assertEquals("jpaworkshop.model.Employee id: 1 name: John salary: 1000", emp.toString());
	}
}
