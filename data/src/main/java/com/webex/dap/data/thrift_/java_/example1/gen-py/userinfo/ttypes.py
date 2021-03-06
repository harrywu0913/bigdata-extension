#
# Autogenerated by Thrift Compiler (0.11.0)
#
# DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
#
#  options string: py
#

from thrift.Thrift import TType, TMessageType, TFrozenDict, TException, TApplicationException
from thrift.protocol.TProtocol import TProtocolException
from thrift.TRecursive import fix_spec

import sys

from thrift.transport import TTransport
all_structs = []


class UserInfo(object):
    """
    Attributes:
     - userid
     - username
     - userpwd
     - sex
     - age
    """


    def __init__(self, userid=None, username=None, userpwd=None, sex=None, age=None,):
        self.userid = userid
        self.username = username
        self.userpwd = userpwd
        self.sex = sex
        self.age = age

    def read(self, iprot):
        if iprot._fast_decode is not None and isinstance(iprot.trans, TTransport.CReadableTransport) and self.thrift_spec is not None:
            iprot._fast_decode(self, iprot, [self.__class__, self.thrift_spec])
            return
        iprot.readStructBegin()
        while True:
            (fname, ftype, fid) = iprot.readFieldBegin()
            if ftype == TType.STOP:
                break
            if fid == 1:
                if ftype == TType.I32:
                    self.userid = iprot.readI32()
                else:
                    iprot.skip(ftype)
            elif fid == 2:
                if ftype == TType.STRING:
                    self.username = iprot.readString().decode('utf-8') if sys.version_info[0] == 2 else iprot.readString()
                else:
                    iprot.skip(ftype)
            elif fid == 3:
                if ftype == TType.STRING:
                    self.userpwd = iprot.readString().decode('utf-8') if sys.version_info[0] == 2 else iprot.readString()
                else:
                    iprot.skip(ftype)
            elif fid == 4:
                if ftype == TType.STRING:
                    self.sex = iprot.readString().decode('utf-8') if sys.version_info[0] == 2 else iprot.readString()
                else:
                    iprot.skip(ftype)
            elif fid == 5:
                if ftype == TType.I32:
                    self.age = iprot.readI32()
                else:
                    iprot.skip(ftype)
            else:
                iprot.skip(ftype)
            iprot.readFieldEnd()
        iprot.readStructEnd()

    def write(self, oprot):
        if oprot._fast_encode is not None and self.thrift_spec is not None:
            oprot.trans.write(oprot._fast_encode(self, [self.__class__, self.thrift_spec]))
            return
        oprot.writeStructBegin('UserInfo')
        if self.userid is not None:
            oprot.writeFieldBegin('userid', TType.I32, 1)
            oprot.writeI32(self.userid)
            oprot.writeFieldEnd()
        if self.username is not None:
            oprot.writeFieldBegin('username', TType.STRING, 2)
            oprot.writeString(self.username.encode('utf-8') if sys.version_info[0] == 2 else self.username)
            oprot.writeFieldEnd()
        if self.userpwd is not None:
            oprot.writeFieldBegin('userpwd', TType.STRING, 3)
            oprot.writeString(self.userpwd.encode('utf-8') if sys.version_info[0] == 2 else self.userpwd)
            oprot.writeFieldEnd()
        if self.sex is not None:
            oprot.writeFieldBegin('sex', TType.STRING, 4)
            oprot.writeString(self.sex.encode('utf-8') if sys.version_info[0] == 2 else self.sex)
            oprot.writeFieldEnd()
        if self.age is not None:
            oprot.writeFieldBegin('age', TType.I32, 5)
            oprot.writeI32(self.age)
            oprot.writeFieldEnd()
        oprot.writeFieldStop()
        oprot.writeStructEnd()

    def validate(self):
        return

    def __repr__(self):
        L = ['%s=%r' % (key, value)
             for key, value in self.__dict__.items()]
        return '%s(%s)' % (self.__class__.__name__, ', '.join(L))

    def __eq__(self, other):
        return isinstance(other, self.__class__) and self.__dict__ == other.__dict__

    def __ne__(self, other):
        return not (self == other)
all_structs.append(UserInfo)
UserInfo.thrift_spec = (
    None,  # 0
    (1, TType.I32, 'userid', None, None, ),  # 1
    (2, TType.STRING, 'username', 'UTF8', None, ),  # 2
    (3, TType.STRING, 'userpwd', 'UTF8', None, ),  # 3
    (4, TType.STRING, 'sex', 'UTF8', None, ),  # 4
    (5, TType.I32, 'age', None, None, ),  # 5
)
fix_spec(all_structs)
del all_structs
