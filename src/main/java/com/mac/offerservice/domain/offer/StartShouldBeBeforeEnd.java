package com.mac.offerservice.domain.offer;

import com.mac.offerservice.infrastructure.validators.StartShouldBeBeforeEndValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = StartShouldBeBeforeEndValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface StartShouldBeBeforeEnd {
    String message() default "End date should be before start date!";

    String startDate();

    String endDate();

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
