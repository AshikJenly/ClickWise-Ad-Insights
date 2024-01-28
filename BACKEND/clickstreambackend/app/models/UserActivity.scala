package models
import java.util.UUID


case class UserActivity(
    userId: String,
    sessionId: UUID,
    pageUrl: String,
    deviceType: String,
    browser: String,
    geoLocation: String,
    eventType: String,
    adClicked: Boolean,
    adId: Option[UUID],
    durationSeconds: Int
)
