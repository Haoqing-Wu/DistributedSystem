package test;

//import java.util.List;

import java.util.ArrayList;
import java.util.Arrays;

public class Vector implements java.io.Serializable {

    java.util.List < Integer > values = new java.util.ArrayList < Integer >();

    public Vector (int x, int y, int z){
        values.add(x);////
        values.add(y);////
        values.add(z);////


    }
    public String toString (){
         return this.values.toString();
    }

}
