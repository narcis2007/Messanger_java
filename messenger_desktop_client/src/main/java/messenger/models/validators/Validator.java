package messenger.models.validators;

public interface Validator<E> {
	Errors validate(E element);
}
