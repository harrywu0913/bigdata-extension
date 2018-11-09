/**
 * Autogenerated by Thrift Compiler (0.11.0)
 *
 * DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
 *  @generated
 */
package com.webex.dap.data.thrift_.java_.example1;

@SuppressWarnings({"cast", "rawtypes", "serial", "unchecked", "unused"})
@javax.annotation.Generated(value = "Autogenerated by Thrift Compiler (0.11.0)", date = "2018-10-09")
public class UserInfo implements org.apache.thrift.TBase<UserInfo, UserInfo._Fields>, java.io.Serializable, Cloneable, Comparable<UserInfo> {
  private static final org.apache.thrift.protocol.TStruct STRUCT_DESC = new org.apache.thrift.protocol.TStruct("UserInfo");

  private static final org.apache.thrift.protocol.TField USERID_FIELD_DESC = new org.apache.thrift.protocol.TField("userid", org.apache.thrift.protocol.TType.I32, (short)1);
  private static final org.apache.thrift.protocol.TField USERNAME_FIELD_DESC = new org.apache.thrift.protocol.TField("username", org.apache.thrift.protocol.TType.STRING, (short)2);
  private static final org.apache.thrift.protocol.TField USERPWD_FIELD_DESC = new org.apache.thrift.protocol.TField("userpwd", org.apache.thrift.protocol.TType.STRING, (short)3);
  private static final org.apache.thrift.protocol.TField SEX_FIELD_DESC = new org.apache.thrift.protocol.TField("sex", org.apache.thrift.protocol.TType.STRING, (short)4);
  private static final org.apache.thrift.protocol.TField AGE_FIELD_DESC = new org.apache.thrift.protocol.TField("age", org.apache.thrift.protocol.TType.I32, (short)5);

  private static final org.apache.thrift.scheme.SchemeFactory STANDARD_SCHEME_FACTORY = new UserInfoStandardSchemeFactory();
  private static final org.apache.thrift.scheme.SchemeFactory TUPLE_SCHEME_FACTORY = new UserInfoTupleSchemeFactory();

  public int userid; // required
  public String username; // required
  public String userpwd; // required
  public String sex; // required
  public int age; // required

  /** The set of fields this struct contains, along with convenience methods for finding and manipulating them. */
  public enum _Fields implements org.apache.thrift.TFieldIdEnum {
    USERID((short)1, "userid"),
    USERNAME((short)2, "username"),
    USERPWD((short)3, "userpwd"),
    SEX((short)4, "sex"),
    AGE((short)5, "age");

    private static final java.util.Map<String, _Fields> byName = new java.util.HashMap<String, _Fields>();

    static {
      for (_Fields field : java.util.EnumSet.allOf(_Fields.class)) {
        byName.put(field.getFieldName(), field);
      }
    }

    /**
     * Find the _Fields constant that matches fieldId, or null if its not found.
     */
    public static _Fields findByThriftId(int fieldId) {
      switch(fieldId) {
        case 1: // USERID
          return USERID;
        case 2: // USERNAME
          return USERNAME;
        case 3: // USERPWD
          return USERPWD;
        case 4: // SEX
          return SEX;
        case 5: // AGE
          return AGE;
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
  private static final int __USERID_ISSET_ID = 0;
  private static final int __AGE_ISSET_ID = 1;
  private byte __isset_bitfield = 0;
  public static final java.util.Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> metaDataMap;
  static {
    java.util.Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> tmpMap = new java.util.EnumMap<_Fields, org.apache.thrift.meta_data.FieldMetaData>(_Fields.class);
    tmpMap.put(_Fields.USERID, new org.apache.thrift.meta_data.FieldMetaData("userid", org.apache.thrift.TFieldRequirementType.DEFAULT,
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.I32)));
    tmpMap.put(_Fields.USERNAME, new org.apache.thrift.meta_data.FieldMetaData("username", org.apache.thrift.TFieldRequirementType.DEFAULT,
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
    tmpMap.put(_Fields.USERPWD, new org.apache.thrift.meta_data.FieldMetaData("userpwd", org.apache.thrift.TFieldRequirementType.DEFAULT,
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
    tmpMap.put(_Fields.SEX, new org.apache.thrift.meta_data.FieldMetaData("sex", org.apache.thrift.TFieldRequirementType.DEFAULT,
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
    tmpMap.put(_Fields.AGE, new org.apache.thrift.meta_data.FieldMetaData("age", org.apache.thrift.TFieldRequirementType.DEFAULT,
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.I32)));
    metaDataMap = java.util.Collections.unmodifiableMap(tmpMap);
    org.apache.thrift.meta_data.FieldMetaData.addStructMetaDataMap(UserInfo.class, metaDataMap);
  }

  public UserInfo() {
  }

  public UserInfo(
    int userid,
    String username,
    String userpwd,
    String sex,
    int age)
  {
    this();
    this.userid = userid;
    setUseridIsSet(true);
    this.username = username;
    this.userpwd = userpwd;
    this.sex = sex;
    this.age = age;
    setAgeIsSet(true);
  }

  /**
   * Performs a deep copy on <i>other</i>.
   */
  public UserInfo(UserInfo other) {
    __isset_bitfield = other.__isset_bitfield;
    this.userid = other.userid;
    if (other.isSetUsername()) {
      this.username = other.username;
    }
    if (other.isSetUserpwd()) {
      this.userpwd = other.userpwd;
    }
    if (other.isSetSex()) {
      this.sex = other.sex;
    }
    this.age = other.age;
  }

  public UserInfo deepCopy() {
    return new UserInfo(this);
  }

  @Override
  public void clear() {
    setUseridIsSet(false);
    this.userid = 0;
    this.username = null;
    this.userpwd = null;
    this.sex = null;
    setAgeIsSet(false);
    this.age = 0;
  }

  public int getUserid() {
    return this.userid;
  }

  public UserInfo setUserid(int userid) {
    this.userid = userid;
    setUseridIsSet(true);
    return this;
  }

  public void unsetUserid() {
    __isset_bitfield = org.apache.thrift.EncodingUtils.clearBit(__isset_bitfield, __USERID_ISSET_ID);
  }

  /** Returns true if field userid is set (has been assigned a value) and false otherwise */
  public boolean isSetUserid() {
    return org.apache.thrift.EncodingUtils.testBit(__isset_bitfield, __USERID_ISSET_ID);
  }

  public void setUseridIsSet(boolean value) {
    __isset_bitfield = org.apache.thrift.EncodingUtils.setBit(__isset_bitfield, __USERID_ISSET_ID, value);
  }

  public String getUsername() {
    return this.username;
  }

  public UserInfo setUsername(String username) {
    this.username = username;
    return this;
  }

  public void unsetUsername() {
    this.username = null;
  }

  /** Returns true if field username is set (has been assigned a value) and false otherwise */
  public boolean isSetUsername() {
    return this.username != null;
  }

  public void setUsernameIsSet(boolean value) {
    if (!value) {
      this.username = null;
    }
  }

  public String getUserpwd() {
    return this.userpwd;
  }

  public UserInfo setUserpwd(String userpwd) {
    this.userpwd = userpwd;
    return this;
  }

  public void unsetUserpwd() {
    this.userpwd = null;
  }

  /** Returns true if field userpwd is set (has been assigned a value) and false otherwise */
  public boolean isSetUserpwd() {
    return this.userpwd != null;
  }

  public void setUserpwdIsSet(boolean value) {
    if (!value) {
      this.userpwd = null;
    }
  }

  public String getSex() {
    return this.sex;
  }

  public UserInfo setSex(String sex) {
    this.sex = sex;
    return this;
  }

  public void unsetSex() {
    this.sex = null;
  }

  /** Returns true if field sex is set (has been assigned a value) and false otherwise */
  public boolean isSetSex() {
    return this.sex != null;
  }

  public void setSexIsSet(boolean value) {
    if (!value) {
      this.sex = null;
    }
  }

  public int getAge() {
    return this.age;
  }

  public UserInfo setAge(int age) {
    this.age = age;
    setAgeIsSet(true);
    return this;
  }

  public void unsetAge() {
    __isset_bitfield = org.apache.thrift.EncodingUtils.clearBit(__isset_bitfield, __AGE_ISSET_ID);
  }

  /** Returns true if field age is set (has been assigned a value) and false otherwise */
  public boolean isSetAge() {
    return org.apache.thrift.EncodingUtils.testBit(__isset_bitfield, __AGE_ISSET_ID);
  }

  public void setAgeIsSet(boolean value) {
    __isset_bitfield = org.apache.thrift.EncodingUtils.setBit(__isset_bitfield, __AGE_ISSET_ID, value);
  }

  public void setFieldValue(_Fields field, Object value) {
    switch (field) {
    case USERID:
      if (value == null) {
        unsetUserid();
      } else {
        setUserid((Integer)value);
      }
      break;

    case USERNAME:
      if (value == null) {
        unsetUsername();
      } else {
        setUsername((String)value);
      }
      break;

    case USERPWD:
      if (value == null) {
        unsetUserpwd();
      } else {
        setUserpwd((String)value);
      }
      break;

    case SEX:
      if (value == null) {
        unsetSex();
      } else {
        setSex((String)value);
      }
      break;

    case AGE:
      if (value == null) {
        unsetAge();
      } else {
        setAge((Integer)value);
      }
      break;

    }
  }

  public Object getFieldValue(_Fields field) {
    switch (field) {
    case USERID:
      return getUserid();

    case USERNAME:
      return getUsername();

    case USERPWD:
      return getUserpwd();

    case SEX:
      return getSex();

    case AGE:
      return getAge();

    }
    throw new IllegalStateException();
  }

  /** Returns true if field corresponding to fieldID is set (has been assigned a value) and false otherwise */
  public boolean isSet(_Fields field) {
    if (field == null) {
      throw new IllegalArgumentException();
    }

    switch (field) {
    case USERID:
      return isSetUserid();
    case USERNAME:
      return isSetUsername();
    case USERPWD:
      return isSetUserpwd();
    case SEX:
      return isSetSex();
    case AGE:
      return isSetAge();
    }
    throw new IllegalStateException();
  }

  @Override
  public boolean equals(Object that) {
    if (that == null)
      return false;
    if (that instanceof UserInfo)
      return this.equals((UserInfo)that);
    return false;
  }

  public boolean equals(UserInfo that) {
    if (that == null)
      return false;
    if (this == that)
      return true;

    boolean this_present_userid = true;
    boolean that_present_userid = true;
    if (this_present_userid || that_present_userid) {
      if (!(this_present_userid && that_present_userid))
        return false;
      if (this.userid != that.userid)
        return false;
    }

    boolean this_present_username = true && this.isSetUsername();
    boolean that_present_username = true && that.isSetUsername();
    if (this_present_username || that_present_username) {
      if (!(this_present_username && that_present_username))
        return false;
      if (!this.username.equals(that.username))
        return false;
    }

    boolean this_present_userpwd = true && this.isSetUserpwd();
    boolean that_present_userpwd = true && that.isSetUserpwd();
    if (this_present_userpwd || that_present_userpwd) {
      if (!(this_present_userpwd && that_present_userpwd))
        return false;
      if (!this.userpwd.equals(that.userpwd))
        return false;
    }

    boolean this_present_sex = true && this.isSetSex();
    boolean that_present_sex = true && that.isSetSex();
    if (this_present_sex || that_present_sex) {
      if (!(this_present_sex && that_present_sex))
        return false;
      if (!this.sex.equals(that.sex))
        return false;
    }

    boolean this_present_age = true;
    boolean that_present_age = true;
    if (this_present_age || that_present_age) {
      if (!(this_present_age && that_present_age))
        return false;
      if (this.age != that.age)
        return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    int hashCode = 1;

    hashCode = hashCode * 8191 + userid;

    hashCode = hashCode * 8191 + ((isSetUsername()) ? 131071 : 524287);
    if (isSetUsername())
      hashCode = hashCode * 8191 + username.hashCode();

    hashCode = hashCode * 8191 + ((isSetUserpwd()) ? 131071 : 524287);
    if (isSetUserpwd())
      hashCode = hashCode * 8191 + userpwd.hashCode();

    hashCode = hashCode * 8191 + ((isSetSex()) ? 131071 : 524287);
    if (isSetSex())
      hashCode = hashCode * 8191 + sex.hashCode();

    hashCode = hashCode * 8191 + age;

    return hashCode;
  }

  @Override
  public int compareTo(UserInfo other) {
    if (!getClass().equals(other.getClass())) {
      return getClass().getName().compareTo(other.getClass().getName());
    }

    int lastComparison = 0;

    lastComparison = Boolean.valueOf(isSetUserid()).compareTo(other.isSetUserid());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetUserid()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.userid, other.userid);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetUsername()).compareTo(other.isSetUsername());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetUsername()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.username, other.username);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetUserpwd()).compareTo(other.isSetUserpwd());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetUserpwd()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.userpwd, other.userpwd);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetSex()).compareTo(other.isSetSex());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetSex()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.sex, other.sex);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetAge()).compareTo(other.isSetAge());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetAge()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.age, other.age);
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
    scheme(iprot).read(iprot, this);
  }

  public void write(org.apache.thrift.protocol.TProtocol oprot) throws org.apache.thrift.TException {
    scheme(oprot).write(oprot, this);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder("UserInfo(");
    boolean first = true;

    sb.append("userid:");
    sb.append(this.userid);
    first = false;
    if (!first) sb.append(", ");
    sb.append("username:");
    if (this.username == null) {
      sb.append("null");
    } else {
      sb.append(this.username);
    }
    first = false;
    if (!first) sb.append(", ");
    sb.append("userpwd:");
    if (this.userpwd == null) {
      sb.append("null");
    } else {
      sb.append(this.userpwd);
    }
    first = false;
    if (!first) sb.append(", ");
    sb.append("sex:");
    if (this.sex == null) {
      sb.append("null");
    } else {
      sb.append(this.sex);
    }
    first = false;
    if (!first) sb.append(", ");
    sb.append("age:");
    sb.append(this.age);
    first = false;
    sb.append(")");
    return sb.toString();
  }

  public void validate() throws org.apache.thrift.TException {
    // check for required fields
    // check for sub-struct validity
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
      __isset_bitfield = 0;
      read(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(in)));
    } catch (org.apache.thrift.TException te) {
      throw new java.io.IOException(te);
    }
  }

  private static class UserInfoStandardSchemeFactory implements org.apache.thrift.scheme.SchemeFactory {
    public UserInfoStandardScheme getScheme() {
      return new UserInfoStandardScheme();
    }
  }

  private static class UserInfoStandardScheme extends org.apache.thrift.scheme.StandardScheme<UserInfo> {

    public void read(org.apache.thrift.protocol.TProtocol iprot, UserInfo struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TField schemeField;
      iprot.readStructBegin();
      while (true)
      {
        schemeField = iprot.readFieldBegin();
        if (schemeField.type == org.apache.thrift.protocol.TType.STOP) { 
          break;
        }
        switch (schemeField.id) {
          case 1: // USERID
            if (schemeField.type == org.apache.thrift.protocol.TType.I32) {
              struct.userid = iprot.readI32();
              struct.setUseridIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 2: // USERNAME
            if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
              struct.username = iprot.readString();
              struct.setUsernameIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 3: // USERPWD
            if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
              struct.userpwd = iprot.readString();
              struct.setUserpwdIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 4: // SEX
            if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
              struct.sex = iprot.readString();
              struct.setSexIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 5: // AGE
            if (schemeField.type == org.apache.thrift.protocol.TType.I32) {
              struct.age = iprot.readI32();
              struct.setAgeIsSet(true);
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

      // check for required fields of primitive type, which can't be checked in the validate method
      struct.validate();
    }

    public void write(org.apache.thrift.protocol.TProtocol oprot, UserInfo struct) throws org.apache.thrift.TException {
      struct.validate();

      oprot.writeStructBegin(STRUCT_DESC);
      oprot.writeFieldBegin(USERID_FIELD_DESC);
      oprot.writeI32(struct.userid);
      oprot.writeFieldEnd();
      if (struct.username != null) {
        oprot.writeFieldBegin(USERNAME_FIELD_DESC);
        oprot.writeString(struct.username);
        oprot.writeFieldEnd();
      }
      if (struct.userpwd != null) {
        oprot.writeFieldBegin(USERPWD_FIELD_DESC);
        oprot.writeString(struct.userpwd);
        oprot.writeFieldEnd();
      }
      if (struct.sex != null) {
        oprot.writeFieldBegin(SEX_FIELD_DESC);
        oprot.writeString(struct.sex);
        oprot.writeFieldEnd();
      }
      oprot.writeFieldBegin(AGE_FIELD_DESC);
      oprot.writeI32(struct.age);
      oprot.writeFieldEnd();
      oprot.writeFieldStop();
      oprot.writeStructEnd();
    }

  }

  private static class UserInfoTupleSchemeFactory implements org.apache.thrift.scheme.SchemeFactory {
    public UserInfoTupleScheme getScheme() {
      return new UserInfoTupleScheme();
    }
  }

  private static class UserInfoTupleScheme extends org.apache.thrift.scheme.TupleScheme<UserInfo> {

    @Override
    public void write(org.apache.thrift.protocol.TProtocol prot, UserInfo struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TTupleProtocol oprot = (org.apache.thrift.protocol.TTupleProtocol) prot;
      java.util.BitSet optionals = new java.util.BitSet();
      if (struct.isSetUserid()) {
        optionals.set(0);
      }
      if (struct.isSetUsername()) {
        optionals.set(1);
      }
      if (struct.isSetUserpwd()) {
        optionals.set(2);
      }
      if (struct.isSetSex()) {
        optionals.set(3);
      }
      if (struct.isSetAge()) {
        optionals.set(4);
      }
      oprot.writeBitSet(optionals, 5);
      if (struct.isSetUserid()) {
        oprot.writeI32(struct.userid);
      }
      if (struct.isSetUsername()) {
        oprot.writeString(struct.username);
      }
      if (struct.isSetUserpwd()) {
        oprot.writeString(struct.userpwd);
      }
      if (struct.isSetSex()) {
        oprot.writeString(struct.sex);
      }
      if (struct.isSetAge()) {
        oprot.writeI32(struct.age);
      }
    }

    @Override
    public void read(org.apache.thrift.protocol.TProtocol prot, UserInfo struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TTupleProtocol iprot = (org.apache.thrift.protocol.TTupleProtocol) prot;
      java.util.BitSet incoming = iprot.readBitSet(5);
      if (incoming.get(0)) {
        struct.userid = iprot.readI32();
        struct.setUseridIsSet(true);
      }
      if (incoming.get(1)) {
        struct.username = iprot.readString();
        struct.setUsernameIsSet(true);
      }
      if (incoming.get(2)) {
        struct.userpwd = iprot.readString();
        struct.setUserpwdIsSet(true);
      }
      if (incoming.get(3)) {
        struct.sex = iprot.readString();
        struct.setSexIsSet(true);
      }
      if (incoming.get(4)) {
        struct.age = iprot.readI32();
        struct.setAgeIsSet(true);
      }
    }
  }

  private static <S extends org.apache.thrift.scheme.IScheme> S scheme(org.apache.thrift.protocol.TProtocol proto) {
    return (org.apache.thrift.scheme.StandardScheme.class.equals(proto.getScheme()) ? STANDARD_SCHEME_FACTORY : TUPLE_SCHEME_FACTORY).getScheme();
  }
}

