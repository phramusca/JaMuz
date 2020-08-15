package jamuz.acoustid;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Results {
	String status;
	List<Result> results = new ArrayList<>();
	ChromaPrint chromaprint;
   
	public AcoustIdResult getBest() {
		if (status.compareTo("ok") == 0) {
			Result best = results.stream().max(Comparator.comparing(Result::getScore)).get();
			return best.getFirst();
		}
		return null;
	}

	public ChromaPrint getChromaprint() {
		return chromaprint;
	}
}