package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.api.GhApiClient;
import guru.qa.niffler.jupiter.annotation.DisabledByIssue;
import org.junit.jupiter.api.extension.ConditionEvaluationResult;
import org.junit.jupiter.api.extension.ExecutionCondition;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.platform.commons.support.AnnotationSupport;
import org.junit.platform.commons.support.SearchOption;

public class IssueExtension implements ExecutionCondition {

    private final GhApiClient ghApiClient = new GhApiClient();

    /* Проверка на решение issue в github перед запуском теста.
    Если не решен и над тестом есть аннотация @DisabledByClose с номером issue,
        тест будет проигнорирован
    Если решен и над тестом есть аннотация @DisabledByClose с номером issue,
        тест будет запущен с сообщением "issue close" */
    @Override
    public ConditionEvaluationResult evaluateExecutionCondition(ExtensionContext context) {
        return AnnotationSupport.findAnnotation(
                context.getRequiredTestMethod(), DisabledByIssue.class
        ).or(() -> AnnotationSupport.findAnnotation(
                        context.getRequiredTestClass(), DisabledByIssue.class, SearchOption.INCLUDE_ENCLOSING_CLASSES
                )
        ).map(
                byIssue -> "open".equals(ghApiClient.issueState(byIssue.value()))
                        ? ConditionEvaluationResult.disabled("Disabled by issue: " + byIssue.value())
                        : ConditionEvaluationResult.enabled("issue closed")
        ).orElseGet(
                () -> ConditionEvaluationResult.enabled("Annotation @DisabledByIssue bot found")
        );
    }
}
