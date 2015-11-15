package ro.ubbcluj.cs.data.repository;

import java.util.Collection;
import java.util.Comparator;

public interface PagingAndSortingRepository<E,ID> extends CRUDRepository<E, ID>  {
	Page<E> getPage(int nr);
	int getPageCount();
	void setComarator(Comparator<E> comparator);
	Collection<Page<E>> generatePages();
	
}
