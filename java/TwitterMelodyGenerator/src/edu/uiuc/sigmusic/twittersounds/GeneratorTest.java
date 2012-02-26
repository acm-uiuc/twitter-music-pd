package edu.uiuc.sigmusic.twittersounds;

public class GeneratorTest {
	public static void main(String args[]) throws Exception{
		MelodyGenerator m = new MelodyGenerator(0, 0, 0);
		try{
			m.generateMelody();
		}
		catch(Exception e){
			System.out.println("Error Generating Melody");
		}
		
		System.out.print("Chord Progression: \n");
		for(int i = 0; i < 4; i++){
			System.out.print(m.chordProgression[i] + " ");
		}
		System.out.print('\n');
		
		System.out.print("Scale Types: \n");
		for(int i = 0; i < 4; i++){
			System.out.print(m.scaleType[i] + " ");
		}
		System.out.print('\n');
		
		System.out.print("Synth: \n");
		for(int i = 0; i < 64; i++){
			System.out.print(m.synth[i]);
			if(m.synth[i] < 10 && m.synth[i]> -1){
				System.out.print("   ");
			}
			else if(m.synth[i] < 100 || m.synth[i] == -1){
				System.out.print("  ");
			}
			else{
				System.out.print(" ");
			}
			if((i + 1) % 16 == 0) System.out.print('\n');
		}
		System.out.print('\n');
		System.out.print("Bass: \n");
		
		for(int i = 0; i < 64; i++){
			System.out.print(m.bass[i]);
			if(m.bass[i] < 10 && m.bass[i]> -1){
				System.out.print("   ");
			}
			else if(m.bass[i] < 100 || m.bass[i] == -1){
				System.out.print("  ");
			}
			else{
				System.out.print(" ");
			}
			
			if((i + 1) % 16 == 0) System.out.print('\n');
		}
		System.out.print('\n');
		
		System.out.print("Hihat: \n");
		for(int i = 0; i < 64; i++){
			System.out.print(m.hihat[i]);
			if(m.hihat[i] < 10 && m.hihat[i]> -1){
				System.out.print("   ");
			}
			else if(m.hihat[i] < 100 || m.hihat[i] == -1){
				System.out.print("  ");
			}
			else{
				System.out.print(" ");
			}
			
			if((i + 1) % 16 == 0) System.out.print('\n');
		}
		System.out.print('\n');
		
		System.out.print("Snare: \n");
		for(int i = 0; i < 64; i++){
			System.out.print(m.snare[i]);
			if(m.snare[i] < 10 && m.snare[i]> -1){
				System.out.print("   ");
			}
			else if(m.snare[i] < 100 || m.snare[i] == -1){
				System.out.print("  ");
			}
			else{
				System.out.print(" ");
			}
			
			if((i + 1) % 16 == 0) System.out.print('\n');
		}
		System.out.print('\n');
		
		System.out.print("Kick: \n");
		for(int i = 0; i < 64; i++){
			System.out.print(m.kick[i]);
			if(m.kick[i] < 10 && m.kick[i]> -1){
				System.out.print("   ");
			}
			else if(m.kick[i] < 100 || m.kick[i] == -1){
				System.out.print("  ");
			}
			else{
				System.out.print(" ");
			}
			
			if((i + 1) % 16 == 0) System.out.print('\n');
		}
		System.out.print('\n');
		System.out.print('\n');
		
		System.out.print("Previous Chord Progression: \n");
		for(int i = 0; i < 4; i++){
			System.out.print(m.prev.chordProgression[i] + " ");
		}
		System.out.print('\n');
		
		System.out.print("Previous Scale Types: \n");
		for(int i = 0; i < 4; i++){
			System.out.print(m.prev.scaleType[i] + " ");
		}
		System.out.print('\n');
		
		System.out.print("Previous Synth: \n");
		for(int i = 0; i < 64; i++){
			System.out.print(m.prev.synth[i]);
			if(m.prev.synth[i] < 10 && m.prev.synth[i]> -1){
				System.out.print("   ");
			}
			else if(m.prev.synth[i] < 100 || m.prev.synth[i] == -1){
				System.out.print("  ");
			}
			else{
				System.out.print(" ");
			}
			
			if((i + 1) % 16 == 0) System.out.print('\n');
		}
		System.out.print('\n');
		System.out.print("Previous Bass: \n");
		
		for(int i = 0; i < 64; i++){
			System.out.print(m.prev.bass[i]);
			if(m.prev.bass[i] < 10 && m.prev.bass[i]> -1){
				System.out.print("   ");
			}
			else if(m.prev.bass[i] < 100 || m.prev.bass[i] == -1){
				System.out.print("  ");
			}
			else{
				System.out.print(" ");
			}
			
			if((i + 1) % 16 == 0) System.out.print('\n');
		}
		System.out.print('\n');
		
		System.out.print("Previous Hihat: \n");
		for(int i = 0; i < 64; i++){
			System.out.print(m.prev.hihat[i]);
			if(m.prev.hihat[i] < 10 && m.prev.hihat[i]> -1){
				System.out.print("   ");
			}
			else if(m.prev.hihat[i] < 100 || m.prev.hihat[i] == -1){
				System.out.print("  ");
			}
			else{
				System.out.print(" ");
			}
			
			if((i + 1) % 16 == 0) System.out.print('\n');
		}
		System.out.print('\n');
		
		System.out.print("Previous Snare: \n");
		for(int i = 0; i < 64; i++){
			System.out.print(m.prev.snare[i]);
			if(m.prev.snare[i] < 10 && m.prev.snare[i]> -1){
				System.out.print("   ");
			}
			else if(m.prev.snare[i] < 100 || m.prev.snare[i] == -1){
				System.out.print("  ");
			}
			else{
				System.out.print(" ");
			}
			
			if((i + 1) % 16 == 0) System.out.print('\n');
		}
		System.out.print('\n');
		
		System.out.print("Previous Kick: \n");
		for(int i = 0; i < 64; i++){
			System.out.print(m.kick[i]);
			if(m.prev.kick[i] < 10 && m.prev.kick[i]> -1){
				System.out.print("   ");
			}
			else if(m.prev.kick[i] < 100 || m.prev.kick[i] == -1){
				System.out.print("  ");
			}
			else{
				System.out.print(" ");
			}
			
			if((i + 1) % 16 == 0) System.out.print('\n');
		}
		System.out.print('\n');
		System.out.print('\n');
	}
}
