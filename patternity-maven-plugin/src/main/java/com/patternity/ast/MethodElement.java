package com.patternity.ast;

/**
 * Represents a method in the metamodel
 */
public class MethodElement extends ModelElement<MethodElement> {
	private final String name;
  private final String signature;

  public MethodElement(String name, String desc) {
    if(name == null || name.length() == 0) throw new NullPointerException("name is mandatory");
    if(desc == null || desc.length() == 0) throw new NullPointerException("signature is mandatory");
		this.name = name;
    this.signature = name+desc;
	}

	public String getName() {
		return name;
	}

  /**
   *
   * @return The signature of the method name(params)
   */
  public String getSignature() {
    return signature;
  }

  @Override
	public ModelType getModelType() {
		return ModelType.METHOD;
	}
}
