package guru.qa.niffler.config;

enum LocalConfig implements Config {
  INSTANCE;

  @Override
  public String frontUrl() {
    return "http://127.0.0.1:3000/";
  }

  @Override
  public String spendUrl() {
    return "http://127.0.0.1:8093/";
  }

  @Override
  public String registrationUrl() {
    return "http://127.0.0.1:9000/register";
  }

  @Override
  public String profileUrl(){return "http://127.0.0.1:3000/profile";}

  @Override
  public String gitHubUrl() {
    return "https://api.github.com/";
  }

}
