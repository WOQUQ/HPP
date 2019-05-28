package utils;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class Post {

	LocalDateTime ts;
	AtomicLong post_id;
	AtomicLong user_id; 
	//String post;
	String user;
	AtomicInteger score = new AtomicInteger(10);
	AtomicInteger scoreComment = new AtomicInteger(0);
	public ArrayList<LocalDateTime> commentTime = new ArrayList<LocalDateTime>();
	public ArrayList<Long> commenter = new ArrayList<Long>();
	
	
	
	
	public AtomicLong getUser_id() {
		return user_id;
	}

	public void setUser_id(AtomicLong user_id) {
		this.user_id = user_id;
	}

//	public String getPost() {
//		return post;
//	}
//
//	public void setPost(String post) {
//		this.post = post;
//	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public AtomicInteger getScore() {
		return score;
	}

	public void setScore(AtomicInteger score) {
		this.score = score;
	}

	public void setPost_id(AtomicLong post_id) {
		this.post_id = post_id;
	}

	public void setTs(LocalDateTime ts) {
		this.ts = ts;
	}

	
	public ArrayList<LocalDateTime> getCommentTime() {
		return commentTime;
	}

	public void setCommentTime(ArrayList<LocalDateTime> commentTime) {
		this.commentTime = commentTime;
	}

	public AtomicInteger getScoreComment() {
		return scoreComment;
	}

	public void setScoreComment(AtomicInteger scoreComment) {
		this.scoreComment = scoreComment;
	}

	public AtomicLong getPost_id() {
		return post_id;
	}

	
	
	
	public Post(LocalDateTime ts, Long post_id, Long user_id, /*String post, */String user) {
		this.ts = ts;
		this.post_id = new AtomicLong(post_id);
		this.user_id = new AtomicLong(user_id);
		//this.post = post;
		this.user = user;
		//score.set(newValue);
	}
	public Post(LocalDateTime ts) {
		this.ts = ts;
		this.post_id = new AtomicLong(0);
		this.user_id = new AtomicLong(0);
		//this.post = "0";
		this.user = "0";
	}
	public Post(LocalDateTime ts,Long post_id) {
		this.ts = ts;
		this.post_id = new AtomicLong(post_id);
		this.user_id = new AtomicLong(0);
		this.user = "0";
	}
	
	public void Print() {
		System.out.println("ts : "+ts);
		System.out.println("post_id : "+post_id);
		System.out.println("user_id : "+user_id);
		//System.out.println("post : "+post);
		System.out.println("user : "+user);
		System.out.println("score :" +score);
		System.out.println("score_comemnt :" + scoreComment);
	}
	
	public void updateScore(LocalDateTime newTime) {
				
		//System.out.println("update score");
		Duration duration = Duration.between(ts, newTime);
		//update the score of the post
//		for(int i = 0;i<duration.toDays();i++) {
//			if(score.get() != 0) {
//				score.decrementAndGet();
//
//			}else {
//				break;
//			}
//		}
		
		int dayP = (int) duration.toDays();
		if(dayP<10) {
			score.set(10 - dayP);
		}else {
			score.set(0);
		}
		
		
		int newScore = 0;
		//update the score of the comments
		Iterator<LocalDateTime> it = commentTime.iterator();
		while(it.hasNext()) {
			
			Duration durationComment = Duration.between(it.next(), newTime);
			
			int days = (int)durationComment.toDays();
			if(days >10) {
				days = 10;
			}else if(days <0) {
				System.out.println("The time of the update in the Post is in error");
			}
			newScore += (10 - days);
		}
		scoreComment.set(newScore);
	}

	public LocalDateTime getTs() {
		return ts;
	}
			
}
