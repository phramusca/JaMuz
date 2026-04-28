package jamuz.soulseek;

import java.util.List;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SlskdDownloadUserTest {

    @Test
    void shouldStoreUsernameAndDirectories() {
        SlskdDownloadDirectory dir = new SlskdDownloadDirectory();
        dir.directory = "/share";

        SlskdDownloadUser user = new SlskdDownloadUser();
        user.username = "alice";
        user.directories = List.of(dir);

        assertEquals("alice", user.username);
        assertEquals("/share", user.directories.get(0).directory);
    }
}
