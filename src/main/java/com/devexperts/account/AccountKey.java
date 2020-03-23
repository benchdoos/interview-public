package com.devexperts.account;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Unique Account identifier
 *
 * <p>
 * NOTE: we suspect that later {@link #accountId} is not going to be uniquely identifying an account,
 * as we might add human-readable account representation and some clearing codes for partners.
 * </p>
 * todo: on accountId business logic change, make real unique account identifier and
 *  switch {@link EqualsAndHashCode.Include} to it.
 * */
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class AccountKey {

    @EqualsAndHashCode.Include
    private final long accountId;

    public static AccountKey valueOf(long accountId) {
        return new AccountKey(accountId);
    }
}
