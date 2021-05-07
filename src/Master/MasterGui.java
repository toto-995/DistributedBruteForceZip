package Master;

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
import javax.swing.text.DefaultCaret;


public class MasterGui  extends JFrame implements ActionListener{
	private JButton start = new JButton("Start Master");
	public JTextArea screen = new JTextArea();
	
	private JLabel totalNodesLabel = new JLabel("Slave connessi: 0");
	private JLabel totalCombLabel = new JLabel("Combinazioni per 0 cifre:  0");
	
	private JPanel panel = new JPanel();
	private JScrollPane scroll;
	private Master ms;
	
	public MasterGui(Master ms) {
		this.ms = ms;
		this.setTitle("Master");
		this.setLayout(new BorderLayout());		
		this.setResizable(false);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int height = screenSize.height;
		int width = screenSize.width;
		setSize(width/2, height/2);
        setLocationRelativeTo(null);
		
		this.setVisible(true);
		this.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
		Gui();
		
	}
	
	
	private void Gui() {
		panel.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
		start.setFont(new Font("Tahoma", Font.PLAIN, 14));
		totalCombLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
		totalNodesLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
		screen.setFont(new Font("Tahoma", Font.PLAIN, 14));
		
		start.setSize(70, 40);
		start.addActionListener(this);
		
		screen.setEditable(false);
		screen.setLineWrap(true);
		DefaultCaret caret = (DefaultCaret)screen.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		scroll = new JScrollPane();
		scroll.setViewportView(screen);
		
		panel.setLayout(new FlowLayout(FlowLayout.CENTER, 80, 5));
		panel.add(totalNodesLabel);
		panel.add(totalCombLabel);
		panel.add(start);
		this.add(panel, BorderLayout.NORTH);
		this.add(scroll, BorderLayout.CENTER);
	}

	void updateTotalNodes(int nSlave){
		this.totalNodesLabel.setText("Slave connessi: " + nSlave);
	}
	
	void updateTotalComb(int comb, int l){
		this.totalCombLabel.setText("Combinazioni per "+l+" cifre: " + comb);
	}
	
	void serverStarted(){
		start.setText("Stop Master");
	}
	
	void serverStoped(){
		start.setText("Start Master");
	}
	
	void print(String text){
		String timeStamp = new SimpleDateFormat("hh:mm:ss dd-MM-yyyy").format(new Date());
		screen.append("[" + timeStamp + "] " + text + "\n");
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(start.getText().equals("Start Master")){
			print("Avvio del Master ...");
			serverStarted();
			ms.start();
		}
		else if(start.getText().equals("Stop Master")){
			print("Stoping Server Thread!");
			serverStoped();
			try {
				ms.stop();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
	}

}
