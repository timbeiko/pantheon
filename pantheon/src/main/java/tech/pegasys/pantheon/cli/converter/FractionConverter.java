/*
 * Copyright 2019 ConsenSys AG.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package tech.pegasys.pantheon.cli.converter;

import tech.pegasys.pantheon.cli.converter.exception.FractionConversionException;
import tech.pegasys.pantheon.util.number.Fraction;

import picocli.CommandLine;

public class FractionConverter implements CommandLine.ITypeConverter<Float> {

  @Override
  public Float convert(final String value) throws FractionConversionException {
    try {
      return Fraction.fromString(value).getValue();
    } catch (NullPointerException | IllegalArgumentException e) {
      throw new FractionConversionException(value);
    }
  }
}
