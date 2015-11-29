package messanger_server.repository.data;

import java.util.Collection;

public interface CRUDRepository<E, ID> extends Repository<E, ID> {
	//I/O in fisiere XML
	int count();
    E find(ID id);
    Collection<E> getAll();
    void save(E e);
    boolean update(E e);
    boolean delete(ID id);
}
