package de.unistgt.ipvs.vs.ex1.server;

import de.unistgt.ipvs.vs.ex1.common.ICalculation;
import java.rmi.RemoteException;
import java.util.regex.Pattern;


/**
 * Add fields and methods to this class as necessary to fulfill the assignment.
 */
public class CalculationSession implements Runnable {

    private String request;
    private String ackMessage;
    private String operator;
    private int    operand;
    private int    counterOk;
    private int    counterErr;
    private int    calResult;

    /**
     * Constructor for CalculationSession
     */
    public CalculationSession() {
        request    = null;
        ackMessage = null;
        operator   = null;
        operand    = 0;
        counterOk  = 0;
        counterErr = 0;
        calResult  = 0;
    }

    /**
     * Setter and Getter
     */
    public void setRequest(String request) { this.request = request; }

    public String getAckMessage() { return ackMessage; }

    public int getCounterOk() { return counterOk; }

    public int getCounterErr() { return counterErr; }

    public int getCalResult() { return calResult; }



    /**
     * @discription Process the message to a standard form.
     * @author Haoqing Wu
     * @date   29.11.2020
     * @param  request  String
     * @return StrArray String[]
     */
    public String[] messageProcessing(String request){

        String validString = request.substring(request.indexOf("<")+1, request.lastIndexOf(">"));
        validString = validString.toUpperCase();
        validString = validString.replaceAll(":"," ");
        validString = validString.replaceAll("\\s+", " ");
        System.out.println("- Server: validString is ---> " + validString);
        String[] strArray = validString.split(" ");
        System.out.println("- Server: Given message length is ---> " + strArray[0]);
        return strArray;
    }



    /**
     * @discription Calculate the elements from the array
     *              From   [strArray]
     *              update [calResult, ackMessage, counterOk, CounterErr]
     * @author Haoqing Wu
     * @date   29.11.2020
     * @param  strArray String[]
     * @link   CalculationImpl impl ICalculation
     * @throws RemoteException
     */
    public void calculationFromArray(String[] strArray) throws RemoteException {

        // Create a local object to store the acknowledgment back to client.
        StringBuilder message = new StringBuilder();

        // Implement the interface Icalculation.
        ICalculation calcImpl = new CalculationImpl();

        // Load the result from last calculation.
        calcImpl.add(calResult);

        for(int i=1; i<strArray.length; i++){

            // Judge if this element is a number and convert to integer.
            if((Pattern.matches("-?[0-9]+",strArray[i]))){
                operand = Integer.parseInt(strArray[i]);
                message.append("OK ").append(operand).append(" ");
                counterOk++;
            }
            // Judge if this element is a valid operator or invalid content, and continue the loop.
            else {
                switch (strArray[i]){
                    case "ADD":
                        operator = "ADD";
                        message.append("OK ADD ");
                        counterOk++;
                        continue;
                    case "SUB":
                        operator = "SUB";
                        message.append("OK SUB ");
                        counterOk++;
                        continue;
                    case "MUL":
                        operator = "MUL";
                        message.append("OK MUL ");
                        counterOk++;
                        continue;
                    case "RES":
                        operator = "RES";
                        message.append("OK RES ").append(calcImpl.getResult()).append(" ");
                        counterOk++;
                        continue;
                    default: // Invalid content
                        counterErr++;
                        message.append("ERR ").append(strArray[i]).append(" ");
                        continue;
                }
            }

            // According to the current operator, invoke the remote method to calculate.
            switch (operator){
                case "ADD":
                    calcImpl.add(operand);
                    break;
                case "SUB":
                    calcImpl.subtract(operand);
                    break;
                case "MUL":
                    calcImpl.multiply(operand);
                    break;
                case "RES":
                    calResult = calcImpl.getResult();
                    break;
                default:
            }
        }

        // Save the result for next calculation.
        calResult = calcImpl.getResult();
        ackMessage = "<" + (message.toString().length() + 5) + ":" + message.toString() + ">";

    }



    /**
     * @discription Remote message processing and calculation main method
     * @author Haoqing Wu
     * @date   29.11.2020
     * @link   regex.Pattern
     */
    public void run() {

        try {
            // Use regex to identify the request String, if it's a valid string.
            boolean validFlag = Pattern.matches(".*<\\d{2}:.+>.*",request);

            if(validFlag){

                // If validFlag is true, means the length position is correct, ++counter.
                counterOk++;
                System.out.println("- Server: Valid message from client ---> " + request);

                // Invoke the method to processing the request into a array.
                String[] strArray = messageProcessing(request);

                // Start calculation.
                calculationFromArray(strArray);
            }
            else {
                System.out.println("- Server: Invalid message from client ---> " + request);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }

    }

}