package jamuz.acoustid;

/**
 * chromaprint
 */
public class ChromaPrint {
   final String chromaprint;
   final String duration;

	/**
	 *
	 * @param chromaprint
	 * @param duration
	 */
	public ChromaPrint(String chromaprint, String duration) {
      this.duration = duration;
      this.chromaprint = chromaprint;
   }

	/**
	 *
	 * @return
	 */
	public String getChromaprint() {
      return chromaprint;
   }

	/**
	 *
	 * @return
	 */
	public String getDuration() {
      return duration;
   }
}