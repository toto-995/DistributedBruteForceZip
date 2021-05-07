package Slave;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface SlaveInterface extends Remote {
	char[] findPassword(String arrayChar, int[] arrayRange, long range) throws RemoteException;
	void uploadFileToSlave(byte[] mydata, int length) throws RemoteException;
	void setN(long n) throws RemoteException;
}