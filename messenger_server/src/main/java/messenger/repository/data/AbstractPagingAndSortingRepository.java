package messenger.repository.data;

import messenger.models.Page;
import messenger.models.validators.Validator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


public abstract class AbstractPagingAndSortingRepository<E, ID> extends AbstractXMLRepository<E,ID> implements PagingAndSortingRepository<E, ID> {

	private List<Page<E>> pages;
	Comparator<E> comparator;

	protected AbstractPagingAndSortingRepository(Validator<E> validator,Comparator<E> comparator,String filename) {
		super(validator,filename);
		this.comparator=comparator;
		pages=(List<Page<E>>) generatePages();
	}



	public AbstractPagingAndSortingRepository(Comparator<E> comparator, String filename) {
		super(filename);
		synchronized (monitor){
			try {
				monitor.wait();
			} catch (InterruptedException e) {
				;
			}
		}
		this.comparator=comparator;
		pages=(List<Page<E>>) generatePages();
	}



	@Override
	public Page<E> getPage(int nr) {
		if(nr<0||nr>=getPageCount())
			return null;
		return pages.get(nr);
	}

	@Override
	public int getPageCount() {
		return pages.size();
	}

	@Override
	public void setComarator(Comparator<E> comparator) {
		this.comparator=comparator;
		
	}

	@Override
	public Collection<Page<E>> generatePages() {
		int maxSize=Page.getMaxPageSize(),count=0;
		List<Page<E>> pages=new ArrayList<Page<E>>();
		elements=elements.parallelStream().sorted(comparator).collect(Collectors.toList());
		for(E e:elements){			//vad daca pot folosi stream
			if(count%maxSize==0){
				Page<E> page=new Page<E>();
				pages.add(page);
			}
			pages.get(count/maxSize).addElement(e);
			count++;
		}
		return pages;
	}

	@Override
	public void save(E e) {
		super.save(e);
		pages=(List<Page<E>>) generatePages();
	}

	@Override
	public boolean delete(ID id) {
		boolean ok= super.delete(id);
		if(ok)
			pages=(List<Page<E>>) generatePages();
		return ok;
	}

	@Override
	public boolean update(E e) {
		boolean ok= super.update(e);
		if(ok)
			pages=(List<Page<E>>) generatePages();
		return ok;
	}
}
