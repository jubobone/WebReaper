package com.twpnn.demo.webreaper;

public class Triplet<Name,Time,Status> {
	   private Name name;
	   private Time time;
	   private Status status;

	   Triplet(Name name, Time time, Status status)
	   {
	    this.name = name;
	    this.time = time;
	    this.status = status;
	   }
	   
	   public Name getName() { return name; }
	   public void setName(Name name) { this.name = name; }

	   public Time getTime() { return time; }
	   public void setTime(Time time) { this.time = time; }

	   public Status getStatus() { return status; }
	   public void setStatus(Status status) { this.status = status;	}
	   
	}


