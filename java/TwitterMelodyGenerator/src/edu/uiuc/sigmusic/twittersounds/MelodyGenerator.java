

package edu.uiuc.sigmusic.twittersounds;

import java.io.BufferedWriter;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * @author SIGMusic Spring 2012
 * 
 * MelodyGenerator class
 * 
 * Handles almost all aspects of generating music
 */

public class MelodyGenerator {
	
	/**
	 * Default DEBUG values...
	 * Key is generated (for now, C)
	 * Chord Progression is generated (for now, I IV I V)
	 * Synth and Bass will just play scales
	 * Drums will be generated
	 */
	
	public boolean DEBUG = true; // false To generate a random melody, true to generate straight scales for testing
	public boolean fileRead = false; // Flag to see if file reading was successful
	MelodyGenerator prev;
	
	/**
	 * Mood Attributes
	 */
	
	public int happiness = 0; // Happiness parameter, from 0 to 100, 0 = depressed, 100 = elated
	public int excitement = 0; // Excitement parameter, from 0 to 100, 0 = bored, 100 = excited
	public int confusion = 0; // Confusion parameter, from 0 to 100, 0 = logical, 100 = confused
	
	/**
	 * Progression Values
	 */
	
	public int key; // The key, this defines the root of the chord progression
	public int scaleType; // Major, minor, etc(?)
	public int[] chordProgression; // The chord progression
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
	public int synthTremeloWaveform;
	
	/**
	 * Bass Values
	 */
	
	public int[] bass;  // Bass line, the lower synth
	public float[] bassvel;  // Bass line, the lower synth velocity
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
	
	public int drumsVolume;
	
	
	/**
	 * MelodyGenerator Constructor
	 * @param h - The happiness parameter
	 * @param e - The excitement parameter
	 * @param c - The confusion parameter
	 * @throws Exception 
	 */
	
	MelodyGenerator() throws Exception{
		try{
			loadFromFile();
		}
		catch(Exception e){
			fileRead = false;
		}
	}
	
	MelodyGenerator(int h, int e, int c){
		
		happiness = h;
		excitement = e;
		confusion = c;
		
		/**
		 * Melody is generated 4 measures at a time, each value in the respective array represents the MIDI
		 * value to be played at that time (0-127, -1 for rest), drums are  defined 0 for rest, 1 for played.
		 * Each value is worth one 16th note in 4/4 time.
		 */
		
		key = 1;
		scaleType = 0;
		
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
		
		boolean up = false;
		
		for(int i = 0; i < 64; i++){
			
			if(i % 8 == 0) up = !up;
			
			if(up){
				synth[i] = i % 8;
				bass[i] = i % 8;
				snare[i] = 1;
				kick[i] = 1;
				hihat[i] = 1;
			}
			else{
				synth[i] = 7 - (i % 8);
				bass[i] = 7 - (i % 8);
				snare[i] = 1; 
				kick[i] = 1;
				hihat[i] = 1;
			}
				
			synthvel[i] = .5f;
			bassvel[i] = .5f;
			snarevel[i] = .5f;
			kickvel[i] = .5f;
			hihatvel[i] = .5f;
			
			if(i < 4){
				chordProgression[i] = 0;
			}
		}
	}
	
	/**
	 * When this function is called, it takes care of all the work of generating music.
	 * @throws Exception 
	 */
	
	public void generateMelody() throws Exception{
		if(!DEBUG){
			generateKey();
			generateScale();
			generateProgression();
			generateSynth();
			generateDrums();
			transpose();
		}
		else{
			generateProgression();
			generateDrums();
			transpose();
			
		}
		try{
			saveToFile();
		}
		catch (Exception e){
			System.out.println("Error Saving");
		}
	}
	
	/**
	 * Choose which key to be played
	 * 0 = C, 1 = C#, 2 = D, 3 = D#, 4 = E, 5 = F, 6 = F#, 7 = G,
	 * 8 = G#, 9 = A, 10 = A#, 11 = B
	 */
	
	public void generateKey(){
		key = 1; // Default C for now
	}
	
	/**
	 * Choose which scale (major or minor) to be played
	 * 
	 */
	
	public void generateScale(){
		if (happiness > 85)
			scaleType = 0;
		else if (happiness > 71)
			scaleType = 1;
		else if (happiness > 57)
			scaleType = 2;
		else if (happiness > 42)
			scaleType = 3;
		else if (happiness > 28)
			scaleType = 4;
		else if (happiness > 14)
			scaleType = 5;
		else
			scaleType = 6;
	}
	
	/**
	 * Generates the progression
	 * 
	 * Some popular 4 chord progressions:
	 * I - IV - V - V
	 * I - I - IV - V
	 * I - IV - I - V
	 * I - IV - V - IV
	 * 
	 * NOTE: I = 0, IV = 3, and such, it'll make transposing much easier.
	 */
	
	public void generateProgression(){
		chordProgression[0] = 0;
		chordProgression[1] = 4;
		chordProgression[2] = 0;
		chordProgression[3] = 6;
	}
	
	/**
	 * Generate the synth line
	 */
	public void generateSynth(){
		// synth = A sweet line
		//double chance = Math.random();
		
		for(int i = 0; i < 64; i++){
			if(i % 2 == 0){ // Logic for eighth notes
				
				//if(i % 2 == 0){
					if(Math.random() * happiness > 20){
						double noteChooser = Math.random();
							if(noteChooser < .25)
								synth[1] = 0;
							if(noteChooser > .25 && noteChooser < .5)
								synth[1] = 2;
							if(noteChooser > .5 && noteChooser < .75)
								synth[1] = 4;
							if(noteChooser > .75)
								synth[1] = 6;
					}
					else{
						synth[i] = (int)(Math.random() * 7);
					}
				//}	
			}
			else
			{
				synth[i] = -1;
			}
			
			if(i == 0 && Math.random() > .1){ // Logic for the first note
				synth[0] = 0;
			}
		}
}
		
	
	/**
	 * Generate the bass line
	 */
  	public void generateBass(){
		// bass = A sick groove
  		for(int i = 0; i < 64; i++){
			if(i == 0 && Math.random() > .1){ // Logic for the first note
				synth[0] = 0;
			}
			if(i != 0 && i % 2 == 0){ // Logic for eigth notes
				
				//if(i % 4 == 0){
					if(Math.random() * happiness > 20){
						double noteChooser = Math.random();
							if(noteChooser < .25)
								synth[1] = 0;
							if(noteChooser > .25 && noteChooser < .5)
								synth[1] = 2;
							if(noteChooser > .5 && noteChooser < .75)
								synth[1] = 4;
							if(noteChooser > .75)
								synth[1] = 6;
					}
					
			}
			else{
				synth[i] = (int)(Math.random() * 7);
			}
		}
  	
  	}
  	
  	/**
  	 * Generate the hihat, snare, and kick
  	 */
  	public void generateDrums(){
  		// hihat = A phat beat
  		// snare = A phat beat
  		// kick = A phat beat
  		generateHihat();
  		generateKick();
  		generateSnare();
  		
  	}
  	
  	public void generateHihat(){
  		for(int i = 0; i < 64; i++){
  			if(i % 2 == 0)
  				hihat[i] = 1;
  			else
  				hihat[i] = 0;
  		}
  	}
  	
  	public void generateSnare(){
  		for(int i = 0; i < 64; i++){
  			if(i % 4 == 0 && i % 8 != 0)
  				snare[i] = 1;
  			else
  				snare[i] = 0;
  		}
  	}
  	
  	public void generateKick(){
  		for(int i = 0; i < 64; i++){
  			if(i % 8 == 0)
  				kick[i] = 1;
  			else
  				kick[i] = 0;
  		}
  	}
  	
  	/**
  	 * Takes notes 0-7 in the instrument arrays and transposes everything into values 0 - 127 representing 
  	 * the correct actual MIDI note to be played, this is for synth and bass only.
  	 */
  	public void transpose(){
  		int[] scaleValues = {0, 2, 4, 6, 7, 9, 11, 12};
  		if (scaleType == 0) // Parallel array that will transpose notes 0 - 7 into a diatonic scale
  			scaleValues = new int[] {0, 2, 4, 6, 7, 9, 11, 12}; 
  		if (scaleType == 1)
  			scaleValues = new int[]{0, 2, 4, 5, 7, 9, 11, 12};
  		if (scaleType == 2)
  			scaleValues = new int[]{0, 2, 4, 5, 7, 9, 10, 12};
  		if (scaleType == 3)
  			scaleValues = new int[]{0, 2, 3, 5, 7, 9, 10, 12};
  		if (scaleType == 4)
  			scaleValues = new int[]{0, 2, 3, 5, 7, 8, 10, 12};
  		if (scaleType == 5)
  			scaleValues = new int[]{0, 1, 3, 5, 7, 8, 10, 12};
  		if (scaleType == 6)
  			scaleValues = new int[]{0, 1, 3, 5, 6, 8, 10, 12};
  		int c = -1; // Current place in the progression that the note will be transposed to
  		
  			
  		for(int i = 0; i < 64; i++){
  			
  			if(i % 16 == 0) // Current place in chord progression
  				c++;
  				
  			if(synth[i] != -1){ // Unless it's a rest, transpose the raw 0 - 7 into the proper note within a scale
  				synth[i] = scaleValues[synth[i]];
  				synth[i] += (chordProgression[c] + 12*5); // Move the note up to the proper scale and octave
  			}
  			
  			if(bass[i] != -1){
  				bass[i] = scaleValues[bass[i]];
  				bass[i] += (chordProgression[c] + 12*3); // Move the note up to the proper scale and octave
  			}
  			
  			// Octave is currently constant, but it should be made variable later (thus the 12 * 5)
  		}
  	}
  	
  	public void saveToFile() throws IOException{
  		try{
  			FileWriter fstream = new FileWriter("melody.txt");
  			BufferedWriter out = new BufferedWriter(fstream);
  			StringBuilder output = new StringBuilder();
  			
  			output.append(happiness + " ");
  			output.append(excitement + " ");
  			output.append(confusion + " ");
  			output.append(key + " ");
  			output.append(scaleType + " ");
  			for(int i = 0; i < 4; i++){
  				output.append(chordProgression[i] + " ");
  			}
  			output.append(start + " ");
  			output.append(bitCrusherCrush + " ");
  			output.append(bitCrusherDepth + " ");
  			output.append(reverbMix + " ");
  			output.append(reverbDamping + " ");
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
  			
  			for(int i = 0; i < 64; i++)
  			{
  				output.append(synth[i] + " "
  						+ bass[i] + " "
  						+ snare[i] + " "
  						+ kick[i] + " "
  						+ hihat[i] + " "
  						+ (int)(synthvel[i]*100) + " " /*To save as an int*/
  						+ (int)(bassvel[i]*100) + " "
  						+ (int)(snarevel[i]*100) + " "
  						+ (int)(kickvel[i]*100) + " "
  						+ (int)(hihatvel[i]*100) + " ");
  			}
  			
  			out.write(output.toString());
  			System.out.println("Success");
  			out.close();
  		}
  		catch(IOException e){
  			
  		}
  	}
  	
  	public void loadFromFile() throws FileNotFoundException{
  		try{
  			FileReader fstream = new FileReader("melody.txt");
  			BufferedReader in = new BufferedReader(fstream);
  		}
  		catch(FileNotFoundException e){
  			fileRead = false;
  		}
  	}
  	
};