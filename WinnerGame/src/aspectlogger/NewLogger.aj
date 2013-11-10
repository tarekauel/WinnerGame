package aspectlogger;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

import annotation.DontLog;
import annotation.Log;

@DontLog
public aspect NewLogger {
	
	private PrintWriter out;

	public NewLogger() throws FileNotFoundException {
		out = new PrintWriter( System.out );
	}
	
	
	private int level = 1;
	
	pointcut logMethod() : 
		((execution( * *.* (..)) && !execution(@DontLog * *.* (..)) && ( !within(@DontLog *) || execution(@Log * *.* (..)) )) || 
		(execution ( *.new(..) ) && !execution(@DontLog *.new(..)) && ( !within(@DontLog *) || execution(@Log *.new(..) )))) && 
		!execution( public static void *.main(String[]));
	
	pointcut logVar() :
		set(@Log * *.* );
	
	pointcut  exit() :
		execution( public static void *.main(String[]));
	
	
	before() : logMethod() {
		for(int i=0; i<level; i++) {
			out.print(" ");
		}
		out.print("Entry: " + thisJoinPoint.getSignature().toLongString() + " Args: ");
		for( Object o : thisJoinPoint.getArgs()) {
			if( o != null)
				out.print( " " + o.toString());
		}
		out.println();
		++level;
	}
	
	after() : logMethod() {
		--level;
		for(int i=0; i<level; i++) {
			out.print(" ");
		}
		out.println("Leaving: " + thisJoinPoint.getSignature().toLongString() );		
	}
	
	before() : logVar() {
		for(int i=0; i<level; i++) {
			out.print(" ");
		}
		out.println("Variable: " + thisJoinPoint.getSignature().toLongString() + " Value " + thisJoinPoint.getArgs()[0]);
	}
	
	before() : exit() {
		out.println("Starting Main: " + thisJoinPoint.getSignature().toShortString() );	
	}
	
	after() : exit() {
		out.println("Leaving Main: " + thisJoinPoint.getSignature().toShortString() );	
		out.close();
	}
	
	
	
}
