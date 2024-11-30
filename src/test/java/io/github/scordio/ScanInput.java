package io.github.scordio;

import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.InvocationInterceptor;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ReflectiveInvocationContext;
import org.junit.jupiter.params.converter.ArgumentConversionException;
import org.junit.jupiter.params.converter.ArgumentConverter;
import org.junit.jupiter.params.converter.ConvertWith;
import org.junit.jupiter.params.support.AnnotationConsumer;
import org.junit.platform.commons.util.Preconditions;

import java.io.InputStream;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.reflect.Method;
import java.util.Objects;
import java.util.Scanner;

import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static java.nio.charset.StandardCharsets.UTF_8;

@Target(PARAMETER)
@Retention(RUNTIME)
@ConvertWith(ScanInput.InputToScannerConverter.class)
@ExtendWith(ScanInput.InputToScannerConverter.class)
public @interface ScanInput {

  String delimiterPattern() default "\\R";

  class InputToScannerConverter implements ArgumentConverter, AnnotationConsumer<ScanInput>, InvocationInterceptor {

    private ScanInput annotation;

    @Override
    public void accept(ScanInput annotation) {
      Preconditions.notNull(annotation, "annotation must not be null");
      this.annotation = annotation;
    }

    @Override
    public Object convert(Object source, ParameterContext context) throws ArgumentConversionException {
      Class<?> sourceType = source.getClass();
      if (!String.class.isAssignableFrom(sourceType)) {
        throw new ArgumentConversionException("Expecting source to be of type String, but was " + sourceType);
      }
      Class<?> targetType = context.getParameter().getType();
      if (!Scanner.class.isAssignableFrom(targetType)) {
        throw new ArgumentConversionException("Expecting target to be of type Scanner, but was " + targetType);
      }
      InputStream inputStream = context.getDeclaringExecutable().getDeclaringClass().getResourceAsStream((String) source);
      return new Scanner(Objects.requireNonNull(inputStream), UTF_8).useDelimiter(annotation.delimiterPattern());
    }

    @Override
    public void interceptTestTemplateMethod(Invocation<Void> invocation,
                                            ReflectiveInvocationContext<Method> invocationContext,
                                            ExtensionContext extensionContext) throws Throwable {
      invocation.proceed();

      Scanner scanner = invocationContext.getArguments().stream()
        .filter(Scanner.class::isInstance)
        .findFirst()
        .map(Scanner.class::cast)
        .orElseThrow();

      scanner.close();
    }
  }

}
