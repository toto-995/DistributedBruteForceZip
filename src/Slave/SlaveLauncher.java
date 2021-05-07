package Slave;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

import javax.swing.filechooser.FileSystemView;

public class SlaveLauncher {
//	private static int port = 1095;
	public static void main(String[] args) throws RemoteException, FileNotFoundException {
		//ho bisogno di un file con porta 1098 per lo slave che si decremente ad ogni avvio 
		//per non avere conflitto di porte per i vari Slave
		FileSystemView filesys = FileSystemView.getFileSystemView();
	    String pathFile = filesys.getHomeDirectory().getAbsolutePath()+"\\portFile.txt";
		int port = 1098;
		try {
			File f = new File(pathFile);
			if(!f.exists()) {
				f.createNewFile();
				FileWriter fstreamWrite = new FileWriter(pathFile);
				fstreamWrite.write("1098");
				fstreamWrite.flush();
				fstreamWrite.close();
				
			}
			//lettura da file
			BufferedReader br = new BufferedReader(new FileReader(pathFile));
			String line = br.readLine();
			port = Integer.parseInt(line);
			br.close();
			
			//scrittura su fule per aggiornare la porta
			FileWriter fstreamWrite = new FileWriter(pathFile);
			fstreamWrite.write(String.valueOf(port-1));
			fstreamWrite.flush();
			fstreamWrite.close();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
	    }
		
		Slave slave = new Slave(port);
		try {
			javax.swing.UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");

		} catch (Exception e) {
			e.printStackTrace();
		} 
		SlaveGui slaveGui = new SlaveGui(slave);
		slave.setGui(slaveGui);
		slaveGui.setVisible(true);

	}

}
