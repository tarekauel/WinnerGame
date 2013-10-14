package Aspect;

import static org.junit.Assert.assertEquals;

import java.util.Random;

import org.junit.Test;

import AspectLogger.FakeRandom;

public class RandomTest {

	Random r = new Random();
	
	@Test
	@FakeRandom( mathRandomNewRandom =  { 1.0, 2.0 }, mathRandomMethodName = { "Aspect.RandomTest.a", "Aspect.RandomTest.b" } )
	public void meinTestA() {
		assertEquals((int) 1.0, (int) a() );
		assertEquals((int) 2.0, (int) b() );
	}
	
	@Test
	@FakeRandom( mathRandomNewRandom = { 7.0, 5.0 }, mathRandomMethodName = { "Aspect.RandomTest.b", "Aspect.RandomTest.a" } )
	public void meinTestB() {
		assertEquals((int) 5.0, (int) a() );
		assertEquals((int) 7.0, (int) b() );
	}
	
	@Test
	@FakeRandom( mathRandomNewRandom = { 7.0, 10.0 }, mathRandomMethodName = { "Aspect.RandomTest.a", "Aspect.RandomTest.meinTestC" }, 
	randomNextIntNewRandom = { 1 }, randomNextIntMethodName = { "Aspect.RandomTest.meinTestC" } )
	public void meinTestC() {
		assertEquals((int) 7.0, (int) c() );
		assertEquals(0.5, b(), 0.5);
		assertEquals(1, r.nextInt());
		assertEquals(1, r.nextInt(100));
		assertEquals(10, (int) Math.random());
	}
	
	@Test
	@FakeRandom( randomNextGaussianNewRandom = { 1.0 }, randomNextGaussianMethodName = { "Aspect.RandomTest.meinTestD" } )
	public void meinTestD() {
		assertEquals(1, (int) r.nextGaussian());
	}
	
	public double a() {
		return Math.random();
	}
	
	public double b() {
		return Math.random();
	}
	
	public double c() {
		return a();
	}

}


