package ro.ubbcluj.cs.data.core;

public class ServiceException extends RuntimeException  {


    public ServiceException(Exception e) {
        super(e);
    }

	public ServiceException(String message) {
		super(message);
	}

}
