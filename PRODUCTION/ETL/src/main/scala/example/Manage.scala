package example

import org.apache.spark.sql.SparkSession

object Manage
{
    def main(args:Array[String]) = {

        val spark = SparkSession.builder().getOrCreate() 
        val event_hub_to_data_lake = new EventHubToDataLake(spark)
        val data_lake_to_cosmos_db = new DataLakeToCosmosDB(spark)
        event_hub_to_data_lake.start
        data_lake_to_cosmos_db.start
        
    }
}