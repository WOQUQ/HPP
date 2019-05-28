package utils;

import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class Comment {
	LocalDateTime ts;
	AtomicLong comment_id;
	AtomicLong user_id;
	//String comment;
	//String user;
	AtomicLong comment_replied = new AtomicLong(0);
	AtomicLong post_commented = new AtomicLong(0);
	
	
	
	
	public AtomicLong getUser_id() {
		return user_id;
	}

	public void setUser_id(AtomicLong user_id) {
		this.user_id = user_id;
	}

	public AtomicLong getComment_id() {
		return comment_id;
	}

	public AtomicLong getComment_replied() {
		return comment_replied;
	}

	public AtomicLong getPost_commented() {
		return post_commented;
	}

	public void setPost_commented(AtomicLong post_commented) {
		this.post_commented = post_commented;
	}

	public LocalDateTime getTs() {
		return ts;
	}

	public Comment(LocalDateTime ts,Long comment_id,Long user_id,/*String comment,String user,*/Long comment_replied,Long post_commented) {
		this.ts = ts;
		this.comment_id = new AtomicLong(comment_id);
		this.user_id = new AtomicLong(user_id);
		//this.comment = comment;
		//this.user = user;
		
		this.comment_replied = new AtomicLong(comment_replied);
		this.post_commented = new AtomicLong(post_commented);
	}
	//for the poison pills
	public Comment(LocalDateTime ts) {
		this.ts = ts;
		this.comment_id = new AtomicLong(0);
		this.user_id = new AtomicLong(0);
		//this.comment = "0";
		//this.user = "0";
		this.comment_replied = new AtomicLong(0);
		this.post_commented = new AtomicLong(0);
	}
	
	public void Print() {
		System.out.println("ts : "+ts);
		System.out.println("comment_id : "+comment_id);
		System.out.println("user_id : "+user_id);
		//System.out.println("comment : "+comment);
		//System.out.println("user : "+user);
		System.out.println("comment_replied : "+comment_replied);
		System.out.println("post_commented : "+post_commented);
		
	}
}
