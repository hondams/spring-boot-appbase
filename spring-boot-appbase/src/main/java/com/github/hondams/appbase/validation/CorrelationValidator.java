package com.github.hondams.appbase.validation;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import com.github.hondams.appbase.exception.SystemException;

public class CorrelationValidator implements ConstraintValidator<Correlation, Object> {

    private static Map<Class<?>, ConstraintValidator<Correlation, Object>> validatorMap =
            new ConcurrentHashMap<>();

    private Class<?> correlatioValidatorType;

    @Override
    public void initialize(Correlation constraintAnnotation) {
        this.correlatioValidatorType = constraintAnnotation.value();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        return getValidator(this.correlatioValidatorType).isValid(value, context);
    }

    @SuppressWarnings("unchecked")
    private static ConstraintValidator<Correlation, Object> getValidator(Class<?> validatorType) {
        ConstraintValidator<Correlation, Object> validator = validatorMap.get(validatorType);
        if (validator == null) {
            try {
                validator = (ConstraintValidator<Correlation, Object>) validatorType
                        .getDeclaredConstructor().newInstance();
            } catch (InstantiationException e) {
                throw new SystemException(e);
            } catch (IllegalAccessException e) {
                throw new SystemException(e);
            } catch (InvocationTargetException e) {
                throw new SystemException(e);
            } catch (NoSuchMethodException e) {
                throw new SystemException(e);
            }
            validatorMap.put(validatorType, validator);
        }
        return validator;
    }
}
