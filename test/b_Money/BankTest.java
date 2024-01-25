package b_Money;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class BankTest {
	Currency SEK, DKK;
	Bank SweBank, Nordea, DanskeBank;
	
	@Before
	public void setUp() throws Exception {
		DKK = new Currency("DKK", 0.20);
		SEK = new Currency("SEK", 0.15);
		SweBank = new Bank("SweBank", SEK);
		Nordea = new Bank("Nordea", SEK);
		DanskeBank = new Bank("DanskeBank", DKK);
		SweBank.openAccount("Ulrika");
		SweBank.openAccount("Bob");
		Nordea.openAccount("Bob");
		DanskeBank.openAccount("Gertrud");
	}

	//Sprawdza, czy metoda getName zwraca poprawną nazwę banku
	@Test
	public void testGetName() {
		assertEquals("Nordea", Nordea.getName());
		assertEquals( "SweBank", SweBank.getName());
	}

	//Sprawdza, czy metoda getCurrency zwraca poprawną walutę banku
	@Test
	public void testGetCurrency() {
		assertEquals( SEK, Nordea.getCurrency());
		assertEquals( DKK, DanskeBank.getCurrency());
	}

	//Testuje otwieranie konta w banku. Rzuci wyjątek AccountExistsException, jeśli próba otwarcia konta o nazwie,
	//która już istnieje, zwraca poprawny stan konta dla nowo otwartego konta i sprawdza, czy saldo konta jest niezerowe.
	@Test
	public void testOpenAccount() throws AccountExistsException, AccountDoesNotExistException {
		AccountExistsException thrown = assertThrows(AccountExistsException.class, () -> SweBank.openAccount("Bob"));
		SweBank.openAccount("Bobbb");
		assertNotNull(SweBank.getBalance("Bobbb"));
	}

	//Testuje operację wpłaty na konto
	@Test
	public void testDeposit() throws AccountDoesNotExistException {
		AccountDoesNotExistException thrown = assertThrows(AccountDoesNotExistException.class, () -> SweBank.deposit("Bobbb", new Money(123, SEK)));
		assertTrue(thrown != null);
		SweBank.deposit("Bobbb", new Money(123, SEK));
		assertEquals(123, (int)SweBank.getBalance("Bobbb"));
	}

	//Testuje operację wypłaty z konta. Rzuci wyjątek AccountDoesNotExistException, jeśli próba wypłaty z nieistniejącego konta.
	//Sprawdza, czy operacja wypłaty zmienia saldo konta zgodnie z oczekiwaniami.
	@Test
	public void testWithdraw() throws AccountDoesNotExistException {
		AccountDoesNotExistException thrown = assertThrows(AccountDoesNotExistException.class, () -> SweBank.withdraw("Bobbb", new Money(123, SEK)));
		assertTrue(thrown != null);
		SweBank.withdraw("Bob", new Money(123, SEK));
		assertEquals(-123, (int)SweBank.getBalance("Bob"));
	}

	//Testuje metodę getBalance. Rzuci wyjątek AccountDoesNotExistException, jeśli próba uzyskania salda dla nieistniejącego konta.
	//Sprawdza, czy saldo konta jest poprawne po operacjach wypłaty i wpłaty oraz po usunięciu płatności cyklicznej.
	@Test
	public void testGetBalance() throws AccountDoesNotExistException {
		AccountDoesNotExistException thrown = assertThrows(AccountDoesNotExistException.class, () -> SweBank.getBalance("Bobbb"));
		assertEquals(0, (int)SweBank.getBalance("Bob"));
		SweBank.withdraw("Bob", new Money(123, SEK));
		assertEquals(-123, (int)SweBank.getBalance("Bob"));
		SweBank.deposit("Bob", new Money(123*2, SEK));
		assertEquals(123, (int)SweBank.getBalance("Bob"));
	}

	//Testuje operację transferu między kontami. Rzuci wyjątek AccountDoesNotExistException,
	// jeśli jedno z kont nie istnieje. Sprawdza, czy operacja transferu poprawnie zmienia saldo obu kont.
	@Test
	public void testTransfer() throws AccountDoesNotExistException {
		Money amount = new Money(100, SEK);
		assertThrows(AccountDoesNotExistException.class, () -> SweBank.transfer("Bobbb", SweBank,"Bob",amount));
		SweBank.transfer("Bob", Nordea,"Bob",amount);
		assertEquals(-100, (int)SweBank.getBalance("Bob"));
		assertEquals(100, (int)Nordea.getBalance("Bob"));
	}

	//Testuje dodawanie, usuwanie i wykonanie płatności cyklicznej. Rzuci wyjątek AccountDoesNotExistException, jeśli konto nie istnieje.
	// Dodaje płatność cykliczną, wykonuje jedno wywołanie tick(), sprawdza, czy saldo konta zmienia się zgodnie z oczekiwaniami oraz
	// czy saldo beneficjenta płatności cyklicznej jest poprawne. Następnie usuwa płatność cykliczną i wykonuje dodatkowe wywołania tick(),
	// sprawdzając, czy saldo konta nie ulega zmianie po usunięciu płatności.
	@Test
	public void testTimedPayment() throws AccountDoesNotExistException {
		assertThrows(AccountDoesNotExistException.class, () -> SweBank.addTimedPayment("Bobbb", "0",3,3, new Money(100, SEK), DanskeBank, "Gertrud"));
		SweBank.addTimedPayment("Bob", "2", 1, 0, new Money(100, SEK), DanskeBank, "Gertrud");
		SweBank.tick();
		assertEquals(-100, (int)SweBank.getBalance("Bob"));
		assertEquals(75, (int)DanskeBank.getBalance("Gertrud"));
		SweBank.removeTimedPayment("Bob","2");
		SweBank.tick();
		SweBank.tick();
		assertEquals(-100, (int)SweBank.getBalance("Bob"));
		assertEquals(75, (int)DanskeBank.getBalance("Gertrud"));
	}
}
