//************************************************************
//************************************************************
//  Logisim Waveform Viewer
//  Copyright (c) 2017 by kai.dorau@gmx.net
//  ALL RIGHTS RESERVED
//************************************************************
//************************************************************
// Date: 02.2017                  Coded by: Kai Dorau
// Filename: Compile.java         Module name: LogisimWave
//                                Source file: Compile.java
// Modul description:
/*
 * - Scanning the origin data file generated by Logisim
 * - Writing a new data file produced from the origin data
 *   file for gnuplot
 * - Creating a gnuplot script which can be executed
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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author kaila
 */
public class Compile {
  
  private String infile = null;
  private String outfile = null;
  private String scriptfile = null;
  private char option;
  private final String delimiter = "\t";
  private int col = 0;
  private int row = 0;
  ArrayList<String> header = new ArrayList<String>();
  List<List<Integer>> listOfLists = new ArrayList<List<Integer>>();  
  
  
  /**
   * Constructor
   */
  public Compile(char option,
	  String infile,
	  String outfile, 
	  String scriptfile) {
    this.option = option;
    this.infile = infile;
    this.outfile = outfile;
    this.scriptfile = scriptfile;
  }
  
  /**
   * readDataIn
   * @return 
   */
  public boolean readDataIn() {
    boolean head = true;
    boolean no_header;
    
    if (infile != null) {
      try {
	BufferedReader br = new BufferedReader(new FileReader(infile));
	String line;
	while ((line = br.readLine()) != null) {
	  line = line.trim();
	  String[] values = line.split(delimiter);
	  if (values.length != 0) {
	    
	    // validate the data line
	    no_header = false;
 	    if (head) {
	      if (checkLine(line)) {
		// no header 
		no_header = true;
		System.out.println ("  WARNING: No header available, build one...");
	      }
	    } else {
	      if (!checkLine(line)) {
		System.err.println ("  ERROR: Data corrupt...");
		return false;
	      }
	    }	    
	    
	    listOfLists.add(new ArrayList<Integer>());
	    for (String value : values) {
	      if (head) {
		// store header if available and count columns
		if (no_header) {
		  header.add(Integer.toString(col+1));
		} else {
		  header.add(value.trim());
		}
		col++;
	      } else {
		// store digital values in a 2d-matrix
		listOfLists.get(row).add(Integer.parseInt(value));
	      }
	    }
	    // count real rows
	    if (!head) {
	      row ++;
	    }
	    head = false;
	  }
	}
      } catch (IOException e) {
	System.err.println ("  ERROR: Error reading the file: "+e);
      }
      System.out.println("INFO: Cols: "+col+" and Rows: "+row+" have been written");
      return true;
    }
    return false;
  }
  
  /**
   * writeDataOut
   * @return 
   */
  public boolean writeDataOut() {
    if (outfile != null) {
      String line;
      try {
	BufferedWriter bw = new BufferedWriter(new FileWriter(outfile));
	for (int r=0; r<row; r++) {
	  // each row
	  line = Integer.toString(r)+delimiter;
	  for (int c=0; c<col; c++) {
	    // build columns
	    line += Integer.toString(listOfLists.get(r).get(c))+delimiter;
	  }
	  // store in file
	  bw.write(line+"\n");
	}
	bw.close();
      } catch (IOException e) {
	System.err.println ("  ERROR: Error reading the file: "+e);
      }
      System.out.println("INFO: "+outfile+" has been written");
      return true;
    }
    return false;
  } 
  
  /**
   * writeScriptOut
   * @return 
   */
  public boolean writeScriptOut() {
    if (scriptfile != null) {
      String line = "";
      try {
	BufferedWriter bw = new BufferedWriter(new FileWriter(scriptfile));
	// gnuplot script
	line += "reset\n";
	line += "\n";
	if(option == 'p') {
	  line += "set term png size 1280,720\n";
	  line += "set output \"output.png\"\n";
	  line += "\n";
	  System.out.println("INFO: output.png file has been written...");
	} else {
	  System.out.println("INFO: Monitor mode activated...");
	}
	line += "set tmargin 0\n";
	line += "set bmargin 0\n";
	line += "set lmargin 1\n";
	line += "set rmargin 1\n";
	line += "\n";
	line += "set xrange [0:"+Integer.toString(row+1)+"]\n";
	line += "set yrange [-0.1:1.1]\n";
	line += "\n";
	line += "unset ytics\n";
	line += "\n";
	line += "set multiplot layout "+Integer.toString(col)+",1 margins 0.05,0.95,.1,.99 spacing 0,0\n";
	line += "\n";
	line += "set xtics format ''\n";
	line += "unset xlabel\n";
	line += "set grid\n";
	for(int i=1; i<col; i++) {
	  line += "plot '"+outfile+"' using 1:"+Integer.toString(i+1)+" with steps lt "+
		  Integer.toString(i)+" title \""+header.get(i-1)+"\"\n";
	}
	line += "\n";
	line += "set format x \"%g\"\n";
	line += "set xtics\n";
	line += "set xlabel \"time\"\n";
	line += "plot '"+outfile+"' using 1:"+Integer.toString(col+1)+" with steps lt "+
		  Integer.toString(col)+" title \""+header.get(col-1)+"\"\n";
	line += "\n";
	line += "unset multiplot\n";
	
	bw.write(line);
	bw.close();
      } catch (IOException e) {
	System.err.println ("  ERROR: Error reading the file: "+e);
      }
      System.out.println("INFO: "+scriptfile+" has been written");
      return true;
    }
    return false;
  }
  
  /**
   * checkLine
   * @param line
   * @return 
   */
  private boolean checkLine(String line) {
    return line.matches("[01 \t]+");
  }
}
