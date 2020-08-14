package jamuz.acoustid;

import java.util.ArrayList;
import java.util.List;

/**
 * result
 */
class Result {
   String id;
   List<Recording> recordings = new ArrayList<>();
   String score;
}