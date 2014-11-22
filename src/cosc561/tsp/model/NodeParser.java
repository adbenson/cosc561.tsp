package cosc561.tsp.model;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class NodeParser {
		
	public static Path parse(String filename) throws NodeParseException {
		List<Node> nodes = new ArrayList<Node>();
		BufferedReader br = openReader(filename);
	    
	    try {
	        String line = readLine(br);

	        while (line != null) {
	            nodes.add(parseLine(line));
	            line = readLine(br);
	        }
		} finally {
			closeReader(br);
	    }
	    
	    return new Path(nodes);
	}
	
	private static String readLine(BufferedReader br) throws NodeParseException {
		try {
			return br.readLine();
		} catch (IOException e) {
			throw new NodeParseException("Unable to read line from file", e);
		}
	}
	
	private static BufferedReader openReader(String fileName) throws NodeParseException {
	    BufferedReader br;
	    
		try {
			br = new BufferedReader(new FileReader(fileName));
		} catch (FileNotFoundException e) {
			throw new NodeParseException("Unable to open file: "+fileName, e);
		}
		
		return br;
	}
	
	private static void closeReader(BufferedReader br) throws NodeParseException {
        try {
        	if (br != null) {
        		br.close();
        	}
		} catch (IOException e) {
			throw new NodeParseException("Exception closing BufferedReader", e);
		}
	}
	
	private static Node parseLine(String line) throws NodeParseException {
		//Split on anything that's not a number
		String[] coords = line.split("\\D");
		
		int x, y;
		try {
			x = Integer.parseInt(coords[0]);
			y = Integer.parseInt(coords[1]);
		} catch (NumberFormatException e) {
			throw new NodeParseException("Unparsable coordinates: \""+line+"\"", e);
		}
			
		return new Node(x, y);
	}
	
	public static class NodeParseException extends Exception {
		private static final long serialVersionUID = 1L;

		public NodeParseException(String message, Throwable cause) {
			super(message, cause);
		}
		
		public NodeParseException(String message) {
			super(message);
		}
	}

}
