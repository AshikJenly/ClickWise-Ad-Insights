package example

import org.apache.spark.sql.SparkSession

object ManageCosmos
{
    def main(args:Array[String]) = {

        val spark = SparkSession.builder().getOrCreate() 

        val data_lake_to_cosmos_db = new DataLakeToCosmosDB(spark)
    
        data_lake_to_cosmos_db.start 
        
    }
}