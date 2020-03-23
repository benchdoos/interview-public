package com.devexperts.service;

import com.devexperts.account.Account;
import com.devexperts.account.AccountKey;
import com.devexperts.annotations.validators.BalanceValidator;
import com.devexperts.exceptions.AccountAlreadyExistsException;
import com.devexperts.exceptions.AccountNotFoundException;
import com.devexperts.exceptions.NotEnoughBalanceException;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class AccountServiceImpl implements AccountService {

    private final Map<AccountKey, Account> accounts = new HashMap<>();

    @Override
    public void clear() {
        accounts.clear();
    }

    @Override
    public void createAccount(Account account) {
        Assert.notNull(account, "Account must not be null");
        //if accountId could be changed after, It would be better to generate new one
        Assert.notNull(account.getAccountKey(), "Account key must be not null");

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
        validateTransfer(source, target, amount);

        final Account sourceAccount = Optional.of(getAccount(source.getAccountKey().getAccountId()))
                .orElseThrow(AccountNotFoundException::new);

        final Account targetAccount = Optional.of(getAccount(target.getAccountKey().getAccountId()))
                .orElseThrow(AccountNotFoundException::new);

        transferMoney(sourceAccount, targetAccount, amount);

    }

    private void validateTransfer(Account source, Account target, double amount) {
        Assert.notNull(source, "Source account must not be null");
        Assert.notNull(source.getAccountKey(), "Source account key must be not null");
        Assert.notNull(target, "Target account must not be null");
        Assert.notNull(target.getAccountKey(), "Target account key must be not null");
        Assert.isTrue(Double.isFinite(amount), "Amount must be a correct number");
        Assert.isTrue(amount > 0, "Amount must be bigger then 0");

        //not the best solution
        final BalanceValidator balanceValidator = new BalanceValidator();
        if (!balanceValidator.isValid(amount, null)) {
            throw new IllegalArgumentException("Amount is not valid");
        }
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

    /**
     * Perform money transfer between accounts.
     *
     * @param source from
     * @param target to
     * @param amount positive number of money to transfer
     * @throws NotEnoughBalanceException if source account will have negative balance
     */
    private void transferMoney(Account source, Account target, double amount) {
        if (source.getAccountKey().equals(target.getAccountKey())) {
            throw new IllegalArgumentException("Money can not be transferred from - to same account");
        }

        if (source.getValidBalance() < 0) {
            throw new NotEnoughBalanceException();
        }

        final double futureBalance = source.getValidBalance() - amount; //todo maybe Math?
        if (futureBalance < 0) {
            throw new NotEnoughBalanceException();
        }

        source.setBalance(futureBalance);
        target.setBalance(target.getValidBalance() + amount);
    }
}
