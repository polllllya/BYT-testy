package b_Money;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class MoneyTest {
	Currency SEK, DKK, NOK, EUR;
	Money SEK100, EUR10, SEK200, EUR20, SEK0, EUR0, SEKn100, DKK100;
	
	@Before
	public void setUp() throws Exception {
		SEK = new Currency("SEK", 0.15);
		DKK = new Currency("DKK", 0.20);
		EUR = new Currency("EUR", 1.5);
		SEK100 = new Money(10000, SEK);
		EUR10 = new Money(1000, EUR);
		SEK200 = new Money(20000, SEK);
		EUR20 = new Money(2000, EUR);
		SEK0 = new Money(0, SEK);
		EUR0 = new Money(0, EUR);
		SEKn100 = new Money(-10000, SEK);
		DKK100 = new Money(10000, DKK);
	}

	//Sprawdza, czy metoda getAmount zwraca poprawne kwoty dla różnych obiektów Money.
	@Test
	public void testGetAmount() {
		assertEquals( 1000, (int)EUR10.getAmount());
		assertEquals( 20000, (int)SEK200.getAmount());
		assertEquals( 2000, (int)EUR20.getAmount());
		assertEquals( 0, (int)EUR0.getAmount());
		assertEquals( -10000, (int)SEKn100.getAmount());
		assertEquals( 10000, (int)DKK100.getAmount());
		assertEquals(10000, (int)SEK100.getAmount());
		assertEquals( 0, (int)SEK0.getAmount());
	}

	//Sprawdza, czy metoda getCurrency zwraca poprawne waluty dla różnych obiektów Money.
	@Test
	public void testGetCurrency() {
		assertEquals( EUR, EUR10.getCurrency());
		assertEquals( DKK, DKK100.getCurrency());
		assertEquals( SEK, SEK100.getCurrency());
	}

	//Testuje poprawność działania metody toString
	@Test
	public void testToString() {
		SEK100 = SEK100.add(new Money(50,SEK));
		assertEquals( "10.0 EUR", EUR10.toString());
		assertEquals( "100.5 SEK", SEK100.toString());
		assertEquals( "100.0 DKK", DKK100.toString());
		assertEquals( "-100.0 SEK", SEKn100.toString());
	}

	//Sprawdza poprawność obliczania wartości globalnej dla różnych obiektów Money.
	@Test
	public void testGlobalValue() {
		assertEquals( (int)(20000*SEK.getRate()), (int)SEK200.universalValue());
		assertEquals( (int)(2000*EUR.getRate()), (int)EUR20.universalValue());
		assertEquals( (int)(10000*SEK.getRate()), (int)SEK100.universalValue());
		assertEquals( (int)(1000*EUR.getRate()), (int)EUR10.universalValue());
		assertEquals( 0, (int)EUR0.universalValue());
		assertEquals( 0, (int)SEK0.universalValue());
		assertEquals( (int)(10000*DKK.getRate()), (int)DKK100.universalValue());
		assertEquals( (int)(-10000*SEK.getRate()), (int)SEKn100.universalValue());
	}

	//Testuje poprawność działania metody equals dla obiektów Money
	@Test
	public void testEqualsMoney() {
		assertEquals( false, SEK100.equals(new Money(1_000,SEK)));
		assertEquals( true, SEK100.equals(new Money(10_000,SEK)));
		assertEquals( false, DKK100.equals(EUR20));
		assertEquals( true, SEK100.equals(EUR10));

	}

	//Testuje poprawność operacji dodawania dwóch obiektów Money
	@Test
	public void testAdd() {
		assertEquals( 2000, (int)EUR10.add(SEK100).getAmount());
		assertEquals( 17500, (int)DKK100.add(SEK100).getAmount());
		assertEquals( 20000, (int)SEK100.add(EUR10).getAmount());
		assertEquals( 0, (int)EUR0.add(SEK0).getAmount());
	}

	//Testuje poprawność operacji odejmowania dwóch obiektów Money
	@Test
	public void testSub() {
		assertEquals( 0, (int)EUR0.sub(SEK0).getAmount());
		assertEquals( 2000, (int)EUR10.sub(SEKn100).getAmount());
		assertEquals( 0, (int)SEK100.sub(EUR10).getAmount());
		assertEquals( 0, (int)EUR10.sub(SEK100).getAmount());
		assertEquals( 2500, (int)DKK100.sub(SEK100).getAmount());
	}

	//Sprawdza, czy metoda isZero zwraca poprawne wartości dla różnych obiektów Money
	@Test
	public void testIsZero() {
		assertEquals( true, EUR0.isZero());
		assertEquals( false, EUR10.isZero());
		assertEquals( true, SEK0.isZero());
	}

	//Testuje poprawność działania metody negate, która zmienia znak kwoty obiektu Money
	@Test
	public void testNegate() {
		assertEquals( -1000, (int)EUR10.negate().getAmount());
		assertEquals( 0, (int)SEK0.negate().getAmount());
		assertEquals( 10_000, (int)SEKn100.negate().getAmount());
	}

	//Sprawdza, czy metoda compareTo porównuje obiekty Money zgodnie z oczekiwaniami
	@Test
	public void testCompareTo() {
		assertEquals( true, EUR10.compareTo(SEK100) == 0);
		assertEquals( true, EUR20.compareTo(SEK100) > 0);
		assertEquals( true, EUR10.compareTo(EUR20) < 0);
		assertEquals( true, EUR0.compareTo(SEK0) == 0);
		assertEquals( true, EUR20.compareTo(SEKn100) > 0);
	}
}
