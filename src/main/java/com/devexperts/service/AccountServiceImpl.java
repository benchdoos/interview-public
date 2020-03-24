package com.devexperts.service;

import com.devexperts.account.Account;
import com.devexperts.account.AccountKey;
import com.devexperts.annotations.validators.BalanceValidator;
import com.devexperts.exceptions.AccountAlreadyExistsException;
import com.devexperts.exceptions.AccountNotFoundException;
import com.devexperts.exceptions.MoneyTransferException;
import com.devexperts.exceptions.NotEnoughBalanceException;
import com.devexperts.exceptions.SameMoneyTransferAccountException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
public class AccountServiceImpl implements AccountService {

    private final Map<AccountKey, Account> accounts = new HashMap<>();

    @Override
    public void clear() {
        log.info("Cleaning accounts storage.");
        accounts.clear();
        log.info("Accounts storage successfully cleaned up.");
    }

    @Override
    public void createAccount(Account account) {
        log.debug("Creating new account: {}", account);
        Assert.notNull(account, "Account must not be null");
        //if accountId could be changed after, It would be better to generate new one
        Assert.notNull(account.getAccountKey(), "Account key must be not null");

        checkIfAccountWithGivenAccountKeyDoesNotExists(account);

        accounts.put(account.getAccountKey(), account);
        log.info("Account with id: {} successfully created.", account.getAccountKey());
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

        log.debug("Transferring money from: {} (balance: {}) to {} (balance:{}).",
                sourceAccount.getAccountKey(),
                sourceAccount.getValidBalance(),
                targetAccount.getAccountKey(),
                targetAccount.getValidBalance());

        transferMoney(sourceAccount, targetAccount, amount);

        log.info("Money transfer successfully finished. From: {} (balance: {}) to {} (balance:{}).",
                sourceAccount.getAccountKey(),
                sourceAccount.getValidBalance(),
                targetAccount.getAccountKey(),
                targetAccount.getValidBalance());
    }

    /**
     * Validate money transfer between 2 accounts
     *
     * @param source account that send money
     * @param target account that receive money
     * @param amount positive number of money to transfer
     */
    private void validateTransfer(Account source, Account target, double amount) {
        Assert.notNull(source, "Source account must not be null");
        Assert.notNull(source.getAccountKey(), "Source account key must be not null");
        Assert.notNull(target, "Target account must not be null");
        Assert.notNull(target.getAccountKey(), "Target account key must be not null");
        Assert.isTrue(Double.isFinite(amount), "Amount must be a correct number. +/-Infinity, NAN are not valid.");
        Assert.isTrue(amount > 0, "Amount must be positive and bigger then 0");

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
            log.warn("Account with given id already exists: {}", account.getAccountKey());
            throw new AccountAlreadyExistsException();
        }
    }

    /**
     * Perform money transfer between accounts.
     *
     * @param source account that send money
     * @param target account that receive money
     * @param amount positive number of money to transfer
     * @throws MoneyTransferException if money transfer is invalid for some reason
     */
    private void transferMoney(Account source, Account target, double amount) {
        if (source.getAccountKey().equals(target.getAccountKey())) {
            throw new SameMoneyTransferAccountException();
        }

        if (source.getValidBalance() < 0) {
            throw new NotEnoughBalanceException();
        }

        final BigDecimal fromBalance = new BigDecimal(source.getValidBalance());
        final BigDecimal toBalance = new BigDecimal(target.getValidBalance());

        final BigDecimal futureSourceBalance = fromBalance
                .subtract(new BigDecimal(amount))
                .setScale(2, RoundingMode.HALF_EVEN);
        final BigDecimal futureTargetBalance = toBalance
                .add(new BigDecimal(amount))
                .setScale(2, RoundingMode.HALF_EVEN);

        if (futureSourceBalance.doubleValue() < 0) {
            throw new NotEnoughBalanceException();
        }

        source.setBalance(futureSourceBalance.doubleValue());
        target.setBalance(futureTargetBalance.doubleValue());
    }
}
