from azure.eventhub import EventHubConsumerClient

# Event Hubs connection string
CONNECTION_STRING = ""
# Event Hubs consumer group and partition
CONSUMER_GROUP = "$Default"
PARTITION_ID = "0"

# Define callback function to process received events
def on_event(partition_context, event):
    print("Received event from partition: {}".format(partition_context.partition_id))
    print("Data: {}".format(event.body_as_str()))
    print("Properties: {}".format(event.properties))
    print("SystemProperties: {}".format(event.system_properties))
    print("--------")

# Create Event Hub consumer client
consumer_client = EventHubConsumerClient.from_connection_string(
    conn_str=CONNECTION_STRING,
    consumer_group=CONSUMER_GROUP
)

try:
    # Start consuming events
    with consumer_client:
        consumer_client.receive(
            on_event=on_event,
            starting_position="-1",  # Start reading from the latest available event
            partition_id=PARTITION_ID
        )
except KeyboardInterrupt:
    print("Receiving has stopped.")
