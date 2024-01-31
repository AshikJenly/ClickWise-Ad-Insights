from faker import Faker
import random
import pandas as pd
import asyncio
from azure.eventhub import EventData
from azure.eventhub.aio import EventHubProducerClient
import json
import time
import datetime
from datetime import date
from datetime import datetime, timedelta


fake = Faker()

# EVENT_HUB_CONNECTION_STR = "Endpoint=sb://forspark.servicebus.windows.net/;SharedAccessKeyName=produce;SharedAccessKey=KwCKwHHdV2WVp1hJRxv+tTeFjqnmkKeG1+AEhGk+RMs=;EntityPath=clickstream"
EVENT_HUB_CONNECTION_STR = "Endpoint=sb://forspark.servicebus.windows.net/;SharedAccessKeyName=produce;SharedAccessKey=7hiTBO6qCXCgXVDEmHr40y+RFUMHAJGoA+AEhKvCiII=;EntityPath=clickstream"  ##fill in with the connection string from EventHub
EVENT_HUB_NAME = "clickstream"  ##fill in with the EventHub instance name
producer = EventHubProducerClient.from_connection_string(conn_str=EVENT_HUB_CONNECTION_STR, eventhub_name=EVENT_HUB_NAME)

async def get_random_data(user_id_starter, num_rows=10):
    data = []
    count = 1
    minutes = 5
    seconds = 0
    for _ in range(num_rows):
        #count = 1
        event_data_batch = await producer.create_batch()
        
        # event_timestamp = fake.date_time_this_decade()
        event_timestamp = datetime.now() + timedelta(minutes=minutes,seconds=seconds)
        
        user_id = f"{user_id_starter}_{_}"
        session_id = fake.uuid4()
        page_url = fake.url()
        device_type = random.choice(["desktop", "mobile", "tablet"])
        browser = fake.user_agent().split()[0]  # Extracting the browser from the user agent
        geo_location = fake.country()
        event_type = random.choice(["page_view", "click", "exit"])
        ad_clicked = random.choice([True, False])
        ad_id = fake.uuid4() if ad_clicked else None
        duration_seconds = random.randint(1, 600)  # Random duration between 1 and 600 seconds

        row = {
            "event_timestamp": event_timestamp,
            "user_id": user_id,
            "session_id": session_id,
            "page_url": page_url,
            "device_type": device_type,
            "browser": browser,
            "geo_location": geo_location,
            "event_type": event_type,
            "ad_clicked": ad_clicked,
            "ad_id": ad_id,
            "duration_seconds": duration_seconds,
        }
        event_data_batch.add(EventData(json.dumps(row, default=str)))

        await producer.send_batch(event_data_batch)
        # time.sleep(2)
        # print(row)
        if count % 10 == 0:
            seconds += random.randint(1,30)
            if count % 100 == 0:
                minutes += 3
                print(f"produced {count}")
        count += 1


asyncio.run(get_random_data("uuser",5000))

# Example usage
# random_data = get_random_data(user_id_starter="user123", num_rows=5000)
# print(random_data)
# random_data.to_csv("random_click_stream_data")
