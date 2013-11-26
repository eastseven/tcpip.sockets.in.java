package cn.eastseven.tcpip.sockets.in.java.ch3;

import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.log4j.Logger;

public class DelimFramer implements Framer {

	private static final Logger log = Logger.getLogger(DelimFramer.class);
	
	private InputStream in;
	private static final byte DELIMITER = "\n".getBytes()[0];
	
	public DelimFramer(InputStream in) {
		this.in = in;
	}
	
	public void frameMsg(byte[] message, OutputStream out) throws IOException {
		for (byte b : message) {
			if(b == DELIMITER) {
				IOException error = new IOException("message contains delimiter");
				log.error(error);
				throw error;
			}
		}
		
		out.write(message);
		out.write(DELIMITER);
		out.flush();
	}

	public byte[] nextMsg() throws IOException {
		ByteArrayOutputStream messageBuffer = new ByteArrayOutputStream();
		int nextByte = -1;
		
		while((nextByte = in.read()) != DELIMITER) {
			if(nextByte == -1) {
				if(messageBuffer.size() == 0) {
					return null;
				} else {
					EOFException error = new EOFException("non-empty message without delimiter");
					log.error(error);
					throw error;
				}
			}
			
			messageBuffer.write(nextByte);
		}
		return messageBuffer.toByteArray();
	}

}
