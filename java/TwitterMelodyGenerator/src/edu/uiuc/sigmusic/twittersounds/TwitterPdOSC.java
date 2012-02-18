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
	
	/**
	 * Right now we have majorscale, harmonicminorscale, and that's it.
	 * @param mode
	 * @throws IOException
	 */
	public void setMode(String mode) throws IOException {
		OSCMessage oscmessage = new OSCMessage("/setmode");
		oscmessage.addArgument(mode);
		oscout.send(oscmessage);
	}
	
	/**
	 * Manually set the mode
	 * @param values
	 * @throws IOException 
	 */
	public void setMode(float[] values) throws IOException {
		for (int i=0; i<values.length; i++) {
			OSCMessage oscmessage = new OSCMessage("/setmodemanual");
			oscmessage.addArgument((float)i);
			oscmessage.addArgument(values[i]);
			oscout.send(oscmessage);
		}
	}
	
	public void sendArray(String message, float[] values) throws IOException {
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
			pdosc.setMode("majorscale");
			pdosc.setMode(new float[] {0,2,4,5,7,9,11,12});

			pdosc.setNotes("synth", new float[] {1,3,5,1,2,  -1,-1,2, 3,4,5,6, 7,1,3,5});
			pdosc.setNotes("bass",  new float[] {1,1,1,3,    5,5,5,5, 6,6,6,6, 7,7,8,8});
			pdosc.setNotes("kick",  new float[] {1,0,0,0,    1,0,0,0, 1,0,0,1, 1,1,1,1});
			pdosc.setNotes("snare",  new float[] {0,0,0,0,  1,0,1,0,  0,0,0,0, 1,0,1,0});
			pdosc.setNotes("highhat",new float[] {1,1,1,1,  1,0,0,0,  1,0,0,1, 1,1,1,1,
					                              1,1,1,1,  1,0,0,0,  1,0,0,1, 1,1,1,1});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
