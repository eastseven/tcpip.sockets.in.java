package cn.eastseven.tcpip.sockets.in.java.ch3;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class VoteMsgBinCoder implements VoteMsgCoder {

	public static final int MIN_WIRE_LENGTH = 4;
	public static final int MAX_WIRE_LENGTH = 16;
	public static final int MAGIC = 0x5400;
	public static final int MAGIC_MASK = 0xfc00;
	public static final int MAGIC_SHIFT = 8;
	public static final int RESPONSE_FLAG = 0x0200;
	public static final int INQUIRE_FLAG = 0x0100;
	
	public byte[] toWire(VoteMsg msg) throws IOException {
		ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
		DataOutputStream out = new DataOutputStream(byteStream);
		
		short magicAndFlags = MAGIC;
		if(msg.isInquiry()) magicAndFlags |= INQUIRE_FLAG;
		if(msg.isResponse()) magicAndFlags |= RESPONSE_FLAG;
		
		out.writeShort(magicAndFlags);
		out.writeShort((short) msg.getCandidateID());
		
		if(msg.isResponse()) out.writeLong(msg.getVoteCount());

		out.flush();
		byte[] data = byteStream.toByteArray();
		
		return data;
	}

	public VoteMsg formWire(byte[] input) throws IOException {
		if(input.length < MIN_WIRE_LENGTH) throw new IOException("runt message");
		
		ByteArrayInputStream bs = new ByteArrayInputStream(input);
		DataInputStream in = new DataInputStream(bs);
		int magic = in.readShort();
		if((magic & MAGIC_MASK) != MAGIC) {
			throw new IOException("bad magic #: " + ((magic & MAGIC_MASK) >> MAGIC_SHIFT));
		}
		
		boolean resp = ((magic & RESPONSE_FLAG) != 0);
		boolean inq = ((magic & INQUIRE_FLAG) != 0);
		int candidateID = in.readShort();
		if(candidateID < 0 || candidateID > 1000) {
			throw new IOException("bad candidate ID: " + candidateID);
		}
		
		long count = 0L;
		if(resp) {
			count = in.readLong();
			if(count < 0) throw new IOException("bad vote count: " + count);
		}
		
		return new VoteMsg(resp, inq, candidateID, count);
	}

}
