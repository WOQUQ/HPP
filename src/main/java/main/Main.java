package main;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import readAndWrite.Writer;
import readAndWrite.Reader;
import sorter.Sorter;
import utils.Comment;
import utils.Post;
import utils.Top3;

public class Main {

	public static void main(String[] args) {
		String postPath = "C:/Users/15325/Desktop/posts.dat";
		String commentPath = "C:/Users/15325/Desktop/comments.dat";
		BlockingQueue<Post> postQueue = new LinkedBlockingQueue<Post>();
		BlockingQueue<Comment> commentQueue = new LinkedBlockingQueue<Comment>();
		BlockingQueue<Top3> top3Queue = new LinkedBlockingQueue<Top3>();
		
		Thread inputThread = new Thread(  new Reader( postPath, commentPath,postQueue,commentQueue));
		
//		Reader reader = new Reader(postPath, commentPath,postQueue,commentQueue);
//		reader.run();
		
		
		Thread sorterThread1 = new Thread( new Sorter(postQueue,commentQueue,top3Queue,1));
		//Thread sorterThread2 = new Thread( new Sorter(postQueue,commentQueue,top3Queue,2));
		//Thread sorterThread3 = new Thread( new Sorter(postQueue,commentQueue,top3Queue,3));
		Thread outputThread = new Thread(new Writer(top3Queue));
		
		
		inputThread.start();
		sorterThread1.start();
		//sorterThread2.start();
		//sorterThread3.start();
		outputThread.start();
		try {
			inputThread.join();
			sorterThread1.join();
			//sorterThread2.join();
			//sorterThread3.join();
			outputThread.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
