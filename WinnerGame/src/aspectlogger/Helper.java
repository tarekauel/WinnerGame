package aspectlogger;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import annotation.DontLog;

@DontLog
public abstract class Helper {
	
	private Helper() {}
	
	@SuppressWarnings("unchecked")
	/**
	 * Sucht die übergebene Annotation im Stacktrace bei einer Methode, die auch die Annotation Test hat.
	 * @param searched
	 * @return 
	 */
	public static <T> T findAnnotation (Class<? extends Annotation> searched) {
		// Stacktrace der aktuellen Methode einlesen
		StackTraceElement[] stack = Thread.currentThread().getStackTrace();
		// Jedes Element auf dem Stacktrace durchgehen
		for (int i = 0; i < stack.length; i++) { 
			String classname = stack[i].getClassName();
			String methodName = stack[i].getMethodName();
			try {
				// Versucht die Klasse zu laden
				Class<?> c = Class.forName(classname);
				Method[] methodArray = c.getMethods();
				// Ueber alle Methoden der Klasse gehen, um die Methode methodName (vom Stacktrace) zu finden.
				for (int j = 0; j < methodArray.length; j++) {
					Method m = methodArray[j];
					// Checkt, ob es sich um die richtige Methode handellt
					if (m.getName().equals(methodName)) {
						// laedt alle Annotations der Methode
						Annotation[] annArray = m.getAnnotations();
						// Referenz auf die gesuchte Annotation
						T annotation = null;
						// Gibt an, ob die Annotation Test bereits gefunden wurde
						boolean foundTest = false;
						// Durchlauft alle Annotation, um Test und die gesuchte zu finden
						for (int k = 0; k < annArray.length; k++) {
							Annotation a = annArray[k];
							Class<?> ac = a.annotationType();
							if (ac.getName().equals(searched.getCanonicalName())) {
								if( foundTest) {
									// Wenn die Test Annotation bereits gefunden wurde und die gesuchte auch wird diese zurueckgegeben
									return (T) m.getAnnotation(searched);
								} else {			
									// Die gesuchte Annotation wurde gefunden, aber die Test fehlt noch
									annotation = (T) m.getAnnotation(searched);
								}
							} else if ( ac.getName().equals(org.junit.Test.class.getCanonicalName())) {
								// Die Test Annotation wurde gefunden
								foundTest = true;
								// Wenn die gesuchte Annotation bereits gefunden wurde, wird diese zurueckgegeben
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
