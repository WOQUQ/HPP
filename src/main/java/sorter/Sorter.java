package sorter;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicLong;

import utils.Comment;
import utils.Post;
import utils.Top3;

public class Sorter implements Runnable{

	//queue of the post and comment
	private BlockingQueue<Post> postQueue;
	private BlockingQueue<Comment> commentQueue;
	private BlockingQueue<Top3> top3Queue;
	
	//list for the sorted posts
	private ArrayList<Post> sortedPost = new ArrayList<Post>();
	
	//Map of the post_id and commnet_id
	private Map<Long,Long> commentToPost = new HashMap<Long,Long>();
	
	LocalDate poisonPill_Date = LocalDate.of(9999, 12, 31);
	LocalTime poisonPill_Time = LocalTime.of(00, 01, 01, 943000000);
	LocalDateTime poisonPill_DateTime = LocalDateTime.of(poisonPill_Date, poisonPill_Time);
	long time = 0;
	
	LocalDateTime timeNow;
	
	LocalDate initia_Date = LocalDate.of(2009, 12, 31);
	LocalTime initia_Time = LocalTime.of(00, 01, 01, 943000000);
	LocalDateTime initia_DateTime = LocalDateTime.of(initia_Date, initia_Time);
	Post postOne = new Post(initia_DateTime,(long)111);
	Post postTwo = new Post(initia_DateTime,(long)111);
	Post postThree = new Post(initia_DateTime,(long)111);
	int thread = 0;
	public void run() {
		// TODO Auto-generated method stub
		System.out.println("Sorter start");
//		boolean postIsEnd  = false;
//		boolean commentIsEnd = false;
		
		Post currentPost = getNewPost();
		Comment currentComment = getNewComment();
		
		//test the function of the duration
//		LocalDateTime timePost = currentPost.getTs();
//		LocalDateTime timeComment = currentComment.getTs();
//		Duration duration = Duration.between(timePost, timeComment);	
//		System.out.println(duration.toDays());
		
		while(true) {
			//System.out.println("Thread"+thread+" :"+time);
			//System.out.println(sortedPost.size());
			if(currentPost.getTs().isEqual(poisonPill_DateTime) && currentComment.getTs().isEqual(poisonPill_DateTime)) {
				break;
			}
			
			
			if(currentComment.getTs().isBefore(currentPost.getTs())) {
				//System.out.println("comment");
				dealWithNewComment(currentComment);
				currentComment =  getNewComment();
			}else {
				//System.out.println("post");
				dealWithNewPost(currentPost);
				currentPost = getNewPost();
			}
			
			time++;
			updateList();
			deleteDeadPost();
			checkTheTop3();
		

		
		}
		
		
//		while(!sortedPost.isEmpty()) {
//			
//			
//			
//			
//			timeNow = timeNow.plusDays(1);
//			
//			Iterator<Post> it = sortedPost.iterator();
//			while(it.hasNext()) {
//				//update the score of the each post in the list
//				it.next().updateScore(timeNow);
//			}
//			deleteDeadPost();
//			//updateList();
//			if(!sortedPost.isEmpty()) {
//				System.out.println("score:!!!!!!" + sortedPost.get(0).getScore().get());
//			}
////			try {
////				Thread.currentThread().sleep(100);
////			} catch (InterruptedException e) {
////				// TODO Auto-generated catch block
////				e.printStackTrace();
////			}
//			checkTheTop3();
//			
//		}
		
		
		
		ArrayList<Post> top3List = new ArrayList<Post>();
		top3List.add(new Post(poisonPill_DateTime));
		top3List.add(new Post(poisonPill_DateTime));
		top3List.add(new Post(poisonPill_DateTime));
		try {
			top3Queue.put(new Top3(top3List,poisonPill_DateTime));
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		System.out.println("Sorter finish");
		
		//sortedPost.get(0).Print();
	}

	
	public Sorter(BlockingQueue<Post> postQueue, BlockingQueue<Comment> commentQueue, BlockingQueue<Top3> top3Queue,int thread) {
		this.postQueue = postQueue;
		this.commentQueue = commentQueue;
		this.top3Queue = top3Queue;
		this.thread = thread;
		
	}
	
	private Post getNewPost() {
		Post post = null;
		try {
			 post = postQueue.take();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return post;
		
	}
	private Comment getNewComment() {
		Comment comment = null;
		
		try {
			comment = commentQueue.take();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return comment;
	}
	
	private  void dealWithNewPost(Post post) {
		//读取新POST的时间
		timeNow = post.getTs();
		sortedPost.add(post);
		//System.out.println(sortedPost.size());
		Iterator<Post> it = sortedPost.iterator();
		while(it.hasNext()) {
			//update the score of the each post in the list
			it.next().updateScore(timeNow);
		}
		
		//update the list by the different scores
		updateList();
		deleteDeadPost();
		checkTheTop3();
		
	}
	
	private void dealWithNewComment(Comment comment) {
		timeNow = comment.getTs();
		
		Long post_id = comment.getPost_commented().get();
		//System.out.println("post_id :"+post_id.get());
		Long commenterId = comment.getUser_id().get();
		
		if(post_id == (long)0) {
			//System.out.println("a comment of a comment");
			//AtomicLong comment_id = comment.getComment_replied();
			AtomicLong comment_id = comment.getComment_replied();
			post_id = commentToPost.get(comment_id.get());
		}
		//System.out.println("post_id :"+post_id);
		commentToPost.put(comment.getComment_id().get(), post_id);
		//System.out.println(commentToPost);
		Iterator<Post> it = sortedPost.iterator();
		while(it.hasNext()) {
			//Post post = postOne; 
			Post post = it.next();
			//System.out.println(sortedPost.size());
			//try {
			//post.Print();
			//System.out.println(post_id );
			if( post.getPost_id().get() == post_id
					/*&& (post.getUser_id().get() != comment.getUser_id().get() )*/) {
				//System.out.println("commentTime");
				post.commentTime.add(timeNow);
				
				if(!post.commenter.contains(commenterId) && commenterId != post.getUser_id().get()) {
				post.commenter.add(commenterId);
				//System.out.println(post_id  +" commenter :"+post.commenter);
				}
				
				
			}
//			}catch(NullPointerException e) {
//				
//			}
			post.updateScore(timeNow);
			
			
			
		}
		updateList();
		deleteDeadPost();
		checkTheTop3();
	}
		
		
		
	
	
	
	//对list中的post按分数进行冒泡排序
	private void updateList() {
		
		for(int i =0; i<sortedPost.size(); i++) {
			Post betterPost = sortedPost.get(i);
			int betterScore = betterPost.getScore().get() + betterPost.getScoreComment().get();
			
			for(int j = i+1;j<sortedPost.size(); j++) {
				
				Post comparePost = sortedPost.get(j);
				int compareScore = comparePost.getScore().get() +comparePost.getScoreComment().get();
				
				if(compareScore >betterScore) {
					Post changePost = sortedPost.get(i);
					sortedPost.set(i, sortedPost.get(j));
					sortedPost.set(j, changePost);
				}else if(compareScore == betterScore) {
					//Active posts having the same total score should be ranked based on their timestamps (in descending order),
					//and if their timestamps are also the same, 
					//they should be ranked based on the timestamps of their last received related comments (in descending order).
					
					if(comparePost.getTs().isAfter(betterPost.getTs())) {
						Post changePost = sortedPost.get(i);
						sortedPost.set(i, sortedPost.get(j));
						sortedPost.set(j, changePost);
					}else if(comparePost.getTs().isEqual(betterPost.getTs()) 
									&& comparePost.commentTime.size() > 0 && betterPost.commentTime.size() > 0) {
						if(comparePost.commentTime.get(comparePost.commentTime.size() - 1)
								.isBefore(betterPost.commentTime.get(betterPost.commentTime.size() - 1))) {
							Post changePost = sortedPost.get(i);
							sortedPost.set(i, sortedPost.get(j));
							sortedPost.set(j, changePost);
						}
					}
					
				}
			}
		}
		
		
		
	}
	
	private  void checkTheTop3() {
		//deleteDeadPost();
		//System.out.println(sortedPost.get(0).getCommentTime().size());
		
		if(sortedPost.size() == 0) {
			ArrayList<Post> top3List = new ArrayList<Post>();
			Top3 top3 = new Top3(top3List,timeNow);
			System.out.println("0 :");

			try {
				top3Queue.put(top3);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else if(sortedPost.size() == 1) {
			if(sortedPost.get(0).getPost_id().get() != postOne.getPost_id().get()
					/*|| !postTwo.getTs().isEqual(initia_DateTime)*/ ){
				ArrayList<Post> top3List = new ArrayList<Post>();
				top3List.add(sortedPost.get(0));
				Top3 top3 = new Top3(top3List,timeNow);
				System.out.println("1 :");
				sortedPost.get(0).Print();
				try {
					top3Queue.put(top3);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
//			}else if()) {
//				
//			}
			postOne = sortedPost.get(0);
		}else if(sortedPost.size() == 2) {
			if(sortedPost.get(0).getPost_id().get() != postOne.getPost_id().get() 
								||  sortedPost.get(1).getPost_id().get() != postTwo.getPost_id().get()
								/*|| !postThree.getTs().isEqual(initia_DateTime)*/) {
				ArrayList<Post> top3List = new ArrayList<Post>();
				top3List.add(sortedPost.get(0));
				top3List.add(sortedPost.get(1));
				Top3 top3 = new Top3(top3List,timeNow);
				System.out.println("2 :");
				sortedPost.get(0).Print();
				try {
					top3Queue.put(top3);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			postOne = sortedPost.get(0);
			postTwo = sortedPost.get(1);
		}else if(sortedPost.size()>2){
			if(sortedPost.get(0).getPost_id().get() != postOne.getPost_id().get() 
					||  sortedPost.get(1).getPost_id().get() != postTwo.getPost_id().get()
					|| 	sortedPost.get(2).getPost_id().get() != postThree.getPost_id().get()) {
				ArrayList<Post> top3List = new ArrayList<Post>();
				top3List.add(sortedPost.get(0));
				top3List.add(sortedPost.get(1));
				top3List.add(sortedPost.get(2));
				Top3 top3 = new Top3(top3List,timeNow);
				System.out.println("3 :");
				sortedPost.get(0).Print();
				try {
					top3Queue.put(top3);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			postOne = sortedPost.get(0);
			postTwo = sortedPost.get(1);
			postThree = sortedPost.get(2);
		}
		
		
		
		
				
	}
	
	private  void deleteDeadPost() {
		Iterator<Post> it = sortedPost.iterator();
		
		while(it.hasNext()) {
			Post post = it.next();
			if(post.getScore().get() + post.getScoreComment().get() < (long)1) {
				it.remove();
				System.out.println("remove!!!");
			}
		}
	}
	
}
