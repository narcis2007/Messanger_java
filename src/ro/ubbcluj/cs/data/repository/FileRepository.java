package ro.ubbcluj.cs.data.repository;

public interface FileRepository<E, ID> extends CRUDRepository<E, ID> {
	
	void updateFile();
	void loadMemory();
	void setFile(String filename);
	
}
