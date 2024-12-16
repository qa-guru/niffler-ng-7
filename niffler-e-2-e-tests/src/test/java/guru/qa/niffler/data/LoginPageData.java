package guru.qa.niffler.data;

import lombok.Data;

@Data
public class LoginPageData {
    protected final String url = "http://auth.niffler.dc:9000/login";
}
