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

public class OverrideHashCodeRuleTest {
  private static final String VALUE_OBJECT = "ValueObject";

  private OverrideHashCodeRule rule = new OverrideHashCodeRule(VALUE_OBJECT);
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
  public void ruleIsVerified_classIsTagged_HashCodeOverridden_WithOverrideAnnotation() throws IOException {
    final ClassElement hashCodeOverriddenWithAnnotation = scanClass(VO_HashCodeOverridden_WithAnnotation.class);
    when(context.isMarked(hashCodeOverriddenWithAnnotation, VALUE_OBJECT)).thenReturn(true);

    rule.verify(hashCodeOverriddenWithAnnotation, context);

    verify(context).isMarked(hashCodeOverriddenWithAnnotation, VALUE_OBJECT);
    verify(context, never()).reportViolation(eq(rule), anyString());
  }

  @Test
  public void ruleIsVerified_classIsTagged_HashCodeOverridden_NoOverrideAnnotation() throws IOException {
    final ClassElement hashCodeOverriddenNoAnnotation = scanClass(VO_HashCodeOverridden_NoAnnotation.class);
    when(context.isMarked(hashCodeOverriddenNoAnnotation, VALUE_OBJECT)).thenReturn(true);

    rule.verify(hashCodeOverriddenNoAnnotation, context);

    verify(context).isMarked(hashCodeOverriddenNoAnnotation, VALUE_OBJECT);
    verify(context, never()).reportViolation(eq(rule), anyString());
  }

  @Test
  public void ruleIsNotVerified_classIsTagged_HashCodeNotOverridden() throws IOException {
    final ClassElement hashCodeNotOverridden = scanClass(VO_HashCodeNotOverridden.class);
    when(context.isMarked(hashCodeNotOverridden, VALUE_OBJECT)).thenReturn(true);

    rule.verify(hashCodeNotOverridden, context);
    verify(context).isMarked(hashCodeNotOverridden, VALUE_OBJECT);
    verify(context).reportViolation(eq(rule), eq("hashCode()I is not overridden"));
  }

  @Test
  public void ruleIsNotVerified_classIsTagged_HashCodeOverloaded() throws IOException {
    final ClassElement hashCodeOverloaded = scanClass(VO_HashCodeOverloaded.class);
    when(context.isMarked(hashCodeOverloaded, VALUE_OBJECT)).thenReturn(true);

    rule.verify(hashCodeOverloaded, context);
    verify(context).isMarked(hashCodeOverloaded, VALUE_OBJECT);
    verify(context).reportViolation(eq(rule), eq("hashCode()I is not overridden"));
  }


  private static class NotTagged {
  }

  @ValueObject
  private static class VO_HashCodeOverridden_WithAnnotation {
    @Override
    public int hashCode() {
      return super.hashCode();
    }
  }

  @ValueObject
  private static class VO_HashCodeOverridden_NoAnnotation {

    @Override
    public int hashCode() {
      return super.hashCode();
    }
  }

  @ValueObject
  private static class VO_HashCodeNotOverridden {
  }

  @ValueObject
  private static class VO_HashCodeOverloaded {
    /**
     * This method overloads hashCode
     * @param o
     * @return
     */
    public int hashCode(Object o) {
      return super.hashCode();
    }
  }
}
