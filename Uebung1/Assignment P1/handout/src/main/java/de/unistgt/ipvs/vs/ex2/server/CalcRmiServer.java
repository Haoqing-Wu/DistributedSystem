package de.unistgt.ipvs.vs.ex2.server;

import de.unistgt.ipvs.vs.ex2.common.ICalculationFactory;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Implement the run-method of this class to complete
 * the assignment. You may also add some fields or methods.
 */
public class CalcRmiServer extends Thread {
	private String regHost;
	private String objName;
    private Registry reg;
	        String url; //Please use this variable to bind the object.

	
	public CalcRmiServer(String regHost, String objName) {
		this.regHost = regHost;
		this.objName = objName;
                this.url = "rmi://" + regHost + "/" + objName;
	}


	@Override
	public void run() {
		if (regHost == null || objName == null) {
			System.err.println("<registryHost> or <objectName> not set!");
			return;
		}
                //Add solution here
        try {
            ICalculationFactory iCalFac = new CalculationImplFactory();
            // In default setting, RMI start from port 1099
            reg = LocateRegistry.createRegistry(1099);
            Naming.rebind(this.url, iCalFac);
            System.out.println("\n" + "- Server: Server is running...");
        } catch (Exception e) {
            System.out.println("- Server: Server startup failed!");
            e.printStackTrace();
        }
	}

        public void stopServer(){
            try {
                Naming.unbind(url);
                // I don't really understand here the exception: Connection refused to host
                UnicastRemoteObject.unexportObject(reg,true);
                System.out.println("\n- Server: Disconnected");
            } catch (RemoteException ex) {
                Logger.getLogger(CalcRmiServer.class.getName()).log(Level.SEVERE, null, ex);
            } catch (NotBoundException ex) {
                Logger.getLogger(CalcRmiServer.class.getName()).log(Level.SEVERE, null, ex);
            } catch (MalformedURLException ex) {
                Logger.getLogger(CalcRmiServer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
}
