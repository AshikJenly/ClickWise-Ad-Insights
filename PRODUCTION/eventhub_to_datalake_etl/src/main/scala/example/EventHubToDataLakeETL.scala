package example

import org.apache.spark.sql.types._
import org.apache.spark.eventhubs._

object EventHubToDataLake
{
      val schema = StructType(Seq(
            StructField("event_timestamp", TimestampType, nullable = false),
            StructField("user_id", StringType, nullable = false),
            StructField("session_id", StringType, nullable = false),
            StructField("page_url", StringType, nullable = false),
            StructField("device_type", StringType, nullable = false),
            StructField("browser", StringType, nullable = false),
            StructField("geo_location", StringType, nullable = false),
            StructField("event_type", StringType, nullable = false),
            StructField("ad_clicked", BooleanType, nullable = false),
            StructField("ad_id", StringType, nullable = false),
            StructField("duration_seconds", IntegerType, nullable = false),
            StructField("Month", IntegerType, nullable = false),
            StructField("year", IntegerType, nullable = false)
            ))
      
            val connectionString = "Endpoint=sb://forspark.servicebus.windows.net/;SharedAccessKeyName=produce;SharedAccessKey=7hiTBO6qCXCgXVDEmHr40y+RFUMHAJGoA+AEhKvCiII=;EntityPath=clickstream" 
            val connectionStringBuilder =  ConnectionStringBuilder(connectionString).setEventHubName("clickstream").build
            val customEventhubParameters =  EventHubsConf(connectionStringBuilder).setMaxEventsPerTrigger(5)
            val incomingStream = spark.readStream.schema(schema).format("eventhubs").options(customEventhubParameters.toMap).load()


            // var df = spark.readStream.schema(schema).option("header",true).csv("/data/click")
            df = df.withColumn("Month",month(col("event_timestamp")))
            df = df.withColumn("year",year(col("event_timestamp")))


              incomingStream.selectExpr("cast(body as string) as json").writeStream.format("console").outputMode("append").start().awaitTermination()


            val res = df.writeStream
                        .outputMode("append")
                        .partitionBy("year","Month")
                        .format("parquet")
                        .option("checkpointLocation","/checkpoint/click")
                        .option("path","/warehouse/click")
                        .start()
                        .awaitTermination

}

