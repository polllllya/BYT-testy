package b_Money;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class AccountTest {
	Currency SEK, EUR;
	Bank Nordea;
	Bank DanskeBank;
	Bank SweBank;
	Account testAccount;

	Money money;
	
	@Before
	public void setUp() throws Exception {
		SEK = new Currency("SEK", 0.15);
		EUR = new Currency("EUR", 1.5);
		SweBank = new Bank("SweBank", SEK);
		SweBank.openAccount("Alice");
		testAccount = new Account("Hans", SEK);
		money = new Money(10_000_000, SEK);
		testAccount.deposit(money);

		SweBank.deposit("Alice", new Money(1000000, SEK));
	}

//	Sprawdza poprawność dodawania i usuwania płatności cyklicznej do/z konta
	@Test
	public void testAddRemoveTimedPayment() {
		testAccount.addTimedPayment("1",31,31,new Money(10,SEK),SweBank, "Alice");
		assertEquals( true,testAccount.timedPaymentExists("1"));
		testAccount.removeTimedPayment("1");
		assertEquals( false,testAccount.timedPaymentExists("1"));
	}


//	Sprawdza poprawność funkcji płatności cyklicznej na koncie
	@Test
	public void testTimedPayment() throws AccountDoesNotExistException {
		testAccount.addTimedPayment("1",2,0, new Money(10,SEK),SweBank, "Alice");
		testAccount.tick();
		assertEquals( 10_000_000 - 10, (int)testAccount.getBalance().getAmount());
		testAccount.tick();
		testAccount.tick();
		testAccount.tick();
		assertEquals( 10_000_000 - 10 - 10, (int)testAccount.getBalance().getAmount());

	}
//	sprawdza poprawność operacji wypłaty i wpłaty na koncie
	@Test
	public void testAddWithdraw() {
		testAccount.withdraw(new Money(200,EUR));
		assertEquals( 9_998_000, (int)testAccount.getBalance().getAmount());
		testAccount.deposit(new Money(200,EUR));
		assertEquals( 10_000_000, (int)testAccount.getBalance().getAmount());
	}

//	sprawdza poprawność metody getBalance, która zwraca aktualny stan konta
	@Test
	public void testGetBalance() {
		assertEquals( true, testAccount.getBalance().equals(money));
	}
}
