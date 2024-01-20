package example

import org.apache.spark.sql.types._
import org.apache.spark.sql.expressions.Window
object DataLakeToCosmos extends App
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
      
            var df = spark.readStream.schema(schema).option("header",true).csv("/warehouse/click")
            
            df =   df.filter(col("event_timestamp") >= to_timestamp(lit("2024-01-10")))
            df =   df.withColumn("page_count",count("page_url").over(Window.partitionBy("page_url")).alias("count"))
            
            val max_number_of_page_view = df.select(max(col("page_count")).alias("max_count")).head.getAs[Long]("max_count")
           
            val max_viewed_page   = df.filter(col("page_count") === lit(max_number_of_page_view)).select("page_url").distinct.collect.map(row => row.get(0)).toList
            val number_of_users =df.count 
            val number_of_ad_clickerd = df.filter(col("ad_clicked") === true).count
            val avg_time_spend =  df.filter(col("ad_clicked") === true).agg(avg("duration_seconds").alias("avg_seconds")).head.getAs[Double]("avg_seconds")
            
            // val resultDF = //create a df with values max_viewed_page,number_of_users,number_of_ad_clickerd,avg_time_spend
            
         
            // Define the schema for the result DataFrame
            val resultSchema = StructType(Seq(
                  StructField("max_viewed_page", StringType, nullable = false),
                  StructField("number_of_users", LongType, nullable = false),
                  StructField("number_of_ad_clicked", LongType, nullable = false),
                  StructField("avg_time_spend", DoubleType, nullable = false)
                 ))
               val resultRows = Seq(
                        Row(max_viewed_page.mkString(","), number_of_users, number_of_ad_clicked, avg_time_spend)
                        )
            val resultDF = spark.createDataFrame(spark.sparkContext.parallelize(resultRows), resultSchema)

            val res = resultDF.writeStream
                        .outputMode("append")
                        .format("console")
                        .option("checkpointLocation","/checkpoint/click")
                        .start()
                        .awaitTermination
}

