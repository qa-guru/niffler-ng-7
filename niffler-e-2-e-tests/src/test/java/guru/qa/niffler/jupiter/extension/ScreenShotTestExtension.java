package guru.qa.niffler.jupiter.extension;

import com.fasterxml.jackson.databind.ObjectMapper;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.ScreenShotTest;
import guru.qa.niffler.model.allure.ScreenDiff;
import io.qameta.allure.Allure;
import lombok.SneakyThrows;
import org.junit.jupiter.api.extension.*;
import org.junit.platform.commons.support.AnnotationSupport;
import org.springframework.core.io.ClassPathResource;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Base64;

public class ScreenShotTestExtension implements ParameterResolver, TestExecutionExceptionHandler {

    public static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(ScreenShotTestExtension.class);

    public static final ObjectMapper objectMapper = new ObjectMapper();

    private static final Config CFG = Config.getInstance();


    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return AnnotationSupport.isAnnotated(extensionContext.getRequiredTestMethod(), ScreenShotTest.class) &&
                parameterContext.getParameter().getType().isAssignableFrom(BufferedImage.class);
    }

    @SneakyThrows
    @Override
    public BufferedImage resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        final ScreenShotTest screenShotTest = extensionContext.getRequiredTestMethod().getAnnotation(ScreenShotTest.class);
        return ImageIO.read(
                new ClassPathResource(
                        CFG.screenshotBaseDir() + screenShotTest.expected()
                ).getInputStream()
        );
    }

    @Override
    public void handleTestExecutionException(ExtensionContext context, Throwable throwable) throws Throwable {
        if (throwable.getMessage().contains("Screen comparison failure")) {

            final ScreenShotTest screenShotTest = context.getRequiredTestMethod().getAnnotation(ScreenShotTest.class);
            if (screenShotTest.rewriteExpected()) {
                final BufferedImage actual = getActual();
                if (actual != null) {
                    ImageIO.write(
                            actual,
                            "png",
                            new File(".screen-output/" + CFG.screenshotBaseDir() + screenShotTest.expected())
                    );
                }
            }
            ScreenDiff screenDiff = new ScreenDiff(
                    "data:image/png;base64," + Base64.getEncoder().encodeToString(imageToBytes(getExpected())),
                    "data:image/png;base64," + Base64.getEncoder().encodeToString(imageToBytes(getActual())),
                    "data:image/png;base64," + Base64.getEncoder().encodeToString(imageToBytes(getDiff()))
            );

            Allure.addAttachment("Screenshot diff",
                    "application/vnd.allure.image.diff",
                    objectMapper.writeValueAsString(screenDiff));
        }

        throw throwable;
    }

    public static void setExpected(BufferedImage expected) {
        TestMethodContextExtension.context().getStore(NAMESPACE).put("expected", expected);
    }

    public static BufferedImage getExpected() {
        return TestMethodContextExtension.context().getStore(NAMESPACE).get("expected", BufferedImage.class);
    }

    public static void setActual(BufferedImage expected) {
        TestMethodContextExtension.context().getStore(NAMESPACE).put("actual", expected);
    }

    public static BufferedImage getActual() {
        return TestMethodContextExtension.context().getStore(NAMESPACE).get("actual", BufferedImage.class);
    }

    public static void setDiff(BufferedImage expected) {
        TestMethodContextExtension.context().getStore(NAMESPACE).put("diff", expected);
    }

    public static BufferedImage getDiff() {
        return TestMethodContextExtension.context().getStore(NAMESPACE).get("diff", BufferedImage.class);
    }

    private static byte[] imageToBytes(BufferedImage image) {
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
            ImageIO.write(image, "png", byteArrayOutputStream);
            return byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
