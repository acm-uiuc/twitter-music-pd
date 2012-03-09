package edu.uiuc.sigmusic.twittersounds.chroma;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Random;

import com.illposed.osc.OSCMessage;
import com.illposed.osc.OSCPortOut;

public class ChromaInterface {
	
	
	OSCPortOut oscout;

	public ChromaInterface() throws IOException {
   		//InetAddress out = InetAddress.getByAddress(new byte[] {(byte) 255,(byte) 255,(byte) 255,(byte) 255});
		//128.174.251.39
		//InetAddress out = InetAddress.getByAddress(new byte[] {(byte) 127,(byte) 0,(byte) 0,(byte) 1});
   		InetAddress out = InetAddress.getByAddress(new byte[] {(byte) 128,(byte) 174,(byte) 251,(byte) 39});
		//InetAddress out = InetAddress.getByAddress(new byte[] {(byte) 192,(byte) 17,(byte) 96,(byte) 105}); //if broadcasting doesn't work, hardcode it here.
   		oscout = new OSCPortOut(out, 11661);

	}
	
	
	public void sendLightArray(int[][] colors) throws IOException {
		OSCMessage oscmessage = new OSCMessage("/setcolors");
		for (int i=0; i<colors.length; i++) {
			oscmessage.addArgument(colors[i][0]);
			oscmessage.addArgument(colors[i][1]);
			oscmessage.addArgument(colors[i][2]);
//			oscmessage.addArgument(colors[i]);
		}
		oscout.send(oscmessage);
	}

	
	
	
	
	public static void main(String[] args) {
		try {
			ChromaInterface c = new ChromaInterface();
			int[][] colors = new int[24][];
			for (int i=0; i<colors.length; i++) {
				int[] color = new int[3];
				colors[i] = color;
			}
			for (int i=0; i<100; i++) {
				long time = System.currentTimeMillis();
				System.out.println("Sending.... NOW "+(System.currentTimeMillis()%10000));
				randomizeColors(colors);
				c.sendLightArray(colors);
				System.out.println("Timediff: "+(System.currentTimeMillis() - time));
				Thread.sleep(100);
			}
			
		} catch (Exception e) { e.printStackTrace(); }
	}
	
	public static void randomizeColors(int[][] colors) {
		for (int i=0; i<colors.length; i++) {
			Random r = new Random();
			int color[] = colors[i];
			color[0] = r.nextInt()%1024;
			color[1] = r.nextInt()%1024;
			color[2] = r.nextInt()%1024;
		}

	}
}
