package test;

import java.rmi.Naming;


public class Client {
    public static void main ( String [] args ) throws Exception {
         // Access the remote objects

         RemoteVector rb1 = (RemoteVector) java.rmi.Naming.lookup ("rmi://localhost/v1");////
         RemoteVector rb2 = (RemoteVector) java.rmi.Naming.lookup ("rmi://localhost/v2");////

         rb1.vecAddition (rb2);
         System.out.println ("1: " + rb1.getVector ());
         System.out.println ("2: " + rb2.getVector ());
         rb2.vecAddition ( rb1 );
         System.out.println ("3: " + rb2.getVector ());
         }

}
