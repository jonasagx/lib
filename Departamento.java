
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Stack;

public class Departamento {
	private String name;
    private InetAddress ip;
    private InetAddress central;
    private Socket con;
    private Socket peer;
    private ObjectInputStream in;
	private ObjectOutputStream out;
	private ServerSocket server;
	
    private ArrayList<Student> students = new ArrayList<Student>();
	
    public Departamento(InetAddress central, String name) {
		try {
		    this.ip = InetAddress.getLocalHost();
		    this.name = name;
		    this.central = central;
		} catch (UnknownHostException e) {
		    e.printStackTrace();
		}
		connect();
    }
	
    public void connect(){
		try {
		    this.con = new Socket(this.central, 8080);
		} catch (IOException e) {
		    e.printStackTrace();
		}
		System.out.println("Conectado a central");
    }
	
    public void listen(){
    	try {
			this.server = new ServerSocket(8081);
			System.out.println("Departamento " + this.name + " listening");
			this.server.accept();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    public void addStudent(Student student){
    	this.students.add(student);
    }

    public void addStudent(String name, InetAddress dep){
		this.students.add( new Student(name, dep) );
		System.out.println(name + " adicionado!");
    }

    public void borrow(String book, Student student){
		int index = this.students.indexOf(student);
		if(index >= 0){
			if(this.students.get(index).canBorrow()){				
				this.students.get(index).addBook(book);
			} else {
				System.out.println("Limite de impréstimos atingido");
				return;
			}
		} else {
			try {
				this.in = new ObjectInputStream( new DataInputStream(this.con.getInputStream()));
				
				//Envia objeto para central
				this.out.writeObject(new Student(student.getId()));
				//Recebe da central
				Student stdt = (Student) this.in.readObject();
				
				//Conecta ao outro departamento
				this.peer = new Socket(stdt.getDep(), 8081);
				this.in = new ObjectInputStream( new DataInputStream(this.peer.getInputStream()));
				this.out = new ObjectOutputStream( new DataOutputStream(this.peer.getOutputStream()));
				this.out.writeObject(student);
				//Recebe cópia do objeto estudante do outro departamento
				stdt = (Student) this.in.readObject();
				
				if(stdt.canBorrow()){
					stdt.addBook(book);
				} else {				
					System.out.println("Limite de impréstimos atingido");
					this.peer.close();
					return;
				}
	
				//Devolve objeto modificado;
				this.out.writeObject(stdt);	
				this.peer.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
    }
}

class Processor extends Thread {
	private Stack<Student> pilha;
	
	public Processor(){
		try {
			this.wait();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void add(Student student){
		this.pilha.add(student);
		if(this.isInterrupted()){			
			this.notify();
		}
	}
	
	@Override
	public void run(){
		
	}
}