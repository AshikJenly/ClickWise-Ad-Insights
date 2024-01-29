package services
import play.api._
import play.api.mvc._
import play.api.libs.json._

import models._
class  ProduceDataToHubs{

    def produceUserActivity(data:JsValue) = {
        println(data)
    }
}


// user_id = f"{user_id_starter}_{_}"
//         session_id = fake.uuid4()
//         page_url = fake.url()
//         device_type = random.choice(["desktop", "mobile", "tablet"])
//         browser = fake.user_agent().split()[0]  # Extracting the browser from the user agent
//         geo_location = fake.country()
//         event_type = random.choice(["page_view", "click", "exit"])
//         ad_clicked = random.choice([True, False])
//         ad_id = fake.uuid4() if ad_clicked else None
//         duration_seconds = random.randint(1, 600) 