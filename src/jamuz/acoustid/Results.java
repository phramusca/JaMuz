package jamuz.acoustid;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;

public class Results {
	String status;
	List<Result> results = new ArrayList<>();
	AcoustIdResult bestResult;
	ChromaPrint chromaprint;
	
	public AcoustIdResult getBest() {
		if(bestResult==null) {
			if (status.compareTo("ok") == 0 && results.size()>0) {
				Result best = results.stream().max(Comparator.comparing(Result::getScore)).get();
				try {
					bestResult = best.getFirst();
				} catch(NoSuchElementException ex) {
				}
			}	
		}
		return bestResult;
	}

	public ChromaPrint getChromaprint() {
		return chromaprint;
	}
}