package $package$.controllers

import play.api.libs.json.Format
import play.api.mvc.Session
import uk.gov.hmrc.crypto.{ApplicationCrypto, PlainText}
import $package$.journeys.$servicePrefixCamel$JourneyStateFormats
import $package$.models._
import $package$.services.{MongoDBCachedJourneyService, $servicePrefixCamel$JourneyService}
import $package$.stubs.$servicePrefixCamel$ApiStubs
import $package$.support.{ServerISpec, TestData, TestJourneyService}

import java.time.LocalDate
import scala.concurrent.ExecutionContext.Implicits.global
import play.api.libs.ws.DefaultWSCookie
import akka.actor.ActorSystem
import $package$.repository.CacheRepository
class $servicePrefixCamel$JourneyISpec extends $servicePrefixCamel$JourneyISpecSetup with $servicePrefixCamel$ApiStubs {

  import journey.model.State._

  val today = LocalDate.now
  val (y, m, d) = (today.getYear(), today.getMonthValue(), today.getDayOfMonth())

  "$servicePrefixCamel$JourneyController" when {
    "GET /" should {
      "show the start page" in {
        implicit val journeyId: JourneyId = JourneyId()
        journey.setState(Start)
        givenAuthorisedForEnrolment(Enrolment("HMRC-XYZ", "$authorisedIdentifierKey$", "foo"))

        val result = await(request("/").get())

        result.status shouldBe 200
        journey.getState shouldBe Start
      }
    }
  }
}

trait $servicePrefixCamel$JourneyISpecSetup extends ServerISpec {

  lazy val journey = new TestJourneyService[JourneyId]
    with $servicePrefixCamel$JourneyService[JourneyId] with MongoDBCachedJourneyService[JourneyId] {

    override lazy val actorSystem: ActorSystem = app.injector.instanceOf[ActorSystem]
    override lazy val cacheRepository = app.injector.instanceOf[CacheRepository]
    override lazy val applicationCrypto = app.injector.instanceOf[ApplicationCrypto]

    override val stateFormats: Format[model.State] =
      $servicePrefixCamel$JourneyStateFormats.formats

    override def getJourneyId(journeyId: JourneyId): Option[String] = Some(journeyId.value)
  }

  final def request(path: String)(implicit journeyId: JourneyId) = {
    val sessionCookie = sessionCookieBaker.encodeAsCookie(Session(Map(journey.journeyKey -> journeyId.value)))

    wsClient
      .url(s"\$baseUrl\$path")
      .withCookies(
        DefaultWSCookie(sessionCookie.name, sessionCookieCrypto.crypto.encrypt(PlainText(sessionCookie.value)).value)
      )
  }
}
