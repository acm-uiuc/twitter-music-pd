package edu.uiuc.sigmusic.twittersounds;

import java.io.IOException;

import org.json.simple.JSONObject;

import edu.uiuc.sigmusic.twittersounds.SimpleJSONServer.JSONInterface;

public class InstrumentServer implements JSONInterface {

	SimpleJSONServer server;
	MelodyGenerator generator;
	
	
	public void run() {
		/**
		 * We need to put in stuff to send to the melody generator
		 */
		server = new SimpleJSONServer(this);
		try {
			server.waitLoop();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	@Override
	public void inRequest(JSONObject json) {
		try {
			generator = new MelodyGenerator();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	
	
}
