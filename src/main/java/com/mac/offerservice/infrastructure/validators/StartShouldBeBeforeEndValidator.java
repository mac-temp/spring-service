package com.mac.offerservice.infrastructure.validators;

import com.mac.offerservice.domain.offer.StartShouldBeBeforeEnd;
import org.springframework.beans.BeanWrapperImpl;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDateTime;

public class StartShouldBeBeforeEndValidator implements ConstraintValidator<StartShouldBeBeforeEnd, Object> {
    private String startDate;
    private String endDate;

    public void initialize(StartShouldBeBeforeEnd constraint) {
        this.startDate = constraint.startDate();
        this.endDate = constraint.endDate();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        LocalDateTime fieldValue = (LocalDateTime) new BeanWrapperImpl(value)
                .getPropertyValue(startDate);
        LocalDateTime fieldMatchValue = (LocalDateTime) new BeanWrapperImpl(value)
                .getPropertyValue(endDate);

        return fieldValue.isBefore(fieldMatchValue);
    }
}
