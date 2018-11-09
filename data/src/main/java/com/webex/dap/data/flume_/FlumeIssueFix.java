package com.webex.dap.data.flume_;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by harry on 9/21/18.
 */
public class FlumeIssueFix {
    static JsonParser jsonParser = new JsonParser();

    public static void main(String[] args) {

        String line = "{\"type\": \"sdf\",\"message\": {\"metricName\"\": \"sdf\",\"featureName\": \"xfd\"}}";
        parse(line);
        line = "{\"type\": \"sdf\",\"message\": {\"metricName\": \"sdf\",\"featureName\": \"xfd\"}}";
        parse(line);
        line = "{\"type\": \"sdf\",\"message\": {\"metricName\": \"sdf\",\"featureName\": \"\"}}";
        parse(line);
        line = "{\"type\": \"sdf\",\"message\": {\"metricName\": \"sdf\"}}";
        parse(line);
        line = "{\"type\": \"sdf\",\"message\": {\"metricName\": \"\",\"featureName\": \"xfd\"}}";
        parse(line);
        line = "{\"type\": \"sdf\",\"message\": {\"featureName\": \"xfd\"}}";
        parse(line);
        line = "{\"type\": \"\",\"message\": {\"metricName\": \"sdf\",\"featureName\": \"xfd\"}}";
        parse(line);
        line = "{\"message\": {\"metricName\": \"sdf\",\"featureName\": \"xfd\"}}";
        parse(line);

        line = "{\"type\": \"sdf\",\"message\": {\"metricName\": \"\",\"featureName\": \"\"}}";
        parse(line);

        line = "{\"type\": \"sdf\",\"message\": {}}";
        parse(line);

        line = "{\"type\": \"\",\"message\": {\"metricName\": \"\",\"featureName\": \"xfd\"}}";
        parse(line);

        line = "{\"message\": {\"featureName\": \"xfd\"}}";
        parse(line);

        line = "{\"type\": \"\",\"message\": {\"metricName\": \"\",\"featureName\": \"\"}}";
        parse(line);

        line = "{\"message\": {\"metricName\": \"\"}}";
        parse(line);

    }

    public static void parse(String line) {
        System.out.println();
        Map<String, String> keyMap = new HashMap<String, String>();
        try {
            JsonObject json = jsonParser.parse(line).getAsJsonObject();
            if (json.has("type")) {
                keyMap.put("type", json.get("type").getAsString().toLowerCase());
            }

            if (json.has("message") && !json.get("message").isJsonNull()) {
                JsonObject message = json.get("message").getAsJsonObject();

                if (message.has("featureName")) {
                    keyMap.put("featureName", message.get("featureName").getAsString().toLowerCase());
                }

                if (message.has("metricName")) {
                    keyMap.put("metricName", message.get("metricName").getAsString().toLowerCase());
                }
            }
        } catch (Exception e) {
//            e.printStackTrace();
        } finally {
            if (!keyMap.containsKey("type")) {
                keyMap.put("type", "others");
            }
            if (!keyMap.containsKey("metricName")) {
                keyMap.put("metricName", "others");
            }
            if (!keyMap.containsKey("featureName")) {
                keyMap.put("featureName", "others");
            }
        }

        for (Map.Entry<String, String> xx : keyMap.entrySet()) {
            System.out.println(xx.getKey() + " -> " + xx.getValue());
        }
        System.out.println();
    }
}
