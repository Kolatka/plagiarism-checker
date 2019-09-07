/**
 * 
 */
package util;


public class Hasher {

	public static int hash(String word) {
		int hash = 7;
		for (int i = 0; i < word.length(); i++) {
		    hash = hash*31 + word.charAt(i);
		}
		return hash;
	}
	
}
