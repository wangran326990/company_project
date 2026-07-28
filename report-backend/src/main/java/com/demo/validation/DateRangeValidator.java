package com.demo.validation;

import com.demo.dto.TransactionSearchRequestDto;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class DateRangeValidator
        implements ConstraintValidator<ValidDateRange, TransactionSearchRequestDto> {

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

        return !form.getStartDate()
                .isAfter(form.getEndDate());
    }
}