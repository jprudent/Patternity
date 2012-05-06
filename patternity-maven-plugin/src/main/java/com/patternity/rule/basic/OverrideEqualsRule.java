package com.patternity.rule.basic;

import ch.qos.logback.classic.Logger;
import com.patternity.ast.ClassElement;
import com.patternity.ast.MethodElement;
import com.patternity.rule.Rule;
import com.patternity.rule.RuleContext;
import org.slf4j.LoggerFactory;

public class OverrideEqualsRule implements Rule {
  private final String tag;
  private final transient String toString;

  private static final String EQUALS_SIGNATURE = "equals(Ljava/lang/Object;)Z";

  org.slf4j.Logger log = LoggerFactory.getLogger(this.getClass());

  public OverrideEqualsRule(final String tag) {
    this.tag = tag;
    this.toString = "OverrideEqualsRule if tag: " + tag;
  }

  @Override
  public void verify(final ClassElement element, final RuleContext context) {
    if (element == null) throw new NullPointerException("element is mandatory");
    if (context == null) throw new NullPointerException("context is mandatory");

    if (!context.isMarked(element, tag)) return;

    for(MethodElement method : element.getMethods()){
      if(EQUALS_SIGNATURE.equals(method.getSignature())){
        return;
      }
    }

    context.reportViolation(this, "equals is not overridden");
  }
}
