package console;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;

import user.Player;


public abstract class AbstractConsole implements Runnable {
	
	protected String noParamCommand;
	protected String paramCommand;
	protected String errorMessage;
	protected String outPutBeforeConsole = "";
	protected Player player;
	protected BufferedReader fromConsole;
	protected final InputStream input;
	protected final PrintStream output;
	
	protected AbstractConsole() {
		this.input = System.in;
		this.output = System.out;
	}
	
	public abstract void display(Object msg);
	
	protected abstract boolean acceptCommand(String command);
	
	protected abstract void handleCommand();
	
	protected abstract void handleCommandError();
	
	public String promptUser(String msg) throws IOException {
		output.print(msg + outPutBeforeConsole);
		if(fromConsole != null) {
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
				output.print(outPutBeforeConsole);
				command = fromConsole.readLine();
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
		catch (Exception ex) {
			System.out.println("Error reading from console");
			ex.printStackTrace();
			this.accept();
		} 
	}
	
	public void error(String msg) {
		display("ERROR:\n" + msg);
	}
	
	public void warning(String msg) {
		display("WARNING:\n" + msg);
	}
	
	public void notice(String msg) {
		display("NOTICE:\n" + msg);
	}
	
	public abstract int getParam();
	
	@Override
	public abstract void run();

}
