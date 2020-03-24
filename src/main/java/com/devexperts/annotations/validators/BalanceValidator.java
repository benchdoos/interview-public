package com.devexperts.annotations.validators;

import com.devexperts.annotations.ValidBalance;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.math.BigDecimal;

/**
 * Validator, that provides validation for account balance
 */
public class BalanceValidator implements ConstraintValidator<ValidBalance, Double> {
    @Override
    public void initialize(ValidBalance constraintAnnotation) {
    }

    @Override
    public boolean isValid(Double value, ConstraintValidatorContext context) {
        if (value == null) {
            return false;
        }

        final boolean validNumber = Double.isFinite(value);
        final boolean validPennies = validateDoublePennies(value);

        return validNumber && validPennies;
    }

    /**
     * Check if pennies of balance scale is less then 2
     *
     * @param value to check
     * @return true, if valid
     */
    private boolean validateDoublePennies(Double value) {
        return (BigDecimal.valueOf(value).scale() <= 2);
    }
}
