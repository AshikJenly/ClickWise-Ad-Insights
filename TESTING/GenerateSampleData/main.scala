import com.azure.messaging.eventhubs.EventHubProducerClient
import com.azure.messaging.eventhubs.EventHubProducerClientBuilder
import com.azure.messaging.eventhubs.EventData
import java.nio.charset.StandardCharsets

class MyEventHubProducer {
  private val connectionString = "your-event-hub-connection-string"
  private val eventHubName = "your-event-hub-name"

  private val producerClient: EventHubProducerClient = new EventHubProducerClientBuilder()
    .connectionString(connectionString, eventHubName)
    .buildClient()

  def sendDataToEventHub(message: String): Unit = {
    val eventData = new EventData(message.getBytes(StandardCharsets.UTF_8))
    producerClient.send(eventData)
  }

  def close(): Unit = {
    producerClient.close()
  }
}
