package b_Money;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class CurrencyTest {
	Currency SEK, DKK, NOK, EUR;
	
	@Before
	public void setUp() throws Exception {
		SEK = new Currency("SEK", 0.15);
		DKK = new Currency("DKK", 0.20);
		EUR = new Currency("EUR", 1.5);
	}

	//Sprawdza, czy metoda getName zwraca poprawne nazwy walut dla utworzonych obiektów Currency (SEK, EUR, DKK)
	@Test
	public void testGetName() {
		assertEquals( "SEK", SEK.getName());
		assertEquals( "EUR", EUR.getName());
		assertEquals( "DKK", DKK.getName());
	}

	//Sprawdza, czy metoda getRate zwraca poprawne kursy wymiany dla utworzonych obiektów Currency (SEK, EUR, DKK)
	@Test
	public void testGetRate() {
		assertEquals(0.15d, SEK.getRate(), 0);
		assertEquals(1.5d, EUR.getRate(), 0);
		assertEquals(0.2d, DKK.getRate(), 0);
	}

	//Testuje poprawność ustawiania kursów wymiany przy użyciu metody setRate dla obiektów Currency (SEK, EUR, DKK)
	@Test
	public void testSetRate() {
		SEK.setRate(0.9);
		assertEquals(0.9, SEK.getRate(), 0);
		DKK.setRate(0.33);
		assertEquals(0.33, DKK.getRate(), 0);
		EUR.setRate(0.84);
		assertEquals(0.84, EUR.getRate(), 0);
	}

	//Sprawdza poprawność obliczania wartości globalnej dla różnych kwot w różnych walutach
	@Test
	public void testGlobalValue() {
		assertEquals( 15,(int)SEK.universalValue(100));
		assertEquals(20_000,(int)DKK.universalValue(100_000));
		assertEquals( 150,(int)EUR.universalValue(100));
	}

	//Sprawdza poprawność obliczania wartości w danej walucie na podstawie kwoty w innej walucie
	@Test
	public void testValueInThisCurrency() {
		assertEquals( 1000,(int)EUR.valueInThisCurrency(10000,SEK));
		assertEquals(10_000,(int)SEK.valueInThisCurrency(1000,EUR));
		assertEquals( 7_500,(int)DKK.valueInThisCurrency(1000,EUR));
	}

}
