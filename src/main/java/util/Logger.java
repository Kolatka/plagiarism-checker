/**
 * 
 */
package util;

/**
 * @author Kolatka
 *
 */
public class Logger {

	String className;
	public Logger(String className){
		this.className = className;
	}
	
	public void log(String message) {
		System.out.println(">>" + className + ": " + message);
	}
	
}
