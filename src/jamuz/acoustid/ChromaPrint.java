package jamuz.acoustid;

/**
 * chromaprint
 */
public class ChromaPrint {
   final String chromaprint;
   final String duration;

   public ChromaPrint(String chromaprint, String duration) {
      this.duration = duration;
      this.chromaprint = chromaprint;
   }

   public String getChromaprint() {
      return chromaprint;
   }

   public String getDuration() {
      return duration;
   }
}