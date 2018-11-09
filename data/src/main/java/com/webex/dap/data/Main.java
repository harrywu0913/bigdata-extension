package com.webex.dap.data;

import com.google.gson.JsonParser;

/**
 * Created by harry on 6/4/18.
 */
public class Main {

    static JsonParser jsonParser = new JsonParser();

    public static void main(String[] args) throws Exception {

//        System.out.println(Main.class.getName());
//        BufferedReader br = new BufferedReader(new FileReader("/Users/harry/Documents/FlumeData-rpbt1hsn007.webex.com.1528090715638.txt"));
//
//        int total = 0;
//        int correct = 0;
//        String line = "";
//        while ((line = br.readLine()) != null) {
//
////            System.out.println(line);
//            total++;
//            JsonObject json = jsonParser.parse(line).getAsJsonObject();
//
//            if (json.has("metrics") && !json.get("metrics").isJsonNull()) {
//                JsonObject metrics = json.get("metrics").getAsJsonObject();
//                if (metrics.has("device") && !metrics.get("device").isJsonNull()) {
//                    String key = metrics.get("device").getAsString();
//                    System.out.println(key);
//                    correct++;
//                }
//            }
//        }
//
//        System.out.println(total + " " + correct);

//        String patternStr = "^(?:\\n)?(\\d\\d\\d\\d-\\d\\d-\\d\\d\\s\\d\\d:\\d\\d)";
//        String msg = "2012-10-18 18:47:57,614 some log line";
//
//
//        Pattern pattern = Pattern.compile(patternStr);
//        Matcher matcher = pattern.matcher(msg);
//
//        if (matcher.find()){
//            for (int group = 0,count = matcher.groupCount();group < count;group++){
//                int groupIndex = group + 1;
//                System.out.println(matcher.group(groupIndex));
//            }
//        }

//        int c = Integer.MAX_VALUE;
//
//        double a = 10000001;
//        double b = 999999;
//        double c_d = c;
//
//        System.out.println(a);
//        System.out.println(b);
//        System.out.println(c_d);

//        String x = "sd\f\"sdf";
//        System.out.println(x);

//        System.out.println(x.replace("\"","\\\""));

        String xx = "/kafka/logstash_meeting_hdfs/j2eereminder/day=2018-11-11/logstash_meeting_hdfs-rpsj2hsn128.webex.com.1541625492318.lzo_deflate";
        System.out.println(xx.length());

        String yy = "savephonenum-save_phone_number_from_page_dialog";
        System.out.println(yy.length());
    }
}
