/**
 * Autogenerated by Thrift
 *
 * DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
 */
package com.valkyrie.db.gen;

import org.apache.commons.lang.builder.HashCodeBuilder;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.EnumMap;
import java.util.Set;
import java.util.HashSet;
import java.util.EnumSet;
import java.util.Collections;
import java.util.BitSet;
import java.nio.ByteBuffer;
import java.util.Arrays;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Key implements org.apache.thrift.TBase<Key, Key._Fields>, java.io.Serializable, Cloneable {
  private static final org.apache.thrift.protocol.TStruct STRUCT_DESC = new org.apache.thrift.protocol.TStruct("Key");

  private static final org.apache.thrift.protocol.TField BYTES_FIELD_DESC = new org.apache.thrift.protocol.TField("bytes", org.apache.thrift.protocol.TType.STRING, (short)1);
  private static final org.apache.thrift.protocol.TField PARTITION_FIELD_DESC = new org.apache.thrift.protocol.TField("partition", org.apache.thrift.protocol.TType.I32, (short)2);

  private ByteBuffer bytes;
  private int partition;

  /** The set of fields this struct contains, along with convenience methods for finding and manipulating them. */
  public enum _Fields implements org.apache.thrift.TFieldIdEnum {
    BYTES((short)1, "bytes"),
    PARTITION((short)2, "partition");

    private static final Map<String, _Fields> byName = new HashMap<String, _Fields>();

    static {
      for (_Fields field : EnumSet.allOf(_Fields.class)) {
        byName.put(field.getFieldName(), field);
      }
    }

    /**
     * Find the _Fields constant that matches fieldId, or null if its not found.
     */
    public static _Fields findByThriftId(int fieldId) {
      switch(fieldId) {
        case 1: // BYTES
          return BYTES;
        case 2: // PARTITION
          return PARTITION;
        default:
          return null;
      }
    }

    /**
     * Find the _Fields constant that matches fieldId, throwing an exception
     * if it is not found.
     */
    public static _Fields findByThriftIdOrThrow(int fieldId) {
      _Fields fields = findByThriftId(fieldId);
      if (fields == null) throw new IllegalArgumentException("Field " + fieldId + " doesn't exist!");
      return fields;
    }

    /**
     * Find the _Fields constant that matches name, or null if its not found.
     */
    public static _Fields findByName(String name) {
      return byName.get(name);
    }

    private final short _thriftId;
    private final String _fieldName;

    _Fields(short thriftId, String fieldName) {
      _thriftId = thriftId;
      _fieldName = fieldName;
    }

    public short getThriftFieldId() {
      return _thriftId;
    }

    public String getFieldName() {
      return _fieldName;
    }
  }

  // isset id assignments
  private static final int __PARTITION_ISSET_ID = 0;
  private BitSet __isset_bit_vector = new BitSet(1);

  public static final Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> metaDataMap;
  static {
    Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> tmpMap = new EnumMap<_Fields, org.apache.thrift.meta_data.FieldMetaData>(_Fields.class);
    tmpMap.put(_Fields.BYTES, new org.apache.thrift.meta_data.FieldMetaData("bytes", org.apache.thrift.TFieldRequirementType.REQUIRED, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING        , true)));
    tmpMap.put(_Fields.PARTITION, new org.apache.thrift.meta_data.FieldMetaData("partition", org.apache.thrift.TFieldRequirementType.OPTIONAL, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.I32)));
    metaDataMap = Collections.unmodifiableMap(tmpMap);
    org.apache.thrift.meta_data.FieldMetaData.addStructMetaDataMap(Key.class, metaDataMap);
  }

  public Key() {
  }

  public Key(
    ByteBuffer bytes)
  {
    this();
    this.bytes = bytes;
  }

  /**
   * Performs a deep copy on <i>other</i>.
   */
  public Key(Key other) {
    __isset_bit_vector.clear();
    __isset_bit_vector.or(other.__isset_bit_vector);
    if (other.isSetBytes()) {
      this.bytes = org.apache.thrift.TBaseHelper.copyBinary(other.bytes);
;
    }
    this.partition = other.partition;
  }

  public Key deepCopy() {
    return new Key(this);
  }

  @Override
  public void clear() {
    this.bytes = null;
    setPartitionIsSet(false);
    this.partition = 0;
  }

  public byte[] getBytes() {
    setBytes(org.apache.thrift.TBaseHelper.rightSize(bytes));
    return bytes == null ? null : bytes.array();
  }

  public ByteBuffer bufferForBytes() {
    return bytes;
  }

  public void setBytes(byte[] bytes) {
    setBytes(bytes == null ? (ByteBuffer)null : ByteBuffer.wrap(bytes));
  }

  public void setBytes(ByteBuffer bytes) {
    this.bytes = bytes;
  }

  public void unsetBytes() {
    this.bytes = null;
  }

  /** Returns true if field bytes is set (has been assigned a value) and false otherwise */
  public boolean isSetBytes() {
    return this.bytes != null;
  }

  public void setBytesIsSet(boolean value) {
    if (!value) {
      this.bytes = null;
    }
  }

  public int getPartition() {
    return this.partition;
  }

  public void setPartition(int partition) {
    this.partition = partition;
    setPartitionIsSet(true);
  }

  public void unsetPartition() {
    __isset_bit_vector.clear(__PARTITION_ISSET_ID);
  }

  /** Returns true if field partition is set (has been assigned a value) and false otherwise */
  public boolean isSetPartition() {
    return __isset_bit_vector.get(__PARTITION_ISSET_ID);
  }

  public void setPartitionIsSet(boolean value) {
    __isset_bit_vector.set(__PARTITION_ISSET_ID, value);
  }

  public void setFieldValue(_Fields field, Object value) {
    switch (field) {
    case BYTES:
      if (value == null) {
        unsetBytes();
      } else {
        setBytes((ByteBuffer)value);
      }
      break;

    case PARTITION:
      if (value == null) {
        unsetPartition();
      } else {
        setPartition((Integer)value);
      }
      break;

    }
  }

  public Object getFieldValue(_Fields field) {
    switch (field) {
    case BYTES:
      return getBytes();

    case PARTITION:
      return new Integer(getPartition());

    }
    throw new IllegalStateException();
  }

  /** Returns true if field corresponding to fieldID is set (has been assigned a value) and false otherwise */
  public boolean isSet(_Fields field) {
    if (field == null) {
      throw new IllegalArgumentException();
    }

    switch (field) {
    case BYTES:
      return isSetBytes();
    case PARTITION:
      return isSetPartition();
    }
    throw new IllegalStateException();
  }

  @Override
  public boolean equals(Object that) {
    if (that == null)
      return false;
    if (that instanceof Key)
      return this.equals((Key)that);
    return false;
  }

  public boolean equals(Key that) {
    if (that == null)
      return false;

    boolean this_present_bytes = true && this.isSetBytes();
    boolean that_present_bytes = true && that.isSetBytes();
    if (this_present_bytes || that_present_bytes) {
      if (!(this_present_bytes && that_present_bytes))
        return false;
      if (!this.bytes.equals(that.bytes))
        return false;
    }

    boolean this_present_partition = true && this.isSetPartition();
    boolean that_present_partition = true && that.isSetPartition();
    if (this_present_partition || that_present_partition) {
      if (!(this_present_partition && that_present_partition))
        return false;
      if (this.partition != that.partition)
        return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    HashCodeBuilder builder = new HashCodeBuilder();

    boolean present_bytes = true && (isSetBytes());
    builder.append(present_bytes);
    if (present_bytes)
      builder.append(bytes);

    boolean present_partition = true && (isSetPartition());
    builder.append(present_partition);
    if (present_partition)
      builder.append(partition);

    return builder.toHashCode();
  }

  public int compareTo(Key other) {
    if (!getClass().equals(other.getClass())) {
      return getClass().getName().compareTo(other.getClass().getName());
    }

    int lastComparison = 0;
    Key typedOther = (Key)other;

    lastComparison = Boolean.valueOf(isSetBytes()).compareTo(typedOther.isSetBytes());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetBytes()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.bytes, typedOther.bytes);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetPartition()).compareTo(typedOther.isSetPartition());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetPartition()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.partition, typedOther.partition);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    return 0;
  }

  public _Fields fieldForId(int fieldId) {
    return _Fields.findByThriftId(fieldId);
  }

  public void read(org.apache.thrift.protocol.TProtocol iprot) throws org.apache.thrift.TException {
    org.apache.thrift.protocol.TField field;
    iprot.readStructBegin();
    while (true)
    {
      field = iprot.readFieldBegin();
      if (field.type == org.apache.thrift.protocol.TType.STOP) { 
        break;
      }
      switch (field.id) {
        case 1: // BYTES
          if (field.type == org.apache.thrift.protocol.TType.STRING) {
            this.bytes = iprot.readBinary();
          } else { 
            org.apache.thrift.protocol.TProtocolUtil.skip(iprot, field.type);
          }
          break;
        case 2: // PARTITION
          if (field.type == org.apache.thrift.protocol.TType.I32) {
            this.partition = iprot.readI32();
            setPartitionIsSet(true);
          } else { 
            org.apache.thrift.protocol.TProtocolUtil.skip(iprot, field.type);
          }
          break;
        default:
          org.apache.thrift.protocol.TProtocolUtil.skip(iprot, field.type);
      }
      iprot.readFieldEnd();
    }
    iprot.readStructEnd();
    validate();
  }

  public void write(org.apache.thrift.protocol.TProtocol oprot) throws org.apache.thrift.TException {
    validate();

    oprot.writeStructBegin(STRUCT_DESC);
    if (this.bytes != null) {
      oprot.writeFieldBegin(BYTES_FIELD_DESC);
      oprot.writeBinary(this.bytes);
      oprot.writeFieldEnd();
    }
    if (isSetPartition()) {
      oprot.writeFieldBegin(PARTITION_FIELD_DESC);
      oprot.writeI32(this.partition);
      oprot.writeFieldEnd();
    }
    oprot.writeFieldStop();
    oprot.writeStructEnd();
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder("Key(");
    boolean first = true;

    sb.append("bytes:");
    if (this.bytes == null) {
      sb.append("null");
    } else {
      org.apache.thrift.TBaseHelper.toString(this.bytes, sb);
    }
    first = false;
    if (isSetPartition()) {
      if (!first) sb.append(", ");
      sb.append("partition:");
      sb.append(this.partition);
      first = false;
    }
    sb.append(")");
    return sb.toString();
  }

  public void validate() throws org.apache.thrift.TException {
    // check for required fields
    if (!isSetBytes()) {
      throw new org.apache.thrift.protocol.TProtocolException("Required field 'bytes' is unset! Struct:" + toString());
    }

  }

  private void writeObject(java.io.ObjectOutputStream out) throws java.io.IOException {
    try {
      write(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(out)));
    } catch (org.apache.thrift.TException te) {
      throw new java.io.IOException(te);
    }
  }

  private void readObject(java.io.ObjectInputStream in) throws java.io.IOException, ClassNotFoundException {
    try {
      // it doesn't seem like you should have to do this, but java serialization is wacky, and doesn't call the default constructor.
      __isset_bit_vector = new BitSet(1);
      read(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(in)));
    } catch (org.apache.thrift.TException te) {
      throw new java.io.IOException(te);
    }
  }

}

