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

public enum ColumnType implements org.apache.thrift.TEnum {
  ColumnType(1),
  IntegerType(2),
  LongType(3),
  DoubleType(4),
  StringType(5),
  DateType(6);

  private final int value;

  private ColumnType(int value) {
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
  public static ColumnType findByValue(int value) { 
    switch (value) {
      case 1:
        return ColumnType;
      case 2:
        return IntegerType;
      case 3:
        return LongType;
      case 4:
        return DoubleType;
      case 5:
        return StringType;
      case 6:
        return DateType;
      default:
        return null;
    }
  }
}