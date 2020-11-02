import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class CheckingTest {
	private Checking checking;
	
	@Before
	public void setUp() throws Exception {
		checking = new Checking(1000, 360.24);
	}
	
	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testCheckingAccountType() {
		assertEquals(checking.getAccountType(), AccountType.CHECKING);
	}


	@Test
	public void deposit1() {
		checking.deposit(100);
		assertEquals(460.24, checking.getBalance(), 0.001);
	}
	
	@Test
	public void deposit2() {
		checking.deposit(10.50);
		assertEquals(370.74, checking.getBalance(), 0.001);
	}
	
	@Test
	public void deposit3() {
		try{
			checking.deposit(-50);
		}catch (RuntimeException e){
			assertEquals(360.24, checking.getBalance(), 0.001);
		}
	}
	
	@Test
	public void withdraw1() {
		checking.withdraw(60);
		assertEquals(300.24, checking.getBalance(), 0.001);
	}
	
	@Test
	public void withdraw2() {
		try{
			checking.withdraw(-100);
		}catch (RuntimeException e){
			assertEquals(360.24, checking.getBalance(), 0.001);
		}
	}
	
	@Test
	public void withdraw3() {
		try{
			checking.withdraw(500);
		}catch (RuntimeException e){
			assertEquals(360.24, checking.getBalance(), 0.001);
		}
		
	}
}