package test;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Server extends java.rmi.server.UnicastRemoteObject implements RemoteVector {
    private Vector myVector ;

    public Server ( int a , int b , int c ) throws java.rmi.RemoteException {
        super ();
        this . myVector = new Vector (a, b, c);
    }

    public Vector getVector () throws java.rmi.RemoteException {

        return this.myVector;

    }

    public void vecAddition ( RemoteVector rv ) throws java.rmi.RemoteException {
        this . myVector = addition ( this . myVector , rv . getVector ());
    }

    public Vector addition ( Vector v , Vector b ){
        for (int i =0; i < v.values.size(); i ++) {
             b.values.set(i, v.values.get(i) + b.values.get(i));
        }
        return b ;
    }

    public static void main ( String [] args ) throws Exception {
        Server serverVector1 = new Server (4 ,5 ,6);
        Server serverVector2 = new Server (1 ,2 ,3);
        LocateRegistry.createRegistry(1099);
        java . rmi . Naming.rebind ("rmi://localhost/v1", serverVector1); ///
        java . rmi . Naming.rebind ("rmi://localhost/v2", serverVector2);
    }
}
