package jamuz.process.video;

import java.util.ArrayList;
import javax.swing.JTable;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TableCellRendererTooltipTest {

    private static final class DummyVideo extends VideoAbstract {
        DummyVideo() {
            super("t", "s", 0, "", "2020", "", 0, "", 0, "", "", "t", "", "", "", "");
        }
        @Override public String getRelativeFullPath() { return "path/file.mkv"; }
        @Override public void moveFilesAndSrt(ProcessVideo.PathBuffer buffer, DbConnVideo conn, jamuz.utils.SSH myConn) {}
        @Override protected String getVideoSummary() { return "summary"; }
        @Override protected ArrayList<jamuz.process.video.FileInfoVideo> getFilesToCleanup(int a, int b, boolean c, boolean d) { return new ArrayList<>(); }
        @Override public void removeFavorite() {}
        @Override public void addFavorite() {}
        @Override public void removeFromWatchList() {}
        @Override public void addToWatchList() {}
        @Override public void setRating(VideoRating rating) {}
        @Override public void setMyVideo(boolean search) {}
        @Override public boolean isLocal() { return true; }
        @Override public boolean isWatched() { return false; }
        @Override public boolean isMovie() { return true; }
    }

    @Test
    void shouldSetTooltipFromVideoPath() {
        TableCellRendererTooltip renderer = new TableCellRendererTooltip();
        DummyVideo video = new DummyVideo();
        javax.swing.JLabel c = (javax.swing.JLabel) renderer.getTableCellRendererComponent(new JTable(), video, false, false, 0, 0);
        assertEquals("path/file.mkv", c.getToolTipText());
    }
}
