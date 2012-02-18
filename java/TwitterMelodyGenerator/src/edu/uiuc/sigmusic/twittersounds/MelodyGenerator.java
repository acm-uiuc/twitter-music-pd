package edu.uiuc.sigmusic.twittersounds;

/**
 * @author SIGMusic Spring 2012
 * 
 * MelodyGenerator class
 * 
 * Handles almost all aspects of generating music
 */

public class MelodyGenerator {
	public int[] synth; // The main melody, higher synth
	public int[] bass;  // Bass line, the lower synth
	public int[] snare; // Snare drum
	public int[] kick; // Kick (Bass) drum
	public int[] hihat; // Hi-hat
	
	public int key = 0; // The key
	public int scale = 0; // Major, minor, etc(?)
	//public int[] chordProgression; // The chord progression (if it becomes implemented)
	
	public int happiness = 0; // Happiness parameter, from 0 to 100, 0 = depressed, 100 = elated
	public int excitement = 0; // Excitement parameter, from 0 to 100, 0 = bored, 100 = excited
	public int confusion = 0; // Confusion parameter, from 0 to 100, 0 = logical, 100 = confused
	
	/*
	int bitcrush = 0;
	int tempo = 0;
	And other PD attributes
	 */
	
	/**
	 * MelodyGenerator Constructor
	 * @param h - The happiness parameter
	 * @param e - The excitement parameter
	 * @param c - The confusion parameter
	 */
	MelodyGenerator(int h, int e, int c){
		
		happiness = h;
		excitement = e;
		confusion = c;
		
		synth = new int[64];
		bass = new int[64];
		snare = new int[64];
		kick = new int[64];
		hihat = new int[64];
		
		for(int i = 0; i < 64; i++){
			synth[i] = i % 8;
			bass[i] = i % 8;
			snare[i] = 1;
			kick[i] = 1;
			hihat[i] = 1;
		}
	}
	
	/**
	 * When this function is called, it takes care of all the work of generating music.
	 */
	
	public void generateMelody(){
		generateKey();
		generateScale();
		generateSynth();
		generateDrums();
	}
	
	/**
	 * Choose which key to be played
	 */
	public void generateKey(){
		
	}
	
	/**
	 * Choose which scale (major or minor) to be played
	 */
	public void generateScale(){
		
	}
	
	/**
	 * Generate the synth line
	 */
	public void generateSynth(){
		// synth = A sweet line
		//double chance = Math.random();
		
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
  	
  	public void saveToFile(){
  	  	
  	}
  	
  	public void loadFromFile(){
  		
  	}
  	
  	
  	/**
  	 *  Future implementable functions
  	 */
  	
  	/*
  	 
	// For easier outputting in sending packets
	
  	public void toString(){
  		
  	}
  	
  	// Allow MelodyGenerator to adjust the PD Attributes, such as bitcrush and tempo. This would help
  	// take advantage of MelodyGenerator's logic and create more effective attribute changes
  	 
  	public void adjustPDAttributes(){
  	
  	}
  	
  	// Allow saving a formatted file of the previous measure's key, synth, bass, and drum lines
  	// to help better judge what to generate next, this would also allow chord/song progression,
  	// smarter adjustment of PD attributes/parameters over time
 
  	
  	
  	public void getPastSynth(){
  	
  	}
  	
  	public void getPastBass(){
  	
  	}
  	
  	public void getPastDrums(){
  	
  	}
  	
  	*/
};