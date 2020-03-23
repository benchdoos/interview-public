package com.devexperts.account;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@AllArgsConstructor
@RequiredArgsConstructor
@Data
@Builder
public class Account {

    private final AccountKey accountKey;

    private final String firstName;

    private final String lastName;

    private Double balance;
}
