package de.unistgt.ipvs.vs.ex1.client;

import java.awt.*;
import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Implement the connectTo-, disconnect-, and calculate-method of this class
 * as necessary to complete the assignment. You may also add some fields or methods.
 */
public class CalcSocketClient {
	private Socket cliSocket;
	private int    rcvdOKs;		// --> Number of valid message contents
	private int    rcvdErs;		// --> Number of invalid message contents
	private int    calcRes;		// --> Calculation result (cf.  'RES')
	private ObjectOutputStream oosOut;
	private ObjectInputStream oisIn;


	public CalcSocketClient() {
		this.cliSocket = null;
		this.rcvdOKs   = 0;
		this.rcvdErs   = 0;
		this.calcRes   = 0;
	}
	
	public int getRcvdOKs() {
		return rcvdOKs;
	}

	public int getRcvdErs() {
		return rcvdErs;
	}

	public int getCalcRes() {
		return calcRes;
	}


	public boolean connectTo(String srvIP, int srvPort) {
               
		//Solution here
		try {
			cliSocket = new Socket(srvIP,srvPort);
			System.out.println("- Client: Connect to Server" + srvIP + " Port:" + srvPort);
			System.out.println("- Client: Server Address: " + cliSocket.getRemoteSocketAddress());

			oosOut = new ObjectOutputStream(cliSocket.getOutputStream());
			oosOut.writeObject("- Client: Hello from " + cliSocket.getLocalSocketAddress());

			oisIn = new ObjectInputStream(cliSocket.getInputStream());
			System.out.println("- Client: Server responses --> " +oisIn.readObject() + "\n");

		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}

		return true;
	}

	public boolean disconnect() {
               
	    //Solution here
		try {
			oosOut.close();
			oisIn.close();
			cliSocket.close();
			System.out.println("- Client: Disconnected\n");
		} catch (IOException e) {
			e.printStackTrace();
		}

		return true;
	}

	public boolean calculate(String request) {
               
		if (cliSocket == null) {
			System.err.println("Client not connected!");
			return false;
		}
		
		//Solution here
		try {
			// Send the message and receive the reaction from server.
			oosOut.writeObject(request);
			System.out.println("- Client: Request sent ---> " + request);
			System.out.println("- Client: Server responses ---> " + oisIn.readObject());
			System.out.println("- Client: Server responses ---> " + oisIn.readObject());
			System.out.println("- Client: Server responses ---> " + oisIn.readObject());

			// Get the result and counters for valid and invalid contents, and save these value.
			System.out.println("- Client: Receive from server ---> result = " + (calcRes = (int) oisIn.readObject()));
			System.out.println("- Client: Receive from server ---> rcvdOks = " + (rcvdOKs = (int) oisIn.readObject()));
			System.out.println("- Client: Receive from server ---> rcvdErs = " + (rcvdErs = (int) oisIn.readObject()) + "\n");
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		return true;
	}
}
