package com.demo.validation;

import com.demo.dto.TransactionSearchRequestDto;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class DateLimitValidator
        implements ConstraintValidator<ValidDateLimits, TransactionSearchRequestDto> {

    @Override
    public boolean isValid(
            TransactionSearchRequestDto form,
            ConstraintValidatorContext context) {

        if (form == null) {
            return true;
        }

        if (form.getStartDate() == null ||
                form.getEndDate() == null) {
            return true;
        }

        return !form.getEndDate().minusDays(30).isAfter(form.getStartDate());
    }
}