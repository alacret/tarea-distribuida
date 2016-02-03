package mgs.utils;

import mgs.server.GroupMember;

public class Message implements Comparable<Message> {
	
	private int time;
	public int getTime() {
		return time;
	}

	private String message;
	private GroupMember member;
	private int ackNumberMustReceive;
	private int ackNumberReceived = 0;
	
	public Message(int time, String message, GroupMember member, int ackNumber){
		this.time = time;
		this.message = message;
		this.member = member;
		this.ackNumberMustReceive = ackNumber;
	}
	
	public String getMessage(){return message;}

	@Override
	public int compareTo(Message o) {
        if(!(o instanceof Message))
            throw new ClassCastException("Invalid object");
        
        int time = ((Message) o).time;
       
        if(this.time > time)    
            return 1;
        else if ( this.time < time)
            return -1;
        else
            return 0;
	}

	public GroupMember getMember() {
		return member;
	}

	public void increaseAckNumber() {
		ackNumberReceived++;
	}

	public boolean isReadyForConsume() {
		return ackNumberReceived == ackNumberMustReceive;
	}
}