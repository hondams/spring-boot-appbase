package com.github.hondams.validation;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.context.MessageSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.context.MessageSourceProperties;
import org.springframework.boot.validation.MessageInterpolatorFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.SmartValidator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

class SampleDataTest {

    @Test
    @Disabled
    void testDisplayName() {
        for (Field field : SampleData.class.getDeclaredFields()) {
            System.out.println(field);
            for (Annotation annotation : field.getAnnotations()) {
                System.out.println("    " + annotation);
            }
        }
        for (Method method : SampleData.class.getDeclaredMethods()) {
            System.out.println(method);
            for (Annotation annotation : method.getAnnotations()) {
                System.out.println("    " + annotation);
            }
        }
    }

    @Test
    void test() {
        SampleData data = new SampleData();
        data.setProp1("a");
        data.setProp2("b");
        LocalValidatorFactoryBean validatorFactoryBean = new LocalValidatorFactoryBean();
        MessageSourceAutoConfiguration messageSourceAutoConfiguration =
                new MessageSourceAutoConfiguration();
        MessageSourceProperties messageSourceProperties =
                messageSourceAutoConfiguration.messageSourceProperties();
        messageSourceProperties.setBasename(
                "messages-validation,messages-data-prop-label,messages-code,messages-log");
        MessageSource messageSource =
                messageSourceAutoConfiguration.messageSource(messageSourceProperties);
        MessageInterpolatorFactory interpolatorFactory =
                new MessageInterpolatorFactory(messageSource);
        validatorFactoryBean.setMessageInterpolator(interpolatorFactory.getObject());
        validatorFactoryBean.afterPropertiesSet();
        Validator validator = validatorFactoryBean.getValidator();
        Set<ConstraintViolation<SampleData>> violations = validator.validate(data);
        System.out.println(violations.isEmpty());
        System.out.println(violations);
    }

    @Test
    void test2() {
        SampleData data = new SampleData();
        data.setProp2("100000000");
        LocalValidatorFactoryBean validatorFactoryBean = new LocalValidatorFactoryBean();
        MessageSourceAutoConfiguration messageSourceAutoConfiguration =
                new MessageSourceAutoConfiguration();
        MessageSourceProperties messageSourceProperties =
                messageSourceAutoConfiguration.messageSourceProperties();
        messageSourceProperties.setBasename(
                "messages-validation,messages-data-prop-label,messages-code,messages-log");
        MessageSource messageSource =
                messageSourceAutoConfiguration.messageSource(messageSourceProperties);
        MessageInterpolatorFactory interpolatorFactory =
                new MessageInterpolatorFactory(messageSource);
        validatorFactoryBean.setMessageInterpolator(interpolatorFactory.getObject());
        validatorFactoryBean.afterPropertiesSet();
        SmartValidator validator = validatorFactoryBean;
        BindingResult bindingResult = new BeanPropertyBindingResult(data, "data");
        // BindingResult bindingResult = new DirectFieldBindingResult(data, "data");
        // Set<ConstraintViolation<SampleData>> violations =
        validator.validate(data, bindingResult);
        // System.out.println(violations.isEmpty());
        // System.out.println(violations);
        System.out.println(bindingResult);

        for (ObjectError error : bindingResult.getAllErrors()) {
            System.out.println(messageSource.getMessage(error, LocaleContextHolder.getLocale()));
        }

    }

}
