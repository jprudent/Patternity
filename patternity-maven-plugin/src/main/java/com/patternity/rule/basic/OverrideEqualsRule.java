package com.patternity.rule.basic;

import com.patternity.ast.ClassElement;
import com.patternity.ast.MethodElement;
import com.patternity.rule.Rule;
import com.patternity.rule.RuleContext;
import com.sun.corba.se.spi.orb.StringPair;

import static com.patternity.rule.basic.OverrideObjectMethodChecker.reportViolation;

public class OverrideEqualsRule implements OverrideMethodRule {
  private final String tag;
  private final transient String toString;

  public OverrideEqualsRule(final String tag) {
    this.tag = tag;
    this.toString = "OverrideEqualsRule if tag: " + tag;
  }

  @Override
  public void verify(final ClassElement element, final RuleContext context) {
    reportViolation(this,element,context);
  }

  @Override
  public String getSignature() {
    return "equals(Ljava/lang/Object;)Z";
  }

  @Override
  public String getTag() {
    return tag;
  }

  @Override
  public String toString(){
    return toString;
  }
}
