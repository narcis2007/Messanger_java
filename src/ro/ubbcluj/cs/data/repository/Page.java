package ro.ubbcluj.cs.data.repository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Page<E> implements Pageable<E> {
	private static int maxPageSize=5;
	List<E> elements;
	
	public Page(){
		elements=new ArrayList<E>();
	}
	
	@Override
	public int getPageSize() {
		return elements.size();
	}

	@Override
	public Collection<E> getElements() {
		return elements;
	}
	
	@Override
	public boolean addElement(E e) {
		if(elements.size()<maxPageSize){
			elements.add(e);
			return true;
		}
		return false;
	}

	public static int getMaxPageSize() {
		return maxPageSize;
	}
}
