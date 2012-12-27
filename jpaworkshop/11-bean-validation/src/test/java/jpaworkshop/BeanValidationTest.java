package jpaworkshop;

import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.groups.Default;
import jpaworkshop.model.Employee;
import org.junit.Assert;

import org.junit.Test;

public class BeanValidationTest {

    private static Logger logger = Logger.getLogger(BeanValidationTest.class.toString());

    @Test
    public void validateEmployee() {
        logger.info("valiateEmployee");

        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

        Employee employee = new Employee();

        Set<ConstraintViolation<Employee>> violations = validator.validate(employee, Default.class);
        for (ConstraintViolation<Employee> constraintViolation : violations) {
            logger.log(Level.SEVERE, constraintViolation.getMessage());
        }
        if (violations.size() > 0) {
            Assert.fail();
        }
    }
}
