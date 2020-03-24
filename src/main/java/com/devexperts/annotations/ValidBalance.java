package com.devexperts.annotations;

import com.devexperts.annotations.validators.BalanceValidator;

import javax.validation.Constraint;
import javax.validation.Payload;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Annotation to check, is balance is valid.
 * Valid balance is Zero, positive and negative.
 * Invalid: {@link Double#NaN}, {@link Double#NEGATIVE_INFINITY}, {@link Double#POSITIVE_INFINITY}
 */
@Target({TYPE, FIELD, ANNOTATION_TYPE, PARAMETER})
@Retention(RUNTIME)
@Constraint(validatedBy = BalanceValidator.class)
public @interface ValidBalance {
    String message() default "Invalid balance. Balance must be not null, not nan or infinite.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
