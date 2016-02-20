package messenger.repository.data;

import java.util.Collection;

public interface CRUDRepository<E, ID> extends Repository<E, ID> {
	//I/O in fisiere XML
	int count(String receiver);
    E find(ID id);
    Collection<E> getAll();
    void save(E e);
    boolean update(E e);
    boolean delete(ID id);
}
