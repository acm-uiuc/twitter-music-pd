package edu.uiuc.sigmusic.twittersounds;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class SimpleJSONServer {
	static final byte[] EOL = {(byte)'\r', (byte)'\n' };
	public static interface JSONInterface {
		public void inRequest(JSONObject json);
	}
	public boolean keepGoing = true;
	JSONInterface callback;
	
	
	public SimpleJSONServer(JSONInterface callback) {
		this.callback = callback;
	}
	
	public void waitLoop() throws IOException {
		System.out.println("Starting simple server..");
		ServerSocket server = new ServerSocket(9133);
		while(keepGoing) {
			System.out.println("waiting on connection..");
			final Socket socket = server.accept();
			System.out.println("connection recieved. spwaning new thread");
			new Thread() { 
				public void run() {
					try {
						System.out.println("reading in text..");
						String in = handleSocket(socket);
						//System.out.println("done reading in socket. closing it.");
						if (!socket.isClosed()) {
							socket.getOutputStream().write(EOL);
							socket.close();
						}
						//System.out.println("making json.");
						JSONObject obj = makeJSON(in);
						//System.out.println("callback json.");
						if (callback != null) {
							callback.inRequest(obj);
						}
						System.out.println("alldone it.");
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}.start();
		}
	}
	
	public String handleSocket(Socket socket) throws IOException {
	    InputStreamReader rd = new InputStreamReader(socket.getInputStream());

	    char[] bytebuff = new char[1024];
	    StringBuffer buffer = new StringBuffer();
	    int numbytes = 0;
	    while ((numbytes = rd.read(bytebuff)) >= 0) {
	    	String read = String.valueOf(bytebuff);
	    	if (read.contains(read))
	    	System.out.println("Read line: "+read);
	        buffer.append(read);
	    }
	    rd.close();
	    
	    return buffer.toString();

	}
	
	public JSONObject makeJSON(String in) throws ParseException   {
		in  = in.trim();
		System.out.println(String.format("Response: '''%s'''", in));
		JSONParser parser = new JSONParser();
		JSONObject obj = (JSONObject)parser.parse(in);
		return obj;
	}
	
	
	public static void main(String[] args) {
		try {
			new SimpleJSONServer(null).waitLoop();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

}
