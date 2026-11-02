import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class ConcurrentBank {
    private final ConcurrentHashMap<UUID, BankAccount> accounts;

    public ConcurrentBank() {
        accounts = new ConcurrentHashMap<>();
    }

    public BankAccount createAccount(BigDecimal balance) {
        BankAccount newAcc = new BankAccount(balance);
        accounts.put(newAcc.getId(), newAcc);
        return newAcc;
    }

    public void transfer(BankAccount from, BankAccount to, BigDecimal amount) {
        if (from.getId() == to.getId()) throw new IllegalArgumentException("accounts id must not be same");

        List<BankAccount> lockAccounts =  lockAccounts(List.of(from, to));
        try {
            if (from.withdraw(amount)) to.deposit(amount);
            else throw new RuntimeException("not enough money");
        } finally {
            unlockAccounts(lockAccounts);
        }
    }

    public BigDecimal getTotalBalance() {
        BigDecimal totalBalance = BigDecimal.ZERO;
        List<BankAccount> sorted = lockAccounts(accounts.values());
        try {
            for (BankAccount account : sorted) {
                totalBalance = totalBalance.add(account.getBalance());
            }
            return totalBalance;
        } finally {
            unlockAccounts(sorted);
        }
    }

    private List<BankAccount> lockAccounts(Collection<BankAccount> accountsToLock) {
        List<BankAccount> sorted = accountsToLock.stream()
                .sorted(Comparator.comparing(BankAccount::getId)).toList();
        for (BankAccount account : sorted) {
            account.lock();
        }
        return sorted;
    }

    private void unlockAccounts(Collection<BankAccount> accountsToUnlock) {
        for (BankAccount account : accountsToUnlock) {
            account.unlock();
        }
    }
}
