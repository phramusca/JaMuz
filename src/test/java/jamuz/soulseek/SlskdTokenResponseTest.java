package jamuz.soulseek;

import org.junit.Test;
import static org.junit.Assert.*;

public class SlskdTokenResponseTest {

	@Test
	public void testRecord() {
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
