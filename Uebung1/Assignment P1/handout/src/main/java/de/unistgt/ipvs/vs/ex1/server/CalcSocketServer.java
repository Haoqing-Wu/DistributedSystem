package de.unistgt.ipvs.vs.ex1.server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Extend the run-method of this class as necessary to complete the assignment.
 * You may also add some fields, methods, or further classes.
 */
public class CalcSocketServer extends Thread {
	private ServerSocket srvSocket;
	private int port;
	private ObjectInputStream oisIn;
	private ObjectOutputStream oosOut;

	public CalcSocketServer(int port) {
		this.srvSocket = null;
		
		this.port = port;
		System.out.println();
	}
	
	@Override
	public void interrupt() {
		try {
			if (srvSocket != null) srvSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
           
		if (port <= 0) {
			System.err.println("Wrong number of arguments.\nUsage: SocketServer <listenPort>\n");
			System.exit(-1);
		}

		// Start listening server socket ..
		try {
			// Create a server socket with port number.
			srvSocket = new ServerSocket(port);
			System.out.println("- Server: Wait for Remote Connection, Port: " + srvSocket.getLocalPort() + "...");

			// Waiting for connection.
			Socket server = srvSocket.accept();
			System.out.println("- Server: Remote client Address: " + server.getRemoteSocketAddress());

			// Create inputStream
			oisIn = new ObjectInputStream(server.getInputStream());
			System.out.println(oisIn.readObject());

			// Ready to receive the message.
			oosOut = new ObjectOutputStream(server.getOutputStream());
			oosOut.writeObject("<08:RDY>");

			CalculationSession cSess = new CalculationSession();

			while(true){

				try{
					String strIn = oisIn.readObject().toString();
					oosOut.writeObject("<07:OK>");
					cSess.setRequest(strIn);

					// Invoke the processing and calculation method.
					cSess.run();

					// Response to the Client
					oosOut.writeObject(cSess.getAckMessage());
					oosOut.writeObject("<08:FIN>");

					// Get the result and counters from remote method, and send to client.
					oosOut.writeObject(cSess.getCalResult());
					oosOut.writeObject(cSess.getCounterOk());
					oosOut.writeObject(cSess.getCounterErr());
				} catch (EOFException e){
					break;
				}
			}
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}

	}
        
        public void waitUnitlRunnig(){
            while(this.srvSocket == null){
                try {
                    Thread.sleep(1);
                } catch (InterruptedException ex) {
                }
            }
        }
}