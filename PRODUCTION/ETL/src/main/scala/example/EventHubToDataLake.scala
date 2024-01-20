package example

import org.apache.spark.sql.types._
import org.apache.spark.sql.functions._
import org.apache.spark.eventhubs._

import org.apache.spark.sql.functions.{from_json}
case class EventHubToDataLake(private val spark:SparkSession)
{
     
                  // .appName("")
                  // .master("local[*]")
                  // .config("spark.sql.streaming.stateStore.stateSchemaCheck", "false")
                  // .config("spark.sql.warehouse.dir","/new/warehouse")
                  // .getOrCreate() 

     private val schema = StructType(Seq(
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
            StructField("duration_seconds", IntegerType, nullable = false)
            ))
      
      private val connectionString = "Endpoint=sb://forspark.servicebus.windows.net/;SharedAccessKeyName=produce;SharedAccessKey=7hiTBO6qCXCgXVDEmHr40y+RFUMHAJGoA+AEhKvCiII=;EntityPath=clickstream" 
      
      // val ehConf = EventHubsConf(connectionString)

      private val connectionStringBuilder =  ConnectionStringBuilder(connectionString).setEventHubName("clickstream").build
      private val customEventhubParameters =  EventHubsConf(connectionStringBuilder).setMaxEventsPerTrigger(5)
      private val incomingStream = spark.readStream.format("eventhubs").options(customEventhubParameters.toMap).load()

      def start = {
      
                  // val incomingStream = spark.readStream.format("eventhubs").options(ehConf.toMap).load()
                  println(incomingStream.printSchema)
                  var df= incomingStream.selectExpr("cast(body as string) as json").select(from_json(col("json"),schema).alias("sdata"))

                  df = df.select("sdata.event_timestamp",
                              "sdata.user_id",
                              "sdata.session_id",
                              "sdata.page_url",
                              "sdata.device_type",
                              "sdata.browser",
                              "sdata.geo_location",
                              "sdata.event_type",
                              "sdata.ad_clicked",
                              "sdata.ad_id",
                              "sdata.duration_seconds"
                              )

                  df = df.withColumn("Month",month(col("event_timestamp")))
                  df = df.withColumn("year",year(col("event_timestamp")))

                  println("Successfully executing")
                  val res = df.writeStream
                              .outputMode("append")
                              .partitionBy("year","Month")
                              .format("parquet")
                              .option("checkpointLocation","/mnt/streamingdata/clickstreamcheckpoint/chrc/click")
                              .option("path","/mnt/streamingdata/clickstream/warehouse/click")
                              .start()
                              .awaitTermination
      }
}

