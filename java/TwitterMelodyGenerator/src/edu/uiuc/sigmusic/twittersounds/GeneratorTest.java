package edu.uiuc.sigmusic.twittersounds;

public class GeneratorTest {
	public static void main(String args[]){
		MelodyGenerator m = new MelodyGenerator(50, 50, 50);
		m.generateMelody();
		for(int i = 0; i < 64; i++){
			System.out.print(m.snare[i]);
			
		}
		System.out.print('\n');
		for(int i = 0; i < 64; i++){
			System.out.print(m.kick[i]);
			
		}
		System.out.print('\n');
		for(int i = 0; i < 64; i++){
			System.out.print(m.hihat[i]);
			
		}
		System.out.print('\n');
		
		
	}
}
