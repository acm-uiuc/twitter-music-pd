package edu.uiuc.sigmusic.twittersounds;

import java.io.IOException;
import java.util.Arrays;
import java.util.Date;

import org.json.simple.JSONObject;

import com.illposed.osc.OSCListener;
import com.illposed.osc.OSCMessage;

import edu.uiuc.sigmusic.twittersounds.SimpleJSONServer.JSONInterface;

public class InstrumentServer implements JSONInterface, OSCListener {

	SimpleJSONServer server;
	MelodyGenerator generator;
	TwitterPdOSC tpo;
	
	
	public void run() {
		try {
			server = new SimpleJSONServer(this);
			tpo = new TwitterPdOSC();
			tpo.addPhraseDoneListener(this);
			server.waitLoop();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	@Override
	public void inRequest(JSONObject json) {
		try {
			generator = new MelodyGenerator(50, 50, 50);
			generator.generateMelody();
			//GeneratorTest.testGenerator(generator);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	@Override
	public void acceptMessage(Date time, OSCMessage message) {
		try {
			System.out.println("SDKLFJLKSDJFDS MESSAGEEEEEEEE");
			System.out.println("Params:" +Arrays.toString(message.getArguments()));
			generator = new MelodyGenerator(50,50,50);
			generator.generateMelody();
			//GeneratorTest.testGenerator(generator);
			tpo.writeMelodyGenerator(generator);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	
	public static void main(String[] args) {
		System.out.println("STARTING");
		new InstrumentServer().run();
		System.out.println("DONE");
	}
}
