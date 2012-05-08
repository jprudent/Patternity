package com.patternity.rule.basic;

import com.patternity.ast.ClassElement;
import com.patternity.data.annotation.ValueObject;
import com.patternity.rule.RuleContext;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.IOException;

import static com.patternity.usecase.TestUsecases.scanClass;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

public class OverrideEqualsRuleTest {
  private static final String VALUE_OBJECT = "ValueObject";

  private OverrideEqualsRule rule = new OverrideEqualsRule(VALUE_OBJECT);
  private RuleContext context;

  @Before
  public void setUp() {
    context = Mockito.mock(RuleContext.class);
  }

  @Test
  public void ruleIsVerified_classIsNotTagged() throws IOException {
    final ClassElement notTagged = scanClass(NotTagged.class);
    when(context.isMarked(notTagged, VALUE_OBJECT)).thenReturn(false);  // NOT TAGGED

    rule.verify(notTagged, context);

    verify(context).isMarked(notTagged, VALUE_OBJECT);
    verify(context, never()).reportViolation(eq(rule), anyString());

  }

  @Test
  public void ruleIsVerified_classIsTagged_EqualsOverridden_WithOverrideAnnotation() throws IOException {
    final ClassElement equalsOverriddenWithAnnotation = scanClass(VO_EqualsOverridden_WithAnnotation.class);
    when(context.isMarked(equalsOverriddenWithAnnotation, VALUE_OBJECT)).thenReturn(true);

    rule.verify(equalsOverriddenWithAnnotation, context);

    verify(context).isMarked(equalsOverriddenWithAnnotation, VALUE_OBJECT);
    verify(context, never()).reportViolation(eq(rule), anyString());
  }

  @Test
  public void ruleIsVerified_classIsTagged_EqualsOverridden_NoOverrideAnnotation() throws IOException {
    final ClassElement equalsOverriddenNoAnnotation = scanClass(VO_EqualsOverridden_NoAnnotation.class);
    when(context.isMarked(equalsOverriddenNoAnnotation, VALUE_OBJECT)).thenReturn(true);

    rule.verify(equalsOverriddenNoAnnotation, context);

    verify(context).isMarked(equalsOverriddenNoAnnotation, VALUE_OBJECT);
    verify(context, never()).reportViolation(eq(rule), anyString());
  }

  @Test
  public void ruleIsNotVerified_classIsTagged_EqualsNotOverridden() throws IOException {
    final ClassElement equalsNotOverridden = scanClass(VO_EqualsNotOverridden.class);
    when(context.isMarked(equalsNotOverridden, VALUE_OBJECT)).thenReturn(true);

    rule.verify(equalsNotOverridden, context);
    verify(context).isMarked(equalsNotOverridden, VALUE_OBJECT);
    verify(context).reportViolation(eq(rule), eq("equals(Ljava/lang/Object;)Z is not overridden"));
  }

  @Test
  public void ruleIsNotVerified_classIsTagged_EqualsOverloaded() throws IOException {
    final ClassElement equalsOverloaded = scanClass(VO_EqualsOverloaded.class);
    when(context.isMarked(equalsOverloaded, VALUE_OBJECT)).thenReturn(true);

    rule.verify(equalsOverloaded, context);
    verify(context).isMarked(equalsOverloaded, VALUE_OBJECT);
    verify(context).reportViolation(eq(rule), eq("equals(Ljava/lang/Object;)Z is not overridden"));
  }


  private static class NotTagged {
  }

  @ValueObject
  private static class VO_EqualsOverridden_WithAnnotation {
    int someField;

    @Override
    public boolean equals(final Object o) {
      return /*Dummy impl*/ true;
    }

  }

  @ValueObject
  private static class VO_EqualsOverridden_NoAnnotation {
    int someField;

    public boolean equals(final Object o) {
      return /*Dummy impl*/ true;
    }
  }

  @ValueObject
  private static class VO_EqualsNotOverridden {
    int someField;
  }

  @ValueObject
  private static class VO_EqualsOverloaded {
    /**
     * This method overloads equals
     * @return
     */
    public boolean equals() {
      return false;
    }
  }
}
