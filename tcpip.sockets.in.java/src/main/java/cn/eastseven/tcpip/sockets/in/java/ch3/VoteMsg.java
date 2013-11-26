package cn.eastseven.tcpip.sockets.in.java.ch3;

public class VoteMsg {

	private boolean isInquiry;
	private boolean isResponse;
	private int candidateID;
	private long voteCount;
	
	public static final int MAX_CANDIDATE_ID = 1000;
	
	public VoteMsg(boolean isResponse, boolean isInquiry, int candidateID, long voteCount) throws IllegalArgumentException {
		if(voteCount != 0 && !isResponse) {
			throw new IllegalArgumentException("request vote count must be zero");
		}
		
		if(candidateID < 0 || candidateID > MAX_CANDIDATE_ID) {
			throw new IllegalArgumentException("bad candidate id: " + candidateID);
		}
		
		if(voteCount < 0) {
			throw new IllegalArgumentException("total must be >= zero");
		}
		
		this.candidateID = candidateID;
		this.isResponse = isResponse;
		this.isInquiry = isInquiry;
		this.voteCount = voteCount;
	}
	
	public void setInquiry(boolean isInquiry) {
		this.isInquiry = isInquiry;
	}
	
	public void setResponse(boolean isResponse) {
		this.isResponse = isResponse;
	}
	
	public boolean isInquiry() {
		return isInquiry;
	}
	
	public boolean isResponse() {
		return isResponse;
	}
	
	public void setCandidateID(int candidateID) throws IllegalArgumentException {
		if(candidateID < 0 || candidateID > MAX_CANDIDATE_ID) {
			throw new IllegalArgumentException("bad candidate id: " + candidateID);
		}
		
		this.candidateID = candidateID;
	}
	
	public int getCandidateID() {
		return candidateID;
	}
	
	public void setVoteCount(long voteCount) {
		if(voteCount < 0) {
			throw new IllegalArgumentException("total must be >= zero");
		}
		this.voteCount = voteCount;
	}
	
	public long getVoteCount() {
		return voteCount;
	}
	
	@Override
	public String toString() {
		String res = (isInquiry ? "inquiry" : "vote") + " for candidate " + candidateID;
		if(isResponse) {
			res = "response to" + res + " who now has " + voteCount + " vote(s)";
		}
		return res;
	}
}
