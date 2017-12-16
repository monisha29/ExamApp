import java.awt.EventQueue;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JTextArea;
import javax.swing.LayoutStyle.ComponentPlacement;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
//import java.io.Serializable;

import javax.swing.JTextField;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

@SuppressWarnings("serial")
public class ExamApp_ThreadedServer extends JFrame {

	private JPanel contentPane;
	static ServerSocket serverSocket;
	private JTextField textField;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
	   
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ExamApp_ThreadedServer frame = new ExamApp_ThreadedServer();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		startServer();
	}

	/**
	 * Create the frame.
	 */
	public ExamApp_ThreadedServer() {
		setTitle("ExamAppServer");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		JTextArea textArea = new JTextArea();
		JButton btnNewButton = new JButton("View Answersheets");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			}
		});
		btnNewButton.setFont(new Font("Tahoma", Font.BOLD, 13));
		btnNewButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				 String home = System.getProperty("user.home")+"\\Answers";
				 File f = new File(home);
				 String[] data = f.list();
				 
				 for(String filename : data)
				 {
					 textArea.append(filename +"\n");
					 System.out.println(filename);
				 }

			}
		});
		
		textField = new JTextField();
		textField.setColumns(10);
		
		JButton btnNewButton_1 = new JButton("Browse");
		btnNewButton_1.setFont(new Font("Tahoma", Font.BOLD, 12));
		btnNewButton_1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				final JFileChooser fc = new JFileChooser();
				int response = fc.showOpenDialog(ExamApp_ThreadedServer.this);
				if(response==JFileChooser.APPROVE_OPTION)
				{
					textField.setText(fc.getSelectedFile().toString());
					System.out.println(fc.getSelectedFile().toString());
				}
				else
				{
					JOptionPane.showMessageDialog(null, "Error Opening File", "Error", JOptionPane.WARNING_MESSAGE); 
				}
			}
		});
		
		JButton btnNewButton_2 = new JButton("Upload QuestionPapers");
		btnNewButton_2.setFont(new Font("Tahoma", Font.BOLD, 13));
		btnNewButton_2.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				InputStream inStream = null;
                OutputStream outStream = null;
                try{
                    File source =new File(textField.getText());
                    String s = textField.getText();
                    String required = s.substring(s.lastIndexOf("\\")+1,s.length());
                    String home = System.getProperty("user.home")+"\\exam_questions";
                    File directory = new File(String.valueOf(home));
                    if(!directory.exists()){

                        directory.mkdir();
                    }
                    System.out.println(home);
                    File dest =new File(home+"\\" + required );
                    
                    inStream = new FileInputStream(source);
                    outStream = new FileOutputStream(dest);

                    byte[] buffer = new byte[1024];

                    int length;
                    while ((length = inStream.read(buffer)) > 0){
                        outStream.write(buffer, 0, length);
                    }

                    if (inStream != null)inStream.close();
                    if (outStream != null)outStream.close();
                    System.out.println("File Copied..");
                    textField.setText("");
                    JOptionPane.showMessageDialog(null, "File Uploaded", "Message", JOptionPane.INFORMATION_MESSAGE); 
                }catch(IOException e1){
                    e1.printStackTrace();
                }
			}
		});
		
		
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(66)
							.addComponent(textArea, GroupLayout.PREFERRED_SIZE, 267, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(73)
							.addComponent(textField, GroupLayout.PREFERRED_SIZE, 166, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(btnNewButton_1))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(95)
							.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
								.addComponent(btnNewButton_2)
								.addComponent(btnNewButton, GroupLayout.PREFERRED_SIZE, 168, GroupLayout.PREFERRED_SIZE))))
					.addContainerGap(91, Short.MAX_VALUE))
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGap(23)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(btnNewButton_1)
						.addComponent(textField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(18)
					.addComponent(btnNewButton_2, GroupLayout.PREFERRED_SIZE, 32, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED, 20, Short.MAX_VALUE)
					.addComponent(btnNewButton)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(textArea, GroupLayout.PREFERRED_SIZE, 92, GroupLayout.PREFERRED_SIZE)
					.addGap(12))
		);
		contentPane.setLayout(gl_contentPane);
	}


	public static void startServer()
	{
		try {
			serverSocket = new ServerSocket(3434);
			Socket socket ;
				while(true){
				 socket = serverSocket.accept();
				    ObjectInputStream dis = new ObjectInputStream(socket.getInputStream());
	                ObjectOutputStream dos = new ObjectOutputStream(socket.getOutputStream());
	                 
	                System.out.println("Assigning new thread for this client");
	 
	                // create a new thread object
	                Thread t = new ClientHandler(socket, dis, dos);
	 
	                // Invoking the start() method
	                t.start();
				}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	}
}
 class ClientHandler extends Thread
 {
	    final ObjectInputStream dis;
	    final ObjectOutputStream dos;
	    final Socket s;
	    public ClientHandler(Socket s, ObjectInputStream dis,ObjectOutputStream dos) 
	    {
	        this.s = s;
	        this.dis = dis;
	        this.dos = dos;
	    }
	    public void run() 
	    {
	    	Request request = null;
			try {
				request = (Request)dis.readObject();
			} catch (ClassNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			System.out.println(request.type);
			if(request.type.equals("Upload"))
			{
			
				Upload urequest = (Upload)(request);
				System.out.println(urequest.filename);
				System.out.println(urequest.file.length);
				int i = urequest.filename.indexOf(".");
				String extension = urequest.filename.substring(i);
				String name = urequest.filename.substring(0,i);
				System.out.println(name);
				//String ip = s.getRemoteSocketAddress().toString();
				String iip = s.getInetAddress().toString();
				iip = iip.replace('/','_');
				name = name+iip+extension ;
				System.out.println(name);
				urequest.filename = name;
				System.out.println(iip);
				
				System.out.println(urequest.filename);
				FileOutputStream fos;
				try {
					 String home = System.getProperty("user.home")+"\\Answers";
	                    File directory = new File(String.valueOf(home));
	                    if(!directory.exists()){

	                        directory.mkdir();
	                    }
	                    boolean contains = false;
	    				File f = new File(home);
	    				String[] data = f.list();
	    				for(String val : data)
	    					if(val.equals(name)){
	    						
	    						contains = true;
	    						break;
	    					}
					if(contains)
						dos.writeObject("Submission already made!!");
					else
				{
						
				fos = new FileOutputStream(home+"\\"+urequest.filename);
				fos.write(urequest.file);
				fos.close();
				dos.writeObject("Answers Uploaded");
				}
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else if(request.type.equals("View"))
			{
				System.out.println("here");
				 String home = System.getProperty("user.home")+"\\exam_questions";
			  File f = new File(home);
			  String[] data = f.list();
			  System.out.println(data.length);
             try {
				dos.writeObject(data);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			}
			else if(request.type.equals("Download"))
			{
				System.out.println("here2");
				Download drequest = (Download)(request);
				String file = System.getProperty("user.home")+"\\exam_questions\\" + drequest.selectedFile;
				System.out.println(file);
				Path p = Paths.get(file);
				byte[] afile;
				try {
					afile = Files.readAllBytes(p);
					dos.writeObject(afile);
				
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				
             System.out.println("buffer");
			}
	    }
	    }
	 
 }
 /*
 @SuppressWarnings("serial")
class Request implements Serializable
{
	public String type;
}
 @SuppressWarnings("serial")
class Upload extends Request 
 {
	public String filename;
	public byte[] file;
	 public Upload()
	 {
		 type="Upload";
	 }
 }
 @SuppressWarnings("serial")
class Download extends Request
 {
	 public String selectedFile;
	 public Download()
	 {
		 type="Download";
	 }
 }
 class View extends Request
 {
	 public View()
	 {
		 type="View";
	 }
 }
 */