package de.unistgt.ipvs.vs.ex2.client;

import de.unistgt.ipvs.vs.ex2.common.ICalculation;
import de.unistgt.ipvs.vs.ex2.common.ICalculationFactory;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Collection;


/**
 * Implement the getCalcRes-, init-, and calculate-method of this class as
 * necessary to complete the assignment. You may also add some fields or methods.
 */
public class CalcRmiClient {
	private ICalculation calc = null;

	public CalcRmiClient() {
		this.calc = null;
	}

	/**
	 * @discription Get the result from calculation if no exception
	 * @return result int
	 */
	public int getCalcRes() {
		try {
			System.out.println("- Client: The result is ---> " + this.calc.getResult());
			return this.calc.getResult();
		} catch (RemoteException e) {
			e.printStackTrace();
			return -1;
		}
	}


	/**
	 * @discription Initialize the RMI connection from client side
	 * @param url String
	 * @return true when find the server, false when throws exceptions
	 */
	public boolean init(String url) {
		try {
			// Look up the registry to search the URL
			ICalculationFactory iCalFac = (ICalculationFactory) Naming.lookup(url);
			System.out.println("- Client: Find server ---> " + url);

			// Create a new session to serve this individual calculation.
			// Return the implement from ICalculation
			this.calc = iCalFac.getSession();
			return true;

		} catch (NotBoundException | RemoteException | MalformedURLException e) {
			e.printStackTrace();
			System.out.println("- Client: Cannot find server. ");
			return false;
		}
	}

	/**
	 * @discription Use RMI to calculate based on the given operator and operands
	 * @param calcMode Enum
	 * @param numbers  Collection <Integer>
	 * @return true when no remote exception
	 */
	public boolean calculate(CalculationMode calcMode, Collection<Integer> numbers) {
		try {
			Object[] numArray = numbers.toArray();
			System.out.println("\n- Client: Start calculation by using RMI ");
			System.out.println("- Client: CalcMode is ---> " + calcMode);
			System.out.println("- Client: Operands are ---> " + numbers);
				switch (calcMode){
					case ADD:
						for (Object o : numArray) { calc.add((int) o); }
						break;
					case SUB:
						for (Object o : numArray) { calc.subtract((int) o); }
						break;
					case MUL:
						for (Object o : numArray) { calc.multiply((int) o); }
						break;
					default:
						System.out.println("- Client: Invalid calculation mode.");
						break;
			  	}
			return true;

		} catch (RemoteException e) {
			e.printStackTrace();
			return false;
		}
	}
}
