package guru.qa.niffler.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import guru.qa.niffler.model.rest.CategoryJson;
import guru.qa.niffler.model.rest.SpendJson;
import guru.qa.niffler.model.rest.UserJson;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.List;

@ParametersAreNonnullByDefault
public record TestData(
    @JsonIgnore String password,
    @JsonIgnore List<CategoryJson> categories,
    @JsonIgnore List<SpendJson> spends,
    @JsonIgnore List<UserJson> friends,
    @JsonIgnore List<UserJson> outcomeInvitations,
    @JsonIgnore List<UserJson> incomeInvitations) {

  public TestData(String password) {
    this(password, new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
  }

  public @Nonnull String[] friendsUsernames() {
    return extractUsernames(friends);
  }

  public @Nonnull String[] incomeInvitationsUsernames() {
    return extractUsernames(incomeInvitations);
  }

  public @Nonnull String[] outcomeInvitationsUsernames() {
    return extractUsernames(outcomeInvitations);
  }

  public @Nonnull String[] categoryDescriptions() {
    return categories.stream().map(CategoryJson::name).toArray(String[]::new);
  }

  private @Nonnull String[] extractUsernames(List<UserJson> users) {
    return users.stream().map(UserJson::username).toArray(String[]::new);
  }
}
