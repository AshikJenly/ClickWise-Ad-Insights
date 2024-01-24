package example

import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.types._
import org.apache.spark.sql.functions._
import org.apache.spark.sql.streaming.Trigger


case class DataLakeToCosmosDB (private val spark:SparkSession) {
    //   spark.conf.set("spark.sql.streaming.stateStoreRetention", "10 minutes")

    private val schema = StructType(Seq(
                    StructField("event_timestamp", TimestampType, nullable = true),
                    StructField("user_id", StringType, nullable = true),
                    StructField("session_id", StringType, nullable = true),
                    StructField("page_url", StringType, nullable = true),
                    StructField("device_type", StringType, nullable = true),
                    StructField("browser", StringType, nullable = true),
                    StructField("geo_location", StringType, nullable = true),
                    StructField("event_type", StringType, nullable = true),
                    StructField("ad_clicked", BooleanType, nullable = true),
                    StructField("ad_id", StringType, nullable = true),
                    StructField("duration_seconds", IntegerType, nullable = true),
                    StructField("Month", IntegerType, nullable = true),
                    StructField("year", IntegerType, nullable = true)
                ))

    private val CONNECTION_URI = "mongodb://forspark:J6cznn1Wd7ukY4lNNxqhhyJxVRm4aBIH8WrAqrHspTjbKTW5KvrIEqHdL9SvsyLmZziicHF4M3v8ACDbna0CdA==@forspark.mongo.cosmos.azure.com:10255/click.test?ssl=true&retrywrites=false&replicaSet=globaldb&maxIdleTimeMS=120000&appName=@forspark@"
    var df  = spark.readStream
                            .schema(schema)
                            .parquet("/mnt/streamingdata/clickstream/warehouse/click")
                            // .withColumn("event_timestamp", col("event_timestamp").cast(TimestampType))
                            // .withColumn("ad_clicked", col("ad_clicked").cast(BooleanType))
    def start = {
        println("Printing Schema")
        println(df.printSchema)
        df  = df
            .withWatermark("event_timestamp","1 day")
            .groupBy(window(col("event_timestamp"), "1 hour").alias("window"))
            .agg(
                  count("user_id").alias("total_users_visited"),
                  approx_count_distinct("user_id").alias("total_unique_users_visited"),
                  avg("duration_seconds").alias("avg_time_spend_in_website"),
                  sum(when(col("ad_clicked"),1).otherwise(0)).alias("add_watched")
                  )



        def writeToMongo(df: org.apache.spark.sql.Dataset[org.apache.spark.sql.Row]){df.write.option("uri",CONNECTION_URI).mode("append").format("mongo").save}
        println("Successfully executing the streaming between ADLS to CosmosDB")
        val query = df.writeStream
                .outputMode("update")
                .foreachBatch {(x:org.apache.spark.sql.Dataset[org.apache.spark.sql.Row], y:scala.Long) => writeToMongo(x) }
                .option("checkpointLocation", "/mnt/streamingdata/clickstreamcheckpoint/asls_to_cosmos/check")
                .trigger(Trigger.ProcessingTime("4 seconds"))
                .start.awaitTermination()
    }
}
