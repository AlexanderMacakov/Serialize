public class CircularReferenceException extends Exception {

    CircularReferenceException() {
        super("circular reference!!!");
    }
}
