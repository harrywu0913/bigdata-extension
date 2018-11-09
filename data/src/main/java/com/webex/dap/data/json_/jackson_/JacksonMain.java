package com.webex.dap.data.json_.jackson_;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.*;

public class JacksonMain {
    public static void main(String[] args) throws JsonProcessingException {
        User user = new User();
        user.setId("01");
        user.setName("张三");
        user.setAge(33);
        user.setPay(6666.88);
        user.setValid(true);
        user.setOne('E');
        user.setBirthday(new Date(20l * 366 * 24 * 3600 * 1000)); //1990年

        Link link = new Link();
        link.setAddress("河南省济源市");
        link.setPhone("13899995555");
        link.setQq("123456");
        user.setLink(link);

        Map map = new HashMap();
        map.put("aa", "this is aa");
        map.put("bb", "this is bb");
        map.put("cc", "this is cc");
        user.setMap(map);

        List list = new ArrayList() {
        };
        list.add("普洱");
        list.add("大红袍");
        user.setList(list);

        Set set = new HashSet();
        set.add("篮球");
        set.add("足球");
        set.add("乒乓球");
        user.setSet(set);

        /**
         * writeValue(File arg0,Object arg1)
         * writeValue(OutputStream arg0,Object arg1)
         * writeValueAsBytes(Object arg0)
         * writeValueAsString(Object arg0)
         */
        ObjectMapper mapper = new ObjectMapper(); //转换器

        String json = mapper.writeValueAsString(user);

        System.out.println(json);
    }
}
