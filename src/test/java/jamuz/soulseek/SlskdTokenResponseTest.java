package jamuz.soulseek;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SlskdTokenResponseTest {

    @Test
    void record_accessorsReturnConstructorValues() {
        SlskdTokenResponse tokenResponse = new SlskdTokenResponse(
            3600,
            1609459200,
            "testUser",
            1609455600,
            "testToken",
            "Bearer"
        );

        assertEquals(3600, tokenResponse.expires());
        assertEquals(1609459200, tokenResponse.issued());
        assertEquals("testUser", tokenResponse.name());
        assertEquals(1609455600, tokenResponse.notBefore());
        assertEquals("testToken", tokenResponse.token());
        assertEquals("Bearer", tokenResponse.tokenType());
    }
}
