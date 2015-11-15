package ro.ubbcluj.cs.data.domain;

public interface Validator<E> {
	Errors validate(E element);
}
