package _test.automaten.spionage;

import gui.GUI;

public class KGBAutomat {
	public int zustand;
	private char[] alphabet = {'0','1','2','3','4','5','6','7','8','9'};
	
	public boolean imAlphabet(char pZeichen){
		for (char a: alphabet) {
			if (a == pZeichen) {
				return true;
			}
		}
		return false;
	}
	
	public boolean teste(String pEingabe){
		zustand = 0;
		for(int i = 0; i < pEingabe.length(); i++) {
			if (!imAlphabet(pEingabe.charAt(i))) {
				System.out.println("Character nicht im Alphabet!");
				return false;
			}

			switch (pEingabe.charAt(i)) {
				case '0':
					if (zustand == 0 | zustand == 1) {
						zustand++;
					}
					break;
				case '7':
					if(zustand == 1) {
						zustand = 0;
					}else if (zustand == 2) {
						zustand++;
					}
					break;
				default:
					if(zustand == 1 || zustand == 2) {
						zustand = 0;
					}
					break;
			}

			if (zustand == 3) {
				return true;
			}
		}
		return false;
	}
	
	public static void main(String[] args) {
		new GUI(new KGBAutomat());
	}
	
}
