package test;

import java.rmi.Remote;

public interface RemoteVector extends Remote { /////

    public Vector getVector () throws java.rmi.RemoteException ;

    public void vecAddition ( RemoteVector rv ) throws java.rmi.RemoteException ;

}
//