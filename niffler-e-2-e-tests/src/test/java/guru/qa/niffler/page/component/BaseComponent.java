package guru.qa.niffler.page.component;

import com.codeborne.selenide.SelenideElement;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public abstract class BaseComponent<T extends BaseComponent<?>> {

  protected final SelenideElement self;

  @Nonnull
  protected BaseComponent(SelenideElement self) {
    this.self = self;
  }
}
