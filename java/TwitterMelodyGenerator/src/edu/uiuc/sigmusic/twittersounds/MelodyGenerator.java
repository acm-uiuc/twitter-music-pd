package edu.uiuc.sigmusic.twittersounds;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

/**
 * @author SIGMusic Spring 2012
 * 
 *         MelodyGenerator class
 * 
 *         Handles almost all aspects of generating music
 */

public class MelodyGenerator {

	/**
	 * Default DEBUG values... Key is generated (for now, C) Chord Progression
	 * is generated (for now, I IV I V) Synth and Bass will just play scales
	 * Drums will be generated
	 */

	public boolean DEBUG = false; // false To generate a random melody, true to
									// generate straight scales for testing
	public boolean fileRead = false; // Flag to see if file reading was
										// successful
	MelodyGenerator prev;

	/**
	 * Mood Attributes
	 */

	public int happiness = 50; // Happiness parameter, from 0 to 100, 0 =
								// depressed, 100 = elated
	public int excitement = 50; // Excitement parameter, from 0 to 100, 0 =
								// bored, 100 = excited
	public int confusion = 50; // Confusion parameter, from 0 to 100, 0 =
								// logical, 100 = confused

	/**
	 * Progression Values
	 */

	public int key; // The key, this defines the root of the chord progression
	public int[] scaleType; // Major, minor, etc(?)
	public int[] chordProgression; // The chord progression
	public int currentMelody; // Each progression consists of four melodies (16
								// measures total), it's incremented by 1 every
								// time a melody is generated
	
	public int[][] happyNotes = { {0, 0, 2, 4, 5, 5, 7, 7}, {0, 0, 2, 3, 4, 5, 5, 7, 7}, {0, 0, 2, 3, 4, 5, 5, 7, 7}, 
								  {0, 0, 2, 3, 4, 5, 5, 7, 7}, {0, 0, 2, 3, 4, 7, 7},
								  {0, 0, 2, 3, 4, 7, 7}, {0, 0, 2, 3, 7, 7} };
	public int[][] sadNotes = { {1, 3, 6}, {1, 6}, {1, 6}, {1, 6}, {1, 5, 6}, {1, 5, 6}, {1, 4, 5, 6}};
	public int start;

	/**
	 * Effect Values
	 */

	public int bitCrusherCrush;
	public int bitCrusherDepth;
	public int reverbMix;
	public int reverbRoom;
	public int reverbDamping;
	public int globalVolume;
	public int tempo;

	/**
	 * Synth Values
	 */

	public int[] synth; // The main melody, higher synth
	public float[] synthvel; // The main melody, higher synth velocity
	public int synthAttack;
	public int synthDecay;
	public int synthSustain;
	public int synthRelease;
	public int synthWaveform;
	public int synthGlissando;
	public int synthVibratoDepth;
	public int synthVibratoSpeed;
	public int synthVibratoWaveform;
	public int synthTremeloDepth;
	public int synthTremeloSpeed;
	public int synthTremeloWaveform;

	/**
	 * Bass Values
	 */

	public int[] bass; // Bass line, the lower synth
	public float[] bassvel; // Bass line, the lower synth velocity
	public int bassAttack;
	public int bassDecay;
	public int bassSustain;
	public int bassRelease;
	public int bassWaveform;
	public int bassGlissando;
	public int bassVibratoDepth;
	public int bassVibratoSpeed;
	public int bassVibratoWaveform;
	public int bassTremeloDepth;
	public int bassTremeloSpeed;
	public int bassTremeloWaveform;

	/**
	 * Drum Values
	 */

	public int[] snare; // Snare drum
	public float[] snarevel; // Snare drum velocity
	public int snareSound;

	public int[] kick; // Kick (Bass) drum
	public float[] kickvel; // Kick (Bass) drum velocity
	public int kickSound;

	public int[] hihat; // Hi-hat
	public float[] hihatvel; // Hi-hat velocity
	public int hihatSound;
	
	public static int[][][] drumFills = {
		{{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
		 {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
		 {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}},
		{{1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
		 {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
		 {0, 0, 1, 0, 1, 0, 0, 0, 0, 0, 1, 0, 1, 0, 0, 0}},
		{{1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 0, 0},
		 {1, 0, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 1, 0, 0, 0},
		 {0, 0, 1, 0, 1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0}},
		{{0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
		 {1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 1, 0},
		 {0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 1, 0, 0, 0, 0, 0}},
		{{1, 0, 1, 0, 1, 0, 0, 0, 1, 0, 1, 0, 1, 0, 0, 0},
		 {0, 1, 0, 0, 1, 0, 1, 1, 0, 0, 0, 0, 1, 0, 0, 0},
		 {1, 0, 0, 1, 0, 0, 0, 0, 1, 0, 1, 0, 0, 1, 1, 0}},
		{{1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 0, 1, 0, 0, 1, 0},
		 {0, 1, 0, 0, 1, 0, 1, 0, 1, 0, 0, 1, 1, 0, 1, 0},
		 {1, 0, 1, 0, 0, 1, 0, 0, 0, 1, 1, 0, 0, 1, 0, 0}},
		{{0, 0, 1, 0, 0, 0, 1, 0, 0, 1, 0, 0, 0, 0, 1, 0},
	     {0, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 1, 0, 0, 1, 0},
		 {1, 0, 1, 0, 0, 0, 1, 1, 0, 1, 1, 0, 1, 1, 0, 0}},
		{{0, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 0},
		 {0, 1, 0, 0, 1, 0, 1, 0, 1, 0, 1, 0, 0, 1, 0, 0},
		 {1, 0, 1, 0, 0, 1, 0, 0, 0, 1, 0, 0, 1, 0, 1, 0}},
	    {{1, 0, 1, 0, 0, 1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0},
		 {1, 0, 0, 1, 0, 0, 1, 0, 1, 0, 0, 1, 1, 0, 1, 0},
		 {0, 1, 1, 0, 1, 0, 0, 1, 0, 1, 1, 0, 0, 1, 0, 0}},
		{{1, 0, 0, 0, 1, 0, 1, 0, 0, 0, 1, 0, 1, 0, 1, 0},
		 {1, 1, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 1, 0, 0, 0},
		 {0, 0, 1, 1, 1, 1, 0, 0, 1, 1, 1, 1, 0, 0, 1, 0}},
		};
	
	
	public int drumsVolume;
	public int drumBeatSelector;

	/**
	 * MelodyGenerator Constructor
	 * 
	 * @param h
	 *            - The happiness parameter
	 * @param e
	 *            - The excitement parameter
	 * @param c
	 *            - The confusion parameter
	 * @throws Exception
	 */

	MelodyGenerator() throws Exception {
		try {
			synth = new int[64];
			bass = new int[64];
			snare = new int[64];
			kick = new int[64];
			hihat = new int[64];

			synthvel = new float[64];
			bassvel = new float[64];
			snarevel = new float[64];
			kickvel = new float[64];
			hihatvel = new float[64];

			chordProgression = new int[4];
			scaleType = new int[4];

			loadFromFile();
		} catch (Exception e) {
			fileRead = false;
		}
	}

	MelodyGenerator(int h, int e, int c) throws Exception {

		happiness = h;
		excitement = e;
		confusion = c;

		/**
		 * Melody is generated 4 measures at a time, each value in the
		 * respective array represents the MIDI value to be played at that time
		 * (0-127, -1 for rest), drums are defined 0 for rest, 1 for played.
		 * Each value is worth one 16th note in 4/4 time.
		 */

		key = 1;
		try {
			prev = new MelodyGenerator();
			currentMelody = prev.currentMelody;
			drumBeatSelector = prev.drumBeatSelector;
			fileRead = true;
		} catch (Exception exception) {
			fileRead = false;
		}

		synth = new int[64];
		bass = new int[64];
		snare = new int[64];
		kick = new int[64];
		hihat = new int[64];

		synthvel = new float[64];
		bassvel = new float[64];
		snarevel = new float[64];
		kickvel = new float[64];
		hihatvel = new float[64];

		chordProgression = new int[4];
		scaleType = new int[4];

		for (int i = 0; i < 64; i++) {
			
			synth[i] = -1;
			bass[i] = -1;
			snare[i] = -1;
			kick[i] = -1;
			hihat[i] = -1;
			
			synthvel[i] = .5f;
			bassvel[i] = .5f;
			snarevel[i] = .5f;
			kickvel[i] = .5f;
			hihatvel[i] = .5f;
			if (i < 4) {
				chordProgression[i] = 0;
				scaleType[i] = 0;
			}
		}
	}

	/**
	 * When this function is called, it takes care of all the work of generating
	 * music.
	 * 
	 * @throws Exception
	 */

	public void generateMelody() throws Exception {
		if (!DEBUG) {
			generateKey();
			generateProgression();
			generateSynth();
			generateBass();
			generateDrums();
			modifyAttributes();
			try {
				saveToFile();
			} catch (Exception e) {
				System.out.println("Error Saving");
			}
			transpose();
			
		} else {
			generateProgression();
			generateDrums();
			transpose();

		}
		
	}

	/**
	 * Choose which key to be played, the key can range from 0 to 24 (C5 to C7
	 * for synth) 0 = C, 1 = C#, 2 = D, 3 = D#, 4 = E, 5 = F, 6 = F#, 7 = G, 8 =
	 * G#, 9 = A, 10 = A#, 11 = B
	 */

	public void generateKey() {
		//double random = Math.random();
		//if (!fileRead) { // If we can't read the melody generated from before
			key = 0;
		//}
		/*if (fileRead && currentMelody == 1) { // If we're on a new
												// progression...
			if (excitement - prev.excitement >= 5) { // Things are getting more
														// exciting
				if (happiness - prev.happiness >= 3) { // Things are getting
														// happier
					key++;
				} else if (happiness - prev.happiness <= -3) { // Things are
																// getting
																// sadder
					key = key - 2;
				}
			}
		}*/
	}

	/**
	 * Choose which scale (major or minor) to be played
	 */

	/**
	 * Generates the progression
	 * 
	 * Some popular 4 chord progressions: 
	 * 
	 * I - IV - V - V
	 * I - I - IV - V 
	 * I - IV - I - V 
	 * I - IV - V - IV
	 * 
	 * NOTE: I = 0, IV = 3, and such, it'll make transposing much easier.
	 */

	public void generateProgression() {
		
		/*
		 * New progression generation
		 */
		
		int[][] chordProgressions = {{0, 3, 4, 4}, {0, 0, 3, 4}, {0, 3, 0, 4},
									 {0, 3, 4, 3}, {0, 3, 4, 0}, {0, 4, 0, 3}};
		
		for (int i = 0; i < 4; i++) // Chooses mode
			if (i % 2 == 0)
				scaleType[i] = 1;
			else
				scaleType[i] = (6 - confusion/60) - happiness/20;
			
		double chordPicker = Math.random() * 6; // Chooses chord progression
		
		for (int i = 0; i < 4; i++)
			chordProgression[i] = chordProgressions[(int)chordPicker][i];
	
		/*
		 * Old progression generation
		/*
	
		if (!fileRead || currentMelody == 1) {
			if (happiness > 66) { // Things are pretty happy!
				 	[0] = 0; // Always start with the root
				// Generate a semi-random progressions of I, IV, and V
				for (int i = 1; i < 3; i++) {
					double chordPicker = Math.random() * 100;
					if (i == 1) {
						if (chordPicker > 50)
							chordProgression[i] = 0;
						else
							chordProgression[i] = 4;
					} else {
						if (chordPicker < 33) {
							chordProgression[i] = 0;
						} else if (chordPicker < 66) {
							chordProgression[i] = 4;
						} else {
							chordProgression[i] = 6;
						}
					}
				}

			} else if (happiness > 33) {
				chordProgression[0] = 0; // Always start with the root
				//scaleType[0] = 1;
				for (int i = 1; i < 3; i++) {
					double chordPicker = Math.random() * 100;
					if (chordPicker < 25) {
						chordProgression[i] = 2;
					} else if (chordPicker < 50) {
						chordProgression[i] = 4;
					} else if (chordPicker < 75) {
						chordProgression[i] = 0;
					} else {
						chordProgression[i] = 6;
					}
				}
			} else {
				chordProgression[0] = 1;
				for (int i = 1; i < 3; i++){
					if(chordProgression[i - 1] == 0){
						if(Math.random() > .4){
							chordProgression[i] = 1;
						}
						else if(Math.random() > .8){
							chordProgression[i] = 4;
						}
						else{
							chordProgression[i] = 0;
						}
					}
					else if(chordProgression[i - 1] == 1){
						if(Math.random() > .4){
							chordProgression[i] = 4;
						}
						else if(Math.random() > .8){
							chordProgression[i] = 1;
						}
						else{
							chordProgression[i] = 1;
						}
					}
					else if(chordProgression[i - 1] == 4){
						if(Math.random() > .4){
							chordProgression[i] = 1;
						}
						else if(Math.random() > .8){
							chordProgression[i] = 0;
						}
						else{
							chordProgression[i] = 4;
						}
					}
				}
			}
			if(happiness > 50){
				for(int i = 0; i < 4; i++){
					scaleType[i] = 1;
				}
			}
			else if(happiness > 25){
				if(Math.random() > .5){
					scaleType[0] = 4;
					scaleType[3] = 4;
					
					scaleType[1] = 1 + (int)Math.random()*5;
					scaleType[2] = scaleType[1];
				}
				else{
					scaleType[0] = 1;
					scaleType[3] = 1;
					
					scaleType[1] = 1 + (int)Math.random()*4;
					scaleType[2] = scaleType[1];
				}
			}
			else{
				scaleType[0] = 4;
				scaleType[3] = 4;
				
				scaleType[1] = 4 + (int)Math.random()*3;
				scaleType[2] = scaleType[1];
			}
		} else {
			for (int i = 0; i < 4; i++){
				chordProgression[i] = prev.chordProgression[i];
				scaleType[i] = prev.scaleType[i];
			}
		}
		*/
	}

	/**
	 * Generate the synth line
	 */

	public void generateSynth() {
		/*
		 * Notes 0 - 15 will be generated one by one based on the attributes,
		 * then the rest will be put through a loop to be generated based off of
		 * notes 0 - 15.
		 */
			
		double noteChooser = 0.0;
		//double hModifier = 0.0;

		// Generate a new melody
			
			for(int i = 0; i < 16; i++){
				double frequencyModifier = 0.0;
				if(i % 4 == 0){
					if(i == 0){
						if (confusion > 80) { // Still play the root at the beginning all
											  // the time, unless things get really confusing
							if (Math.random() < .75) {
								synth[0] = 0;
							} 
							else {
								if (Math.random() > .5)
									synth[0] = 2;
								else
									synth[0] = 4;
							}
						} 
						else {
							synth[0] = 0;
						}
					}
					else{
						if(synth[i - 4] == -1){ // If the quarter note previous isn't resting
							frequencyModifier = .25;
						}
						if (Math.random() / 2 < ((double)happiness)/100){ // See if we're going to choose a happy or sad note
							if(Math.random() < .75 + frequencyModifier + ((double)excitement)/400){ // See if we're going to play a note or not
								noteChooser = Math.random();
								synth[i] = happyNotes[scaleType[i/16]][(int)(noteChooser * happyNotes[scaleType[i/16]].length)];
							}
							else {
								synth[i] = -1;
							}
						}

						else { // Sad tier of notes
							if(Math.random() < .6 + ((double)excitement)/400) { // Less of a chance to play a note
								noteChooser = Math.random();
								synth[i] = sadNotes[scaleType[i/16]][(int)(noteChooser * sadNotes[scaleType[i/16]].length)];
							}
						}
					}
				}
				// Logic for eighth notes
				else if(i % 2 == 0){
					// Increase the chances if previous notes are already being played.
					if(synth[i - 2] != -1){
						frequencyModifier += .1;
					}
					if(i == 2 || synth[i - 4] != -1){
						frequencyModifier += .1;
					}
					/*if(synth[i - 1] != -1){ // Trying to make eighth notes less frequent..
						frequencyModifier += .2;
					}*/
					if(Math.random() < frequencyModifier + ((double)excitement)/400 + .6){ // Will this eighth note be played?
						if(i != 2 && synth[i - 4] != -1){ // Give it a high probability to equal the previous off-beat eighth note if one exists
							if(Math.random() < .5){
								synth[i] = synth[i - 4];
							}
						}
						else if(Math.random() < ((double)(100 - confusion))/150){
							synth[i] = synth[i - 2];
						}
						else if(Math.random() < ((double)happiness)/100){ // See if we're going to choose a happy or sad note
							if(Math.random() < .75 + frequencyModifier + ((double)excitement)/400){ // See if we're going to play a note or not
								noteChooser = Math.random();
								synth[i] = happyNotes[scaleType[i/16]][(int)(noteChooser * happyNotes[scaleType[i/16]].length)];
							}
							else {
								synth[i] = -1;
							}
						}
						else{ // Sad tier of notes
							if(Math.random() < .5 + ((double)excitement)/400){ // Less of a chance to play a note
								noteChooser = Math.random();
								synth[i] = sadNotes[scaleType[i/16]][(int)(noteChooser * sadNotes[scaleType[i/16]].length)];
							}
						}
					}
					else{
						synth[i] = -1;
					}
				}
				// LOGIC FOR SIXTEENTH NOTES
				else{
					if(synth[i - 1] != -1){
						frequencyModifier += .2;
					}
					if(Math.random() < frequencyModifier + ((double)excitement)/200){ // Are we going to play this note or not
						if(synth[i - 1] != -1){
							if(Math.random() < .25){
								if(synth[i - 1] <= 6)
									synth[i] = synth[i - 1] + 1;
								else
									synth[i] = synth[i - 1];
							}
							else if(Math.random() < .5){
								if(synth[i - 1] <= 5)
									synth[i] = synth[i - 1] + 2;
								else
									synth[i] = synth[i - 1];
							}
							else if(Math.random() < .75){
								if(synth[i] > 0)
									synth[i] = synth[i - 1] - 1;
								else
									synth[i] = synth[i - 1];
							}
							else{
								synth[i] = synth[i - 1];
							}
						}
						else{
							if(excitement > 80 && confusion > 80){
								if(Math.random() < ((double)happiness)/100){ // See if we're going to choose a happy or sad note
									noteChooser = Math.random();
									synth[i] = happyNotes[scaleType[i/16]][(int)(noteChooser * happyNotes[scaleType[i/16]].length)];
								}
								else{ // sad tier
									noteChooser = Math.random();
									synth[i] = sadNotes[scaleType[i/16]][(int)(noteChooser * sadNotes[scaleType[i/16]].length)];
								}
							}
						}
					}
				}
			}
			
			for(int i = 0; i < 32; i++){
				synth[i + 16] = synth[i % 16];
				
				if(Math.random() < .2 && synth[i + 16] != -1){
					noteChooser = Math.random();
					if(noteChooser < .45 && synth[i + 16] <= 5){
						synth[i + 16] += 2;
					}
					else if (noteChooser < .9 && synth[i + 16] >=2){
						synth[i + 16] -= 2;
					}
				}
			}
			
			/**
			 * I'm commenting this out temporarily, I would like to fix this so things
			 * get elongated properly, but I can't find a good way to do so. 
			 * Let's try to get this working before EOH.
			 */
			if (excitement > 20 && excitement < 80) {
				for (int i = 4; i < 60; i++) { // Randomly elongates each note of the melody
					if(Math.random() > Math.abs(50 - excitement))
					{
						if (synth[i] == -1) {
							double frequencyModifier = .0;
							if (synth[i + 1] == -1 && synth[i- 1] == -1)
								frequencyModifier += 0.05;
							if (synth[i + 2] == -1 && synth[i - 2] == -1 && frequencyModifier > 0)
								frequencyModifier += 0.05;
							if (synth[i + 3] == -1 && synth[i - 3] == -1 && frequencyModifier > 0)
								frequencyModifier += 0.05;
							if (Math.random() > -excitement/200 + frequencyModifier && synth[i] == -1) {
								if (synth[i - 1] != -1)
									synth[i] = synth[i - 1];
							}
						}
					}
				}
			}
			
			int offset = 12;
			
			if(currentMelody != 1) {
				if(excitement > 50 && confusion > 40 && happiness > 20){
					// if(Math.random() > .5)
						offset = 12;
				}
				for (int i = 0; i < 13; i++){ 	// Last measure mirrors the first and is 
										  		// offset by one eighth at times
					synth[i + 48] = synth[offset - i];
					// chordProgression[0] = chordProgression[3]; // First and last measure 
				}											   // are on the same chord
				for (int i = 13; i < 15; i++)
					synth[i + 48] = synth[i + 47];
			}
			else
				for (int i = 0; i < 16; i++)
					synth[i + 48] = synth[i];
			
			/*
			 * If the we're not in the first melody of the progression, copy over the first part
			 * of the 1st and 3rd measures to the current melody, this will add a ton of structure
			 */
			
			if (fileRead && currentMelody != 1){
				for(int i = 0; i < 8; i++)
					synth[i] = prev.synth[i];
				
				for(int i = 32; i < 40; i++){
					synth[i] = prev.synth[i];
			}
		}
			
	}
	
	/**
	 * Generate the bass line
	 */
	public void generateBass() {
		// bass = A sick groove
		for (int i = 0; i < 64; i++) {
			if(bass[i] == -1){
				if(Math.random() < .7){
					bass[i] = synth[i];
				}
				else{
					if(Math.random() < .5)
						if (synth[i] <= 3)
							bass[i] = synth[i] + 3;
						else if(synth[i] > 3){
							bass[i] = synth[i] - 3;
					}
				}
				if(Math.random() < .5){
					if(i != 63 && i != 0 && synth[i - 1] != -1 && synth[i + 1] != -1 && excitement > 70){
						bass[i] = (Math.max(synth[i + 1], synth[i - 1]) - Math.min(synth[i + 1], synth[i - 1])) + Math.min(synth[i + 1], synth[i - 1]);
						if(i % 2 == 1){
							bass[i + 1] = bass[i];
						}
					}
				}
				if(i % 2 == 1){/*
					 * Let's put in some sweet drum fills here!
					 */
					if((Math.random() > ((double)excitement)/125 && synth[i - 1] != -1) || (bass[i - 1] != -1 && Math.random() < .8)){
						bass[i - 1] = synth[i - 1];
						bass[i] = synth[i - 1];
					}
				}
			}	
		}
	}

	/**
	 * Generate the hihat, snare, and kick
	 */
	public void generateDrums() {
		int temp = drumBeatSelector;
		if(currentMelody == 1 || !fileRead){
			drumBeatSelector = (int)(10 * ((((double)excitement)/151 + (((double)happiness)/301))));
			temp = drumBeatSelector;
		}
			generateHihat();
			generateSnare();
			generateKick();
			
			drumBeatSelector = temp;
		
		if(currentMelody == 4){
			int fillChooser = excitement/10;
			if (fillChooser >= 10)
				fillChooser = 9;
			for (int i = 48; i < 64; i++) {
				hihat[i] = drumFills[fillChooser][0][i%16];
				//snare[i] = drumFills[fillChooser][1][i%16];
				kick[i] = drumFills[fillChooser][2][i%16];
				hihatvel[i] = .6f;
				snarevel[i] = .6f;
				kickvel[i] = .6f + excitement/200;		
			}
		}
		
	}

	public void generateHihat() {
		int[][] hiHatGrooves = {{1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0},
				{1, 0, 0, 0, 1, 0, 1, 0, 1, 0, 0, 0, 1, 0, 1, 0},
				{1, 0, 1, 0, 1, 0, 0, 1, 1, 0, 1, 0, 1, 0, 0, 1},
				{1, 0, 0, 1, 0, 0, 1, 0, 1, 0, 0, 1, 0, 0, 1, 0},
				{1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0},
				{1, 0, 1, 0, 1, 0, 1, 1, 1, 0, 1, 0, 1, 0, 1, 1},
				{1, 1, 1, 0, 1, 1, 1, 0, 1, 1, 1, 0, 1, 1, 1, 0},
				{1, 0, 1, 1, 0, 1, 1, 0, 1, 0, 1, 1, 0, 1, 1, 0},
				{1, 1, 1, 1, 1, 0, 1, 0, 1, 1, 1, 1, 1, 0, 1, 0},
				{1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1}
		};		
		if(drumBeatSelector != 0){
			if(Math.random() < .5){
				drumBeatSelector--;
			}
		}
		for(int i = 0; i < 64; i++){
			hihat[i] = hiHatGrooves[drumBeatSelector][i % 16];
			if (i % 2 == 0)
				hihatvel[i] = hihatvel[i] + (float)(Math.random()/4); // Each eighth note is amplified at random
		}
		
	}

	public void generateSnare() {
		int[][] snareGrooves = {{0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0},
				{0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0},
				{0, 0, 0, 0, 1, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0},
				{0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 1, 0, 0, 0},
				{0, 0, 0, 0, 1, 0, 0, 1, 0, 1, 0, 0, 1, 0, 0, 0},
		};
		if(drumBeatSelector != 0){
			if(Math.random() < .3){
				drumBeatSelector--;
			}
		}
		for (int i = 0; i < 64; i++) {
			snare[i] = snareGrooves[drumBeatSelector/2][i % 16];
			if (i % 4 == 0)
				snarevel[i] = snarevel[i] + .2f; // Everything besides two and four are made quieter
			else 
				snarevel[i] = snarevel[i] - .2f; // 2 and 4 are amplified
		}
	}

	public void generateKick() {

		int[][] kickGrooves = {{1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
				{1, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0},
				{1, 0, 1, 0, 0, 0, 0, 0, 1, 0, 1, 0, 0, 0, 0, 0},
				{1, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 0, 0},
				{1, 0, 0, 1, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0},
				{1, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 1},
				{1, 0, 0, 1, 0, 0, 0, 0, 0, 1, 1, 0, 0, 1, 1, 0},
				{1, 1, 0, 1, 0, 1, 1, 0, 0, 1, 1, 0, 0, 0, 1, 0},
				{1, 0, 1, 1, 0, 1, 0, 1, 1, 0, 1, 1, 0, 1, 1, 0},
				{1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
		};
		drumBeatSelector = drumBeatSelector*2;
		if(drumBeatSelector >= 4){
			if(Math.random() < .4){
				drumBeatSelector = drumBeatSelector - 2;
			}
			else if(Math.random() < .8){
				drumBeatSelector = drumBeatSelector - 4;
			}
		}
		for (int i = 0; i < 64; i++) {
			kick[i] = kickGrooves[drumBeatSelector/2][i % 16];
			if (kick[i] != 0) {
				if (i % 4 == 0) 
					kickvel[i] = kickvel[i] + .1f; // Quarter notes are amplified
				else
					kickvel[i] = kickvel[i] - .05f; // Sixteenth notes are quieted
			}
		}
		for (int i = 16; i < 64; i++) { // Same is done to the last three measures
			kick[i] = kick[i - 16];
			if (kick[i] != 0) {
				if (i % 4 == 0)
					kickvel[i] = kickvel[i] + .1f;
				else 
					kickvel[i] = kickvel[i] - .05f; 
			}
		}
	}

	public void modifyAttributes(){
		//if(!fileRead){
			currentMelody = (currentMelody + 1) % 5;
			if(currentMelody == 0) currentMelody = 1;
			start = 1;
			bitCrusherCrush = (excitement - 25 - happiness)/5;
			if (bitCrusherCrush < 0)
				bitCrusherCrush = 0;
			bitCrusherDepth = bitCrusherCrush;
			reverbRoom = 100-(int)(excitement*1.2);
			reverbMix = confusion + (50 - happiness);	
			reverbDamping = (100 - confusion);
			globalVolume = 100 + excitement/2;
			
			tempo = 375 - (happiness/10 + excitement);

			synthAttack = 0;
			synthDecay = 0;
			synthSustain = 100;
			synthRelease = 105;
			synthGlissando = confusion/10; 					  
			synthVibratoDepth = confusion/10; 				  
			synthVibratoSpeed = confusion/20 + excitement/20;
			synthTremeloDepth = confusion/2;
			synthTremeloSpeed = excitement/2;
			
			if (happiness + excitement < 120){
				synthVibratoWaveform = 0;
				synthTremeloWaveform = 0;
				synthWaveform = 0;
			}
			else {
				synthVibratoWaveform = 1;
				synthTremeloWaveform = 1;
				synthWaveform = 1;
			}
			
			if (excitement > 70) {
				synthVibratoDepth = 0;
				synthVibratoSpeed = 0;
				synthGlissando = 0;
				synthWaveform = 2;
				synthTremeloDepth = 0;
				synthTremeloSpeed = 0;
			}
			for (int i = 0; i < 64; i++) {
				if (excitement > 70)
					synthvel[i] = .12f;
				else
					synthvel[i] = .5f;
			}

			bassAttack = 20 + confusion + (((100 - happiness)/4));
			bassDecay = 100 - confusion;
			bassSustain = 80 - happiness + 80 - excitement;
			bassRelease = confusion/4;
			
			bassGlissando = confusion/2;
			bassVibratoDepth = confusion/2;
			bassVibratoSpeed = confusion/2 + excitement/2;
			if(happiness < 50 && excitement < 30){
				bassVibratoWaveform = 0;
				bassTremeloWaveform = 0;
				bassWaveform = 0;
			}
			else{
				bassVibratoWaveform = 1;
				bassTremeloWaveform = 1;
				bassWaveform = 1;
			}
			bassTremeloDepth = confusion/2;
			bassTremeloSpeed = excitement/2;
			
			kickSound = 8;
			snareSound = 5;
			hihatSound = 2;
			/*
			if (excitement < 15)
				hihatSound = 2;
			else if (excitement < 85)
				hihatSound = 1;
			else
				hihatSound = 3;
				*/
			/*
			if (excitement < 15)
				snareSound = 6;
			else if (excitement < 85)
				snareSound = 5;
				
			else
				snareSound = 4;
				*/
			/*
			if (excitement < 75)
				kickSound = 8;
			else
				kickSound = 3;
				*/
		//}
	}
	/**
	 * Takes notes 0-7 in the instrument arrays and transposes everything into
	 * values 0 - 127 representing the correct actual MIDI note to be played,
	 * this is for synth and bass only.
	 */
	public void transpose() {
		int[][] scaleValues = new int[][] { { 0, 2, 4, 5, 7, 9, 11, 12 },
				{ 0, 2, 4, 5, 7, 9, 11, 12 }, { 0, 2, 4, 5, 7, 9, 11, 12 },
				{ 0, 2, 4, 5, 7, 9, 11, 12 } }; // Parallel array that will
												// transpose notes 0 - 7 into a
												// diatonic scale

		for (int i = 0; i < 4; i++) {
			if (scaleType[i] == 0)
				scaleValues[i] = new int[] { 0, 2, 4, 6, 7, 9, 11, 12 }; // Lydian - "brightest"
			if (scaleType[i] == 1)
				scaleValues[i] = new int[] { 0, 2, 4, 5, 7, 9, 11, 12 }; // Ionian (major)
			if (scaleType[i] == 2)
				scaleValues[i] = new int[] { 0, 2, 4, 5, 7, 9, 10, 12 }; // Mixolydian
			if (scaleType[i] == 3)
				scaleValues[i] = new int[] { 0, 2, 3, 5, 7, 9, 10, 12 }; // Dorian
			if (scaleType[i] == 4)
				scaleValues[i] = new int[] { 0, 2, 3, 5, 7, 8, 10, 12 }; // Aeolian (natural minor)
			if (scaleType[i] == 5)
				scaleValues[i] = new int[] { 0, 1, 3, 5, 7, 8, 10, 12 }; // Phrygian
			if (scaleType[i] == 6)
				scaleValues[i] = new int[] { 0, 1, 3, 5, 6, 8, 10, 12 }; // Locrian - "darkest"
		}

		int c = -1; // Current place in the progression that the note will be
					// transposed to

		for (int i = 0; i < 64; i++) {
			if (i % 16 == 0) // Current place in chord progression
				c++;

			if (synth[i] != -1) { // Unless it's a rest, transpose the raw 0 - 7
									// into the proper note within a scale
				synth[i] = scaleValues[c][synth[i]];
				synth[i] += (chordProgression[c] + 12 * 5); // Move the note up
															// to the proper
															// scale and octave
			}

			if (bass[i] != -1) {
				bass[i] = scaleValues[c][bass[i]];
				bass[i] += (chordProgression[c] + 12 * 3); // Move the note up
															// to the proper
															// scale and octave
			}
		}
	}

	public void saveToFile() throws IOException {
		try {
			FileWriter fstream = new FileWriter("melody.txt");
			BufferedWriter out = new BufferedWriter(fstream);
			StringBuilder output = new StringBuilder();

			output.append(happiness + " ");
			output.append(excitement + " ");
			output.append(confusion + " ");
			output.append(currentMelody + " ");
			output.append(drumBeatSelector + " ");
			output.append(key + " ");

			for (int i = 0; i < 4; i++) {
				output.append(chordProgression[i] + " ");
				output.append(scaleType[i] + " ");
			}
			output.append(start + " ");
			output.append(bitCrusherCrush + " ");
			output.append(bitCrusherDepth + " ");
			output.append(reverbMix + " ");
			output.append(reverbDamping + " ");
			output.append(reverbRoom + " ");
			output.append(globalVolume + " ");
			output.append(tempo + " ");

			output.append(synthAttack + " ");
			output.append(synthDecay + " ");
			output.append(synthSustain + " ");
			output.append(synthRelease + " ");
			output.append(synthWaveform + " ");
			output.append(synthGlissando + " ");
			output.append(synthVibratoDepth + " ");
			output.append(synthVibratoSpeed + " ");
			output.append(synthVibratoWaveform + " ");
			output.append(synthTremeloDepth + " ");
			output.append(synthTremeloWaveform + " ");

			output.append(bassAttack + " ");
			output.append(bassDecay + " ");
			output.append(bassSustain + " ");
			output.append(bassRelease + " ");
			output.append(bassWaveform + " ");
			output.append(bassGlissando + " ");
			output.append(bassVibratoDepth + " ");
			output.append(bassVibratoSpeed + " ");
			output.append(bassVibratoWaveform + " ");
			output.append(bassTremeloDepth + " ");
			output.append(bassTremeloWaveform + " ");

			output.append(snareSound + " ");
			output.append(kickSound + " ");
			output.append(hihatSound + " ");
			output.append(drumsVolume + " ");

			for (int i = 0; i < 64; i++) {
				output.append(synth[i] + " " + bass[i] + " " + snare[i] + " "
						+ kick[i] + " " + hihat[i] + " "
						+ (int) (synthvel[i] * 100) + " " /* To save as an int */
						+ (int) (bassvel[i] * 100) + " "
						+ (int) (snarevel[i] * 100) + " "
						+ (int) (kickvel[i] * 100) + " "
						+ (int) (hihatvel[i] * 100) + " ");
			}

			out.write(output.toString());
			System.out.println("Success loading the Melody.");
			out.close();
		} catch (IOException e) {

		}
	}

	public void loadFromFile() throws FileNotFoundException {
		try {
			FileReader fstream = new FileReader("melody.txt");
			Scanner in = new Scanner(fstream);
			happiness = in.nextInt();
			excitement = in.nextInt();
			confusion = in.nextInt();
			currentMelody = in.nextInt();
			drumBeatSelector = in.nextInt();
			key = in.nextInt();

			for (int i = 0; i < 4; i++) {
				chordProgression[i] = in.nextInt();
				scaleType[i] = in.nextInt();
			}
			start = in.nextInt();
			bitCrusherCrush = in.nextInt();
			bitCrusherDepth = in.nextInt();
			reverbMix = in.nextInt();
			reverbDamping = in.nextInt();
			reverbRoom = in.nextInt();
			globalVolume = in.nextInt();
			tempo = in.nextInt();

			synthAttack = in.nextInt();
			synthDecay = in.nextInt();
			synthSustain = in.nextInt();
			synthRelease = in.nextInt();
			synthWaveform = in.nextInt();
			synthGlissando = in.nextInt();
			synthVibratoDepth = in.nextInt();
			synthVibratoSpeed = in.nextInt();
			synthVibratoWaveform = in.nextInt();
			synthTremeloDepth = in.nextInt();
			synthTremeloWaveform = in.nextInt();

			bassAttack = in.nextInt();
			bassDecay = in.nextInt();
			bassSustain = in.nextInt();
			bassRelease = in.nextInt();
			bassWaveform = in.nextInt();
			bassGlissando = in.nextInt();
			bassVibratoDepth = in.nextInt();
			bassVibratoSpeed = in.nextInt();
			bassVibratoWaveform = in.nextInt();
			bassTremeloDepth = in.nextInt();
			bassTremeloWaveform = in.nextInt();

			snareSound = in.nextInt();
			kickSound = in.nextInt();
			hihatSound = in.nextInt();
			drumsVolume = in.nextInt();

			for (int i = 0; i < 64; i++) {
				synth[i] = in.nextInt();
				bass[i] = in.nextInt();
				snare[i] = in.nextInt();
				kick[i] = in.nextInt();
				hihat[i] = in.nextInt();
				synthvel[i] = (float) (in.nextInt() / 100);
				bassvel[i] = (float) (in.nextInt() / 100);
				snarevel[i] = (float) (in.nextInt() / 100);
				kickvel[i] = (float) (in.nextInt() / 100);
				hihatvel[i] = (float) (in.nextInt() / 100);
			}
			fileRead = true;
		}

		catch (FileNotFoundException e) {
			fileRead = false;
		}
	}
	
	public static int[] getLastAttributes() throws FileNotFoundException {
		try {
			int[] atts = new int[3];
			FileReader fstream = new FileReader("melody.txt");
			Scanner in = new Scanner(fstream);
			for(int i = 0; i < 3; i++){
				atts[i] = in.nextInt();
			}
			return atts;
		}
		catch(Exception e){
			System.out.println("Error getting the past 3 attributes, going to default values...");
			int[] a = {50, 50, 20};
			return a;
		}
	}
	
	public void print(){
		System.out.println("\n------ MELODY GENERATED ------");
		System.out.println("Current Melody: " + prev.currentMelody);
		System.out.println("Happiness: " + happiness + ", Excitement: " + excitement + ", Confusion: " + confusion);
		System.out.println("Chord Progressions: " + chordProgression[0] + " " + chordProgression[1] + " " + chordProgression[2] + " " + chordProgression[3]);
		System.out.println("Scale Types: " + scaleType[0] + " " + scaleType[1] + " " + scaleType[2] + " " + scaleType[3]);
		System.out.println("------------------------------\n");
	}
			
};