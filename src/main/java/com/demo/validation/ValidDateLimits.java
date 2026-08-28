package com.demo.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;


@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = DateLimitValidator.class)
@Documented
public @interface ValidDateLimits {

    String message() default "The limit is only 30 days";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}