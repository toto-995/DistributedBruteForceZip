package Master;

import java.rmi.RemoteException;

public class MasterLauncher {
	private static int port = 1099;
	private static String hostName = "localhost";

	public static void main(String[] args) throws RemoteException {
		try {
			javax.swing.UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");

		} catch (Exception e) {
			e.printStackTrace();
		} 
		Master ms = new Master(port, hostName);
		MasterGui masterGui = new MasterGui(ms);
		ms.setGui(masterGui);
		masterGui.setVisible(true);

	}

}
