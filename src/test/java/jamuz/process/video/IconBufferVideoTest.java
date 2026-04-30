package jamuz.process.video;

import javax.swing.ImageIcon;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class IconBufferVideoTest {

    /**
     * Fetches a real image from the TheTVDB CDN.  Disabled by default because
     * it requires a live internet connection and an external service that may
     * be unavailable in CI. Run manually to validate the network path.
     */
    @Disabled("Requires live network access to thetvdb.com — run manually")
    @Test
    void getCoverIcon_withLiveUrl_returnsNonNullIcon() {
        String url = "http://thetvdb.com/banners/posters/273181-9.jpg";
        ImageIcon result = IconBufferVideo.getCoverIcon(url, true);
        assertNotNull(result, "Image should be retrieved from the URL");
    }
}
