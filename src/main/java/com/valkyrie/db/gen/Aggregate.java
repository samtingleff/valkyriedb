/**
 * Autogenerated by Thrift Compiler (0.8.0)
 *
 * DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
 *  @generated
 */
package com.valkyrie.db.gen;


import java.util.Map;
import java.util.HashMap;
import org.apache.thrift.TEnum;

public enum Aggregate implements org.apache.thrift.TEnum {
  None(1),
  Count(2),
  Sum(3),
  Distinct(4);

  private final int value;

  private Aggregate(int value) {
    this.value = value;
  }

  /**
   * Get the integer value of this enum value, as defined in the Thrift IDL.
   */
  public int getValue() {
    return value;
  }

  /**
   * Find a the enum type by its integer value, as defined in the Thrift IDL.
   * @return null if the value is not found.
   */
  public static Aggregate findByValue(int value) { 
    switch (value) {
      case 1:
        return None;
      case 2:
        return Count;
      case 3:
        return Sum;
      case 4:
        return Distinct;
      default:
        return null;
    }
  }
}
