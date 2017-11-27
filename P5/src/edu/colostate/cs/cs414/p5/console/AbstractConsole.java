package edu.colostate.cs.cs414.p5.console;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.InterruptedIOException;
import java.io.PrintStream;

public abstract class AbstractConsole implements Runnable {

	public static final String RED="\033[0;31m";
	public static final String YELLOW="\033[0;33m";
	public static final String WHITE="\033[0;37m";
	public static final String NORM="\033[0m";

	protected String outPutBeforeConsole = "";
	protected BufferedReader fromConsole;
	protected Boolean needsPrompt;
	protected final InputStream input;
	protected final PrintStream output;
	protected final PrintStream error;
	protected int messageCount;

	protected AbstractConsole() {
		needsPrompt = true;
		this.input = System.in;
		this.output = System.out;
		this.error = System.err;
		messageCount = 0;
	}

	public abstract void display(Object msg);
	
	public abstract void displayNoPrompt(Object msg);
	
	public abstract void clear();
	
	public abstract String securePrompt(String prompt) throws IOException;

	protected abstract void handleCommand(String command);

	public synchronized String promptUser(String msg) throws IOException {
		display(msg);
		if(fromConsole != null) synchronized(fromConsole) {
			String ret;
			while( (ret=fromConsole.readLine()) == null);
			needsPrompt = true;
			return ret;
		}
		return "";
	}

	public void accept() {
		fromConsole = new BufferedReader(new InputStreamReader(input));
		int count;
		try{
			String command;
			while (true) {
				count = messageCount;
				synchronized(output) {
					if(needsPrompt)
						display("");
				}
				synchronized(fromConsole) {
					command = fromConsole.readLine();
				}
				
				if(command == null || command.isEmpty()) {
					needsPrompt = true;
					continue;
				} else {
					handleCommand(command);
					needsPrompt = (count == messageCount);
				}

			}
		}
		catch(InterruptedIOException e1) {
			accept();
		}
		catch (Exception e2) {
			error("Error reading from console");
			e2.printStackTrace();
			this.accept();
		}
	}

	public synchronized void error(String msg) {
		display(RED+msg+NORM);
	}

	public synchronized void warning(String msg) {
		display(YELLOW+msg+NORM);
	}

	public synchronized void notice(String msg) {
		display(WHITE+msg+NORM);
	}

	@Override
	public abstract void run();

}
