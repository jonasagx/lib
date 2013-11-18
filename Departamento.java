

import java.io.IOException;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class Departamento {
	private String name;
	private InetAddress ip;
	private InetAddress central;
	private Socket con = null;
	private Socket peer = null;
	private ArrayList<Student> students = new ArrayList<Student>();
	
	public Departamento(InetAddress central, String name) {
		try {
			this.ip = InetAddress.getLocalHost();
			this.name = name;
			this.central = central;
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}
	
	public void connect(){
		try {
			this.con = new Socket(this.central, 8080);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void addStudent(){
		
	}
	
	public void updateCentral(){
		
	}
}
