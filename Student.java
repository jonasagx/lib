import java.net.InetAddress;
import java.util.ArrayList;

public class Student {

	private String name;
	private ArrayList<String> books;
	private InetAddress dep;
	private int id;
	public boolean request = false;

	public Student(String name, InetAddress dep){
	    setName(name);
	    setDep(dep);
	    this.id = this.hashCode();
	}
	
	public Student(int id){
		this.id = id;
		this.request = true;
	}
	
	public boolean canBorrow(){
		return (this.books.size() <= 3) ? true : false;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public ArrayList<String> getBooks() {
		return books;
	}
	
	public void setBooks(ArrayList<String> books) {
		this.books = books;
	}
	
	public void addBook(String book){
		this.books.add(book);
	}
	
	public boolean removeBook(String book){
		if(this.books.remove(book)){
			System.out.println( book + " removido da lista de " + this.name);
		}
	}
	
	public InetAddress getDep() {
		return dep;
	}

	public void setDep(InetAddress dep) {
		this.dep = dep;
	}
	public int getId(){
		return this.id;
	}
}
