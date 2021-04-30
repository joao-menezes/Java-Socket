package java_socket;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class Server extends Thread {

	private static ArrayList<BufferedWriter>client;
	private static ServerSocket server;
	private String name;
	private Socket socket;
	private InputStream ins;
	private InputStreamReader inr;
	private BufferedReader bfr;
	
	public Server(Socket con){
		   this.socket = con;
		   try {
		         ins  = con.getInputStream();
		         inr = new InputStreamReader(ins);
		          bfr = new BufferedReader(inr);
		   } catch (IOException e) {
		          e.printStackTrace();
		   }
		}
	
	public void run(){

		  try{

		    String msg;
		    OutputStream ou =  this.socket.getOutputStream();
		    Writer ouw = new OutputStreamWriter(ou);
		    BufferedWriter bfw = new BufferedWriter(ouw);
		    client.add(bfw);
		    name = msg = bfr.readLine();

		    while(!"Sair".equalsIgnoreCase(msg) && msg != null)
		      {
		       msg = bfr.readLine();
		       sendToAll(bfw, msg);
		       System.out.println(msg);
		       }

		   }catch (Exception e) {
		     e.printStackTrace();

		   }
		}
	
	public void sendToAll(BufferedWriter bwExit, String msg) throws  IOException
	{
	  BufferedWriter bwS;

	  for(BufferedWriter bw : client){
	   bwS = (BufferedWriter)bw;
	   if(!(bwExit == bwS)){
	     bw.write(name + " -> " + msg+"\r\n");
	     bw.flush();
	   }
	  }
	}
	
	public static void main(String []args) {

		  try{
		    //Cria os objetos necessário para instânciar o servidor
		    JLabel lblMessage = new JLabel("Server Path:");
		    JTextField txtPath = new JTextField("");
		    Object[] txts = {lblMessage, txtPath };
		    JOptionPane.showMessageDialog(null, txts);
		    server = new ServerSocket(Integer.parseInt(txtPath.getText()));
		    client = new ArrayList<BufferedWriter>();
		    JOptionPane.showMessageDialog(null,"Server Active: "+txtPath.getText());

		     while(true){
		       System.out.println("Waiting Connection...");
		       Socket con = server.accept();
		       System.out.println("Client Connected...");
		       Thread t = new Server(con);
		        t.start();
		    }

		  }catch (Exception e) {
			  System.err.println("Error");
		    e.printStackTrace();
		  }
		 }
		}

