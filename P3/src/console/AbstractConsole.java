package console;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import user.Player;


public abstract class AbstractConsole implements Runnable {
	
	protected String noParamCommand;
	protected String paramCommand;
	protected String errorMessage;
	protected String outPutBeforeConsole = "";
	protected Player player;
	protected BufferedReader fromConsole;
	
	public abstract void display(Object msg);
	
	protected abstract boolean acceptCommand(String command);
	
	protected abstract void handleCommand();
	
	protected abstract void handleCommandError();
	
	public String promptUser(String msg) throws IOException {
		System.out.print(msg + outPutBeforeConsole);
		if(fromConsole != null) {
			return fromConsole.readLine();
		}
		return "";
	}
	
	public void accept() {
		fromConsole = new BufferedReader(new InputStreamReader(System.in));
		try{
			String command;

			while (true) {
				System.out.print(outPutBeforeConsole);
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
