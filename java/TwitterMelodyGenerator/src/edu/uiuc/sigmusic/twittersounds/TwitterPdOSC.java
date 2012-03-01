package edu.uiuc.sigmusic.twittersounds;

import java.io.IOException;
import java.net.InetAddress;

import com.illposed.osc.OSCListener;
import com.illposed.osc.OSCMessage;
import com.illposed.osc.OSCPortIn;
import com.illposed.osc.OSCPortOut;

public class TwitterPdOSC {
	OSCPortOut oscout;
	OSCPortIn oscin;
	
	public TwitterPdOSC() throws IOException {
   		//InetAddress out = InetAddress.getByAddress(new byte[] {(byte) 255,(byte) 255,(byte) 255,(byte) 255});
   		InetAddress out = InetAddress.getByAddress(new byte[] {(byte) 127, (byte) 0, (byte) 0, (byte) 1});
		//InetAddress out = InetAddress.getByAddress(new byte[] {(byte) 192,(byte) 17,(byte) 96,(byte) 105}); //if broadcasting doesn't work, hardcode it here.
   		oscout = new OSCPortOut(out, 1338);
   		oscin = new OSCPortIn(1339);
   		oscin.startListening();
	}
	
	public void addPhraseDoneListener(OSCListener l ) {
		oscin.addListener("/phrasedone", l);
	}
	
	/**
	 * Send notes to the PD instrument, where 0-7 map to notes 1-8 in the scale set by setMode
	 * @param instrument
	 * @param values
	 * @throws IOException
	 */
	public void setNotes(String instrument, float[] values) throws IOException {
		sendArray("/setnote/"+instrument, values);
	}
	
	public void setNotes(String instrument, int[] values) throws IOException {
		sendArray("/setnote/"+instrument, values);
	}
	
	/**
	 * Send notes to the PD instrument, where 0-7 map to notes 1-8 in the scale set by setMode
	 * @param instrument
	 * @param values
	 * @throws IOException
	 */
	public void setVelocities(String instrument, float[] values) throws IOException {
		sendArray("/setvel/"+instrument, values);
	}
	
	public void setVelocities(String instrument, int[] values) throws IOException {
		sendArray("/setvel/"+instrument, values);
	}
	
	/**
	 * The vast majority of parameters are from 0 - 127 (just laziness on my part)
	 * @param param
	 * @param value
	 * @throws IOException
	 */
	public void setParameter(String param, float value) throws IOException {
		OSCMessage oscmessage = new OSCMessage("/setparam");
		oscmessage.addArgument(param);
		oscmessage.addArgument((float)value);
		oscout.send(oscmessage);
	}
	
	public void setParameter(String param, int value) throws IOException {
		OSCMessage oscmessage = new OSCMessage("/setparam");
		oscmessage.addArgument(param);
		oscmessage.addArgument((float)value);
		oscout.send(oscmessage);
	}
	
	public void sendArray(String message, float[] values) throws IOException {
		for (int i=0; i<values.length; i++) {
			OSCMessage oscmessage = new OSCMessage(message);
			oscmessage.addArgument((float)i);
			oscmessage.addArgument(values[i]);
			oscout.send(oscmessage);
		}
	}
	public void sendArray(String message, int[] values) throws IOException {
		for (int i=0; i<values.length; i++) {
			OSCMessage oscmessage = new OSCMessage(message);
			oscmessage.addArgument((float)i);
			oscmessage.addArgument(values[i]);
			oscout.send(oscmessage);
		}
	}

	
	public void writeMelodyGenerator(MelodyGenerator m) {
		try {
			TwitterPdOSC pdosc = this;
			pdosc.setParameter("tempo-ms", 300);

			pdosc.setNotes("synth", m.synth);
			pdosc.setVelocities("synth", m.synthvel);
			pdosc.setParameter("synth-attack", 10);
			pdosc.setParameter("synth-decay", 10);
			pdosc.setParameter("synth-sustain", 75);
			pdosc.setParameter("synth-release", 0);
			pdosc.setParameter("synth-waveform", 1); //0 = sine 1 = triangle 2 = square 3 = saw
			pdosc.setParameter("synth-glissando", 20); 
			pdosc.setParameter("synth-vibrato-depth", 12);
			pdosc.setParameter("synth-vibrato-speed", 50);
			pdosc.setParameter("synth-vibrato-waveform", 2); //0 = sine 1 = triangle 2 = square 3 = saw
			pdosc.setParameter("synth-tremolo-depth", 22);
			pdosc.setParameter("synth-tremolo-speed", 10);
			pdosc.setParameter("synth-tremolo-waveform", 1); //0 = sine 1 = triangle 2 = square 3 = saw
			pdosc.setParameter("synth-volume", 100);

			pdosc.setNotes("bass", m.bass);
			pdosc.setVelocities("bass", m.bassvel);
			pdosc.setParameter("bass-attack", 30);
			pdosc.setParameter("bass-decay", 20);
			pdosc.setParameter("bass-sustain", 100);
			pdosc.setParameter("bass-release", 50);
			pdosc.setParameter("bass-waveform", 1); //0 = sine 1 = triangle 2 = square 3 = saw
			pdosc.setParameter("bass-glissando", 1); 
			pdosc.setParameter("bass-vibrato-depth", 16);
			pdosc.setParameter("bass-vibrato-speed", 70);
			pdosc.setParameter("bass-vibrato-waveform", 2); //0 = sine 1 = triangle 2 = square 3 = saw
			pdosc.setParameter("bass-tremolo-depth", 12);
			pdosc.setParameter("bass-tremolo-speed", 20);
			pdosc.setParameter("bass-tremolo-waveform", 1); //0 = sine 1 = triangle 2 = square 3 = saw
			pdosc.setParameter("bass-volume", 50);

			pdosc.setNotes("kick", m.kick);
			pdosc.setVelocities("kick", m.kickvel);
			pdosc.setParameter("kick-select", 8);
			
			pdosc.setNotes("snare", m.snare);
			pdosc.setVelocities("snare", m.snarevel);
			pdosc.setParameter("snare-select", 5);
			
			pdosc.setNotes("highhat", m.hihat);
			pdosc.setVelocities("highhat", m.hihatvel);
			pdosc.setParameter("highhat-select", 2);
			
			pdosc.setParameter("drums-volume", 200);

			pdosc.setParameter("bitcrusher-crush", 0);
			pdosc.setParameter("bitcrusher-depth", 0);
			
			pdosc.setParameter("reverb-mix", 30);
			pdosc.setParameter("reverb-room", 20);
			pdosc.setParameter("reverb-damping", 10);
			
			pdosc.setParameter("global-volume", 100);
			//pdosc.setParameter("start", 1);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	
	/**
	 * A demo of how this works
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			TwitterPdOSC pdosc = new TwitterPdOSC();
			
			MelodyGenerator m = new MelodyGenerator(100, 100, 0);
			m.generateMelody();
			
			pdosc.setParameter("tempo-ms", 300);

			pdosc.setNotes("synth", m.synth);
			pdosc.setVelocities("synth", m.synthvel);
			pdosc.setParameter("synth-attack", m.synthAttack);
			pdosc.setParameter("synth-decay", m.synthDecay);
			pdosc.setParameter("synth-sustain", m.synthSustain);
			pdosc.setParameter("synth-release", m.synthRelease);
			pdosc.setParameter("synth-waveform", m.synthWaveform); //0 = sine 1 = triangle 2 = square 3 = saw
			pdosc.setParameter("synth-glissando", m.synthGlissando); 
			pdosc.setParameter("synth-vibrato-depth", m.synthVibratoDepth);
			pdosc.setParameter("synth-vibrato-speed", m.synthVibratoSpeed);
			pdosc.setParameter("synth-vibrato-waveform", m.synthVibratoWaveform); //0 = sine 1 = triangle 2 = square 3 = saw
			pdosc.setParameter("synth-tremolo-depth", m.synthTremeloDepth);
			pdosc.setParameter("synth-tremolo-speed", m.synthTremeloSpeed);
			pdosc.setParameter("synth-tremolo-waveform", m.synthTremeloWaveform); //0 = sine 1 = triangle 2 = square 3 = saw
			pdosc.setParameter("synth-volume", 100);

			pdosc.setNotes("bass", m.bass);
			pdosc.setVelocities("bass", m.bassvel);
			pdosc.setParameter("bass-attack", m.bassAttack);
			pdosc.setParameter("bass-decay", m.bassDecay);
			pdosc.setParameter("bass-sustain", m.bassSustain);
			pdosc.setParameter("bass-release", m.bassRelease);
			pdosc.setParameter("bass-waveform", m.bassWaveform); //0 = sine 1 = triangle 2 = square 3 = saw
			pdosc.setParameter("bass-glissando", m.bassGlissando); 
			pdosc.setParameter("bass-vibrato-depth", m.bassVibratoDepth);
			pdosc.setParameter("bass-vibrato-speed", m.bassVibratoSpeed);
			pdosc.setParameter("bass-vibrato-waveform", m.bassVibratoWaveform); //0 = sine 1 = triangle 2 = square 3 = saw
			pdosc.setParameter("bass-tremolo-depth", m.bassTremeloDepth);
			pdosc.setParameter("bass-tremolo-speed", m.bassTremeloSpeed);
			pdosc.setParameter("bass-tremolo-waveform", m.synthTremeloWaveform); //0 = sine 1 = triangle 2 = square 3 = saw
			pdosc.setParameter("bass-volume", 70);

			pdosc.setNotes("kick", m.kick);
			pdosc.setVelocities("kick", m.kickvel);
			pdosc.setParameter("kick-select", 8);
			
			pdosc.setNotes("snare", m.snare);
			pdosc.setVelocities("snare", m.snarevel);
			pdosc.setParameter("snare-select", 5);
			
			pdosc.setNotes("highhat", m.hihat);
			pdosc.setVelocities("highhat", m.hihatvel);
			pdosc.setParameter("highhat-select", 2);
			
			pdosc.setParameter("drums-volume", 200);

			pdosc.setParameter("bitcrusher-crush", m.bitCrusherCrush);
			pdosc.setParameter("bitcrusher-depth", m.bitCrusherDepth);
			
			pdosc.setParameter("reverb-mix", m.reverbMix);
			pdosc.setParameter("reverb-room", m.reverbRoom);
			pdosc.setParameter("reverb-damping", m.reverbDamping);
			
			pdosc.setParameter("global-volume", m.globalVolume);
			pdosc.setParameter("start", m.start);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
