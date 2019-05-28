package readAndWrite;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.BlockingQueue;
import java.util.regex.Pattern;

import utils.Comment;
import utils.Post;

public class Reader implements Runnable{

	//Path of the post and comment file
	private String postPath;
	private String commentPath;
	
	//Buffer of the post and comment file
	private BufferedReader postBuffer;
	private BufferedReader commentBuffer;
	
	//the line read by the Buffer
	private String postLine;
	private String commentLine;
	
	//queue of the post and comment
	private BlockingQueue<Post> postQueue;
	private BlockingQueue<Comment> commentQueue;
	
	//check the end of the data
	private Boolean pEndOrNot = false; 
	private Boolean cEndOrNot = false;
	
	//poison pill
	LocalDate poisonPill_Date = LocalDate.of(9999, 12, 31);
	LocalTime poisonPill_Time = LocalTime.of(00, 01, 01, 943000000);
	LocalDateTime poisonPill_DateTime = LocalDateTime.of(poisonPill_Date, poisonPill_Time);
	
	public void run() {
		// TODO Auto-generated method stub
		System.out.println("Reader Start");

		while(!pEndOrNot || !cEndOrNot) {
			if(!pEndOrNot) {
				postReadLine();
				if(pEndOrNot) {
					continue;
				}
				Post post  = getPost();
				try {
					postQueue.put(post);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			if(!cEndOrNot) {
				commentReadLine();
				if(cEndOrNot) {
					continue;
				}
				Comment comment = getComment();
				try {
					commentQueue.put(comment);
					//comment.Print();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			
		}
		
		
		// add the poison pills
		try {
			commentQueue.put(new Comment(poisonPill_DateTime));
			commentQueue.put(new Comment(poisonPill_DateTime));
			commentQueue.put(new Comment(poisonPill_DateTime));
			postQueue.put(new Post(poisonPill_DateTime));
			postQueue.put(new Post(poisonPill_DateTime));
			postQueue.put(new Post(poisonPill_DateTime));
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		try {
			postBuffer.close();
			commentBuffer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("Reader Finish");
	}
	
	
	
	public Reader(String postPath, String commentPath, BlockingQueue<Post> postQueue, BlockingQueue<Comment> commentQueue) {
		this.postPath = postPath;
		this.commentPath = commentPath;
		this.postQueue = postQueue;
		this.commentQueue = commentQueue;
		
		File postFile = new File(postPath);
		File commentFile = new File(commentPath);
		
		try {
			postBuffer = new BufferedReader(new FileReader(postFile)) ;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Don't find the postFile");
		}
		
		
		try {
			commentBuffer = new BufferedReader(new FileReader(commentFile));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Don't find the commentFile");
		}
		
		
	}
	
	
	private void postReadLine() {
		try {
			postLine = postBuffer.readLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("error in the postReadLine");
		}
		if(postLine==null) {
			this.pEndOrNot =  true;
			return;
		}
	}
	
	private void commentReadLine() {
		try {
			commentLine = commentBuffer.readLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("error in the commentReadLine");
		}
		if(commentLine==null) {
			this.cEndOrNot =  true;
			return;
		}
	}
	
	public Post getPost() {
	
		String[] currentLine = postLine.split(Pattern.quote("|"));
		//String[] Line = postLine.split("|");
		
//		for(int i = 0;i<currentLine.length;i++)
//			System.out.println(currentLine[i]);
		String timeData = currentLine[0];
		//System.out.println(timeData);
		
		timeData = timeData.substring(0, timeData.length() - 5);
		//System.out.println(timeData);
		LocalDateTime ts = LocalDateTime.parse(timeData, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS"));
		//System.out.println(ts);
		
		//Integer post_id = Integer.valueOf(currentLine[1]);
		Long post_id = Long.parseLong(currentLine[1]);
		//System.out.println(post_id);
		Long user_id = Long.parseLong(currentLine[2]);
		//String post = currentLine[3];
		String user = currentLine[4];
		
		return new Post(ts,post_id,user_id/*,post*/,user);
	}
	
	public Comment getComment() {
		String[] currentLine = commentLine.split(Pattern.quote("|"));
		Long comment_replied = (long) -1;
		Long post_commented = (long) -1;
		
		//System.out.println("comment: "+ currentLine[5]);
		
		String timeData = currentLine[0];
		if(timeData.length() != 0) {
			timeData = timeData.substring(0, timeData.length() - 5);
			LocalDateTime ts = LocalDateTime.parse(timeData, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS"));
		
			
			Long comment_id = Long.parseLong(currentLine[1]);
			Long user_id = Long.parseLong(currentLine[2]);
			//String comment =  currentLine[3];
			//String user  = currentLine[4];
			try {
				if(!currentLine[5].isEmpty())
					{//System.out.println("is not empty");
						comment_replied = Long.parseLong(currentLine[5]);
					}else {
						comment_replied = (long) 0;
					}
			}catch(NumberFormatException e) {
				//System.out.println("comment_replied is empty");
			}
			if(currentLine.length >6 && !currentLine[6].isEmpty()) {
				post_commented = Long.parseLong(currentLine[6]);
			}else {
				post_commented = (long) 0;
			}
			return new Comment(ts,comment_id,user_id,/*comment,user,*/comment_replied,post_commented);
		}else {
			return new Comment(poisonPill_DateTime);
		}
	}
	
	
}
