package guru.qa.niffler.utils;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;
import java.util.Random;

public class OAuthUtils {

    private static final int CODE_VERIFIER_LENGTH = 64;

    public static String generateCodeVerifier() {
        byte[] randomBytes = new byte[CODE_VERIFIER_LENGTH];
        new Random().nextBytes(randomBytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes);

    }


    public static String generateCodeChallenge(String codeVerifier) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(codeVerifier.getBytes(StandardCharsets.UTF_8));
            return Base64.getUrlEncoder().withoutPadding().encodeToString(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 algorithm not found", e);
        }

    }

    public static String extractCodeFromUrl(String url) {
        try {
            URI uri = new URI(url);
            String query = uri.getQuery();
            if (query == null) return null;

            return Arrays.stream(query.split("&"))
                    .filter(param -> param.startsWith("code="))
                    .map(param -> param.split("=")[1])
                    .findFirst()
                    .orElse(null);
        } catch (URISyntaxException e) {
            throw new RuntimeException("Ошибка при разборе URL", e);
        }
    }
}
