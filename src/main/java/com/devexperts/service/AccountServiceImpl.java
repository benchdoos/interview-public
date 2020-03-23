package com.devexperts.service;

import com.devexperts.account.Account;
import com.devexperts.account.AccountKey;
import com.devexperts.exceptions.AccountAlreadyExistsException;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.HashMap;
import java.util.Map;

@Service
public class AccountServiceImpl implements AccountService {

    private final Map<AccountKey, Account> accounts = new HashMap<>();

    @Override
    public void clear() {
        accounts.clear();
    }

    @Override
    public void createAccount(Account account) {
        Assert.notNull(account);
        Assert.notNull(account.getAccountKey()); //if accountId could be changed after, It would be better to generate new one

        checkIfAccountWithGivenAccountKeyDoesNotExists(account);

        accounts.put(account.getAccountKey(), account);
    }

    @Override
    public Account getAccount(long id) {
        final AccountKey accountKey = AccountKey.valueOf(id);
        return accounts.get(accountKey);
    }

    @Override
    public void transfer(Account source, Account target, double amount) {
        //do nothing for now
    }

    /**
     * Validate if account with given {@link AccountKey} already exists.
     *
     * @param account account
     * @throws AccountAlreadyExistsException if account with given id already exists
     */
    private void checkIfAccountWithGivenAccountKeyDoesNotExists(Account account) {
        final boolean contains = accounts.containsKey(account.getAccountKey());
        if (contains) {
            throw new AccountAlreadyExistsException();
        }
    }
}
