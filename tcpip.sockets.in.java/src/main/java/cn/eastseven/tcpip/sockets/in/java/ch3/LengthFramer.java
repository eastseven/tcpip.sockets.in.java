package cn.eastseven.tcpip.sockets.in.java.ch3;

import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.log4j.Logger;

public class LengthFramer implements Framer {

	private static final Logger log = Logger.getLogger(LengthFramer.class);
	
	public static final int MAX_MESSAGE_LENGTH = Integer.MAX_VALUE;
	public static final int BYTE_MASK = 0xff;
	public static final int SHORT_MASK = 0xffff;
	public static final int BYTE_SHIFT = 8;
	
	private DataInputStream in;
	
	public LengthFramer(InputStream in) {
		this.in = new DataInputStream(in);
	}
	
	public void frameMsg(byte[] message, OutputStream out) throws IOException {
		if(message.length > MAX_MESSAGE_LENGTH) {
			IOException error = new IOException("message too long");
			log.error(error);
			throw error;
		}
		int prefix = (message.length >> BYTE_SHIFT) & BYTE_MASK;
		int value = message.length & BYTE_MASK;
		out.write(prefix);
		out.write(value);
		
		out.write(message);
		out.flush();
		
		log.debug(prefix + "|" + value + "|" + message);
	}

	public byte[] nextMsg() throws IOException {
		int length = -1;
		
		try {
			log.debug(in);
			length = in.readUnsignedShort();
			log.debug(length);
		} catch (EOFException e) {
			e.printStackTrace();
			log.error(e);
			return null;
		}
		
		byte[] msg = new byte[length];
		in.readFully(msg);
		
		return msg;
	}

}
