package com.webex.dap.kafka.admin;

import org.apache.kafka.common.utils.Utils;

/**
 * Created by harry on 7/26/18.
 */
public class ConsumerGroupCommandMain {
    public static void main(String[] args){

//        System.out.println(Utils.abs("clap_sj5a".hashCode()) % 200);

        String commandLine = "clap_sj1 SCM_PRI KMOffsetCache-rpsj1clg001.webex.com CASSANDRA_LOCAL_SJC02 WEBDB_TEST_ALLREP_AMS01 CASSANDRA_LOCAL_DFW01 eb-service-blissite_prod TAHOEDB_TAHOE_YYZ01 WEBDB_TEST_ALLREP_DFW01 WEBDB_TEST_ALLREP_SJC02 CASSANDRA_LOCAL_AMS01 cache_rpsj1drt003.webex.com_1531990017620 TAHOEDB_TAHOE_SIN01 WEBDB_TEST_YYZ01 TAHOEDB_TAHOE_LHR03 console-consumer-54731 eb-itjabber-sub1_prod clap_sj5 TAHOEDB_TAHOE_IAD03 TAHOEDB_TAHOE_DFW02 CASSANDRA_LOCAL_NRT02 DNS_PRI SJC02_SIN01_MM flume governanceAgentrpsj1dga101.webex.com WEBDB_TEST_LHR03 WEBDB_TEST_SIN01 SJC02_LHR03_MM cache_rpsj1drt001.webex.com_1531990017621 cache_rpsj1drt003.webex.com_1531990015496 TEODB_TEST_SJC02 WEBDB_TEST_ALLREP_IAD03 CASSANDRA_LOCAL_IAD03 TEODB_XXRPTH_DFW01 WEBDB_TEST_ALLREP_DFW02 TEODB_XXRPTH_SJC02 TAHOEDB_TAHOE_NRT02 TEODB_TEST_DFW01 CASSANDRA_LOCAL_DFW02 sap_PROD updateForDemo12 clap_splunk_sj WEBDB_TEST_DFW02 cache_rpsj1drt001.webex.com_1531990015495 WEBDB_TEST_ALLREP_NRT02 GLOOKUPDB_GLOOKUP_PRI CONFIGDB_TEST_PRI WEBDB_TEST_IAD03 BILLINGDB_TEST_PRI WEBDB_TEST_AMS01 GLOOKUPDB_GLOOKUP_GSB TAHOEDB_TAHOE_DFW01 TAHOEDB_TAHOE_SJC02 WEBDB_TEST_ALLREP_YYZ01 CASSANDRA_LOCAL_YYZ01 CASSANDRA_LOCAL_SIN01 clap_splunk CASSANDRA_LOCAL_LHR03 TAHOEDB_TAHOE_AMS01 eb-service-userudl2_prod ComeFromSJC clap_sj5a CASSANDRA_GLOBAL_PRI console-consumer-44975 clap_sj1a WEBDB_TEST_NRT02 WEBDB_TEST_ALLREP_LHR03 Shareplex2Kafka test WEBDB_TEST_DFW01 eb-itjabber-user1_prod WEBDB_TEST_SJC02 eb-service-siteudl2_prod WEBDB_TEST_ALLREP_SIN01 TAHOEDB_TEST_DFW02 ComeFromSJC20180719 governanceAgentrpsj1dga102.webex.com cache_rpsj1drt002.webex.com_1531990015499 cache_rpsj1drt002.webex.com_1531990017622";

        for (String x : commandLine.split(" ")){
//            if (Utils.abs(x.hashCode()) % 200 == 30)
                System.out.println(Utils.abs(x.hashCode()) % 200 + " " + x) ;

        }

//        ConsumerGroupCommand.main("--bootstrap-server localhost:9092 --describe --group clap_sj1a".split(" "));
//        ConsumerGroupCommand.main("--bootstrap-server localhost:9092 --list".split(" "));
    }
}
