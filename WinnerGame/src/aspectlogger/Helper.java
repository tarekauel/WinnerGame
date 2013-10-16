package aspectlogger;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import annotation.DontLog;

@DontLog
public abstract class Helper {
	
	private Helper() {}
	
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
