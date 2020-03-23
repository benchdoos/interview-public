package com.devexperts.account;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
@RequiredArgsConstructor
@Data
@Builder
public class Account {

    private final AccountKey accountKey;

    private final String firstName;

    private final String lastName;

    /**
     * todo: better use {@link BigDecimal}. Using {@link Double} is not save
     */
    private Double balance;


    public double getValidBalance() {
        return this.balance == null ? 0.0 : this.balance;
    }
}
