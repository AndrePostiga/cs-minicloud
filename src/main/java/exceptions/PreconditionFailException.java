package exceptions;

public class PreconditionFailException extends Exception{
    private final static long serialVersionUID = 1;
    public PreconditionFailException(String msg) {
        super(msg);
    }
}
