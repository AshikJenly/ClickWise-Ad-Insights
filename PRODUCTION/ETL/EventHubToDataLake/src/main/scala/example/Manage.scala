package example

import org.apache.spark.sql.SparkSession
import scala.concurrent.{Future,Await}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration
import org.apache.spark.SparkConf

object Manage
{
    def main(args:Array[String]) = {

        val sparkConf1 = new SparkConf().setAppName("spark1")
        val sparkConf2 = new SparkConf().setAppName("spark2")

        sparkConf1.set("spark.executor.memory", "4g")
        sparkConf2.set("spark.executor.memory", "4g")

        val spark1 = SparkSession.builder().config(sparkConf1).getOrCreate() 
        val spark2 = SparkSession.builder().config(sparkConf2).getOrCreate() 

        val data_lake_to_cosmos_db = new DataLakeToCosmosDB(spark1)
        val event_hub_to_data_lake = new EventHubToDataLake(spark2)
    
        val future1 = Future { event_hub_to_data_lake.start }
        val future2 = Future { data_lake_to_cosmos_db.start }
        val allQueries = Future.sequence(Seq(future1, future2))

        val results = Await.result(allQueries, Duration.Inf)
        if (results.nonEmpty) {
            println("Both streaming queries have completed successfully.")
        }   
    }
}