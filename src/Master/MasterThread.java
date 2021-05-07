package Master;

import java.rmi.RemoteException;
import Slave.SlaveInterface;


public class MasterThread extends Thread{
	private int[] arrayRange;
	private long range;
	private String arrayChar;
	private Master ms;
	private SlaveInterface slave;

	public MasterThread(Master ms, SlaveInterface slave, int[] arrayRange, String arrayChar, long range) {
		this.slave = slave;
		this.arrayRange = arrayRange;
		this.range = range;
		this.arrayChar = arrayChar;
		this.ms = ms;
	}
	
	public void run() {
		char[] pass2;
		try {
			ms.acquireSem();
			 pass2 = slave.findPassword(arrayChar, arrayRange, this.range); 
			 if(pass2[0]== (char)64) {
				 ms.releaseSem();
			 }else{
				 ms.killAll(pass2);
			 }
		} catch (Exception e) {
			System.err.println("Master exception: ");
			e.printStackTrace();
		} 
	}

	public void closeSlave() {
		try {
			slave.setN(range);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

}
