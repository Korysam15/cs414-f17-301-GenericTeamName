package edu.colostate.cs.cs414.p5.client_server.logger;

import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Logger {
	
	private static final Logger instance = new Logger();
	
	public static Logger getInstance() {
		return instance;
	}
	
	private LOG_LEVEL logLevel;
	private PrintStream output;
	private SimpleDateFormat dateFormat;
	
	public Logger(PrintStream output) {
		if(output == null) {
			throw new IllegalArgumentException("Logger can not be created with a null PrintStream");
		}
		this.output = output;
		logLevel = LOG_LEVEL.DEFAULT;
		dateFormat = new SimpleDateFormat("HH:mm:ss.S", Locale.ENGLISH);
	}
	
	public Logger() {
		this(System.out);
	}
	
	public synchronized void setOutputStream(PrintStream output) {
		if(output == null) {
			throw new IllegalArgumentException("Can not set the output stream to null");
		}
		this.output = output;
	}
	
	public void setLogLevel(LOG_LEVEL logLevel) {
		this.logLevel = logLevel;
	}
	
	public void info(String msg) {
		if(logLevel.equals(LOG_LEVEL.OFF)) {
			return;
		} else if(logLevel.equals(LOG_LEVEL.INFO) ||
				logLevel.equals(LOG_LEVEL.INFO_ERROR) ||
				logLevel.equals(LOG_LEVEL.ALL)) {
			log("[INFO]: ",msg);
		} else {
			// ignore: level does support this
		}
	}
	
	public void debug(String msg) {
		if(logLevel.equals(LOG_LEVEL.OFF)) {
			return;
		} else if(logLevel.equals(LOG_LEVEL.DEBUG) ||
				logLevel.equals(LOG_LEVEL.ALL)) {
			log("[DEBUG]: ",msg);
		} else {
			// ignore: level does support this
		}
	}
	
	public void error(String msg) {
		if(logLevel.equals(LOG_LEVEL.OFF)) {
			return;
		} else if(logLevel.equals(LOG_LEVEL.ERROR) ||
				logLevel.equals(LOG_LEVEL.INFO_ERROR) ||
				logLevel.equals(LOG_LEVEL.ALL)) {
			log("[ERROR]: ",msg);
		} else {
			// ignore: level does support this
		}
	}
	
	private String getDate() {
		return dateFormat.format(new Date());
	}
	
	private synchronized void log(String prefix, String msg) {
		output.println("[" + getDate() + "]" + prefix + msg);		
	}
	
	public static enum LOG_LEVEL {
		DEBUG,
		INFO,
		ERROR,
		INFO_ERROR,
		ALL,
		OFF;
		
		public static final LOG_LEVEL DEFAULT = INFO;
	}
}