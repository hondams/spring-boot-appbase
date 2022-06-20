package com.github.hondams.validation;

import java.util.Objects;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import com.github.hondams.appbase.validation.Correlation;

public class SampleDataCorrelatioValidator implements ConstraintValidator<Correlation, SampleData> {

    @Override
    public boolean isValid(SampleData value, ConstraintValidatorContext context) {
        if (!Objects.equals(value.getProp1(), value.getProp2())) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("test1")//
                    .addPropertyNode("prop1")//
                    .addConstraintViolation();
            return false;
        }
        return true;
    }

}
