package edu.uiuc.sigmusic.twittersounds;

import java.io.IOException;
import java.net.InetAddress;
import java.net.SocketException;

import com.illposed.osc.OSCMessage;
import com.illposed.osc.OSCPacket;
import com.illposed.osc.OSCPortOut;

public class TwitterPdOSC {
	OSCPortOut oscout;
	
	public TwitterPdOSC() throws IOException {
   		InetAddress out = InetAddress.getByAddress(new byte[] {(byte) 255,(byte) 255,(byte) 255,(byte) 255});
		//InetAddress out = InetAddress.getByAddress(new byte[] {(byte) 192,(byte) 17,(byte) 96,(byte) 105}); //if broadcasting doesn't work, hardcode it here.
   		oscout = new OSCPortOut(out, 1338);
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

	
	
	
	
	
	
	/**
	 * A demo of how this works
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			TwitterPdOSC pdosc = new TwitterPdOSC();
			
			MelodyGenerator m = new MelodyGenerator(50, 50, 50);
			m.generateMelody();
			
			/*
			float[] synthnotes = new float[64];
			float[] bassnotes = new float[64];
			float[] kicknotes = new float[64];
			float[] snarenotes = new float[64];
			float[] highhatnotes = new float[64];
			float[] synthvel = new float[64];
			float[] bassvel = new float[64];
			float[] kickvel = new float[64];
			float[] snarevel = new float[64];
			float[] highhatvel = new float[64];
			
			
			for (int i=0; i<synthnotes.length; i++) {
				synthnotes[i] = ((float)Math.random()*35)+60f;
				synthvel[i] = i%8 / 8f;
				bassnotes[i] = ((float)Math.random()*10)+40f;
				bassvel[i] = i%4 / 4f;
				kicknotes[i] = (i%8 == 0) ? 1 : -1;
				kickvel[i] = (i%16 == 0) ? 0.5f : 1;
				snarenotes[i] = ((i+4)%8 == 0) ? 1 : -1;
				snarevel[i] = ((i+4)%16 == 0) ? 0.5f : 1;
				highhatnotes[i] = (i%2 == 0) ? 1 : -1;
				highhatvel[i] = 1;
			}
			*/
			
			
			pdosc.setParameter("tempo-ms", 500);

			pdosc.setNotes("synth", m.synth);
			pdosc.setVelocities("synth", m.synthvel);
			pdosc.setParameter("synth-attack", 10);
			pdosc.setParameter("synth-decay", 10);
			pdosc.setParameter("synth-sustain", 0);
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
			pdosc.setParameter("bass-sustain", 127);
			pdosc.setParameter("bass-release", 20);
			pdosc.setParameter("bass-waveform", 2); //0 = sine 1 = triangle 2 = square 3 = saw
			pdosc.setParameter("bass-glissando", 1); 
			pdosc.setParameter("bass-vibrato-depth", 16);
			pdosc.setParameter("bass-vibrato-speed", 70);
			pdosc.setParameter("bass-vibrato-waveform", 2); //0 = sine 1 = triangle 2 = square 3 = saw
			pdosc.setParameter("bass-tremolo-depth", 12);
			pdosc.setParameter("bass-tremolo-speed", 20);
			pdosc.setParameter("bass-tremolo-waveform", 1); //0 = sine 1 = triangle 2 = square 3 = saw
			pdosc.setParameter("bass-volume", 30);

			pdosc.setNotes("kick", m.kick);
			pdosc.setVelocities("kick", m.kickvel);
			pdosc.setParameter("kick-select", 6);
			
			pdosc.setNotes("snare", m.snare);
			pdosc.setVelocities("snare", m.snarevel);
			pdosc.setParameter("snare-select", 4);
			
			pdosc.setNotes("highhat", m.hihat);
			pdosc.setVelocities("highhat", m.hihatvel);
			pdosc.setParameter("highhat-select", 5);
			
			pdosc.setParameter("drums-volume", 200);

			pdosc.setParameter("bitcrusher-crush", 10);
			pdosc.setParameter("bitcrusher-depth", 1);
			
			pdosc.setParameter("reverb-mix", 30);
			pdosc.setParameter("reverb-room", 10);
			pdosc.setParameter("reverb-damping", 10);
			
			pdosc.setParameter("global-volume", 100);
			pdosc.setParameter("start", 1);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
