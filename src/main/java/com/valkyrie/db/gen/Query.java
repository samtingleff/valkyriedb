/**
 * Autogenerated by Thrift Compiler (0.8.0)
 *
 * DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
 *  @generated
 */
package com.valkyrie.db.gen;

import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.thrift.scheme.IScheme;
import org.apache.thrift.scheme.SchemeFactory;
import org.apache.thrift.scheme.StandardScheme;

import org.apache.thrift.scheme.TupleScheme;
import org.apache.thrift.protocol.TTupleProtocol;
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

public class Query implements org.apache.thrift.TBase<Query, Query._Fields>, java.io.Serializable, Cloneable {
  private static final org.apache.thrift.protocol.TStruct STRUCT_DESC = new org.apache.thrift.protocol.TStruct("Query");

  private static final org.apache.thrift.protocol.TField COLUMNS_FIELD_DESC = new org.apache.thrift.protocol.TField("columns", org.apache.thrift.protocol.TType.LIST, (short)1);
  private static final org.apache.thrift.protocol.TField CONDITIONS_FIELD_DESC = new org.apache.thrift.protocol.TField("conditions", org.apache.thrift.protocol.TType.LIST, (short)2);
  private static final org.apache.thrift.protocol.TField GROUPINGS_FIELD_DESC = new org.apache.thrift.protocol.TField("groupings", org.apache.thrift.protocol.TType.LIST, (short)3);

  private static final Map<Class<? extends IScheme>, SchemeFactory> schemes = new HashMap<Class<? extends IScheme>, SchemeFactory>();
  static {
    schemes.put(StandardScheme.class, new QueryStandardSchemeFactory());
    schemes.put(TupleScheme.class, new QueryTupleSchemeFactory());
  }

  private List<AggregateColumnSpec> columns; // required
  private List<Condition> conditions; // required
  private List<Grouping> groupings; // required

  /** The set of fields this struct contains, along with convenience methods for finding and manipulating them. */
  public enum _Fields implements org.apache.thrift.TFieldIdEnum {
    COLUMNS((short)1, "columns"),
    CONDITIONS((short)2, "conditions"),
    GROUPINGS((short)3, "groupings");

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
        case 1: // COLUMNS
          return COLUMNS;
        case 2: // CONDITIONS
          return CONDITIONS;
        case 3: // GROUPINGS
          return GROUPINGS;
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
  public static final Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> metaDataMap;
  static {
    Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> tmpMap = new EnumMap<_Fields, org.apache.thrift.meta_data.FieldMetaData>(_Fields.class);
    tmpMap.put(_Fields.COLUMNS, new org.apache.thrift.meta_data.FieldMetaData("columns", org.apache.thrift.TFieldRequirementType.REQUIRED, 
        new org.apache.thrift.meta_data.ListMetaData(org.apache.thrift.protocol.TType.LIST, 
            new org.apache.thrift.meta_data.StructMetaData(org.apache.thrift.protocol.TType.STRUCT, AggregateColumnSpec.class))));
    tmpMap.put(_Fields.CONDITIONS, new org.apache.thrift.meta_data.FieldMetaData("conditions", org.apache.thrift.TFieldRequirementType.REQUIRED, 
        new org.apache.thrift.meta_data.ListMetaData(org.apache.thrift.protocol.TType.LIST, 
            new org.apache.thrift.meta_data.StructMetaData(org.apache.thrift.protocol.TType.STRUCT, Condition.class))));
    tmpMap.put(_Fields.GROUPINGS, new org.apache.thrift.meta_data.FieldMetaData("groupings", org.apache.thrift.TFieldRequirementType.REQUIRED, 
        new org.apache.thrift.meta_data.ListMetaData(org.apache.thrift.protocol.TType.LIST, 
            new org.apache.thrift.meta_data.StructMetaData(org.apache.thrift.protocol.TType.STRUCT, Grouping.class))));
    metaDataMap = Collections.unmodifiableMap(tmpMap);
    org.apache.thrift.meta_data.FieldMetaData.addStructMetaDataMap(Query.class, metaDataMap);
  }

  public Query() {
  }

  public Query(
    List<AggregateColumnSpec> columns,
    List<Condition> conditions,
    List<Grouping> groupings)
  {
    this();
    this.columns = columns;
    this.conditions = conditions;
    this.groupings = groupings;
  }

  /**
   * Performs a deep copy on <i>other</i>.
   */
  public Query(Query other) {
    if (other.isSetColumns()) {
      List<AggregateColumnSpec> __this__columns = new ArrayList<AggregateColumnSpec>();
      for (AggregateColumnSpec other_element : other.columns) {
        __this__columns.add(new AggregateColumnSpec(other_element));
      }
      this.columns = __this__columns;
    }
    if (other.isSetConditions()) {
      List<Condition> __this__conditions = new ArrayList<Condition>();
      for (Condition other_element : other.conditions) {
        __this__conditions.add(new Condition(other_element));
      }
      this.conditions = __this__conditions;
    }
    if (other.isSetGroupings()) {
      List<Grouping> __this__groupings = new ArrayList<Grouping>();
      for (Grouping other_element : other.groupings) {
        __this__groupings.add(new Grouping(other_element));
      }
      this.groupings = __this__groupings;
    }
  }

  public Query deepCopy() {
    return new Query(this);
  }

  @Override
  public void clear() {
    this.columns = null;
    this.conditions = null;
    this.groupings = null;
  }

  public int getColumnsSize() {
    return (this.columns == null) ? 0 : this.columns.size();
  }

  public java.util.Iterator<AggregateColumnSpec> getColumnsIterator() {
    return (this.columns == null) ? null : this.columns.iterator();
  }

  public void addToColumns(AggregateColumnSpec elem) {
    if (this.columns == null) {
      this.columns = new ArrayList<AggregateColumnSpec>();
    }
    this.columns.add(elem);
  }

  public List<AggregateColumnSpec> getColumns() {
    return this.columns;
  }

  public void setColumns(List<AggregateColumnSpec> columns) {
    this.columns = columns;
  }

  public void unsetColumns() {
    this.columns = null;
  }

  /** Returns true if field columns is set (has been assigned a value) and false otherwise */
  public boolean isSetColumns() {
    return this.columns != null;
  }

  public void setColumnsIsSet(boolean value) {
    if (!value) {
      this.columns = null;
    }
  }

  public int getConditionsSize() {
    return (this.conditions == null) ? 0 : this.conditions.size();
  }

  public java.util.Iterator<Condition> getConditionsIterator() {
    return (this.conditions == null) ? null : this.conditions.iterator();
  }

  public void addToConditions(Condition elem) {
    if (this.conditions == null) {
      this.conditions = new ArrayList<Condition>();
    }
    this.conditions.add(elem);
  }

  public List<Condition> getConditions() {
    return this.conditions;
  }

  public void setConditions(List<Condition> conditions) {
    this.conditions = conditions;
  }

  public void unsetConditions() {
    this.conditions = null;
  }

  /** Returns true if field conditions is set (has been assigned a value) and false otherwise */
  public boolean isSetConditions() {
    return this.conditions != null;
  }

  public void setConditionsIsSet(boolean value) {
    if (!value) {
      this.conditions = null;
    }
  }

  public int getGroupingsSize() {
    return (this.groupings == null) ? 0 : this.groupings.size();
  }

  public java.util.Iterator<Grouping> getGroupingsIterator() {
    return (this.groupings == null) ? null : this.groupings.iterator();
  }

  public void addToGroupings(Grouping elem) {
    if (this.groupings == null) {
      this.groupings = new ArrayList<Grouping>();
    }
    this.groupings.add(elem);
  }

  public List<Grouping> getGroupings() {
    return this.groupings;
  }

  public void setGroupings(List<Grouping> groupings) {
    this.groupings = groupings;
  }

  public void unsetGroupings() {
    this.groupings = null;
  }

  /** Returns true if field groupings is set (has been assigned a value) and false otherwise */
  public boolean isSetGroupings() {
    return this.groupings != null;
  }

  public void setGroupingsIsSet(boolean value) {
    if (!value) {
      this.groupings = null;
    }
  }

  public void setFieldValue(_Fields field, Object value) {
    switch (field) {
    case COLUMNS:
      if (value == null) {
        unsetColumns();
      } else {
        setColumns((List<AggregateColumnSpec>)value);
      }
      break;

    case CONDITIONS:
      if (value == null) {
        unsetConditions();
      } else {
        setConditions((List<Condition>)value);
      }
      break;

    case GROUPINGS:
      if (value == null) {
        unsetGroupings();
      } else {
        setGroupings((List<Grouping>)value);
      }
      break;

    }
  }

  public Object getFieldValue(_Fields field) {
    switch (field) {
    case COLUMNS:
      return getColumns();

    case CONDITIONS:
      return getConditions();

    case GROUPINGS:
      return getGroupings();

    }
    throw new IllegalStateException();
  }

  /** Returns true if field corresponding to fieldID is set (has been assigned a value) and false otherwise */
  public boolean isSet(_Fields field) {
    if (field == null) {
      throw new IllegalArgumentException();
    }

    switch (field) {
    case COLUMNS:
      return isSetColumns();
    case CONDITIONS:
      return isSetConditions();
    case GROUPINGS:
      return isSetGroupings();
    }
    throw new IllegalStateException();
  }

  @Override
  public boolean equals(Object that) {
    if (that == null)
      return false;
    if (that instanceof Query)
      return this.equals((Query)that);
    return false;
  }

  public boolean equals(Query that) {
    if (that == null)
      return false;

    boolean this_present_columns = true && this.isSetColumns();
    boolean that_present_columns = true && that.isSetColumns();
    if (this_present_columns || that_present_columns) {
      if (!(this_present_columns && that_present_columns))
        return false;
      if (!this.columns.equals(that.columns))
        return false;
    }

    boolean this_present_conditions = true && this.isSetConditions();
    boolean that_present_conditions = true && that.isSetConditions();
    if (this_present_conditions || that_present_conditions) {
      if (!(this_present_conditions && that_present_conditions))
        return false;
      if (!this.conditions.equals(that.conditions))
        return false;
    }

    boolean this_present_groupings = true && this.isSetGroupings();
    boolean that_present_groupings = true && that.isSetGroupings();
    if (this_present_groupings || that_present_groupings) {
      if (!(this_present_groupings && that_present_groupings))
        return false;
      if (!this.groupings.equals(that.groupings))
        return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    HashCodeBuilder builder = new HashCodeBuilder();

    boolean present_columns = true && (isSetColumns());
    builder.append(present_columns);
    if (present_columns)
      builder.append(columns);

    boolean present_conditions = true && (isSetConditions());
    builder.append(present_conditions);
    if (present_conditions)
      builder.append(conditions);

    boolean present_groupings = true && (isSetGroupings());
    builder.append(present_groupings);
    if (present_groupings)
      builder.append(groupings);

    return builder.toHashCode();
  }

  public int compareTo(Query other) {
    if (!getClass().equals(other.getClass())) {
      return getClass().getName().compareTo(other.getClass().getName());
    }

    int lastComparison = 0;
    Query typedOther = (Query)other;

    lastComparison = Boolean.valueOf(isSetColumns()).compareTo(typedOther.isSetColumns());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetColumns()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.columns, typedOther.columns);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetConditions()).compareTo(typedOther.isSetConditions());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetConditions()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.conditions, typedOther.conditions);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetGroupings()).compareTo(typedOther.isSetGroupings());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetGroupings()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.groupings, typedOther.groupings);
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
    schemes.get(iprot.getScheme()).getScheme().read(iprot, this);
  }

  public void write(org.apache.thrift.protocol.TProtocol oprot) throws org.apache.thrift.TException {
    schemes.get(oprot.getScheme()).getScheme().write(oprot, this);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder("Query(");
    boolean first = true;

    sb.append("columns:");
    if (this.columns == null) {
      sb.append("null");
    } else {
      sb.append(this.columns);
    }
    first = false;
    if (!first) sb.append(", ");
    sb.append("conditions:");
    if (this.conditions == null) {
      sb.append("null");
    } else {
      sb.append(this.conditions);
    }
    first = false;
    if (!first) sb.append(", ");
    sb.append("groupings:");
    if (this.groupings == null) {
      sb.append("null");
    } else {
      sb.append(this.groupings);
    }
    first = false;
    sb.append(")");
    return sb.toString();
  }

  public void validate() throws org.apache.thrift.TException {
    // check for required fields
    if (!isSetColumns()) {
      throw new org.apache.thrift.protocol.TProtocolException("Required field 'columns' is unset! Struct:" + toString());
    }

    if (!isSetConditions()) {
      throw new org.apache.thrift.protocol.TProtocolException("Required field 'conditions' is unset! Struct:" + toString());
    }

    if (!isSetGroupings()) {
      throw new org.apache.thrift.protocol.TProtocolException("Required field 'groupings' is unset! Struct:" + toString());
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
      read(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(in)));
    } catch (org.apache.thrift.TException te) {
      throw new java.io.IOException(te);
    }
  }

  private static class QueryStandardSchemeFactory implements SchemeFactory {
    public QueryStandardScheme getScheme() {
      return new QueryStandardScheme();
    }
  }

  private static class QueryStandardScheme extends StandardScheme<Query> {

    public void read(org.apache.thrift.protocol.TProtocol iprot, Query struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TField schemeField;
      iprot.readStructBegin();
      while (true)
      {
        schemeField = iprot.readFieldBegin();
        if (schemeField.type == org.apache.thrift.protocol.TType.STOP) { 
          break;
        }
        switch (schemeField.id) {
          case 1: // COLUMNS
            if (schemeField.type == org.apache.thrift.protocol.TType.LIST) {
              {
                org.apache.thrift.protocol.TList _list0 = iprot.readListBegin();
                struct.columns = new ArrayList<AggregateColumnSpec>(_list0.size);
                for (int _i1 = 0; _i1 < _list0.size; ++_i1)
                {
                  AggregateColumnSpec _elem2; // required
                  _elem2 = new AggregateColumnSpec();
                  _elem2.read(iprot);
                  struct.columns.add(_elem2);
                }
                iprot.readListEnd();
              }
              struct.setColumnsIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 2: // CONDITIONS
            if (schemeField.type == org.apache.thrift.protocol.TType.LIST) {
              {
                org.apache.thrift.protocol.TList _list3 = iprot.readListBegin();
                struct.conditions = new ArrayList<Condition>(_list3.size);
                for (int _i4 = 0; _i4 < _list3.size; ++_i4)
                {
                  Condition _elem5; // required
                  _elem5 = new Condition();
                  _elem5.read(iprot);
                  struct.conditions.add(_elem5);
                }
                iprot.readListEnd();
              }
              struct.setConditionsIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 3: // GROUPINGS
            if (schemeField.type == org.apache.thrift.protocol.TType.LIST) {
              {
                org.apache.thrift.protocol.TList _list6 = iprot.readListBegin();
                struct.groupings = new ArrayList<Grouping>(_list6.size);
                for (int _i7 = 0; _i7 < _list6.size; ++_i7)
                {
                  Grouping _elem8; // required
                  _elem8 = new Grouping();
                  _elem8.read(iprot);
                  struct.groupings.add(_elem8);
                }
                iprot.readListEnd();
              }
              struct.setGroupingsIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          default:
            org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
        }
        iprot.readFieldEnd();
      }
      iprot.readStructEnd();
      struct.validate();
    }

    public void write(org.apache.thrift.protocol.TProtocol oprot, Query struct) throws org.apache.thrift.TException {
      struct.validate();

      oprot.writeStructBegin(STRUCT_DESC);
      if (struct.columns != null) {
        oprot.writeFieldBegin(COLUMNS_FIELD_DESC);
        {
          oprot.writeListBegin(new org.apache.thrift.protocol.TList(org.apache.thrift.protocol.TType.STRUCT, struct.columns.size()));
          for (AggregateColumnSpec _iter9 : struct.columns)
          {
            _iter9.write(oprot);
          }
          oprot.writeListEnd();
        }
        oprot.writeFieldEnd();
      }
      if (struct.conditions != null) {
        oprot.writeFieldBegin(CONDITIONS_FIELD_DESC);
        {
          oprot.writeListBegin(new org.apache.thrift.protocol.TList(org.apache.thrift.protocol.TType.STRUCT, struct.conditions.size()));
          for (Condition _iter10 : struct.conditions)
          {
            _iter10.write(oprot);
          }
          oprot.writeListEnd();
        }
        oprot.writeFieldEnd();
      }
      if (struct.groupings != null) {
        oprot.writeFieldBegin(GROUPINGS_FIELD_DESC);
        {
          oprot.writeListBegin(new org.apache.thrift.protocol.TList(org.apache.thrift.protocol.TType.STRUCT, struct.groupings.size()));
          for (Grouping _iter11 : struct.groupings)
          {
            _iter11.write(oprot);
          }
          oprot.writeListEnd();
        }
        oprot.writeFieldEnd();
      }
      oprot.writeFieldStop();
      oprot.writeStructEnd();
    }

  }

  private static class QueryTupleSchemeFactory implements SchemeFactory {
    public QueryTupleScheme getScheme() {
      return new QueryTupleScheme();
    }
  }

  private static class QueryTupleScheme extends TupleScheme<Query> {

    @Override
    public void write(org.apache.thrift.protocol.TProtocol prot, Query struct) throws org.apache.thrift.TException {
      TTupleProtocol oprot = (TTupleProtocol) prot;
      {
        oprot.writeI32(struct.columns.size());
        for (AggregateColumnSpec _iter12 : struct.columns)
        {
          _iter12.write(oprot);
        }
      }
      {
        oprot.writeI32(struct.conditions.size());
        for (Condition _iter13 : struct.conditions)
        {
          _iter13.write(oprot);
        }
      }
      {
        oprot.writeI32(struct.groupings.size());
        for (Grouping _iter14 : struct.groupings)
        {
          _iter14.write(oprot);
        }
      }
    }

    @Override
    public void read(org.apache.thrift.protocol.TProtocol prot, Query struct) throws org.apache.thrift.TException {
      TTupleProtocol iprot = (TTupleProtocol) prot;
      {
        org.apache.thrift.protocol.TList _list15 = new org.apache.thrift.protocol.TList(org.apache.thrift.protocol.TType.STRUCT, iprot.readI32());
        struct.columns = new ArrayList<AggregateColumnSpec>(_list15.size);
        for (int _i16 = 0; _i16 < _list15.size; ++_i16)
        {
          AggregateColumnSpec _elem17; // required
          _elem17 = new AggregateColumnSpec();
          _elem17.read(iprot);
          struct.columns.add(_elem17);
        }
      }
      struct.setColumnsIsSet(true);
      {
        org.apache.thrift.protocol.TList _list18 = new org.apache.thrift.protocol.TList(org.apache.thrift.protocol.TType.STRUCT, iprot.readI32());
        struct.conditions = new ArrayList<Condition>(_list18.size);
        for (int _i19 = 0; _i19 < _list18.size; ++_i19)
        {
          Condition _elem20; // required
          _elem20 = new Condition();
          _elem20.read(iprot);
          struct.conditions.add(_elem20);
        }
      }
      struct.setConditionsIsSet(true);
      {
        org.apache.thrift.protocol.TList _list21 = new org.apache.thrift.protocol.TList(org.apache.thrift.protocol.TType.STRUCT, iprot.readI32());
        struct.groupings = new ArrayList<Grouping>(_list21.size);
        for (int _i22 = 0; _i22 < _list21.size; ++_i22)
        {
          Grouping _elem23; // required
          _elem23 = new Grouping();
          _elem23.read(iprot);
          struct.groupings.add(_elem23);
        }
      }
      struct.setGroupingsIsSet(true);
    }
  }

}
