package edu.uiuc.sigmusic.twittersounds;

import java.io.IOException;
import java.util.Arrays;
import java.util.Date;

import org.json.simple.JSONObject;

import com.illposed.osc.OSCListener;
import com.illposed.osc.OSCMessage;

import edu.uiuc.sigmusic.twittersounds.SimpleJSONServer.JSONInterface;
import edu.uiuc.sigmusic.twittersounds.chroma.BasicAnimation;
import edu.uiuc.sigmusic.twittersounds.chroma.ChromaInterface;

public class InstrumentServer implements JSONInterface, OSCListener {

	SimpleJSONServer server;
	MelodyGenerator generator;
	TwitterPdOSC tpo;
	BasicAnimation animation;
	ChromaInterface chromaint;
	
	
	public void run() {
		try {
			chromaint = new ChromaInterface();
			animation = new BasicAnimation(chromaint);
			animation.start();
			server = new SimpleJSONServer(this);
			tpo = new TwitterPdOSC();
			tpo.addPhraseDoneListener(this);
			tpo.addBeatListener(this);
			server.waitLoop();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	@Override
	public void inRequest(JSONObject json) {
		try {
			generator = new MelodyGenerator();
			generator.generateMelody();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	@Override
	public void acceptMessage(Date time, OSCMessage message) {
		try {
			System.out.println("SDKLFJLKSDJFDS MESSAGEEEEEEEE +"+message.getAddress());

			if (message.getAddress().equals("/phrasedone")) {
				System.out.println("Params:" +Arrays.toString(message.getArguments()));
				int phrase = (Integer)message.getArguments()[0];
				generator = new MelodyGenerator(50,50,50);
				generator.generateMelody();
				tpo.writeMelodyGenerator(generator);
				animation.updatePhrase(phrase, generator);
			} else if (message.getAddress().equals("/beat")) {
				int beat = (Integer)message.getArguments()[0];
				animation.updateBeat(beat, generator);
			}
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
