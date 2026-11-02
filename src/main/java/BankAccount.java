/*
В виртуальном банке "ConcurrentBank" решено внедрить многопоточность для обработки операций по счетам клиентов.
Система должна поддерживать возможность одновременного пополнения (deposit), снятия (withdraw),
а также переводов (transfer) между счетами. Каждый счет имеет свой уникальный номер.

Реализуйте класс BankAccount с методами deposit, withdraw и getBalance, поддерживающими многопоточное взаимодействие.

Реализуйте класс ConcurrentBank для управления счетами и выполнения переводов между ними.
Класс должен предоставлять методы createAccount для создания нового счета и transfer для выполнения переводов между счетами.

Переводы между счетами должны быть атомарными, чтобы избежать ситуаций,
когда одна часть транзакции выполняется успешно, а другая нет.

Реализуйте метод getTotalBalance, который возвращает общий баланс всех счетов в банке.
 */

import java.math.BigDecimal;
import java.util.UUID;
import java.util.concurrent.locks.ReentrantLock;

public class BankAccount {

    private final UUID id;
    private BigDecimal balance;
    private final ReentrantLock lock = new ReentrantLock();

    public BankAccount(BigDecimal balance) {
        if (balance == null) throw new IllegalArgumentException("Balance cant be NULL");
        if (balance.compareTo(BigDecimal.ZERO) >= 0) {
            id = UUID.randomUUID();
            this.balance = balance;
        } else {
            throw new IllegalArgumentException("Balance must be > 0");
        }
    }

    public UUID getId() {
        return id;
    }

    public BigDecimal getBalance() {
        lock.lock();
        try {
            return balance;
        } finally {
            lock.unlock();
        }
    }

    public boolean deposit(BigDecimal amount) {
        if (isPositive(amount)) {
            lock.lock();
            try {
                balance = balance.add(amount);
                return true;
            } finally {
                lock.unlock();
            }
        }
        return false;
    }


    public boolean withdraw(BigDecimal amount) {
        if (isPositive(amount)) {
            lock.lock();
            try {
                if (amount.compareTo(balance) <= 0) {
                    balance = balance.subtract(amount);
                    return true;
                } else return false;

            } finally {
                lock.unlock();
            }
        }
        return false;
    }

    public void lock() {
        lock.lock();
    }

    public void unlock() {
        lock.unlock();
    }

    private boolean isPositive(BigDecimal amount) {
        return amount != null && amount.compareTo(BigDecimal.ZERO) > 0;
    }

}
