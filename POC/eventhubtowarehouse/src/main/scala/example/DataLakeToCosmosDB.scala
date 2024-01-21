package example

import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.types._
import org.apache.spark.sql.functions._
import org.apache.spark.sql.streaming._

object DataLakeToCosmos extends App {
  val spark = SparkSession.builder.appName("DataLakeToCosmos").getOrCreate()
      spark.conf.set("spark.sql.streaming.stateStoreRetention", "1 minutes")

  val schema = StructType(Seq(
    StructField("event_timestamp", StringType, nullable = false),
    StructField("user_id", StringType, nullable = false),
    StructField("session_id", StringType, nullable = false),
    StructField("page_url", StringType, nullable = false),
    StructField("device_type", StringType, nullable = false),
    StructField("browser", StringType, nullable = false),
    StructField("geo_location", StringType, nullable = false),
    StructField("event_type", StringType, nullable = false),
    StructField("ad_clicked", StringType, nullable = false),
    StructField("ad_id", StringType, nullable = false),
    StructField("duration_seconds", StringType, nullable = false),
    StructField("Month", StringType, nullable = false),
    StructField("year", StringType, nullable = false)
  ))


  var df  = spark.readStream
                 .schema(schema)
                 .parquet("/warehouse/click")
                 .withColumn("event_timestamp", col("event_timestamp").cast(TimestampType))
                 .withColumn("ad_clicked", col("ad_clicked").cast(BooleanType))
                 

      df  = df
            .withWatermark("event_timestamp","5 minutes")
            .groupBy(window(col("event_timestamp"), "1 minutes").alias("window"))
            .agg(
                  count("user_id").alias("total_users_visited"),
                  approx_count_distinct("user_id").alias("total_unique_users_visited"),
                  avg("duration_seconds").alias("avg_time_spend_in_website"),
                  sum(when(col("ad_clicked"),1).otherwise(0)).alias("add_watched")
                  )



def writeToMongo(df: org.apache.spark.sql.Dataset[org.apache.spark.sql.Row]){df.show;df.write.option("uri","mongodb://localhost:27017/click.test").mode("overwrite").format("mongo").save}
   val query = df.writeStream
           .outputMode("complete")
           .foreachBatch {(x:org.apache.spark.sql.Dataset[org.apache.spark.sql.Row], y:scala.Long) => writeToMongo(x) }
           .option("checkpointLocation", "/checkpoint/click")
           .start.awaitTermination()

//   val query = resultDF.writeStream
//     .outputMode("complete").format("console").option("checkpointLocation", "/checkpoint/click").start()
}
