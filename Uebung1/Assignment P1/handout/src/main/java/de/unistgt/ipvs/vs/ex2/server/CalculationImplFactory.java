package de.unistgt.ipvs.vs.ex2.server;


import de.unistgt.ipvs.vs.ex2.common.ICalculation;
import de.unistgt.ipvs.vs.ex2.common.ICalculationFactory;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * Change this class (implementation/signature/...) as necessary to complete the assignment.
 * You may also add some fields or methods.
 */

 public class CalculationImplFactory extends UnicastRemoteObject implements ICalculationFactory {
 	private static final long serialVersionUID = 8409100566761383094L;

 	private ICalculation ical;

    public CalculationImplFactory() throws RemoteException{

        this.ical = new CalculationImpl();
    }

    @Override
    public ICalculation getSession() throws RemoteException {
        System.out.println("- Server: New session created.");
        return this.ical;
    }
}
