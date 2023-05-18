package com.example.football_api.exceptions.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.BeanWrapperImpl;

import java.util.Objects;

public class FieldsValueMatchValidator
        implements ConstraintValidator<MatchConstraints, Object> {

    private String homeTeam;
    private String awayTeam;

    @Override
    public void initialize(MatchConstraints constraintAnnotation) {
        this.homeTeam = constraintAnnotation.homeTeam();
        this.awayTeam = constraintAnnotation.awayTeam();
    }

    @Override
    public boolean isValid(Object value,
                           ConstraintValidatorContext context) {
        Object fieldValue = new BeanWrapperImpl(value)
                .getPropertyValue(homeTeam);
        Object fieldMatchValue = new BeanWrapperImpl(value)
                .getPropertyValue(awayTeam);

        return checkUnique(fieldValue, fieldMatchValue);
    }

    public boolean checkUnique(Object obj1, Object obj2){
        if(obj1 == null || obj2 == null){
            return true;
        }
        return !Objects.equals(obj1, obj2);
    }
}
