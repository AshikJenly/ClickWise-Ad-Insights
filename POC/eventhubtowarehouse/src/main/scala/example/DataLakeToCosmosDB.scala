package example

import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.types._
import org.apache.spark.sql.functions._
import org.apache.spark.sql.streaming._

object DataLakeToCosmos extends App {
  val spark = SparkSession.builder.appName("DataLakeToCosmos").getOrCreate()

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


  var df  = spark.readStream.schema(schema).parquet("/warehouse/click")

      df  = df.filter(col("event_timestamp") > to_timestamp(lit("2023-12-30")))
                  .groupBy(window(col("event_timestamp"), "100 day").alias("window"))
                  .agg(
                        count("user_id").alias("total_users_visited"),
                        countDistinct("user_id").alias("total_unique_users_visited"),
                        avg("duration_seconds").alias("avg_time_spend_in_website"),
                        sum(when(col("ad_clicked"),1).otherwise(0)).alias("add_watched")
                        )



// def writeToMongo(df: org.apache.spark.sql.Dataset[org.apache.spark.sql.Row]){df.show;df.write.option("uri","mongodb://localhost:27017/click.test").mode("overwrite").format("mongo").save}
//    resultDF.writeStream.outputMode("complete").foreachBatch {(x:org.apache.spark.sql.Dataset[org.apache.spark.sql.Row], y:scala.Long) => writeToMongo(x) }.option("checkpointLocation", "/checkpoint/click").start

   val query = resultDF.writeStream
           .outputMode("overwrite")
           .format("mongo")
           .option("uri","mongodb://localhost:27017/click.test")
           .option("checkpointLocation", "/checkpoint/click")
           .start.awaitTermination()

//   val query = resultDF.writeStream
//     .outputMode("complete").format("console").option("checkpointLocation", "/checkpoint/click").start()
  query.awaitTermination()
}
