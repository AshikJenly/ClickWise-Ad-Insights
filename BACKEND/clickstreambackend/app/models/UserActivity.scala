package models

import java.time.Instant
import play.api.libs.json._

case class UserActivity(
    event_timestamp: Instant,
   var userId: String,
    sessionId: String,
    pageUrl: String,
    deviceType: String,
    browser: String,
    geoLocation: String,
    eventType: String,
    adClicked: Boolean,
    adId: String,
    durationSeconds: Int
)

object UserActivity {
  implicit val format: Format[UserActivity] = Json.format[UserActivity]
}