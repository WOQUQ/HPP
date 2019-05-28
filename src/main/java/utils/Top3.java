package utils;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class Top3 {

	ArrayList<Post> currentTop3;
	LocalDateTime ts;
	
	
	
	public Top3(LocalDateTime ts) {
		this.currentTop3 = new ArrayList<Post>(3);
		this.ts = ts;
	}
	
	public Top3(ArrayList<Post> top3, LocalDateTime ts) {
		this.currentTop3 = top3;
		this.ts = ts;
	}

	public ArrayList<Post> getCurrentTop3() {
		return currentTop3;
	}

	public void setCurrentTop3(ArrayList<Post> currentTop3) {
		this.currentTop3 = currentTop3;
	}

	public LocalDateTime getTs() {
		return ts;
	}

	public void setTs(LocalDateTime ts) {
		this.ts = ts;
	}
}
