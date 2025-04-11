package guru.qa.niffler.test.fake;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.ApiLogin;
import guru.qa.niffler.jupiter.annotation.Token;
import guru.qa.niffler.jupiter.extension.ApiLoginExtension;
import guru.qa.niffler.service.impl.AuthApiClient;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;


@Disabled
public class OAuthTest {

  private static final Config CFG = Config.getInstance();
  private final AuthApiClient authApiClient = new AuthApiClient();

  @Test
  @ExtendWith({ApiLoginExtension.class})
  @ApiLogin(username = "duck", password = "12345")
  void oauthExtensionTest(@Token String token) {
    Assertions.assertNotNull(token);
  }
}

