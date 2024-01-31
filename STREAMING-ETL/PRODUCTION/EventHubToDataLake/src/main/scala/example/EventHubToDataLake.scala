package example

import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.types._
import org.apache.spark.sql.functions._
import org.apache.spark.eventhubs._

import org.apache.spark.sql.functions.{from_json}
case class EventHubToDataLake(private val spark:SparkSession)
{
     
     private val schema = StructType(Seq(
            StructField("event_timestamp", StringType, nullable = false),
            StructField("userId", StringType, nullable = false),
            StructField("sessionId", StringType, nullable = false),
            StructField("pageUrl", StringType, nullable = false),
            StructField("deviceType", StringType, nullable = false),
            StructField("browser", StringType, nullable = false),
            StructField("geoLocation", StringType, nullable = false),
            StructField("eventType", StringType, nullable = false),
            StructField("adClicked", BooleanType, nullable = false),
            StructField("adId", StringType, nullable = false),
            StructField("durationSeconds", IntegerType, nullable = false)
))
      
      private val CONNECTION_STRING = "Endpoint=sb://forspark.servicebus.windows.net/;SharedAccessKeyName=produce;SharedAccessKey=7hiTBO6qCXCgXVDEmHr40y+RFUMHAJGoA+AEhKvCiII=;EntityPath=clickstream" 
      
      private val connectionStringBuilder =  ConnectionStringBuilder(CONNECTION_STRING).setEventHubName("clickstream").build
      private val customEventhubParameters =  EventHubsConf(connectionStringBuilder).setMaxEventsPerTrigger(5)
      private val incomingStream = spark.readStream.format("eventhubs").options(customEventhubParameters.toMap).load()

      def start = {
      
                  println(incomingStream.printSchema)
                  
                  var df = incomingStream.select(col("body").cast(StringType).alias("json"))
                                         .select(from_json(col("json"), schema).alias("sdata"))
                                         .select(
                                          "sdata.event_timestamp",
                                          "sdata.userId",
                                          "sdata.sessionId",
                                          "sdata.pageUrl",
                                          "sdata.deviceType",
                                          "sdata.browser",
                                          "sdata.geoLocation",
                                          "sdata.eventType",
                                          "sdata.adClicked",
                                          "sdata.adId",
                                          "sdata.durationSeconds"
                                    )

                  df = df.withColumn("Month",month(col("event_timestamp")))
                  df = df.withColumn("year",year(col("event_timestamp")))

                  println("Successfully executing stream to warehouse process")
                  val res = df.writeStream
                              .outputMode("append")
                              .partitionBy("year","Month")
                              .format("parquet")
                              .option("checkpointLocation","/mnt/streamingdata/clickstreamcheckpoint/event_to_adls/check")
                              .option("path","/mnt/streamingdata/clickstream/warehouse/click")
                              .start()
                              .awaitTermination
      }
}

