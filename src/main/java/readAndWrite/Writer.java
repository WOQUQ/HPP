package readAndWrite;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.concurrent.BlockingQueue;

import utils.Post;
import utils.Top3;

public class Writer implements Runnable{

	BlockingQueue<Top3> top3Queue;
	LocalDate poisonPill_Date = LocalDate.of(9999, 12, 31);
	LocalTime poisonPill_Time = LocalTime.of(00, 01, 01, 943000000);
	LocalDateTime poisonPill_DateTime = LocalDateTime.of(poisonPill_Date, poisonPill_Time);
	
	public Writer(BlockingQueue<Top3> top3Queue) {
		this.top3Queue = top3Queue;
	}
	public void run() {
		System.out.println("Writer Start");
		File outputFile = new File("./src/main/resources/output.txt");
		FileWriter fwOutput = null;
		BufferedWriter bwOutput = null;
		
		try {
			fwOutput = new FileWriter(outputFile);
			bwOutput = new BufferedWriter(fwOutput);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
				
		Top3 tmp = null;
//		try {
//			tmp = top3Queue.take();
//		} catch (InterruptedException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
		while(true) {

			try {
				tmp = top3Queue.take();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(tmp.getTs().isEqual(poisonPill_DateTime)) {
				break;
			}
			
			try {
				bwOutput.write(getOutput(tmp));
				bwOutput.newLine();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			
			
			
		}
		try {
			bwOutput.close();
			fwOutput.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		System.out.println("Writer finish");
	}
	private String getOutput(Top3 top3s) {

			String ts = top3s.getTs().toString();

			StringBuilder ans = new StringBuilder();

			ans.append(ts).append("+0000").append(',');
			// We add to ans the posts stored in top3sQueue
			for (Post p : top3s.getCurrentTop3()) {
				ans.append(p.getPost_id()).append(',').append(p.getUser()).append(',')
			.append(p.getScore().get()+p.getScoreComment().get()).append(',').append(p.commenter.size()).append(',');
			}

			// If there is less than 3 posts in top3sQueue, we fill ans by dashes
			for (int i = top3s.getCurrentTop3().size(); i < 3; i++) {
				ans.append(",-,-,-,-");
			}

			return ans.toString();
		}
	

	
}
