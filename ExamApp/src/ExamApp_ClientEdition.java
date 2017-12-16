import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JList;
import javax.swing.JOptionPane;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;

@SuppressWarnings("serial")
public class ExamApp_ClientEdition extends JFrame {

	private JPanel contentPane;
	private JTextField textField;
	private JTextField textField_1;
	private JTextField textField_2;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ExamApp_ClientEdition frame = new ExamApp_ClientEdition();
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
	public ExamApp_ClientEdition() {
		setFont(new Font("Tahoma", Font.BOLD, 14));
		setTitle("ExamAppForStudents");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(200, 200, 500, 380);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		JLabel lblUpload = new JLabel("Upload");
		lblUpload.setFont(new Font("Tahoma", Font.BOLD, 12));
		
		JLabel lblDownload = new JLabel("Download");
		lblDownload.setFont(new Font("Tahoma", Font.BOLD, 12));
		
		JLabel lblEnterStudentid = new JLabel("Enter StudentId");
		
		textField = new JTextField();
		textField.setColumns(10);
		
		JLabel lblEnterMachineno = new JLabel("Enter MachineNo");
		
		textField_1 = new JTextField();
		textField_1.setColumns(10);
		
		textField_2 = new JTextField();
		textField_2.setColumns(10);
		
		JButton btnNewButton = new JButton("Browse");
		btnNewButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				final JFileChooser fc = new JFileChooser();
				int response = fc.showOpenDialog(ExamApp_ClientEdition.this);
				if(response==JFileChooser.APPROVE_OPTION)
				{
					textField_2.setText(fc.getSelectedFile().toString());
					System.out.println(fc.getSelectedFile().toString());
				}
				else
				{
					JOptionPane.showMessageDialog(null, "Error Opening File", "Error", JOptionPane.WARNING_MESSAGE); 
				}
			}
		});
		
		JButton btnNewButton_1 = new JButton("Upload");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String path = textField_2.getText();
				Path p = Paths.get(path); 
				try {
					Socket socket = new Socket("localhost", 3434);
					Upload request = new Upload();
					request.file=Files.readAllBytes(p);
                    request.filename = textField.getText() + " _"+ textField_1.getText() + path.substring(path.indexOf("."));
                    ObjectOutputStream out = new ObjectOutputStream(socket.
                            getOutputStream());
                    out.writeObject(request);
                    ObjectInputStream i = new ObjectInputStream(socket.getInputStream());
                    String msg = i.readObject().toString();
                    if(msg!=null)
                    JOptionPane.showMessageDialog(null,msg, "Message", JOptionPane.INFORMATION_MESSAGE);
                   socket.close();
					
				} catch (UnknownHostException e) {
					// TODO Auto-generated catch block
					JOptionPane.showMessageDialog(null, "Connection to Server Failed!!", "Error", JOptionPane.WARNING_MESSAGE);
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		btnNewButton_1.setFont(new Font("Tahoma", Font.BOLD, 13));
		@SuppressWarnings("rawtypes")
		JList list = new JList();
		JButton btnNewButton_2 = new JButton("View Question Papers");
		btnNewButton_2.addMouseListener(new MouseAdapter() {
			@SuppressWarnings("unchecked")
			@Override
			public void mouseClicked(MouseEvent e) {
				try {
					@SuppressWarnings("resource")
					Socket socket = new Socket("localhost",3434);
					View request =  new View();
					ObjectOutputStream out = new ObjectOutputStream(socket.
                            getOutputStream());
                    out.writeObject(request);
                    System.out.println("requested");
					DefaultListModel<String> model = new DefaultListModel<>();
					ObjectInputStream input = new ObjectInputStream(socket.getInputStream());
					Object[] arr = (Object[]) input.readObject();
					for (Object o : arr) {
			            System.out.println(o);
			            model.addElement(o.toString());
					}
					list.setModel(model);
				} catch (UnknownHostException e1) {
					// TODO Auto-generated catch block
					JOptionPane.showMessageDialog(null, "Connection to Server Failed!!", "Error", JOptionPane.WARNING_MESSAGE);
					e1.printStackTrace();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (ClassNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		btnNewButton_2.setFont(new Font("Tahoma", Font.BOLD, 12));
		
	
		btnNewButton_2.setFont(new Font("Tahoma", Font.BOLD, 12));
		
		
		
		JButton btnNewButton_3 = new JButton("Download");
		btnNewButton_3.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				System.out.println(list.getSelectedIndex());
				String s = (String) list.getModel().getElementAt(list.getSelectedIndex());
			//String	s ="paper1.txt";
				System.out.println(s);
				try {
					Download request = new Download();
					Socket socket = new Socket("localhost",3434);
					request.selectedFile=s;
					ObjectOutputStream out = new ObjectOutputStream(socket.
                            getOutputStream());
					out.writeObject(request);
                    
                    System.out.println("requested again");
                    ObjectInputStream input = new ObjectInputStream(socket.getInputStream());
                    byte[] file = (byte[])input.readObject();
                    String home = System.getProperty("user.home")+"\\exam";
                    File directory = new File(String.valueOf(home));
                    if(!directory.exists()){

                        directory.mkdir();
                    }
                    FileOutputStream fos = new FileOutputStream(home+"\\" +s);
					fos.write(file);
					fos.close();
				System.out.println("completed ");
				  JOptionPane.showMessageDialog(null, "Question Paper Downloaded at path : "+ home+"\\"+s, "Message", JOptionPane.INFORMATION_MESSAGE); 
				/*int dialogButton = JOptionPane.YES_NO_OPTION;
				int dialogResult = JOptionPane.showConfirmDialog (null, "Would ?","Warning",dialogButton);
				if(dialogResult == JOptionPane.YES_OPTION){
					JOptionPane.showMessageDialog(null, "File Path : " + s, "Message", JOptionPane.INFORMATION_MESSAGE);
					
				}*/
				  socket.close();
				} catch (UnknownHostException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (ClassNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
			}
		});
		btnNewButton_3.setFont(new Font("Tahoma", Font.BOLD, 13));
		btnNewButton_3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			}
		});
		
		
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGap(69)
					.addComponent(lblUpload, GroupLayout.PREFERRED_SIZE, 130, GroupLayout.PREFERRED_SIZE)
					.addGap(68)
					.addComponent(lblDownload, GroupLayout.PREFERRED_SIZE, 137, GroupLayout.PREFERRED_SIZE))
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGap(10)
					.addComponent(lblEnterStudentid, GroupLayout.PREFERRED_SIZE, 108, GroupLayout.PREFERRED_SIZE)
					.addGap(10)
					.addComponent(textField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addGap(26)
					.addComponent(btnNewButton_2, GroupLayout.PREFERRED_SIZE, 184, GroupLayout.PREFERRED_SIZE))
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGap(49)
					.addComponent(btnNewButton_1, GroupLayout.PREFERRED_SIZE, 112, GroupLayout.PREFERRED_SIZE)
					.addGap(120)
					.addComponent(btnNewButton_3, GroupLayout.PREFERRED_SIZE, 123, GroupLayout.PREFERRED_SIZE))
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(10)
							.addComponent(lblEnterMachineno, GroupLayout.PREFERRED_SIZE, 108, GroupLayout.PREFERRED_SIZE)
							.addGap(10)
							.addComponent(textField_1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(8)
							.addComponent(textField_2, GroupLayout.PREFERRED_SIZE, 130, GroupLayout.PREFERRED_SIZE)
							.addGap(10)
							.addComponent(btnNewButton, GroupLayout.PREFERRED_SIZE, 91, GroupLayout.PREFERRED_SIZE)))
					.addGap(18)
					.addComponent(list, GroupLayout.DEFAULT_SIZE, 157, Short.MAX_VALUE)
					.addContainerGap())
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGap(29)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addComponent(lblUpload, GroupLayout.PREFERRED_SIZE, 33, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblDownload, GroupLayout.PREFERRED_SIZE, 33, GroupLayout.PREFERRED_SIZE))
					.addGap(11)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addComponent(lblEnterStudentid, GroupLayout.PREFERRED_SIZE, 33, GroupLayout.PREFERRED_SIZE)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(6)
							.addComponent(textField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(5)
							.addComponent(btnNewButton_2)))
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(22)
							.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_contentPane.createSequentialGroup()
									.addGap(3)
									.addComponent(lblEnterMachineno))
								.addComponent(textField_1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
							.addGap(26)
							.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_contentPane.createSequentialGroup()
									.addGap(1)
									.addComponent(textField_2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
								.addComponent(btnNewButton)))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(7)
							.addComponent(list, GroupLayout.PREFERRED_SIZE, 78, GroupLayout.PREFERRED_SIZE)))
					.addGap(18)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addComponent(btnNewButton_1, GroupLayout.PREFERRED_SIZE, 41, GroupLayout.PREFERRED_SIZE)
						.addComponent(btnNewButton_3, GroupLayout.PREFERRED_SIZE, 41, GroupLayout.PREFERRED_SIZE)))
		);
		contentPane.setLayout(gl_contentPane);
	}
}
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
 @SuppressWarnings("serial")
class View extends Request
 {
	 public View()
	 {
		 type="View";
	 }
 }
 