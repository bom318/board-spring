package bom.basicboard.validation;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.junit.jupiter.api.Test;

import bom.basicboard.domain.Member;


public class ValidationTest {
    
    @Test
    void beanValidation() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();

        Member member = new Member();
        member.setMemberId(" ");
        member.setMemberName(null);
        member.setPassword("1111");

        Set<ConstraintViolation<Member>> violations = validator.validate(member);
        for (ConstraintViolation<Member> violation : violations) {
            System.out.println("violation="+violation);
            System.out.println("violation.message="+violation.getMessage());
        }

    }
}
