import java.util.ArrayList;
import java.util.HashMap;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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
    	ArrayList<Processor> lupalupas = new ArrayList<Processor>();
		try {
		    Socket dep = this.deps.accept();
		    lupalupas.add(new Processor(dep, this));
		} catch (IOException e) {
		    e.printStackTrace();
		}
    }
	
    public InetAddress whoIs(int student){
    	return this.students.get(student);
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
    private ObjectInputStream in;
    private ObjectOutputStream out;

    public Processor(Socket s, Central c){
		try {
			this.in = new ObjectInputStream( new DataInputStream(s.getInputStream()));
			this.out = new ObjectOutputStream( new DataOutputStream(s.getOutputStream()));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.s = s;
		this.c = c;
		this.start();
    }
	
    @Override
    public void run(){
    	Student student = null;
		
    	try {
			student = (Student) in.readObject();
		} catch (ClassNotFoundException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		student.setDep(this.c.whoIs(student.getId()));
		
		try {
			this.out.writeObject(student);
			this.s.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
    }
}
