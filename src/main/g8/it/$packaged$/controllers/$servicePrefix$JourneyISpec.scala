package $package$.controllers

import akka.actor.ActorSystem
import play.api.libs.json.Format
import play.api.libs.ws.DefaultWSCookie
import play.api.libs.ws.StandaloneWSRequest
import play.api.mvc.AnyContent
import play.api.mvc.Call
import play.api.mvc.Cookie
import play.api.mvc.Request
import play.api.mvc.Session
import play.api.test.FakeRequest
import uk.gov.hmrc.crypto.ApplicationCrypto
import uk.gov.hmrc.crypto.PlainText
import $package$.journeys.$servicePrefix$JourneyStateFormats
import $package$.repository.CacheRepository
import $package$.services.MongoDBCachedJourneyService
import $package$.services.$servicePrefix$JourneyService
import $package$.support.ServerISpec
import $package$.support.TestJourneyService

import java.time.LocalDate
import scala.concurrent.ExecutionContext.Implicits.global
class $servicePrefix$JourneyISpec extends $servicePrefix$JourneyISpecSetup {

  import journey.model.State._

  val today = LocalDate.now
  val (y, m, d) = (today.getYear(), today.getMonthValue(), today.getDayOfMonth())

  "$servicePrefix$JourneyController" when {
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

trait $servicePrefix$JourneyISpecSetup extends ServerISpec {

  lazy val journey = new TestJourneyService[JourneyId]
    with $servicePrefix$JourneyService[JourneyId] with MongoDBCachedJourneyService[JourneyId] {

    override lazy val actorSystem: ActorSystem = app.injector.instanceOf[ActorSystem]
    override lazy val cacheRepository = app.injector.instanceOf[CacheRepository]
    override lazy val applicationCrypto = app.injector.instanceOf[ApplicationCrypto]

    override val stateFormats: Format[model.State] =
      $servicePrefix$JourneyStateFormats.formats

    override def getJourneyId(journeyId: JourneyId): Option[String] = Some(journeyId.value)
  }

  final def fakeRequest(cookies: Cookie*)(implicit
    journeyId: JourneyId
  ): Request[AnyContent] =
    fakeRequest("GET", "/", cookies: _*)

  final def fakeRequest(method: String, path: String, cookies: Cookie*)(implicit
    journeyId: JourneyId
  ): Request[AnyContent] =
    FakeRequest(Call(method, path))
      .withCookies(cookies: _*)
      .withSession(journey.journeyKey -> journeyId.value)

  final def request(path: String)(implicit journeyId: JourneyId): StandaloneWSRequest = {
    val sessionCookie =
      sessionCookieBaker
        .encodeAsCookie(Session(Map(journey.journeyKey -> journeyId.value)))
    wsClient
      .url(s"\$baseUrl\$path")
      .withCookies(
        DefaultWSCookie(
          sessionCookie.name,
          sessionCookieCrypto.crypto.encrypt(PlainText(sessionCookie.value)).value
        )
      )
  }

  final def requestWithCookies(path: String, cookies: (String, String)*)(implicit
    journeyId: JourneyId
  ): StandaloneWSRequest = {
    val sessionCookie =
      sessionCookieBaker
        .encodeAsCookie(Session(Map(journey.journeyKey -> journeyId.value)))

    wsClient
      .url(s"\$baseUrl\$path")
      .withCookies(
        (cookies.map(c => DefaultWSCookie(c._1, c._2)) :+ DefaultWSCookie(
          sessionCookie.name,
          sessionCookieCrypto.crypto.encrypt(PlainText(sessionCookie.value)).value
        )): _*
      )
  }
}
