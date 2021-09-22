package se.nackademin.java20.lab1.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AccountTest {

    @Test
    void withdraw() {
        Account account = new Account("Me", 100L);
        account.withdraw(50);
        assertEquals(50, account.getBalance());
    }

    @Test
    void withDrawExcessiveAmount() {
        Account account = new Account("Me", 50);
        assertThrows(IllegalStateException.class, () -> account.withdraw(100));
        assertDoesNotThrow(() -> account.withdraw(20));
    }

    @Test
    void deposit() {
        Account account = new Account("Me", 100L);
        account.deposit(100L);
        assertEquals(200, account.getBalance());
    }

    //returning a private field the test isn't really worth testing with junit
    @Test
    void getId() {
        Account account = new Account("Me", 100L);
        assertEquals(0, account.getId());
    }

    @Test
    void getHolder() {
        Account account = new Account("Me", 100L);
        assertEquals("Me", account.getHolder());
    }

    @Test
    void getBalance() {
        Account account = new Account("Me", 100L);
        assertEquals(100, account.getBalance());
    }

}