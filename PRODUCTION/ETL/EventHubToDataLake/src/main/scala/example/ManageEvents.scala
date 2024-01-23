package example

import org.apache.spark.sql.SparkSession

object ManageEvents
{
    def main(args:Array[String]) = {

        val spark = SparkSession.builder().getOrCreate() 

        val event_hub_to_datalake = new EventHubToDataLake(spark)
    
        event_hub_to_datalake.start 
        
    }
}