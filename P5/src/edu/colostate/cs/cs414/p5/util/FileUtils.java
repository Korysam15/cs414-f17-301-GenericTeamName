package edu.colostate.cs.cs414.p5.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

import edu.colostate.cs.cs414.p5.client_server.logger.Logger;

public class FileUtils {
	private static final Logger LOG = Logger.getInstance();
	
	public static void copyFileUsingStream(File source, File dest) {
	    FileInputStream is = null;
	    FileOutputStream os = null;
	    try {
	        is = new FileInputStream(source);
	        os = new FileOutputStream(dest);
	        byte[] buffer = new byte[1024];
	        int length;
	        while ((length = is.read(buffer)) > 0) {
	            os.write(buffer, 0, length);
	        }
	    } catch(IOException e) {
			LOG.error("IOException occurred when copying: " + source.getAbsolutePath() + " to " + dest.getAbsolutePath());;
		} finally {
	        closeFileStream(is);
	        closeFileStream(os);
	    }
	}

	
	public static void copyFileUsingChannel(File source, File dest) {
		FileChannel sourceChannel = null;
		FileChannel destChannel = null;
		FileInputStream sourceStream = null;
		FileOutputStream destStream = null;
		try {
			sourceStream = new FileInputStream(source);
			sourceChannel = sourceStream.getChannel();
			destStream = new FileOutputStream(dest);
			destChannel = destStream.getChannel();
			destChannel.transferFrom(sourceChannel, 0, sourceChannel.size());
			LOG.info("Successfully copied: " + source.getAbsolutePath() + " to " + dest.getAbsolutePath());
		} catch(IOException e) {
			LOG.error("IOException occurred when copying: " + source.getAbsolutePath() + " to " + dest.getAbsolutePath());;
		} finally{
			closeFileChannel(sourceChannel);
			closeFileChannel(destChannel);
			closeFileStream(sourceStream);
			closeFileStream(destStream);
		}
	}
	
	public static void closeFileChannel(FileChannel channel) {
		if(channel != null && channel.isOpen()) {
			try {
				channel.close();
			} catch (IOException e) {
				LOG.error("IOException occurred when closing the file channel");
			}
		}
	}
	
	public static void closeFileStream(FileInputStream stream) {
		if(stream != null) {
			try {
				stream.close();
			} catch(Exception e) {
				LOG.error("An error occurred when closing the file input stream.");
			}
		}
	}
	
	public static void closeFileStream(FileOutputStream stream) {
		if(stream != null) {
			try {
				stream.close();
			} catch(Exception e) {
				LOG.error("An error occurred when closing the file output stream.");
			}
		}
	}
}
