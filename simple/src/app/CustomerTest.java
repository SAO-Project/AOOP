package app;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class CustomerTest {
	private Customer customer1;
	private Customer customer2;
	
	@Before
	public void setUp() throws Exception {
		customer1 = new Customer(
			"Mickey",
			"Mouse",
			"November",
			"1313 Disneyland",
			"(714) 781-4636",
			000-00-0001,
			new Checking(
				1000,
				960.94
			),
			new Savings(
				2000,
				3845.93
			),
			new Credit(
				3000,
				-1549.33
			));
		
		customer2 = new Customer(
			"Donald",
			"Duck",
			"September",
			"1313 Disneyland",
			"(714) 781-4636",
			000000002,
			new Checking(
				1001,
				1688.89
			),
			new Savings(
				2001,
				1731.09
			),
			new Credit(
				3001,
				-986.23
			));
	}
	
	@After
	public void tearDown() throws Exception {
	}
	
	@Test
	public void transfer1() {
		customer1.transfer(customer1.getSavings(), customer1.getChecking(), 500);
		
		assertEquals(
			"savings should be 3345.93",
			3345.93,
			customer1.getSavings().getBalance(),
			0.001);
		
		assertEquals(
			"checkings should be 1460.94",
			1460.94,
			customer1.getChecking().getBalance(),
			0.001);
		
		customer1.transfer(customer1.getChecking(), customer1.getCredit(), 1000);
		
		assertEquals(
			"checkings should be 460.94",
			460.94,
			customer1.getChecking().getBalance(),
			0.001);
		
		assertEquals(
			"checkings should be -549.33",
			-549.33,
			customer1.getCredit().getBalance(),
			0.001);
	}
	
	@Test
	public void transfer2() {
		customer2.transfer(customer2.getSavings(), customer2.getCredit(), 900.89);
		
		assertEquals(
			830.2,
			customer2.getSavings().getBalance(),
			0.001);
		
		assertEquals(
			-85.34,
			customer2.getCredit().getBalance(),
			0.001);
		
		try{
			customer2.transfer(customer2.getSavings(), customer2.getCredit(), 100);
		}catch (RuntimeException e){
			assertEquals(
				830.2,
				customer2.getSavings().getBalance(),
				0.001);
		}
		
		assertEquals(
			-85.34,
			customer2.getCredit().getBalance(),
			0.001);
	}
	
	@Test
	public void transfer3() {
		customer2.transfer(customer1.getSavings(), customer1.getChecking(), 3845.90);
		
		assertEquals(
			0.03,
			customer1.getSavings().getBalance(),
			0.001);
		
		assertEquals(
			4806.84,
			customer1.getChecking().getBalance(),
			0.001);
		
		try{
			customer2.transfer(customer1.getSavings(), customer1.getChecking(), 10);
		}catch (RuntimeException e){
			assertEquals(
				0.03,
				customer1.getSavings().getBalance(),
				0.001);
		}
		
		assertEquals(
			4806.84,
			customer1.getChecking().getBalance(),
			0.001);
	}
	
	@Test
	public void transfer4() {
		customer1.getChecking().deposit(1000);
		assertEquals(1960.94, customer1.getChecking().getBalance(), 0.001);
	}
	
	@Test
	public void paySomeone1(){
		customer1.paySomeone(customer2, 100);
		assertEquals(860.94, customer1.getChecking().getBalance(), 0.001);
		assertEquals(1788.89, customer2.getChecking().getBalance(), 0.001);
	}
	
	@Test
	public void paySomeone2(){
		customer2.paySomeone(customer1, 1600.50);
		assertEquals(2561.44, customer1.getChecking().getBalance(), 0.001);
		assertEquals(88.39, customer2.getChecking().getBalance(), 0.001);
	}
	
	@Test
	public void paySomeone3(){
		try{
			customer1.paySomeone(customer2, 1000.50);
		}catch (RuntimeException e){
			assertEquals(960.94, customer1.getChecking().getBalance(), 0.001);
			assertEquals(1688.89, customer2.getChecking().getBalance(), 0.001);
		}
	}
}