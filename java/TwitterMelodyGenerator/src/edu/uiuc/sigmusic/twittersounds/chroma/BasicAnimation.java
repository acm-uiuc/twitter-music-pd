package edu.uiuc.sigmusic.twittersounds.chroma;

import java.io.IOException;
import java.util.Random;

import edu.uiuc.sigmusic.twittersounds.MelodyGenerator;

public class BasicAnimation {
	ChromaInterface chromeint;
	int[][] colors = new int[24][];
	float[][] colorsf = new float[24][];

	
	public BasicAnimation(ChromaInterface chromeint) {
		this.chromeint = chromeint;
		for (int i=0; i<colors.length; i++) {
			int[] color = new int[3];
			colors[i] = color;
			float[] colorf = new float[3];
			colorsf[i] = colorf;
		}
	}
	
	public class AnimThread extends Thread {
		boolean keepRunning = true;
		public void run() {
			while (keepRunning) {
				updateFrame(16f/1000f);
				
				try {
					Thread.sleep(16);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	public void start() {
		new AnimThread().start();
	}

	public void updateBeat(int beat, MelodyGenerator generator) {
		if (generator == null) return;
		setColorsForDrums(generator, beat);
		setColorsForMelody(generator, beat);
		sendArray(colorsf);
	}
	public void updateFrame(float fps) {
		for (int i=0; i<colors.length; i+=1) {
			colorsf[i][0] = colorsf[i][0] * 0.95f;
			colorsf[i][1] = colorsf[i][1] * 0.95f;
			colorsf[i][2] = colorsf[i][2] * 0.95f;
		}
		sendArray(colorsf);
	}

	public void updatePhrase(int beat, MelodyGenerator generator) {
		randomizeColors(colors);
		sendArray(colors);
	}
	
	private void sendArray(float[][] colorsf) {
		for (int i=0; i<colors.length; i++) {
			colors[i][0] = Math.max(0,Math.min(1023,(int)colorsf[i][0]));
			colors[i][1] = Math.max(0,Math.min(1023,(int)colorsf[i][1]));
			colors[i][2] = Math.max(0,Math.min(1023,(int)colorsf[i][2]));
		}
		sendArray(colors);
	}
	private void sendArray(int[][] colors) {
		try {
			chromeint.sendLightArray(colors);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	
	public void randomizeColors(int[][] colors) {
		for (int i=0; i<colors.length; i++) {
			Random r = new Random();
			int color[] = colors[i];
			color[0] = r.nextInt()%1024;
			color[1] = r.nextInt()%1024;
			color[2] = r.nextInt()%1024;
		}
	}

	
	public void setColorsForDrums(MelodyGenerator generator, int beat) {
		int hhat = generator.hihat[beat];
		float hhatvel = generator.hihatvel[beat];
		int snare = generator.snare[beat];
		float snarevel = generator.snarevel[beat];
		int kick = generator.kick[beat];
		float kickvel = generator.kickvel[beat];
		for (int i=0; i<colors.length; i+=4) {
			colorsf[i][0] += (hhatvel * hhat * 123);
			colorsf[i][1] += (hhatvel * hhat * 323);
			colorsf[i][2] += (kickvel * kick * 823);
		}
		for (int i=1; i<colors.length; i+=4) {
			colorsf[i][0] += (hhatvel * hhat * 123);
			colorsf[i][1] += (hhatvel * hhat * 323);
			colorsf[i][2] += (kickvel * kick * 823);
		}
		for (int i=2; i<colors.length; i+=4) {
			colorsf[i][0] += (hhatvel * hhat * 123);
			colorsf[i][1] += (snarevel * snare * 823) + (hhatvel * hhat * 323);
		}
		for (int i=3; i<colors.length; i+=4) {
			colorsf[i][0] += (hhatvel * hhat * 123);
			colorsf[i][1] += (snarevel * snare * 823) + (hhatvel * hhat * 323);
		}
	}
	public void setColorsForMelody(MelodyGenerator generator, int beat) {
		int melodynote = (generator.synth[beat]-generator.key) % 24;
		int bassnote = (generator.bass[beat]-generator.key) % 8;
		if (melodynote >= 0) {
			colorsf[melodynote][0] += (1023f);
			colorsf[melodynote][1] += (523f);
			colorsf[melodynote][2] += (523f);
		} 
		if (bassnote >= 0) {
			colorsf[bassnote*3+0][0] += (43f);
			colorsf[bassnote*3+0][1] += (43f);
			colorsf[bassnote*3+0][2] += (423f);
			colorsf[bassnote*3+1][0] += (123f);
			colorsf[bassnote*3+1][1] += (123f);
			colorsf[bassnote*3+1][2] += (1023f);
			colorsf[bassnote*3+2][0] += (43f);
			colorsf[bassnote*3+2][1] += (43f);
			colorsf[bassnote*3+2][2] += (423f);
		}
	}
}
