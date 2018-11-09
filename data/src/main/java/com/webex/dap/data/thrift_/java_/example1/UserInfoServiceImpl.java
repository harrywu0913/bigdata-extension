package com.webex.dap.data.thrift_.java_.example1;

import org.apache.thrift.TException;

import java.util.HashMap;
import java.util.Map;

public class UserInfoServiceImpl implements UserInfoService.Iface {
    private static Map<Integer, UserInfo> userMap = new HashMap<Integer, UserInfo>();

    static {
        userMap.put(1, new UserInfo(1, "mao", "mao", "男", 2016));
        userMap.put(2, new UserInfo(2, "ci", "ci", "女", 7));
        userMap.put(3, new UserInfo(3, "yuan", "yuan", "男", 28));
    }

    @Override
    public UserInfo getUserInfoById(int userid) throws TException {
        return userMap.get(userid);
    }

    @Override
    public String getUserNameById(int userid) throws TException {
        if (checkUserById(userid)){
            return userMap.get(userid).getUsername();
        }

        return null;
    }

    @Override
    public int getUserCount() throws TException {
        return userMap.size();
    }

    @Override
    public boolean checkUserById(int userid) throws TException {
        return userMap.containsKey(userid);
    }
}
