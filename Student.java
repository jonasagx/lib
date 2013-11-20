import java.net.InetAddress;
import java.util.ArrayList;

public class Student {

	private String name;
	private ArrayList<String> books;
	private InetAddress dep;
	private int id;

	public Student(String name, InetAddress dep){
	    setName(name);
	    setDep(dep);
	    this.id = this.hashCode();
	}
	
	public Student(String name){
		this.name = name;
	}
	
	public Student(int id){
		this.id = id;
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
