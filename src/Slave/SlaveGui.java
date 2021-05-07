package Slave;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.text.DefaultCaret;


public class SlaveGui extends JFrame implements ActionListener {

	private JButton masterConnect = new JButton("Start");
	private JTextField masterIP = new JTextField("127.0.0.1");
	private JTextField masterPort = new JTextField("1099");
	private JTextArea screen = new JTextArea();
	private String pass = "Password: ";
	private JLabel tryPass = new JLabel(pass);
	private JLabel serverIPLabel = new JLabel("Master IP: ");
	private JLabel serverPortLabel = new JLabel("Master Port: ");
	private JPanel northPanel = new JPanel();
	private JPanel northSubPanel1 = new JPanel();
	private JPanel southPanel = new JPanel();
	private JScrollPane scroll;
	private Slave sl;

	public SlaveGui(Slave sl) {
		this.sl = sl;
		this.setTitle("Slave");
		this.setLayout(new BorderLayout());		
		this.setResizable(false);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int height = screenSize.height;
		int width = screenSize.width;
		setSize(width/2, height/2);
        setLocationRelativeTo(null);
		Gui();
		this.setVisible(true);
		this.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
	}

	
	void Gui() {
		masterConnect.setFont(new Font("Tahoma", Font.PLAIN, 14));
		tryPass.setFont(new Font("Tahoma", Font.PLAIN, 14));
		screen.setFont(new Font("Tahoma", Font.PLAIN, 14));
		masterIP.setFont(new Font("Tahoma", Font.PLAIN, 14));
		masterPort.setFont(new Font("Tahoma", Font.PLAIN, 14));
		serverIPLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
		serverPortLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
		masterConnect.addActionListener(this);
		masterIP.setColumns(10);
		masterPort.setColumns(5);
		screen.setEditable(false);
		screen.setLineWrap(true);
		DefaultCaret caret = (DefaultCaret)screen.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		scroll = new JScrollPane();
		scroll.setViewportView(screen);
		
		northSubPanel1.setLayout(new FlowLayout(FlowLayout.CENTER, 40, 5));
		northSubPanel1.add(serverIPLabel);
		northSubPanel1.add(masterIP);
		northSubPanel1.add(serverPortLabel);
		northSubPanel1.add(masterPort);
		northSubPanel1.add(masterConnect);
		northSubPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
		
		northPanel.setLayout(new BorderLayout());
		northPanel.add(northSubPanel1, BorderLayout.NORTH);

		southPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 80, 5));
		southPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
		southPanel.add(tryPass);
		
		this.add(northPanel, BorderLayout.NORTH);
		this.add(scroll, BorderLayout.CENTER);
		this.add(southPanel, BorderLayout.SOUTH);
	}
	
	void print(String text){
		String timeStamp = new SimpleDateFormat("hh:mm:ss dd-MM-yyyy").format(new Date());
		screen.append("[" + timeStamp + "] " + text + "\n");
	}
	
	void setPass(String range) {
		tryPass.setText(pass+range);
	}
	
	void setEnab(boolean b) {
		masterConnect.setEnabled(b);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource().equals(masterConnect)){
			if(masterConnect.getText().equals("Start")){
				print("Connessione al Master ...");
				boolean join = sl.joinMaster(Integer.parseInt(masterPort.getText()), masterIP.getText());
				masterConnect.setEnabled(!join);
				
			}
		}
	}

}
