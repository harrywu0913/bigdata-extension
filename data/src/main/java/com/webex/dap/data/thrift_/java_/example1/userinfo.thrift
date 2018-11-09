namespace java com.webex.dap.data.thrift_.java_.example1

struct UserInfo
{
    1:i32 userid,
    2:string username,
    3:string userpwd,
    4:string sex,
    5:i32 age,
}

service UserInfoService
{
    UserInfo getUserInfoById(1:i32 userid),
    string getUserNameById(1:i32 userid),
    i32 getUserCount(),
    bool checkUserById(1:i32 userid),
}