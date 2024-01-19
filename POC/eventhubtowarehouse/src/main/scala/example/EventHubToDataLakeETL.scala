package example

import org.apache.spark.sql.types._
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
      
            var df = spark.readStream.s(schema).option("header",true).csv("/data/click")
            df = df.withColumn("Month",month(col("event_timestamp")))
            df = df.withColumn("year",year(col("event_timestamp")))

            val res = df.writeStream
                        .outputMode("append")
                        .partitionBy("year","Month")
                        .format("parquet")
                        .option("checkpointLocation","/checkpoint/click")
                        .option("path","/warehouse/click")
                        .start()
                        .awaitTermination

}

