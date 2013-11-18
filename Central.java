import java.util.HashMap;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

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
		Processor sec = new Processor();
		try {
			Socket dep = this.deps.accept();
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
	
	public void remove(Student student){
		this.students.remove(student.getId());
	}
}

class Processor extends Thread{
	@Override
	public void run(){
		
	}
}
