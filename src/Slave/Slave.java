package Slave;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.filechooser.FileSystemView;
import Master.MasterInterface;
import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.exception.ZipException;

public class Slave implements SlaveInterface {
	private static SlaveGui slaveGui;
	private long n;
	private int unzipped;
	private String slavePath;

	public Slave(int port) throws RemoteException {
		super();
		UnicastRemoteObject.exportObject(this, port);
	}

	
	@Override
	public char[] findPassword(String arrayChar, int[] arrayRange, long range) throws RemoteException {
		unzipped = 0;
		n = 0;
		int maxIndex = arrayChar.length();
		char[] pass = new char[arrayRange.length];
		char[] pass0 = convertPass(arrayChar, arrayRange);
		FileSystemView filesys = FileSystemView.getFileSystemView();
		String folderName = filesys.getHomeDirectory().getAbsolutePath();
		
		while (unzipped == 0 && n < range) {
			pass = convertPass(arrayChar, arrayRange);
			int last = arrayRange.length - 1;
			slaveGui.setPass(new String(pass));
			try {
				ZipFile zipfile = new ZipFile(slavePath);
				if (zipfile.isEncrypted()) {
					zipfile.setPassword(pass);
				}
				zipfile.extractAll(folderName);
				unzipped = 1;
				break;
			} catch (ZipException e) {
				int next=2;
				while (arrayRange[0] <= (maxIndex - 1)) {
					if(arrayRange[last] == (maxIndex-1)) {
						if(last==0) {
							next=0;
							break;
						}
						last--;
					}
					if (arrayRange[last] < maxIndex - 1) {
						arrayRange[last] += 1;
						n++; 
						for (int i = last + 1; i < arrayRange.length; i++) {
							arrayRange[i] = 0;
						}
						next = 1;
						break;
					}
					next=0;
				}
				
				
				if (next == 0) { // run out of possibilities
					break ;
				}
				continue;
			}
		}
		if (unzipped == 0) { // se non trovata, setto pass[0] con valore impossibile
			slaveGui.print("Nessuna password trovata nel range: "+new String(pass0)+" / "+new String(pass));
			pass[0] = (char) 64;
		}else if(unzipped ==1){
			slaveGui.print("Password trovata: "+new String(pass));
		}
		return pass;
	}

	private char[] convertPass(String arrayChar, int[] arrayRange) {
		char[] result = new char[arrayRange.length];
		for (int i = 0; i < arrayRange.length; i++) {
			result[i] = arrayChar.charAt(arrayRange[i]);
		}
		return result;
	}

	public boolean joinMaster(int port, String host) {
		boolean join = false;
		try {
			String masterName = "Master";
			String completeName = "//" + host + ":" + port + "/" + masterName;
			MasterInterface master = (MasterInterface) Naming.lookup(completeName);
			join = master.join(this);
			if(join) {
				slaveGui.print("Connessione riuscita! IP : " + host + ", Port : " + port);
			}else {
				slaveGui.print("Connessione non riuscita, attacco già iniziato!");
			}
		} catch (Exception e) {
			slaveGui.print("Errore: Master non trovato!");
			slaveGui.setEnab(true);
		}
		return join;
	}

	@Override
	public void uploadFileToSlave(byte[] mydata, int length) throws RemoteException {
		try {
			FileSystemView filesys = FileSystemView.getFileSystemView();
			String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
			new File(filesys.getHomeDirectory().getAbsolutePath()+"\\Slave").mkdirs();
			slavePath = filesys.getHomeDirectory().getAbsolutePath()+"\\Slave\\"+timeStamp+".zip";
			File slavepathfile = new File(slavePath);
			FileOutputStream out = new FileOutputStream(slavepathfile);
			byte[] data = mydata;
			out.write(data);
			out.flush();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void setGui(SlaveGui gui) {
		this.slaveGui = gui;
	}
	
	public void setN(long n) {
		this.n = n;
		this.slaveGui.setEnab(true);
	}
	
}
