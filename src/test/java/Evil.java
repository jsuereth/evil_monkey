
public class Evil {
	
	private final int foo = 1;
	private final static int bar = 2;

	public Evil() {

	}

	protected void dumb(String input) {
		System.out.println("DUMB!!! " + input);
	}

	private String dumb2(String input) {
		return input + "more";
	}
}