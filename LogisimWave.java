//************************************************************
//************************************************************
//  Logisim Waveform Viewer
//  Copyright (c) 2017 by kai.dorau@gmx.net
//  ALL RIGHTS RESERVED
//************************************************************
//************************************************************
// Date: 02.2017                  Coded by: Kai Dorau
// Filename: LogisimWave.java     Module name: LogisimWave
//                                Source file: LogisimWave.java
// Modul description:
/*
 * - see Compile.java
 */
//
//************************************************************
// Libraries and software support: -
//
//************************************************************
// Development environment: NetBeans 8.2
//
//************************************************************
// System requirements: Java SDK 7.x
//
//************************************************************
// Start date: 01.02.2017
// Update history:
//            DATE            MODIFICATION
//	01.02.2017	      nop command inserted
//
//************************************************************
// Test history:
//  TEST PROTOCOL           DATE       TEST RESULTS
//
//************************************************************
// Programmer comments:
//
//************************************************************

package logisimwave;

/**
 *
 * @author kaila
 */
public class LogisimWave {

  static String outfile = "gnuplot.dat";
  static String scriptfile = "lw.plt";
  
  /**
   * @param args the command line arguments
   */
  public static void main(String[] args) {
    // TODO code application logic here
    char option;
    
    if (2 == args.length) {
      if (args[0].trim().charAt(0) == '-') {
	option = args[0].trim().charAt(1);
	Compile comp = new Compile(option, args[1].trim(),outfile, scriptfile);
	if (comp.readDataIn()) {
	  if (comp.writeDataOut()) {
	    if (!comp.writeScriptOut()) {
	      System.err.println("Write Script File Error");
	    }
	  } else {
	    System.err.println("Write Data File Error");
	  }
	} else {
	  System.err.println("Read Data File Error");
	}
      }
    } else {
      System.err.println("LogisimWave <-p/m> <logfile>");
      System.err.println("-p: png 16:9 output");
      System.err.println("-m: monitor output");
    }
    return;
  }
  
}
