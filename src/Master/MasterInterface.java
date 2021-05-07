package Master;

import java.rmi.Remote;
import java.rmi.RemoteException;
import Slave.SlaveInterface;

public interface MasterInterface extends Remote {
	char[] forceZip(int start, int end, int[] character) throws RemoteException;
	boolean join(SlaveInterface server) throws RemoteException;
	void uploadFileToServer(byte[] mydata, int length) throws RemoteException;
	void stopAttack() throws RemoteException;
}