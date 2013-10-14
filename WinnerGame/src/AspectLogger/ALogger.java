package AspectLogger;

public aspect ALogger {
/*
  	pointcut logMethodWithoutGet() : 
 
	      ( execution ( * *.* (..)) && ( !execution( * *.get* (..)) || !execution(@Noget * *.get* (..) )) || initialization( *.new(..))) && !within(ALogger);

	pointcut logVar() :
		set(@LogThis * *.*);

	pointcut logSpecificMethod():
		execution(@LogThis * *.* (..)) || ( initialization(@LogThis *.new(..)) && !within(ALogger));
	
	before(): logMethodWithoutGet() {
		StringBuffer output = new StringBuffer();
		String methodeString = thisJoinPoint.getSignature().toString();
		output.append("Methode: " + methodeString);
		if (thisJoinPoint.getArgs().length > 0) {
			output.append("Param: ");
			for (int i = 0; i < thisJoinPoint.getArgs().length; i++) {
				output.append(thisJoinPoint.getArgs()[i]);
				if (i < thisJoinPoint.getArgs().length - 1) {
					output.append(", ");
				}
			}
		}
		System.out.println(output.toString());
	}

	before() : logVar() {
		System.err.print("Variable: " + thisJoinPoint.getSignature());
		System.err.print(" Neuer Wert: ");
		for (Object o : thisJoinPoint.getArgs()) {
			System.err.print(o + ", ");
		}
		System.err.println("");
	}

	before() : logSpecificMethod() {
		StringBuffer output = new StringBuffer();
		System.err.print("Methode: " + thisJoinPoint.getSignature());
		if (thisJoinPoint.getArgs().length > 0) {
			System.err.print("Param: ");
			for (int i = 0; i < thisJoinPoint.getArgs().length; i++) {
				System.err.print(thisJoinPoint.getArgs()[i]+"");
				if (i < thisJoinPoint.getArgs().length - 1) {
					System.err.print(", ");
				}
			}
		}
		System.err.println(output.toString());
	}
*/
}