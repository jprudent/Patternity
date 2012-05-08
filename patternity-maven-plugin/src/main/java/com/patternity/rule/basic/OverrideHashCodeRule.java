package com.patternity.rule.basic;

import com.patternity.ast.ClassElement;
import com.patternity.rule.RuleContext;

import static com.patternity.rule.basic.OverrideObjectMethodChecker.reportViolation;

public class OverrideHashCodeRule implements OverrideMethodRule {
  private final String tag;
  private final transient String toString;

  public OverrideHashCodeRule(final String tag) {
    this.tag = tag;
    this.toString = "OverrideHashCodeRule if tag: " + tag;
  }

  @Override
  public void verify(final ClassElement element, final RuleContext context) {
    reportViolation(this, element, context);
  }

  @Override
  public String getSignature() {
    return "hashCode()I";
  }

  @Override
  public String getTag() {
    return tag;
  }

  @Override
  public String toString() {
    return toString;
  }
}
