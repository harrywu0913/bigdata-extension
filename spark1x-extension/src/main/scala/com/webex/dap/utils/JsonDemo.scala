package com.webex.dap.utils

import com.google.gson.JsonParser
import com.typesafe.config.ConfigFactory
/**
  * Created by harry on 9/26/18.
  */
object JsonDemo {
  val jsonParser = new JsonParser()
  val conf = ConfigFactory.load();

  def main(args: Array[String]): Unit = {
    val xx = "{\"path\":\"/opt/webex/ms/logs/wbxcb-1_metrics_09142018_0.2081.log\",\"component\":\"meeting\",\"@timestamp\":\"2018-09-14T12:08:50.702Z\",\"@version\":\"1\",\"host\":\"elcb417.webex.com\",\"eventtype\":\"metrics\",\"message\":\" {\\\\\\\"componentAddress\\\\\\\":\\\\\\\"tcp://elcb41701.webex.com:1270\\\\\\\",\\\\\\\"componentType\\\\\\\":\\\\\\\"CB\\\\\\\",\\\\\\\"componentVer\\\\\\\":\\\\\\\"927, 5.7.0, 07262018, 8690\\\\\\\",\\\\\\\"confId\\\\\\\":\\\\\\\"106050978437926491\\\\\\\",\\\\\\\"confName\\\\\\\":\\\\\\\"null\\\\\\\",\\\\\\\"confNum\\\\\\\":\\\\\\\"599031361\\\\\\\",\\\\\\\"featureName\\\\\\\":\\\\\\\"WaitingRoomUserLeave\\\\\\\",\\\\\\\"meetingDomainName\\\\\\\":\\\\\\\"elmd\\\\\\\",\\\\\\\"metricName\\\\\\\":\\\\\\\"Meeting\\\\\\\",\\\\\\\"siteId\\\\\\\":\\\\\\\"1069237\\\\\\\",\\\\\\\"siteVer\\\\\\\":\\\\\\\"32.16\\\\\\\",\\\\\\\"timestamp\\\\\\\":\\\\\\\"2018-09-14T12:08:50Z\\\\\\\",\\\\\\\"trackingId\\\\\\\":\\\\\\\"null\\\\\\\",\\\\\\\"values\\\\\\\":{\\\\\\\"MCS_Reason\\\\\\\":\\\\\\\"64\\\\\\\",\\\\\\\"client_ip_addr\\\\\\\":\\\\\\\"\\\\xDAy\\\\\\\\u0007\\\\xB1\\\\xF32\\\\xA8\\\\xADZ(\\\\xF6\\\\x9EA\\\\xEC|̪a `g\\\\xA9\\\\xC0ϭlQG\\\\xB4\\\\xF2\\\\\\\\u000Fc\\\\xE6:\\\\x8Am\\\\\\\\u0015\\\\x91\\\\xB8\\\\xEA)\\\\xB4P\\\\\\\\u0003\\\\xA8\\\\xA6\\\\xD1@\\\\\\\\u000E\\\\xA2\\\\x9BE\\\\\\\",\\\\\\\"client_type\\\\\\\":\\\\\\\"WebexUser\\\\\\\",\\\\\\\"meeting_type\\\\\\\":\\\\\\\"PMR with TP integrated\\\\\\\",\\\\\\\"node_id\\\\\\\":\\\\\\\"33558529\\\\\\\",\\\\\\\"user_name\\\\\\\":\\\\\\\"Doug Wekamp\\\\\\\"}}\",\"type\":\"eurmmcbra\",\"tags\":[\"_jsonparsefailure\"]}";

    val json = jsonParser.parse(xx).getAsJsonObject;


    print(json)

    if (json.has("type") && !json.get("type").isJsonNull) {
      print(json.get("type").getAsString().toLowerCase());
    }

    if (json.has("message") && !json.get("message").isJsonNull) {
      if (json.get("message").isJsonObject) {
        val message = json.get("message").getAsJsonObject
        if (message.has("featureName") && message.get("featureName").isJsonPrimitive) {
          print("featureName", message.get("featureName").getAsString().toLowerCase());
        }
        if (message.has("metricName") && message.get("metricName").isJsonPrimitive) {
          print("metricName", message.get("metricName").getAsString().toLowerCase());
        }
      }
    }

  }
}
