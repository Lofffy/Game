package exceptions;

abstract public class GameActionException extends Exception {

	public GameActionException() {
		
	}

	public GameActionException(String message) {
		super(message);
	}

}
