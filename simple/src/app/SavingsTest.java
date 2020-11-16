package app;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
/**
	* @author Alex Avila
	* @since 10/10/20
	* @version 1
	*/
public class SavingsTest {
	private Savings savings;
	
	@Before
	public void setUp() throws Exception {
		savings = new Savings(2000, 430.64);
	}
	
	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testAccountType() {
		assertEquals(savings.getAccountType(), AccountType.SAVINGS);
	}

	@Test
	public void deposit1() {
		savings.deposit(20);
		assertEquals(450.64, savings.getBalance(), 0.001);
	}
	
	@Test
	public void deposit2() {
		savings.deposit(20.50);
		assertEquals(451.14, savings.getBalance(), 0.001);
	}
	
	@Test
	public void deposit3() {
		try{
			savings.deposit(-50);
		}catch (RuntimeException e){
			assertEquals(430.64, savings.getBalance(), 0.001);
		}
		
	}
	
	@Test
	public void withdraw1() {
		savings.withdraw(100);
		assertEquals(330.64, savings.getBalance(), 0.001);
	}
	
	@Test
	public void withdraw2() {
		try{
			savings.withdraw(-100);
		}catch (RuntimeException e){
			assertEquals(430.64, savings.getBalance(), 0.001);
		}
	}
	
	@Test
	public void withdraw3() {
		try{
			savings.withdraw(500);
		}catch (RuntimeException e){
			assertEquals(430.64, savings.getBalance(), 0.001);
		}
		
	}
}