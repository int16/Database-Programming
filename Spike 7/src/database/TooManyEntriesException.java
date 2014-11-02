package database;

public class TooManyEntriesException extends Exception {
	private static final long serialVersionUID = 1L;

	public TooManyEntriesException(String msg) {
		super(msg);
	}
}
