package edu.uiuc.sigmusic.twittersounds;

import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.Queue;

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
	Queue<JSONObject> inQueue = new LinkedList<JSONObject>();
	
	// Current happiness, excitement, confusion. Used when flushing the queue of JSONObjects when a phrasedone message has been sent
	int currH;
	int currE;
	int currC;
	
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
		inQueue.add(json);
	}
	
	void parseJSON(JSONObject json) {
		try {
			
			JSONObject weights = (JSONObject)json.get("weights");
			int happiness = (Integer)weights.get("happiness");
			int excitement = (Integer)weights.get("excitement");
			int randomness = (Integer)weights.get("randomness");
			System.out.println("In request: happiness:"+happiness+", excitement:"+excitement+" confusion:"+randomness);
			
			currE += excitement;
			currH += happiness;
			currC += randomness;
			
			//generator = new MelodyGenerator(happiness, excitement, randomness);
			//generator.generateMelody();
			//GeneratorTest.testGenerator(generator);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	@Override
	public void acceptMessage(Date time, OSCMessage message){
		try {
			//System.out.println("SDKLFJLKSDJFDS MESSAGEEEEEEEE +"+message.getAddress());
			if (message.getAddress().equals("/phrasedone")) {
				System.out.println("Params:" +Arrays.toString(message.getArguments()));
				int phrase = (Integer)message.getArguments()[0];
				
				// Basing the new generator off of the old attributes
				int atts[] = MelodyGenerator.getLastAttributes();
				int parsed = 0; // Number of tweets parsed
				
				// Flushing the queue... parsing the objects for values
				while(!inQueue.isEmpty()){
					parseJSON(inQueue.remove());
					parsed++;
				}
				
				System.out.println("Tweets Parsed: " + parsed);
				
				// Scaling curr values down to the amount of tweets
				if(parsed > 3){
					currH = currH/((int)(parsed/3));
					currE = currE/((int)(parsed/3));
					currC = currC/((int)(parsed/3));
				}
				
				System.out.print("Change in attributes: ");
				System.out.print("Happiness: " + currH + ", Excitment: " + currE + ", Confusion: " + currC);
				if(parsed > 3){
					System.out.println(" (scaled by down a factor of " + (int)(parsed/3) + ")");
				}
				else{
					System.out.println(" (not scaled)");
				}
				
				if(currH > 10) currH = 10;
				else if(currH <= -10) currH = -10;
				
				if(currE > 10) currE = 10;
				else if(currE <= -10) currE = -10;
				
				if(currC > 10) currC = 10;
				else if(currC <= -10) currC = -10;
				
				int h = currH + atts[0];
				int e = currE + atts[1];
				int c = currC + atts[2];
				
				if(h > 100) h = 100;
				else if (h < 0) h = 0;
				
				if(e > 100) e = 100;
				else if (e < 0) e = 0;
				
				if(c > 100) c = 100;
				else if (c < 0) c = 0;
				
				// Test generator
				generator = new MelodyGenerator(100, 25, 100);
				// Generating new melody based on the current modifications
				// generator = new MelodyGenerator(h, e, c);
				generator.generateMelody();
				generator.print();
				tpo.writeMelodyGenerator(generator);
				animation.updatePhrase(phrase, generator);
				
				currH = 0;
				currE = 0;
				currC = 0;
			} else if (message.getAddress().equals("/beat")) {
				int beat = (Integer)message.getArguments()[0];
				animation.updateBeat(beat, generator);
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//this is what runs it	
	public static void main(String[] args) {
		System.out.println("STARTING");
		new InstrumentServer().run();
		System.out.println("DONE");
	}
}
