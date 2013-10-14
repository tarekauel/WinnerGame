package AspectLogger;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

public aspect AspectRandom {
/*	String	fakeRandomName	= FakeRandom.class.getCanonicalName();

	pointcut manipulateRandom() : 
		call( public static double java.lang.Math.random() );

	pointcut manipulateNextGaussian() : 
		call( public double java.util.Random.nextGaussian());

	pointcut manipulateNextInt() : 
		call( public int java.util.Random.nextInt(..) );

	double around() : manipulateRandom() {
		FakeRandom fakeRandom = findFakeRandom();
		if (fakeRandom != null) {
			String[] methodNames = fakeRandom.mathRandomMethodName();
			for (int l = 0; l < methodNames.length; l++) {
				String callerMethodName = Thread.currentThread().getStackTrace()[2].getClassName() + "."
						+ Thread.currentThread().getStackTrace()[2].getMethodName();
				System.out.println(callerMethodName);
				if (callerMethodName.equals(methodNames[l])) {
					return fakeRandom.mathRandomNewRandom()[l];
				}
			}
		}

		return proceed();
	}

	int around() : manipulateNextInt() {
		FakeRandom fakeRandom = findFakeRandom();
		if (fakeRandom != null) {
			String[] methodNames = fakeRandom.randomNextIntMethodName();
			for (int l = 0; l < methodNames.length; l++) {
				String callerMethodName = Thread.currentThread().getStackTrace()[2].getClassName() + "."
						+ Thread.currentThread().getStackTrace()[2].getMethodName();
				System.out.println(callerMethodName);
				if (callerMethodName.equals(methodNames[l])) {
					return fakeRandom.randomNextIntNewRandom()[l];
				}
			}
		}

		return proceed();
	}

	double around() : manipulateNextGaussian() {

		FakeRandom fakeRandom = findFakeRandom();
		if (fakeRandom != null) {
			String[] methodNames = fakeRandom.randomNextGaussianMethodName();
			for (int l = 0; l < methodNames.length; l++) {
				String callerMethodName = Thread.currentThread().getStackTrace()[2].getClassName() + "."
						+ Thread.currentThread().getStackTrace()[2].getMethodName();
				System.out.println(callerMethodName);
				if (callerMethodName.equals(methodNames[l])) {
					return fakeRandom.randomNextGaussianNewRandom()[l];
				}
			}
		}

		return proceed();
	}

	private FakeRandom findFakeRandom() {
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
						for (int k = 0; k < annArray.length; k++) {
							Annotation a = annArray[k];
							Class<?> ac = a.annotationType();
							if (ac.getName().equals(fakeRandomName)) {
								return m.getAnnotation(FakeRandom.class);
							}
						}
					}
				}
			} catch (ClassNotFoundException e) {

			}
		}
		return null;
	}*/
}
