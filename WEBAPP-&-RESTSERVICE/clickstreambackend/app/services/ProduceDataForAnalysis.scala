package services
import play.api._
import play.api.mvc._
import play.api.libs.json._
import models._
import java.nio.charset.StandardCharsets

import com.microsoft.azure.eventhubs._
import java.util.concurrent.Executors

class ProduceDataToHubs{

  // Replace these values with your actual Event Hubs namespace, name, and SAS key
    val EVENT_HUB_CONNECTION_STR:String = sys.env.get("AZUREEVENTHUB")
    val EVENT_HUB_NAME :String= "clickstream" 
    implicit val user_activity: Format[UserActivity] = Json.format[UserActivity]


  def produceDataToHubs(user_activity_data:UserActivity) ={
     val connStr = new ConnectionStringBuilder(EVENT_HUB_CONNECTION_STR).setEventHubName(EVENT_HUB_NAME)
    val executorService = Executors.newScheduledThreadPool(4)
    val retryPolicy = RetryPolicy.getDefault

    val ehClient = EventHubClient.createSync(connStr.toString, executorService)

    try {
        val json: JsValue = Json.toJson(user_activity_data)

        // Convert the JsValue to a JSON string
        val jsonString: String = Json.stringify(json)
      val messagePayload = jsonString
      val messageBytes = messagePayload.getBytes(StandardCharsets.UTF_8)

      val eventData = EventData.create(messageBytes)
    //   // You can add custom properties to the event if needed
      eventData.getProperties.put("key", "value")

      ehClient.sendSync(eventData)
      println("Event sent successfully.")
    } finally {
    //   ehClient.closeSync()
    }
  }

   def produceUserActivity(data:JsValue) = {
        // println(data)
    }
}
