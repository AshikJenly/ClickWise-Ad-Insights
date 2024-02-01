package example

import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.types._
import org.apache.spark.sql.functions._
import org.apache.spark.sql.streaming.Trigger


case class DataLakeToCosmosDB (private val spark:SparkSession) {
    //   spark.conf.set("spark.sql.streaming.stateStoreRetention", "10 minutes")

      private val schema = StructType(Seq(
            StructField("event_timestamp", TimestampType, nullable = false),
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
      

    private val CONNECTION_URI = "mongodb://forspark:J6cznn1Wd7ukY4lNNxqhhyJxVRm4aBIH8WrAqrHspTjbKTW5KvrIEqHdL9SvsyLmZziicHF4M3v8ACDbna0CdA==@forspark.mongo.cosmos.azure.com:10255/click.test?ssl=true&retrywrites=false&replicaSet=globaldb&maxIdleTimeMS=120000&appName=@forspark@"
    var data  = spark.readStream
                            .schema(schema)
                            .parquet("/mnt/streamingdatafinal/clickstream/warehouse/click")
                            // .withColumn("event_timestamp", col("event_timestamp").cast(TimestampType))
                            // .withColumn("ad_clicked", col("ad_clicked").cast(BooleanType))
        
    def start = {
        println("Printing Schema")
        println(data.printSchema)
       var df  = data.withWatermark("event_timestamp","1 hour")
                    .groupBy(window(col("event_timestamp"), "5 seconds").alias("window"))
                        .agg(
                            count("userId").alias("total_users_visited"),
                            approx_count_distinct("userId").alias("total_unique_users_visited"),
                            avg("durationSeconds").alias("avg_time_spend_in_website"),
                            sum(when(col("adClicked"),1).otherwise(0)).alias("add_watched"))



        def writeToMongo(df: org.apache.spark.sql.Dataset[org.apache.spark.sql.Row]){df.write.option("uri",CONNECTION_URI).mode("append").format("mongo").save}
        println("Successfully executing the streaming between ADLS to CosmosDB")
        val query = df.writeStream
                .outputMode("update")
                .foreachBatch {(x:org.apache.spark.sql.Dataset[org.apache.spark.sql.Row], y:scala.Long) => writeToMongo(x) }
                .option("checkpointLocation", "/mnt/streamingdatafinal/clickstreamcheckpoint/asls_to_cosmos/finalcheck")
                // .trigger(Trigger.ProcessingTime("4 seconds"))
                .start.awaitTermination()
    }
}
