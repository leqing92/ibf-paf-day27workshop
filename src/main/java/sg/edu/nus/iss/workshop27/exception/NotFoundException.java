package sg.edu.nus.iss.workshop27.exception;

public class NotFoundException extends RuntimeException {
    public NotFoundException() {
        super();
    }

    public NotFoundException(String msg){
        super(msg);
    }
}
