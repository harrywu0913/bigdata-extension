#1 git clone

#2 mvn clean package

#3 install package into dap cluster servers

#4 metrics.properties

    master.source.jvm.class=org.apache.spark.metrics.source.JvmSource
    worker.source.jvm.class=org.apache.spark.metrics.source.JvmSource
    driver.source.jvm.class=org.apache.spark.metrics.source.JvmSource
    executor.source.jvm.class=org.apache.spark.metrics.source.JvmSource

    *.sink.kafka.class=org.apache.spark.metrics.sink.KafkaSink
    *.sink.kafka.broker=rphf1kaf001.qa.webex.com:9092,rphf1kaf002.qa.webex.com:9092,rphf1kaf003.qa.webex.com:9092
    *.sink.kafka.topic=test
    *.sink.kafka.period=10
    *.sink.kafka.unit=seconds

#5. spark-submit --jars ${dap-spark-extension-....jar} --conf spark.metrics.conf=metrics.properties --file metrics.properties ........