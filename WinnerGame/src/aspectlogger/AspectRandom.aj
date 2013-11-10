package aspectlogger;

import annotation.DontLog;
import annotation.FakeRandom;
import annotation.FakeSupplierMarketOfferQualities;

@DontLog
public aspect AspectRandom {

	pointcut manipulateRandom() : 
		call( public static double java.lang.Math.random() );

	pointcut manipulateNextGaussian() : 
		call( public double java.util.Random.nextGaussian());

	pointcut manipulateNextInt() : 
		call( public int java.util.Random.nextInt(..) );
	
	pointcut fakeOffersFromSupplierMarket() :
		call (private int[] getOfferQualities( int ));

	
	int[] around() : fakeOffersFromSupplierMarket() {
		FakeSupplierMarketOfferQualities fakeMarket = Helper.findAnnotation( FakeSupplierMarketOfferQualities.class);
		if( fakeMarket != null ) {
			int[] diffs = fakeMarket.differences();
			int input = (int) thisJoinPoint.getArgs()[0];
			int[] neu = { input + diffs[0], input + diffs[1], input + diffs[2] };
			for( int i=0; i<neu.length;i++) {
				neu[i] = (neu[i] < 1) ? 1 : neu[i];
				neu[i] = (neu[i] > 100) ? 100 : neu[i];
			}
			return neu; 
		}
		return proceed();
	}
	
	double around() : manipulateRandom() {
		FakeRandom fakeRandom = Helper.findAnnotation( FakeRandom.class);
		if (fakeRandom != null) {
			try {
				return getReturnValue( fakeRandom.mathRandomMethodName(), fakeRandom.mathRandomNewRandom());
			} catch( IllegalArgumentException e) {}
		}
		return proceed();
	}

	int around() : manipulateNextInt() {
		FakeRandom fakeRandom = Helper.findAnnotation( FakeRandom.class);
		if (fakeRandom != null) {
			try {
				return getReturnValue( fakeRandom.randomNextIntMethodName(), fakeRandom.randomNextIntNewRandom());
			} catch( IllegalArgumentException e) {}
		}
		return proceed();
	}

	double around() : manipulateNextGaussian() {
		FakeRandom fakeRandom = Helper.findAnnotation( FakeRandom.class);
		if (fakeRandom != null) {
			try {
				return getReturnValue( fakeRandom.randomNextGaussianMethodName(), fakeRandom.randomNextGaussianNewRandom());
			} catch( IllegalArgumentException e) {}
		}
		return proceed();
	}
	
	public static double getReturnValue( String[] methodNames, double[] newValues) {
		for (int l = 0; l < methodNames.length; l++) {
			String callerMethodName = Thread.currentThread().getStackTrace()[3].getClassName() + "."
					+ Thread.currentThread().getStackTrace()[3].getMethodName();
			System.out.println(callerMethodName);
			if (callerMethodName.equals(methodNames[l])) {
				return newValues[l];
			}
		}		
		throw new IllegalArgumentException( "caller method name not found");
	}
	
	public static int getReturnValue( String[] methodNames, int[] newValues) {
		for (int l = 0; l < methodNames.length; l++) {
			String callerMethodName = Thread.currentThread().getStackTrace()[3].getClassName() + "."
					+ Thread.currentThread().getStackTrace()[3].getMethodName();
			System.out.println(callerMethodName);
			if (callerMethodName.equals(methodNames[l])) {
				return newValues[l];
			}
		}		
		throw new IllegalArgumentException( "caller method name not found");
	}
	
	
}
