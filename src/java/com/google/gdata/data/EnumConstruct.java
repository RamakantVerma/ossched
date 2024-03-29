/* Copyright (c) 2008 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package com.google.gdata.data;

import com.google.gdata.util.common.xml.XmlWriter.Namespace;

import java.util.Set;

/**
 * The EnumConstruct class defines a special type of {@link ValueConstruct}
 * where the attribute values are constrained to a discrete set of valid
 * values.
 */
public abstract class EnumConstruct extends ValueConstruct {

  /**
   * The set of valid values for the enum construct.
   */
  protected Set<String> values;

  /**
   * Constructs a new EnumConstruct instance associated with a particular
   * XML representation and set of expected values.
   */
  protected EnumConstruct(Namespace namespace,
                          String tagName,
                          String attrName,
                          Set<String> values) {
    this(namespace, tagName, attrName, values, null);
  }

  /**
   * Constructs a new EnumConstruct instance associated with a particular
   * XML representation and set of expected values.  An an initial value
   * is provided and it is not {@code null}, the constructed instance will
   * be initialized to the value and will be immutable.
   */
  protected EnumConstruct(Namespace namespace,
                          String tagName,
                          String attrName,
                          Set<String> values,
                          String initialValue) {
    super(namespace, tagName, attrName, initialValue);
    if (values == null) {
      throw new NullPointerException("Null values set");
    }
    if (initialValue != null && !values.contains(initialValue)) {
      throw new IllegalArgumentException("Invalid " + localName + " value:"
          + initialValue);
    }
    this.values = values;
  }

  @Override
  public void setValue(String v) throws IllegalArgumentException {
    if (values != null && !values.contains(v)) {
      throw new IllegalArgumentException("Invalid " + localName + " value:" +v);
    }
    super.setValue(v);
  }
}
