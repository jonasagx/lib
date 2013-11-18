import java.util.HashMap;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

/*
 * Para a central cada departamento é apenas um IP (InetAddress)
 */

public class Central {
	private HashMap<Integer, InetAddress> students = null;
	private ServerSocket deps = null;
	
	public Central(){
		this.students = new HashMap<Integer, InetAddress>();
		try {
			this.deps = new ServerSocket(8080);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void listen(){
		try {
			Socket dep = this.deps.accept();
			Processor p = new Processor(dep, this);
			p.start();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public InetAddress whoIs(int student){
		return this.students.get(students);
	}
	
	public void update(Student student){
		this.students.put(student.getId(), student.getDep());
	}
	
	public void insert(Student student){
		update(student);
	}
	
	public void insert(Student [] students){
		for(Student s : students){
			this.students.put(s.getId(), s.getDep());
		}
		System.out.println("Students addeds");
	}
	
	public void remove(Student student){
		this.students.remove(student.getId());
	}
}

class Processor extends Thread{
	private Socket s;
	private Central c;
	public Processor(Socket s, Central c){
		this.s = s;
		this.c = c;
	}
	
	@Override
	public void run(){
		
	}
}
