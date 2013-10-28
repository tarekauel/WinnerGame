package aspecttest;

import static org.junit.Assert.assertEquals;

import java.util.Random;

import org.junit.Test;

import annotation.FakeRandom;

public class RandomTest {

	Random r = new Random();
	
	@Test
	@FakeRandom( mathRandomNewRandom =  { 1.0, 2.0 }, mathRandomMethodName = { "aspecttest.RandomTest.a", "aspecttest.RandomTest.b" } )
	public void meinTestA() {
		assertEquals((int) 1.0, (int) a() );
		assertEquals((int) 2.0, (int) b() );
	}
	
	public double a() {
		return Math.random();
	}
	
	public double b() {
		return Math.random();
	}
	
	@Test
	@FakeRandom( mathRandomNewRandom = { 7.0, 5.0 }, mathRandomMethodName = { "aspecttest.RandomTest.b", "aspecttest.RandomTest.a" } )
	public void meinTestB() {
		assertEquals((int) 5.0, (int) a() );
		assertEquals((int) 7.0, (int) b() );
	}
	
	@FakeRandom( mathRandomNewRandom = { 7.0, 10.0 }, mathRandomMethodName = { "aspecttest.RandomTest.a", "aspecttest.RandomTest.meinTestC" },
	randomNextIntNewRandom = { 1 }, randomNextIntMethodName = { "aspecttest.RandomTest.meinTestC" } )
	@Test
	public void meinTestC() {
		assertEquals((int) 7.0, (int) c() );
		assertEquals(0.5, b(), 0.5);
		assertEquals(1, r.nextInt());
		assertEquals(1, r.nextInt(100));
		assertEquals(10, (int) Math.random());
	}
	
	@Test
	@FakeRandom( randomNextGaussianNewRandom = { 1.0 }, randomNextGaussianMethodName = { "aspecttest.RandomTest.meinTestD" } )
	public void meinTestD() {
		assertEquals(1, (int) r.nextGaussian());
	}
	
	@Test	
	public void meinTestE() {
		assertEquals(0.5, d(), 0.5);
	}
		
	public double c() {
		return a();
	}
	
	@FakeRandom( mathRandomNewRandom = { 2.0 }, mathRandomMethodName = { "aspecttest.RandomTest.a" } )
	public double d() {
		// Fake Random wirkt in diesem Fall nicht, da es nicht über einer @Test-Methode steht
		return a();
	}

}


