package com.elezeta.jarspliceplus;

import java.util.ArrayList;

import org.ninjacave.jarsplice.core.Splicer;
import org.ninjacave.jarsplice.gui.JarSpliceFrame;

public class JarSplicePlusLauncher {
	public static void main(String args[]) {
		// display help
		if (System.getProperty("help") != null) {
			System.out.println("JarSplicePlus - An Extension to JarSplice");
			System.out.println("Copyright (c) 2013, Luis Quesada - https://github.com/lquesada");
			System.out.println();
			System.out.println("Call without properties to open the GUI.");
			System.out.println();
			System.out.println("Usage: java -jar -D[property]=[value] JarSplicePlus.jar");
			System.out.println("Properties:");
			System.out.println("\tinput   --> input JAR file list");
			System.out.println("\tnatives --> input native file list");
			System.out.println("\tmain    --> main class");
			System.out.println("\tparams  --> JVM parameters");
			System.out.println("\toutput  --> output JAR file");
			System.out.println();
			System.out.println("Separate file lists with the : (colon) character.");
			return;
		}

		// retrieve properties
		String
			sInputJars    = System.getProperty("input"),
			sInputNatives = System.getProperty("natives"),
			sParams       = System.getProperty("params"),
			sMain         = System.getProperty("main"),
			sOutput       = System.getProperty("output");

		// no arguments: launch JarSplice
		if (sInputJars == null && sInputNatives == null && sParams == null && sMain == null && sOutput == null) {
		    new JarSpliceFrame();
		    return;
		}

		// check for required properties
		if (sInputJars == null || sMain == null || sOutput == null) {
			System.err.println("Missing required properties. Run with option -Dhelp for example usage.");
			System.exit(1);
		}

		// input JARs
		ArrayList<String> inputJars = new ArrayList<String>();
		for (String s : sInputJars.split(":"))
			inputJars.add(s);
		if (inputJars.size() == 0) {
			System.err.println("No input JAR files.");
			System.exit(1);
		}

		// input natives
		ArrayList<String> inputNatives = new ArrayList<String>();
		if (sInputNatives != null) {
			for (String s : sInputNatives.split(":"))
				inputNatives.add(s);
		}

		// main class
		String mainClass = sMain;
		if (mainClass.isEmpty()) {
			System.err.println("No main class.");
			System.exit(1);
		}

		// JVM parameters
		String parameters = (sParams == null) ? "" : sParams;

		// output JAR
		String output = sOutput;
		if (output.isEmpty()) {
			System.err.println("No output file.");
			return;
		}

		// invoke JarSplice
		try {
			new Splicer().createFatJar(
				inputJars.toArray(new String[0]),
				inputNatives.toArray(new String[0]),
				output,
				mainClass,
				parameters
			);
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println();
			System.err.println("Error while building output JAR file.");
			System.exit(1);
		}

		System.out.printf("Output JAR file %s built successfully.\n", output);
		return;
	}
}
