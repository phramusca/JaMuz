package jamuz.soulseek;

import static org.junit.Assert.assertEquals;

import java.io.File;
import org.junit.Test;

public class SlskdDockerSharedDirTest {

    @Test
    public void buildSharedDirEnvValue_emptyExclude() {
        assertEquals("/music", SlskdDocker.buildSharedDirEnvValue("/tmp/music", null));
        assertEquals("/music", SlskdDocker.buildSharedDirEnvValue("/tmp/music", ""));
        assertEquals("/music", SlskdDocker.buildSharedDirEnvValue("/tmp/music", "   \n  "));
    }

    @Test
    public void buildSharedDirEnvValue_relativePaths() throws Exception {
        File root = new File("target/slskd-shared-test-root").getAbsoluteFile();
        root.mkdirs();
        String rootPath = root.getCanonicalPath();
        String v = SlskdDocker.buildSharedDirEnvValue(rootPath, "a/b\n!c\n-d");
        assertEquals("/music;/music/a/b;/music/c;/music/d", v);
    }

    @Test
    public void buildSharedDirEnvValue_withOrWithoutPrefixSameResult() throws Exception {
        File root = new File("target/slskd-shared-test-root-prefix").getAbsoluteFile();
        root.mkdirs();
        String rootPath = root.getCanonicalPath();
        String plain = SlskdDocker.buildSharedDirEnvValue(rootPath, "sub");
        String withBang = SlskdDocker.buildSharedDirEnvValue(rootPath, "!sub");
        String withDash = SlskdDocker.buildSharedDirEnvValue(rootPath, "-sub");
        assertEquals(plain, withBang);
        assertEquals(plain, withDash);
        assertEquals("/music;/music/sub", plain);
    }

    @Test
    public void buildSharedDirEnvValue_absoluteUnderRoot() throws Exception {
        File root = new File("target/slskd-shared-test-root2").getAbsoluteFile();
        root.mkdirs();
        File sub = new File(root, "x/y").getAbsoluteFile();
        sub.mkdirs();
        String rootPath = root.getCanonicalPath();
        String subPath = sub.getCanonicalPath();
        String v = SlskdDocker.buildSharedDirEnvValue(rootPath, subPath);
        assertEquals("/music;/music/x/y", v);
    }

    @Test
    public void buildSharedDirEnvValue_skipsOutsideRoot() throws Exception {
        File root = new File("target/slskd-shared-test-root3").getAbsoluteFile();
        root.mkdirs();
        String rootPath = root.getCanonicalPath();
        String v = SlskdDocker.buildSharedDirEnvValue(rootPath, "/etc/passwd");
        assertEquals("/music", v);
    }
}
