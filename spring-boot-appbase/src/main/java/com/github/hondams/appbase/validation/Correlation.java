package com.github.hondams.appbase.validation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

@Documented
@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {CorrelationValidator.class}) // (1)
public @interface Correlation {

    String message() default "{com.github.hondams.validation.Correlation.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    Class<?> value();

    @Documented
    @Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface List {
        Correlation[] value();
    }
}
