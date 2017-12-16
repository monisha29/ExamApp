import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFileChooser;

import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.JOptionPane;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.swing.JTextArea;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

@SuppressWarnings("serial")
public class ExamApp_ServerEdition extends JFrame {

	private JPanel contentPane;
	private JTextField textField;
	static ServerSocket serverSocket;
	boolean serverStatus=true;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ExamApp_ServerEdition frame = new ExamApp_ServerEdition();
					frame.setVisible(true);
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public ExamApp_ServerEdition() {
		setTitle("ExamAppForFaculty");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 449, 335);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JButton btnNewButton = new JButton("SwitchOn Server");
		btnNewButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				try {
					serverSocket = new ServerSocket(3434);
					Socket socket ;
					while(true){
					 socket = serverSocket.accept();
					ObjectInputStream input = new ObjectInputStream(socket.getInputStream());
					try {
						Request request = (Request) input.readObject();
						System.out.println(request.type);
						if(request.type.equals("Upload"))
						{
						
							Upload urequest = (Upload)(request);
							System.out.println(urequest.filename);
							System.out.println(urequest.file.length);
							FileOutputStream fos = new FileOutputStream("J:\\Answers\\"+urequest.filename);
							fos.write(urequest.file);
							fos.close();
						}
						else if(request.type.equals("View"))
						{
							System.out.println("here");
							ObjectOutputStream out = new ObjectOutputStream(socket.
		                            getOutputStream());
						  File f = new File("J:\\exam_question");
						  String[] data = f.list();
						  System.out.println(data.length);
		                    out.writeObject(data);
						}
						else if(request.type.equals("Download"))
						{
							System.out.println("here2");
							Download drequest = (Download)(request);
							String file = "J:\\exam_question\\" + drequest.selectedFile;
							System.out.println(file);
							Path p = Paths.get(file);
							byte[] afile = Files.readAllBytes(p);
							ObjectOutputStream out = new ObjectOutputStream(socket.
		                            getOutputStream());
		                    out.writeObject(afile);
		                    System.out.println("buffer");
						}
					} catch (ClassNotFoundException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
				}
				catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
			}
			
		});
		btnNewButton.setFont(new Font("Tahoma", Font.BOLD, 13));
		btnNewButton.setBounds(54, 24, 152, 37);
		contentPane.add(btnNewButton);
		
		textField = new JTextField();
		textField.setBounds(54, 81, 152, 20);
		contentPane.add(textField);
		textField.setColumns(10);
		
		JButton btnBrowse = new JButton("Browse");
		btnBrowse.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		btnBrowse.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				final JFileChooser fc = new JFileChooser();
				int response = fc.showOpenDialog(ExamApp_ServerEdition.this);
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
		btnBrowse.setBounds(225, 80, 89, 23);
		contentPane.add(btnBrowse);
		
		JButton btnNewButton_1 = new JButton("Upload QuestionPaper");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		btnNewButton_1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				    InputStream inStream = null;
	                OutputStream outStream = null;
	                try{
	                    File source =new File(textField.getText());
	                    String s = textField.getText();
	                    String required = s.substring(s.lastIndexOf("\\")+1,s.length());
	                    File dest =new File("J:\\exam_question\\" + required );
	                    
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
		btnNewButton_1.setFont(new Font("Tahoma", Font.BOLD, 12));
		btnNewButton_1.setBounds(119, 112, 152, 23);
		contentPane.add(btnNewButton_1);
		DefaultListModel<String> model = new DefaultListModel<>();
		JTextArea textArea = new JTextArea();
		textArea.setToolTipText("will show the list of answer sheets.");
		textArea.setEditable(false);
		textArea.setBounds(37, 188, 321, 97);
		contentPane.add(textArea);
		JButton btnNewButton_2 = new JButton("View AnswerSheets");
		btnNewButton_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		btnNewButton_2.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				 File f = new File("J:\\Answers");
				 String[] data = f.list();
				 
				 for(String filename : data)
				 {
					 textArea.append(filename +"\n");
					 model.addElement(filename);
					 System.out.println(filename);
				 }
				 
			}
		});
		btnNewButton_2.setFont(new Font("Tahoma", Font.BOLD, 12));
		btnNewButton_2.setBounds(92, 146, 223, 23);
		contentPane.add(btnNewButton_2);
		
		JButton btnNewButton_3 = new JButton("SwitchOff Server");
		btnNewButton_3.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				serverStatus = false;
			}
		});
		btnNewButton_3.setFont(new Font("Tahoma", Font.BOLD, 13));
		btnNewButton_3.setBounds(225, 24, 144, 37);
		contentPane.add(btnNewButton_3);
		
		
		
		
		
		
	}

 public static void testmethod()
 {
	 try {
			serverSocket = new ServerSocket(3434);
			Socket socket ;
		//	while(true){
			 socket = serverSocket.accept();
			ObjectInputStream input = new ObjectInputStream(socket.getInputStream());
			try {
				Request request = (Request) input.readObject();
				System.out.println(request.type);
				if(request.type.equals("Upload"))
				{
				
					Upload urequest = (Upload)(request);
					System.out.println(urequest.filename);
					System.out.println(urequest.file.length);
					FileOutputStream fos = new FileOutputStream("J:\\Answers\\"+urequest.filename);
					fos.write(urequest.file);
					fos.close();
				}
				else if(request.type.equals("View"))
				{
					System.out.println("here");
					ObjectOutputStream out = new ObjectOutputStream(socket.
                         getOutputStream());
				  File f = new File("J:\\exam_question");
				  String[] data = f.list();
				  System.out.println(data.length);
                 out.writeObject(data);
				}
				else if(request.type.equals("Download"))
				{
					System.out.println("here2");
					Download drequest = (Download)(request);
					String file = "J:\\exam_question\\" + drequest.selectedFile;
					System.out.println(file);
					Path p = Paths.get(file);
					byte[] afile = Files.readAllBytes(p);
					ObjectOutputStream out = new ObjectOutputStream(socket.
                         getOutputStream());
                 out.writeObject(afile);
                 System.out.println("buffer");
				}
			} catch (ClassNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
	//	}
		}
		catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
	
 }
}
