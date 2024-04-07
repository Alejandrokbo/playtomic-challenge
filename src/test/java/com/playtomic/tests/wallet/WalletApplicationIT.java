package com.playtomic.tests.wallet;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@ActiveProfiles(profiles = "test")
public class WalletApplicationIT {

	@Test
	public void emptyTest() {
	}
}
