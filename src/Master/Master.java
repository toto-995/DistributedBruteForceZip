package Master;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.Semaphore;
import javax.swing.filechooser.FileSystemView;
import Slave.SlaveInterface;

public class Master extends UnicastRemoteObject implements MasterInterface {
	private long allComb; // number of all combinations
	private int nSlave = 0;
	private int base; // number of characters array
	private MasterThread[] arrayThread;
	private SlaveInterface[] arraySlave = new SlaveInterface[50];		//setto limite iniziale di 50 slave
	private String arrayChar;
	private String lowerChar = "abcdefghijklmnopqrstuvwxyz"; // posizione 0
	private String upperChar = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"; // posizione 1
	private String numChar = "0123456789"; 					 // posizione 2
	private static MasterGui masterGui;
	private Semaphore sem;
	private char[] pass;
	private int found;
	private int port;
	private String hostName;
	private String masterPath;
	private boolean started = false;

	protected Master(int port, String host) throws RemoteException {
		super();
		this.port = port;
		this.hostName = host;
		LocateRegistry.createRegistry(port);
	}

	public char[] forceZip(int start, int end, int[] character) throws RemoteException {
		this.started = true;
		pass = new char[100];
		this.found = 0;
		arrayThread = new MasterThread[nSlave]; 
		createArrayChar(character);

		sem = new Semaphore(nSlave);
		try {
			sendFileToSlaves();
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		for (int i = start; i <= end; i++) {
			allComb = (long) Math.pow(base, i);
			masterGui.updateTotalComb((int)allComb, i);
			long range = allComb / nSlave;
			if (nSlave > 1) {
				// n-1 Slave
				for (int j = 0; j < nSlave-1; j++) {
					int[] arrayRange = arrayRange(j * range, i); // convert range in in array
					MasterThread slave = new MasterThread(this, arraySlave[j], arrayRange, arrayChar, range);
					arrayThread[j] = slave;
					slave.start();
				}
				
				// N-esimo Slave
				int[] arrayRangeN = arrayRange((nSlave - 1) * range, i);
				MasterThread slaveN = new MasterThread(this, arraySlave[nSlave-1], arrayRangeN, arrayChar, allComb-(nSlave-1)*range);
				arrayThread[nSlave-1] = slaveN;
				slaveN.start();

			} else {
				int[] arrayRange0 = arrayRange(0 * range, i);
				MasterThread slave0 = new MasterThread(this, arraySlave[0], arrayRange0, arrayChar, range);
				arrayThread[0] = slave0;
				slave0.start();
			}
			try {
				Thread.sleep(1000);
				this.sem.acquire(nSlave);
				this.sem.release(nSlave);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if(this.found == 1) {
				masterGui.print("Password trovata: "+new String(pass));
				break;
			}else if(this.found == 2) {
				masterGui.print("Attacco bloccato dal Client! ");
				break;
			}
			else masterGui.print("Nessuna password di lunghezza "+i+" trovata!");
		}
		System.out.println(pass);
		cleanAll();
		return pass;
	}


	@Override
	public boolean join(SlaveInterface slave) throws RemoteException {
		if(!started) {
			this.arraySlave[nSlave] = slave;
			nSlave++;
			masterGui.updateTotalNodes(nSlave);
			return true;
		}else {
			return false;
		}
	}
	
	
	public void start() {
		try {
			String serviceName = "Master";
			String completeName = "//" + hostName + ":" + port + "/" + serviceName;
			Naming.rebind(completeName, this);
			masterGui.print("Master avviato! IP : " + hostName + ", Port : " + port);
		} catch (Exception e) {
			System.err.println("Master exception: ");
			e.printStackTrace();
		}
	}

	public void stop() throws Exception {
		try {
			Naming.unbind("Master");
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Metodo che permette di effettuare il cambio base del "range".
	 * Se per esempio range = 60, e siamo in base 26 (con solo lettere minuscole)
	 * voglio la 60esima combinazione di 2 lettere ovvero "ci" 
	 * (la 26esima è "ba", la 27esima è "bb" la 28esima "ac" ecc), 
	 * @param range
	 * @param passLen
	 * @return arrayRange
	 */
	private int[] arrayRange(long range, int passLen) { 
		int remainder;
		long integ;
		int[] passIndex = new int[passLen];
		int cont = 0;
		remainder = (int) (range % base);
		integ = range / base;
		if(integ==0) {
			passIndex[passLen-1] = remainder;
		}
		while (integ > 0) {
			cont += 1;
			passIndex[passLen - cont] = remainder;
			passIndex[passLen - cont - 1] = (int) integ;	//se l'operazione di sotto (integ = integ / base;) mi restituisce 0, ho già riempito la posizione successiva con integ
			remainder = (int) (integ % base);
			integ = integ / base;
		}
		cont++;
		for (int j = 0; j < passLen - cont; j++) {
			passIndex[j] = 0;
		}
		for (int j = 0; j < passLen; j++) {
		}
		return passIndex;
	}

	/**
	 * Metodo utilizzato per riempire il vettore character con
	 * i valori corretti selezionati dal Client
	 * @param character
	 */
	private void createArrayChar(int[] character) {		
		int cont = 0;
		if (character[0] == 1) {
			cont += 26;
			arrayChar = lowerChar;
			if (character[1] == 1) {
				cont += 26;
				arrayChar += upperChar;
			}
			if (character[2] == 1) {
				cont += 10;
				arrayChar += numChar;
			}
		} else if (character[1] == 1) {
			cont += 26;
			arrayChar = upperChar;
			if (character[2] == 1) {
				cont += 10;
				arrayChar += numChar;
			}
		} else if (character[2] == 1) {
			cont += 10;
			arrayChar = numChar;
		}
		this.base = cont;

	}

	public void uploadFileToServer(byte[] mydata, int length) throws RemoteException {
		try {
			FileSystemView filesys = FileSystemView.getFileSystemView();
			String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
			new File(filesys.getHomeDirectory().getAbsolutePath()+"\\Master").mkdirs();
			masterPath = filesys.getHomeDirectory().getAbsolutePath()+"\\Master\\"+timeStamp+".zip";
			File serverpathfile = new File(masterPath);
			FileOutputStream out = new FileOutputStream(serverpathfile);
			byte[] data = mydata;

			out.write(data);
			out.flush();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void sendFileToSlaves() throws IOException  {
		File masterPathFile = new File(masterPath);
		byte [] mydata=new byte[(int) masterPathFile.length()];
		FileInputStream in=new FileInputStream(masterPathFile);	
		 in.read(mydata, 0, mydata.length);	
		 for(int i=0; i<nSlave; i++) {
				arraySlave[i].uploadFileToSlave(mydata, (int) masterPathFile.length());
		 }
		 in.close();
	}

	public void acquireSem() {			
		try {
			this.sem.acquire(1);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void releaseSem() {
		this.sem.release(1);
	}

	public void killAll(char[] pass) {
		this.pass = pass;
		if(pass[0] == (char)64) {
			this.found = 2;
		}else {
			this.found = 1;
		}
		this.sem.release();
		for(int i = 0; i< nSlave; i++) {
			if(arrayThread[i] != null) {
				arrayThread[i].closeSlave();
			}
		}
	}
	
	public void stopAttack() throws RemoteException{
		char[] c = new char[1];
		c[0] = (char)64;			//inserisco @ ovvero il simbolo che fa capire di non aver trovato la password
		killAll(c);
	}
	
	public void setGui(MasterGui gui) {
		this.masterGui = gui;
	}
	
	public void cleanAll() {
		this.nSlave = 0;
		this.arraySlave =  new SlaveInterface[40];
		this.started = false;
	}

}
