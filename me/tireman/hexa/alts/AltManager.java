
package me.tireman.hexa.alts;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;


public class AltManager {
    public static Alt lastAlt;
    public static ArrayList<Alt> registry;
 
    static {
    	registry = new ArrayList<Alt>();
    }

    public ArrayList<Alt> getRegistry() {
        return registry;
    }

    public void setLastAlt(Alt alt2) {
        lastAlt = alt2;
    }
}

