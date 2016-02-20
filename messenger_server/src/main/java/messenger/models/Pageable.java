package messenger.models;

import java.util.Collection;

public interface Pageable<E> {
	int getPageSize();
	Collection<E> getElements();
	boolean addElement(E e);
}
