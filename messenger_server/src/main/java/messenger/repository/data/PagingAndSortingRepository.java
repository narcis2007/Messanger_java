package messenger.repository.data;

import java.util.Collection;
import java.util.Comparator;

import messenger.models.Page;

public interface PagingAndSortingRepository<E,ID> extends CRUDRepository<E, ID>  {
	Page<E> getPage(int nr);
	int getPageCount();
	void setComarator(Comparator<E> comparator);
	Collection<Page<E>> generatePages();
	
}
