package console;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.InterruptedIOException;
import java.io.OutputStream;
import java.io.PrintStream;

import user.Player;


public abstract class AbstractConsole implements Runnable {
	
	public static final String RED="\033[0;31m";
	public static final String YELLOW="\033[0;33m";
	public static final String WHITE="\033[0;37m";
	public static final String NORM="\033[0m";
	
	protected String noParamCommand;
	protected String paramCommand;
	protected String errorMessage;
	protected String outPutBeforeConsole = "";
	protected Player player;
	protected BufferedReader fromConsole;
	protected final InputStream input;
	protected final PrintStream output;
	protected final PrintStream error;
	
	protected AbstractConsole() {
		this.input = System.in;
		this.output = System.out;
		this.error = System.err;
	}
	
	public abstract void display(Object msg);
	
	protected abstract boolean acceptCommand(String command);
	
	protected abstract void handleCommand();
	
	protected abstract void handleCommandError();
	
	public synchronized String promptUser(String msg) throws IOException {
		synchronized(output) {
			output.print(msg + outPutBeforeConsole);
		}
		if(fromConsole != null) synchronized(fromConsole) {
			String ret;
			while( (ret=fromConsole.readLine()) == null);
			return ret;
		}
		return "";
	}
	
	public void accept() {
		fromConsole = new BufferedReader(new InputStreamReader(input));
		try{
			String command;

			while (true) {
				synchronized(output) {
					output.print(outPutBeforeConsole);
				}
				synchronized(fromConsole) {
					command = fromConsole.readLine();
				}
				if(command == null || command.isEmpty())
					continue;
				else if(acceptCommand(command)) {
					handleCommand();
				}
				else {
					handleCommandError();
				}
				
			}
		}
		catch(InterruptedIOException e) {
			accept();
		}
		catch (Exception ex) {
			error("Error reading from console");
			//ex.printStackTrace();
			this.accept();
		} 
	}
	
	public synchronized void error(String msg) {
		synchronized(error) {
			error.println(RED+msg+NORM);
		}
	}
	
	public synchronized void warning(String msg) {
		display(YELLOW+msg+NORM);
	}
	
	public synchronized void notice(String msg) {
		display(WHITE+msg+NORM);
	}
	
	public abstract int getParam();
	
	@Override
	public abstract void run();

}
