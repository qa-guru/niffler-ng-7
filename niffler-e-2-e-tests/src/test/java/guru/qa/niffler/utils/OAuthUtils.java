package guru.qa.niffler.utils;

import lombok.SneakyThrows;

import javax.annotation.Nonnull;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Base64;

public class OAuthUtils {

    private static final int CODE_VERIFIER_LENGTH = 32;
    private static SecureRandom secureRandom = new SecureRandom();

    public static String generateCodeVerifier() {
        byte[] randomBytes = new byte[CODE_VERIFIER_LENGTH];
        secureRandom.nextBytes(randomBytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes);

    }


    @SneakyThrows
    @Nonnull
    public static String generateCodeChallenge(String codeVerifier) {
        byte[] bytes = codeVerifier.getBytes(StandardCharsets.US_ASCII);
        MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
        messageDigest.update(bytes, 0, bytes.length);
        byte[] digest = messageDigest.digest();
        return Base64.getUrlEncoder().withoutPadding().encodeToString(digest);
    }

    @SneakyThrows
    public static String extractCodeFromUrl(String url) {
        URI uri = new URI(url);
        String query = uri.getQuery();
        if (query == null) return null;

        return Arrays.stream(query.split("&"))
                .filter(param -> param.startsWith("code="))
                .map(param -> param.split("=")[1])
                .findFirst()
                .orElse(null);
    }
}
