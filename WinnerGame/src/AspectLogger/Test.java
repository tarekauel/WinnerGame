package AspectLogger;

import java.io.PrintStream;

public class Test {
	 static PrintStream newOut = null;
	static {
		try {
			newOut = new PrintStream("log.txt");
		} catch (Exception e) {

		}
	}

	@LogThis
	private int x;

	public static void main(String[] args) {
		System.setOut(newOut);
		Test t = new Test();
		t.doIt();
		t.getThis(5);
	}

	public Test() {

	}

	public void doIt() {
		this.x = 10;
	}

	@LogThis
	public void getThis(int x) {
		this.x = x;
	}

}
