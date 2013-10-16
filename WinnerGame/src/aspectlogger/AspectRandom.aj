package aspectlogger;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

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
		FakeSupplierMarketOfferQualities fakeMarket = findAnnotation( FakeSupplierMarketOfferQualities.class);
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
		FakeRandom fakeRandom = findAnnotation( FakeRandom.class);
		if (fakeRandom != null) {
			try {
				return getReturnValue( fakeRandom.mathRandomMethodName(), fakeRandom.mathRandomNewRandom());
			} catch( IllegalArgumentException e) {}
		}
		return proceed();
	}

	int around() : manipulateNextInt() {
		FakeRandom fakeRandom = findAnnotation( FakeRandom.class);
		if (fakeRandom != null) {
			try {
				return getReturnValue( fakeRandom.randomNextIntMethodName(), fakeRandom.randomNextIntNewRandom());
			} catch( IllegalArgumentException e) {}
		}
		return proceed();
	}

	double around() : manipulateNextGaussian() {
		FakeRandom fakeRandom = findAnnotation( FakeRandom.class);
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
	
	@SuppressWarnings("unchecked")
	public static <T> T findAnnotation (Class<? extends Annotation> searched) {
		StackTraceElement[] stack = Thread.currentThread().getStackTrace();
		for (int i = 0; i < stack.length; i++) {
			String classname = stack[i].getClassName();
			String methodName = stack[i].getMethodName();
			try {
				Class<?> c = Class.forName(classname);
				Method[] methodArray = c.getMethods();
				for (int j = 0; j < methodArray.length; j++) {
					Method m = methodArray[j];
					if (m.getName().equals(methodName)) {
						Annotation[] annArray = m.getAnnotations();
						T annotation = null;
						boolean foundTest = false;
						for (int k = 0; k < annArray.length; k++) {
							Annotation a = annArray[k];
							Class<?> ac = a.annotationType();
							if (ac.getName().equals(searched.getCanonicalName())) {
								if( foundTest) {
									return (T) m.getAnnotation(searched);
								} else {									
									annotation = (T) m.getAnnotation(searched);
								}
							} else if ( ac.getName().equals(org.junit.Test.class.getCanonicalName())) {
								foundTest = true;
								if( annotation != null )
									return annotation;
							}
						}
					}
				}
			} catch (ClassNotFoundException e) {

			}
		}
		return null;
	}	
}
