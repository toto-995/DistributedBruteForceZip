package Client;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.rmi.Naming;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.filechooser.FileNameExtensionFilter;
import Master.MasterInterface;
import net.lingala.zip4j.exception.ZipException;

public class Client extends javax.swing.JFrame {
	
	private javax.swing.JLabel masterIP = new javax.swing.JLabel("Master IP:");
	private javax.swing.JTextField textMIP = new javax.swing.JTextField("127.0.0.1");
	private javax.swing.JLabel masterPort = new javax.swing.JLabel("Master Port:");
	private javax.swing.JTextField textMP = new javax.swing.JTextField("1099");
	private javax.swing.JButton next = new javax.swing.JButton();
	private javax.swing.JButton browse = new javax.swing.JButton();
	private javax.swing.JFileChooser fileChooser = new javax.swing.JFileChooser();
	private javax.swing.JButton jButton1 = new javax.swing.JButton("BruteForce!");
	private javax.swing.JLabel load = new javax.swing.JLabel( new ImageIcon(getClass().getResource("/resources/c.gif")));
	private javax.swing.JLabel jLabel1 = new javax.swing.JLabel("File:");
	private javax.swing.JLabel jLabel2 = new javax.swing.JLabel("Lunghezza minima:");
	private javax.swing.JLabel jLabel3 = new javax.swing.JLabel("Lunghezza massima:");
	SpinnerModel model2 = new SpinnerNumberModel(5, 1, 100, 1);
	SpinnerModel model3 = new SpinnerNumberModel(8, 1, 100, 1);
	private javax.swing.JSpinner jSpinner2 = new javax.swing.JSpinner(model2);
	private javax.swing.JSpinner jSpinner3 = new javax.swing.JSpinner(model3);
	private javax.swing.JPanel jPanel1 = new javax.swing.JPanel();
	private javax.swing.JPanel info = new javax.swing.JPanel();
	private javax.swing.JTextField jTextField1 = new javax.swing.JTextField();
	private javax.swing.JCheckBox jCheckBoxUpp = new javax.swing.JCheckBox("A-Z");
	private javax.swing.JCheckBox jCheckBoxLow = new javax.swing.JCheckBox("a-z");
	private javax.swing.JCheckBox jCheckBoxNum = new javax.swing.JCheckBox("0-9");
	private String ip;
	private String port;
	private int start = 1;
	private int end = 1;
	private int [] character = {0,0,0};
	private int stop;
	private MasterInterface master;

	public Client() {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int height = screenSize.height;
		int width = screenSize.width;
		setSize(width/2, height/2);
	    setLocationRelativeTo(null);
		masterInfoGui();
	}

	private void masterInfoGui() {
		setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
		
		info.setBorder(javax.swing.BorderFactory.createTitledBorder("Master info"));
		masterIP.setFont(new Font("Tahoma", Font.PLAIN, 14));
		masterPort.setFont(new Font("Tahoma", Font.PLAIN, 14));
		next.setText("Connect");
		next.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				nextActionPerformed(evt);
			}
		});
		
		
		javax.swing.GroupLayout infoLayout = new javax.swing.GroupLayout(info);
		info.setLayout(infoLayout);
		infoLayout.setHorizontalGroup(infoLayout
				.createSequentialGroup()
				.addGroup(infoLayout
						.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
						.addGroup(infoLayout
								.createSequentialGroup()
								.addComponent(masterIP)
								.addComponent(textMIP,javax.swing.GroupLayout.PREFERRED_SIZE, 100,
										javax.swing.GroupLayout.PREFERRED_SIZE)
								)
						.addGroup(infoLayout
								.createSequentialGroup()
								.addComponent(masterPort)
								.addComponent(textMP,javax.swing.GroupLayout.PREFERRED_SIZE, 100,
										javax.swing.GroupLayout.PREFERRED_SIZE)
								)
						)
				.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
				.addGroup(infoLayout
						.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER).addGap(0, 0, 100)
						.addComponent(next)
						)
				
				);
		
		infoLayout.setVerticalGroup(infoLayout
				.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
				.addGroup(infoLayout
						.createSequentialGroup()
						.addGroup(infoLayout
								.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
								.addComponent(masterIP)
								.addComponent(textMIP)
								)
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
						.addGroup(infoLayout
								.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
								.addComponent(masterPort)
								.addComponent(textMP)
								)
						)
				.addGroup(infoLayout
						.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
						.addComponent(next)
						)
				);
		
		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(layout.createSequentialGroup().addContainerGap().addComponent(info,
						javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addContainerGap()));
		layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(layout.createSequentialGroup().addContainerGap()
						.addComponent(info, javax.swing.GroupLayout.PREFERRED_SIZE,
								javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
						.addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
		pack();
		
	}

	private void clientGui() {
		this.setTitle("Unzipper");
		setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

		jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
		browse.setText("Browse");
		browse.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				FileChooserAction(evt);
			}
		});

		jTextField1.setEditable(false);
		jLabel1.setFont(new Font("Tahoma", Font.PLAIN, 14));
		jLabel2.setFont(new Font("Tahoma", Font.PLAIN, 13));
		jLabel3.setFont(new Font("Tahoma", Font.PLAIN, 13));
		jButton1.setFont(new Font("Tahoma", Font.PLAIN, 13));

		jButton1.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				try {
					bruteForceActionPerformed(evt);
				} catch (ZipException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		
		javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
		jPanel1.setLayout(jPanel1Layout);
		jPanel1Layout.setHorizontalGroup(jPanel1Layout
				.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(jPanel1Layout
						.createSequentialGroup()
						.addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE,
								Short.MAX_VALUE)
						.addComponent(jLabel1)
						.addPreferredGap(
								javax.swing.LayoutStyle.ComponentPlacement.RELATED)
						.addComponent(jTextField1,
								javax.swing.GroupLayout.PREFERRED_SIZE, 235,
								javax.swing.GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
						.addComponent(browse)
						)
				.addGroup(jPanel1Layout
						.createSequentialGroup()
						.addGroup(jPanel1Layout
								.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
								.addGroup(jPanel1Layout
										.createSequentialGroup()
										.addComponent(jLabel2)
										.addComponent(jSpinner2,javax.swing.GroupLayout.PREFERRED_SIZE, 50,
												javax.swing.GroupLayout.PREFERRED_SIZE)
										)
								.addGroup(jPanel1Layout
										.createSequentialGroup()
										.addComponent(jLabel3)
										.addComponent(jSpinner3,javax.swing.GroupLayout.PREFERRED_SIZE, 50,
												javax.swing.GroupLayout.PREFERRED_SIZE)
										)
								)
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
						.addGroup(jPanel1Layout
								.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING).addGap(0, 0, 100)
								.addComponent(jCheckBoxUpp)
								.addComponent(jCheckBoxLow)
								.addComponent(jCheckBoxNum)
								)
						)
				.addGroup(jPanel1Layout
						.createSequentialGroup()
						.addGap(0, 0, 147)
						.addComponent(jButton1)
						.addGap(0, 0, 10)
						.addComponent(load)
						)
				);
		
		jPanel1Layout.setVerticalGroup(jPanel1Layout
					.createSequentialGroup().addContainerGap()
					.addGroup(jPanel1Layout
							.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
							.addComponent(browse)
							.addComponent(jLabel1)
							.addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE,
									javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
							)
					.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
					
					
					.addGroup(jPanel1Layout
							.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
							.addGroup(jPanel1Layout
									.createSequentialGroup()
									.addGroup(jPanel1Layout
											.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
											.addComponent(jLabel2)
											.addComponent(jSpinner2)
											)
									.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
									.addGroup(jPanel1Layout
											.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
											.addComponent(jLabel3)
											.addComponent(jSpinner3)
											)
									)
							.addGroup(jPanel1Layout
									.createSequentialGroup()
									.addGap(0, 0, 5)
									.addComponent(jCheckBoxUpp)
									.addComponent(jCheckBoxLow)
									.addComponent(jCheckBoxNum)
									)
							
							)
					.addGap(10)
					.addGroup(jPanel1Layout
							.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
							.addGroup(jPanel1Layout
									.createSequentialGroup()
									.addGroup(jPanel1Layout
											.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
											.addComponent(jButton1)
											.addComponent(load)
											)
									)
							)
				);

		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(layout.createSequentialGroup().addContainerGap().addComponent(jPanel1,
						javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addContainerGap()));
		layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(layout.createSequentialGroup().addContainerGap()
						.addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE,
								javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
						.addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
		load.setVisible(false);
		pack();
	}
	
	private void nextActionPerformed(java.awt.event.ActionEvent evt) {
		if(textMIP.getText().isEmpty() || textMP.getText().isEmpty()) {
			JOptionPane.showMessageDialog(new JFrame(), "Riempire entrambi i campi!","Errore",JOptionPane.ERROR_MESSAGE);            
            return;
		}
		else{
			info.setVisible(false);
			ip = textMIP.getText();
			port = textMP.getText();
			clientGui();
		}
	}
	

	private void FileChooserAction(java.awt.event.ActionEvent evt) {
		fileChooser.setFileFilter(new FileNameExtensionFilter("Zip Files", "zip"));
		int returnVal = fileChooser.showOpenDialog(this);
		
		if (returnVal == fileChooser.APPROVE_OPTION) {
			File zipFile = fileChooser.getSelectedFile();
			jTextField1.setText(zipFile.getAbsolutePath());
		} else {
			System.out.println("File access cancelled by user.");
		}
	}
	

	private void bruteForceActionPerformed(java.awt.event.ActionEvent evt) throws ZipException, IOException {
		if(jButton1.getText().equals("BruteForce!")) {
			try {
				start = (int) jSpinner2.getValue();
				end = (int) jSpinner3.getValue();
				if (start > end) {
					JOptionPane.showMessageDialog(new JFrame(), "Lunghezza minima maggiore della massima", "Errore",
							JOptionPane.ERROR_MESSAGE);
					jButton1.setText("BruteForce!");
					return;
				}
				if (start < 1) {
					JOptionPane.showMessageDialog(new JFrame(), "Il valore minimo accettabile è 1", "Errore",
							JOptionPane.ERROR_MESSAGE);
					jButton1.setText("BruteForce!");
					return;
				}
				
			} catch (NumberFormatException e) {
				JOptionPane.showMessageDialog(new JFrame(), "Errore", "Errore",
						JOptionPane.ERROR_MESSAGE);
				return;
			}
	
			String filename = jTextField1.getText().toLowerCase();
			if (!filename.endsWith(".zip")) { // wrong file ext.
				JOptionPane.showMessageDialog(new JFrame(), "Il file deve essere in formato Zip", "Errore",
						JOptionPane.ERROR_MESSAGE);
				jButton1.setText("BruteForce!");
				return;
			}
			boolean flag = false;
			if(jCheckBoxLow.isSelected()) {
				character[0] = 1;
				flag = true;
			}
			if(jCheckBoxUpp.isSelected()) {
				character[1] = 1;
				flag = true;
			}
			if(jCheckBoxNum.isSelected()) {
				character[2] = 1;
				flag = true;
			}
			
			if (!flag) {					//se non viene selezionato nulla, di default si selezionano tutti i caratteri
				character[0] = 1;
				character[1] = 1;
				character[2] = 1;
			}
					
			Thread t = new Thread(new Runnable() {
				public void run() {
					connectToMaster(filename);
				}
			});
			t.start();
			load();
		}else if(jButton1.getText().equals("Stop!")) {
			jButton1.setText("BruteForce!");
			load.setVisible(false);
			master.stopAttack();
		}
	}
	
	private void load() {
		load.setVisible(true);
		jButton1.setText("Stop!");
		repaint();
	}
	
	private void connectToMaster(String filePath) {
		int port = Integer.parseInt(textMP.getText());
		String serviceName = "Master";
		String hostName = textMIP.getText();

		try {
			String completeName = "//" + hostName + ":" + port + "/" + serviceName;
			master = (MasterInterface)Naming.lookup(completeName);

			//SendFile
			File clientpathfile = new File(filePath);
			byte [] mydata=new byte[(int) clientpathfile.length()];
			FileInputStream in=new FileInputStream(clientpathfile);	
			System.out.println("uploading to master...");		
			in.read(mydata, 0, mydata.length);					 
		 	master.uploadFileToServer(mydata, (int) clientpathfile.length());
		 	in.close();
			
			char[] pass = master.forceZip(start, end, character);
			if(pass[0] == (char)64) {	//se password non trovata viene settato pass[0] = @
				String success = "nessuna password trovata!";
				jButton1.setText("BruteForce!");
				load.setVisible(false);
				JOptionPane.showMessageDialog(new JFrame(), success);
			}
			else {
				String success = "Trovata! La password è: " + new String(pass);
				jButton1.setText("BruteForce!");
				load.setVisible(false);
				JOptionPane.showMessageDialog(new JFrame(), success);
			}

		} catch (Exception e) {
			System.err.println("Client exception: ");
			e.printStackTrace(); 
		}
	}


}
