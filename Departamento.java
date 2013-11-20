
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
import java.util.HashMap;
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
	
    private HashMap<Integer, Student> students = new HashMap<Integer, Student>();
	
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
    	Processor p = new Processor(this);
    	try {
			this.server = new ServerSocket(8081);
			System.out.println("Departamento " + this.name + " listening");
			//Passa conexão (socket) para o processador
			p.add(this.server.accept());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    public void addStudent(Student student){
    	this.students.put(student.getId(), student);
    }

    public void addStudent(String name, InetAddress dep){
		Student tmp = new Student(name, dep);
		this.students.put(tmp.getId(), tmp);
		System.out.println(name + " adicionado!");
    }

    public Student getStudent(Student student){
    	return this.students.get(student.getId());
    }
    
    public void update(Student student){
    	this.students.put(student.getId(), student);
    	System.out.println(student.getName() + " atualizado!");
    }
    
    public void giveBack(String book, Student student){
    	if( this.students.containsKey(student.getId()) ){
    		this.students.get(student.getId()).removeBook(book);
    	} else {
    		try {
				this.in = new ObjectInputStream( new DataInputStream(this.con.getInputStream()));
				this.out = new ObjectOutputStream( new DataOutputStream( this.con.getOutputStream()));
				
				//Pede localização para central
				this.out.writeObject( student );
				Student stdt = (Student) this.in.readObject();
				
				//Cria conexão com dep
				this.peer = new Socket(stdt.getDep(), 8081);
				this.in = new ObjectInputStream( new DataInputStream(this.peer.getInputStream()));
				this.out = new ObjectOutputStream( new DataOutputStream( this.peer.getOutputStream()));
				stdt.request = true;
				
				//Informa o objecto requisitado
				this.out.writeObject(stdt);
				stdt = (Student) this.in.readObject();
				
				//Atualiza o objeto
				stdt.removeBook(book);
				stdt.request = false;
				
				//Devolve atualizado
				this.out.writeObject(stdt);
				this.peer.close();
				
			} catch (IOException | ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
    }
    
    public void borrow(String book, Student student){
		int index = student.getId();
		if(this.students.get(student.getId()) != null){
			if(this.students.get(index).canBorrow()){				
				this.students.get(index).addBook(book);
			} else {
				System.out.println("Limite de impréstimos atingido");
				return;
			}
		} else {
			try {
				this.in = new ObjectInputStream( new DataInputStream(this.con.getInputStream()));
				this.out = new ObjectOutputStream( new DataOutputStream( this.con.getOutputStream()));
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
	private Stack<Socket> stack;
	private ObjectInputStream in;
	private ObjectOutputStream out;
	private Socket dep;
	private Departamento departament;
	
	public Processor(Departamento d){
		this.departament = d;
		try {
			this.wait();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void add(Socket student){
		this.stack.add(student);
		if(this.isInterrupted()){			
			this.notify();
		}
	}
	
	@Override
	public void run(){
		while(true){
			this.dep = this.stack.pop();
			try {
				this.in = new ObjectInputStream( new DataInputStream( this.dep.getInputStream()));
				this.out = new ObjectOutputStream( new DataOutputStream( this.dep.getOutputStream()));
				
				Student stdt = (Student) this.in.readObject();
				//Diferenciar atualização de requisição
				if(stdt.request){					
					//Request
					stdt = this.departament.getStudent(stdt);
					this.out.writeObject(stdt);
					this.dep.close();				
					this.wait();
				} else {
					//Update
					this.departament.update(stdt);
				}
				
			} catch (IOException | ClassNotFoundException | InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}