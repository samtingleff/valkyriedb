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

public class MapReduceResponse implements org.apache.thrift.TBase<MapReduceResponse, MapReduceResponse._Fields>, java.io.Serializable, Cloneable {
  private static final org.apache.thrift.protocol.TStruct STRUCT_DESC = new org.apache.thrift.protocol.TStruct("MapReduceResponse");

  private static final org.apache.thrift.protocol.TField EXISTS_FIELD_DESC = new org.apache.thrift.protocol.TField("exists", org.apache.thrift.protocol.TType.BOOL, (short)1);
  private static final org.apache.thrift.protocol.TField DATA_FIELD_DESC = new org.apache.thrift.protocol.TField("data", org.apache.thrift.protocol.TType.STRING, (short)2);
  private static final org.apache.thrift.protocol.TField COUNTERS_FIELD_DESC = new org.apache.thrift.protocol.TField("counters", org.apache.thrift.protocol.TType.MAP, (short)3);

  private boolean exists;
  private ByteBuffer data;
  private Map<String,Long> counters;

  /** The set of fields this struct contains, along with convenience methods for finding and manipulating them. */
  public enum _Fields implements org.apache.thrift.TFieldIdEnum {
    EXISTS((short)1, "exists"),
    DATA((short)2, "data"),
    COUNTERS((short)3, "counters");

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
        case 1: // EXISTS
          return EXISTS;
        case 2: // DATA
          return DATA;
        case 3: // COUNTERS
          return COUNTERS;
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
  private static final int __EXISTS_ISSET_ID = 0;
  private BitSet __isset_bit_vector = new BitSet(1);

  public static final Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> metaDataMap;
  static {
    Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> tmpMap = new EnumMap<_Fields, org.apache.thrift.meta_data.FieldMetaData>(_Fields.class);
    tmpMap.put(_Fields.EXISTS, new org.apache.thrift.meta_data.FieldMetaData("exists", org.apache.thrift.TFieldRequirementType.REQUIRED, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.BOOL)));
    tmpMap.put(_Fields.DATA, new org.apache.thrift.meta_data.FieldMetaData("data", org.apache.thrift.TFieldRequirementType.OPTIONAL, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING        , true)));
    tmpMap.put(_Fields.COUNTERS, new org.apache.thrift.meta_data.FieldMetaData("counters", org.apache.thrift.TFieldRequirementType.OPTIONAL, 
        new org.apache.thrift.meta_data.MapMetaData(org.apache.thrift.protocol.TType.MAP, 
            new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING), 
            new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.I64))));
    metaDataMap = Collections.unmodifiableMap(tmpMap);
    org.apache.thrift.meta_data.FieldMetaData.addStructMetaDataMap(MapReduceResponse.class, metaDataMap);
  }

  public MapReduceResponse() {
  }

  public MapReduceResponse(
    boolean exists)
  {
    this();
    this.exists = exists;
    setExistsIsSet(true);
  }

  /**
   * Performs a deep copy on <i>other</i>.
   */
  public MapReduceResponse(MapReduceResponse other) {
    __isset_bit_vector.clear();
    __isset_bit_vector.or(other.__isset_bit_vector);
    this.exists = other.exists;
    if (other.isSetData()) {
      this.data = org.apache.thrift.TBaseHelper.copyBinary(other.data);
;
    }
    if (other.isSetCounters()) {
      Map<String,Long> __this__counters = new HashMap<String,Long>();
      for (Map.Entry<String, Long> other_element : other.counters.entrySet()) {

        String other_element_key = other_element.getKey();
        Long other_element_value = other_element.getValue();

        String __this__counters_copy_key = other_element_key;

        Long __this__counters_copy_value = other_element_value;

        __this__counters.put(__this__counters_copy_key, __this__counters_copy_value);
      }
      this.counters = __this__counters;
    }
  }

  public MapReduceResponse deepCopy() {
    return new MapReduceResponse(this);
  }

  @Override
  public void clear() {
    setExistsIsSet(false);
    this.exists = false;
    this.data = null;
    this.counters = null;
  }

  public boolean isExists() {
    return this.exists;
  }

  public void setExists(boolean exists) {
    this.exists = exists;
    setExistsIsSet(true);
  }

  public void unsetExists() {
    __isset_bit_vector.clear(__EXISTS_ISSET_ID);
  }

  /** Returns true if field exists is set (has been assigned a value) and false otherwise */
  public boolean isSetExists() {
    return __isset_bit_vector.get(__EXISTS_ISSET_ID);
  }

  public void setExistsIsSet(boolean value) {
    __isset_bit_vector.set(__EXISTS_ISSET_ID, value);
  }

  public byte[] getData() {
    setData(org.apache.thrift.TBaseHelper.rightSize(data));
    return data == null ? null : data.array();
  }

  public ByteBuffer bufferForData() {
    return data;
  }

  public void setData(byte[] data) {
    setData(data == null ? (ByteBuffer)null : ByteBuffer.wrap(data));
  }

  public void setData(ByteBuffer data) {
    this.data = data;
  }

  public void unsetData() {
    this.data = null;
  }

  /** Returns true if field data is set (has been assigned a value) and false otherwise */
  public boolean isSetData() {
    return this.data != null;
  }

  public void setDataIsSet(boolean value) {
    if (!value) {
      this.data = null;
    }
  }

  public int getCountersSize() {
    return (this.counters == null) ? 0 : this.counters.size();
  }

  public void putToCounters(String key, long val) {
    if (this.counters == null) {
      this.counters = new HashMap<String,Long>();
    }
    this.counters.put(key, val);
  }

  public Map<String,Long> getCounters() {
    return this.counters;
  }

  public void setCounters(Map<String,Long> counters) {
    this.counters = counters;
  }

  public void unsetCounters() {
    this.counters = null;
  }

  /** Returns true if field counters is set (has been assigned a value) and false otherwise */
  public boolean isSetCounters() {
    return this.counters != null;
  }

  public void setCountersIsSet(boolean value) {
    if (!value) {
      this.counters = null;
    }
  }

  public void setFieldValue(_Fields field, Object value) {
    switch (field) {
    case EXISTS:
      if (value == null) {
        unsetExists();
      } else {
        setExists((Boolean)value);
      }
      break;

    case DATA:
      if (value == null) {
        unsetData();
      } else {
        setData((ByteBuffer)value);
      }
      break;

    case COUNTERS:
      if (value == null) {
        unsetCounters();
      } else {
        setCounters((Map<String,Long>)value);
      }
      break;

    }
  }

  public Object getFieldValue(_Fields field) {
    switch (field) {
    case EXISTS:
      return new Boolean(isExists());

    case DATA:
      return getData();

    case COUNTERS:
      return getCounters();

    }
    throw new IllegalStateException();
  }

  /** Returns true if field corresponding to fieldID is set (has been assigned a value) and false otherwise */
  public boolean isSet(_Fields field) {
    if (field == null) {
      throw new IllegalArgumentException();
    }

    switch (field) {
    case EXISTS:
      return isSetExists();
    case DATA:
      return isSetData();
    case COUNTERS:
      return isSetCounters();
    }
    throw new IllegalStateException();
  }

  @Override
  public boolean equals(Object that) {
    if (that == null)
      return false;
    if (that instanceof MapReduceResponse)
      return this.equals((MapReduceResponse)that);
    return false;
  }

  public boolean equals(MapReduceResponse that) {
    if (that == null)
      return false;

    boolean this_present_exists = true;
    boolean that_present_exists = true;
    if (this_present_exists || that_present_exists) {
      if (!(this_present_exists && that_present_exists))
        return false;
      if (this.exists != that.exists)
        return false;
    }

    boolean this_present_data = true && this.isSetData();
    boolean that_present_data = true && that.isSetData();
    if (this_present_data || that_present_data) {
      if (!(this_present_data && that_present_data))
        return false;
      if (!this.data.equals(that.data))
        return false;
    }

    boolean this_present_counters = true && this.isSetCounters();
    boolean that_present_counters = true && that.isSetCounters();
    if (this_present_counters || that_present_counters) {
      if (!(this_present_counters && that_present_counters))
        return false;
      if (!this.counters.equals(that.counters))
        return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    HashCodeBuilder builder = new HashCodeBuilder();

    boolean present_exists = true;
    builder.append(present_exists);
    if (present_exists)
      builder.append(exists);

    boolean present_data = true && (isSetData());
    builder.append(present_data);
    if (present_data)
      builder.append(data);

    boolean present_counters = true && (isSetCounters());
    builder.append(present_counters);
    if (present_counters)
      builder.append(counters);

    return builder.toHashCode();
  }

  public int compareTo(MapReduceResponse other) {
    if (!getClass().equals(other.getClass())) {
      return getClass().getName().compareTo(other.getClass().getName());
    }

    int lastComparison = 0;
    MapReduceResponse typedOther = (MapReduceResponse)other;

    lastComparison = Boolean.valueOf(isSetExists()).compareTo(typedOther.isSetExists());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetExists()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.exists, typedOther.exists);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetData()).compareTo(typedOther.isSetData());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetData()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.data, typedOther.data);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetCounters()).compareTo(typedOther.isSetCounters());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetCounters()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.counters, typedOther.counters);
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
        case 1: // EXISTS
          if (field.type == org.apache.thrift.protocol.TType.BOOL) {
            this.exists = iprot.readBool();
            setExistsIsSet(true);
          } else { 
            org.apache.thrift.protocol.TProtocolUtil.skip(iprot, field.type);
          }
          break;
        case 2: // DATA
          if (field.type == org.apache.thrift.protocol.TType.STRING) {
            this.data = iprot.readBinary();
          } else { 
            org.apache.thrift.protocol.TProtocolUtil.skip(iprot, field.type);
          }
          break;
        case 3: // COUNTERS
          if (field.type == org.apache.thrift.protocol.TType.MAP) {
            {
              org.apache.thrift.protocol.TMap _map0 = iprot.readMapBegin();
              this.counters = new HashMap<String,Long>(2*_map0.size);
              for (int _i1 = 0; _i1 < _map0.size; ++_i1)
              {
                String _key2;
                long _val3;
                _key2 = iprot.readString();
                _val3 = iprot.readI64();
                this.counters.put(_key2, _val3);
              }
              iprot.readMapEnd();
            }
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
    oprot.writeFieldBegin(EXISTS_FIELD_DESC);
    oprot.writeBool(this.exists);
    oprot.writeFieldEnd();
    if (this.data != null) {
      if (isSetData()) {
        oprot.writeFieldBegin(DATA_FIELD_DESC);
        oprot.writeBinary(this.data);
        oprot.writeFieldEnd();
      }
    }
    if (this.counters != null) {
      if (isSetCounters()) {
        oprot.writeFieldBegin(COUNTERS_FIELD_DESC);
        {
          oprot.writeMapBegin(new org.apache.thrift.protocol.TMap(org.apache.thrift.protocol.TType.STRING, org.apache.thrift.protocol.TType.I64, this.counters.size()));
          for (Map.Entry<String, Long> _iter4 : this.counters.entrySet())
          {
            oprot.writeString(_iter4.getKey());
            oprot.writeI64(_iter4.getValue());
          }
          oprot.writeMapEnd();
        }
        oprot.writeFieldEnd();
      }
    }
    oprot.writeFieldStop();
    oprot.writeStructEnd();
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder("MapReduceResponse(");
    boolean first = true;

    sb.append("exists:");
    sb.append(this.exists);
    first = false;
    if (isSetData()) {
      if (!first) sb.append(", ");
      sb.append("data:");
      if (this.data == null) {
        sb.append("null");
      } else {
        org.apache.thrift.TBaseHelper.toString(this.data, sb);
      }
      first = false;
    }
    if (isSetCounters()) {
      if (!first) sb.append(", ");
      sb.append("counters:");
      if (this.counters == null) {
        sb.append("null");
      } else {
        sb.append(this.counters);
      }
      first = false;
    }
    sb.append(")");
    return sb.toString();
  }

  public void validate() throws org.apache.thrift.TException {
    // check for required fields
    if (!isSetExists()) {
      throw new org.apache.thrift.protocol.TProtocolException("Required field 'exists' is unset! Struct:" + toString());
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

