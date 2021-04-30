package java_socket;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.Socket;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class Client extends JFrame implements ActionListener, KeyListener {

	private static final long serialVersionUID = 1L;
	private JTextArea txt;
	private JTextField txtMsg;
	private JButton btnSend;
	private JButton btnExit;
	private JLabel lblHistoric;
	private JLabel lblMsg;
	private JPanel pnlContent;
	private Socket socket;
	private OutputStream ous ;
	private Writer ouw;
	private BufferedWriter bfw;
	private JTextField txtIP;
	private JTextField txtPorta;
	private JTextField txtName;
	
	public Client() throws IOException{

	    JLabel lblMessage = new JLabel("Verificar!");
	    txtIP = new JTextField("127.0.0.1");
	    txtPorta = new JTextField("Insira o numero da porta");
	    txtName = new JTextField();
	    Object[] texts = {lblMessage, txtIP, txtPorta, txtName };
	    JOptionPane.showMessageDialog(null, texts);
	     pnlContent = new JPanel();
	     txt = new JTextArea(10,20);
	     txt.setEditable(false);
	     txt.setBackground(new Color(240,240,240));
	     txtMsg = new JTextField(20);
	     lblHistoric = new JLabel("chat history");
	     lblMsg = new JLabel("Message");
	     btnSend = new JButton("Send");
	     btnSend.setToolTipText("Send message");
	     btnExit = new JButton("Exit");
	     btnExit.setToolTipText("Sair do Chat");
	     btnSend.addActionListener(this);
	     btnExit.addActionListener(this);
	     btnSend.addKeyListener(this);
	     txtMsg.addKeyListener(this);
	     JScrollPane scroll = new JScrollPane(txt);
	     txt.setLineWrap(true);
	     pnlContent.add(lblHistoric);
	     pnlContent.add(scroll);
	     pnlContent.add(lblMsg);
	     pnlContent.add(txtMsg);
	     pnlContent.add(btnExit);
	     pnlContent.add(btnSend);
	     pnlContent.setBackground(Color.LIGHT_GRAY);
	     txt.setBorder(BorderFactory.createEtchedBorder(Color.GRAY,Color.GRAY));
	     txtMsg.setBorder(BorderFactory.createEtchedBorder(Color.GREEN, Color.GREEN));
	     setTitle(txtName.getText());
	     setContentPane(pnlContent);
	     setLocationRelativeTo(null);
	     setResizable(true);
	     setSize(300,350);
	     setVisible(true);
	     setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	
	public void conection() throws IOException{

		  socket = new Socket(txtIP.getText(),Integer.parseInt(txtPorta.getText()));
		  ous = socket.getOutputStream();
		  ouw = new OutputStreamWriter(ous);
		  bfw = new BufferedWriter(ouw);
		  bfw.write(txtName.getText()+"\r\n");
		  bfw.flush();
		}

	  public void sendMsg(String msg) throws IOException{

		    if(msg.equals("Sair")){
		      bfw.write("Desconectado \r\n");
		      txt.append("Desconectado \r\n");
		    }else{
		      bfw.write(msg+"\r\n");
		      txt.append( txtName.getText() + " Say: " + txtMsg.getText()+"\r\n");
		    }
		     bfw.flush();
		     txtMsg.setText("");
		}
	  
	  public void listen() throws IOException{

		   InputStream in = socket.getInputStream();
		   InputStreamReader inr = new InputStreamReader(in);
		   BufferedReader bfr = new BufferedReader(inr);
		   String msg = "";

		    while(!"Sair".equalsIgnoreCase(msg))

		       if(bfr.ready()){
		         msg = bfr.readLine();
		       if(msg.equals("Exit"))
		         txt.append("Server down! \r\n");
		        else
		         txt.append(msg+"\r\n");
		        }
		}
	  
	  public void exit() throws IOException{

		   sendMsg("Exit");
		   bfw.close();
		   ouw.close();
		   ous.close();
		   socket.close();
		}
	  
	  @Override
	  public void actionPerformed(ActionEvent e) {

	    try {
	       if(e.getActionCommand().equals(btnSend.getActionCommand()))
	          sendMsg(txtMsg.getText());
	       else
	          if(e.getActionCommand().equals(btnExit.getActionCommand()))
	          exit();
	       } catch (IOException e1) {
	            // TODO Auto-generated catch block
	            e1.printStackTrace();
	       }
	  }
	  
	  @Override
	  public void keyPressed(KeyEvent e) {

	      if(e.getKeyCode() == KeyEvent.VK_ENTER){
	         try {
	            sendMsg(txtMsg.getText());
	         } catch (IOException e1) {
	             // TODO Auto-generated catch block
	             e1.printStackTrace();
	         }
	     }
	  }

	  @Override
	  public void keyReleased(KeyEvent arg0) {
	    // TODO Auto-generated method stub
	  }

	  @Override
	  public void keyTyped(KeyEvent arg0) {
	    // TODO Auto-generated method stub
	  }
	  
	  public static void main(String []args) throws IOException{
		  
		   Client app = new Client();
		   app.conection();
		   app.listen();
		}
}
