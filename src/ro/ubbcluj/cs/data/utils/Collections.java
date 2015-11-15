package ro.ubbcluj.cs.data.utils;

import java.util.ArrayList;
import java.util.List;

public class Collections {
	public static <E> List<E> makeList(Iterable<E> iter) {
	    List<E> list = new ArrayList<E>();
	    for (E item : iter) {
	        list.add(item);
	    }
	    return list;
	}
}
