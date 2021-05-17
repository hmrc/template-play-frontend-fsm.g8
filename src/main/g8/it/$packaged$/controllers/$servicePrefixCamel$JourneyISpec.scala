package $package$.controllers

import play.api.libs.json.Format
import play.api.mvc.Session
import uk.gov.hmrc.crypto.{ApplicationCrypto, PlainText}
import $package$.connectors.$servicePrefixCamel$Result
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
        journey.getState shouldBe EnterDeclarationDetails()
      }
    }

    "GET /new/declaration-details" should {
      "show declaration details page if at Start" in {
        implicit val journeyId: JourneyId = JourneyId()
        journey.setState(EnterDeclarationDetails())
        givenAuthorisedForEnrolment(Enrolment("HMRC-XYZ", "$authorisedIdentifierKey$", "foo"))

        val result = await(request("/new/declaration-details").get())

        result.status shouldBe 200
        result.body should include(htmlEscapedPageTitle("view.declaration-details.title"))
        result.body should include(htmlEscapedMessage("view.declaration-details.heading"))
        journey.getState shouldBe EnterDeclarationDetails()
      }

      "redisplay pre-filled enter declaration details page " in {
        implicit val journeyId: JourneyId = JourneyId()
        journey.setState(
          AnswerExampleQuestionsRequestType(
            ExampleQuestionsStateModel(
              DeclarationDetails(EPU(235), EntryNumber("A11111X"), LocalDate.parse("2020-09-23")),
              ExampleQuestions()
            )
          )
        )
        givenAuthorisedForEnrolment(Enrolment("HMRC-XYZ", "$authorisedIdentifierKey$", "foo"))

        val result = await(request("/new/declaration-details").get())

        result.status shouldBe 200
        result.body should include(htmlEscapedPageTitle("view.declaration-details.title"))
        result.body should include(htmlEscapedMessage("view.declaration-details.heading"))
        result.body should (include("235") and include("A11111X"))
        result.body should (include("2020") and include("09") and include("23"))
        journey.getState shouldBe EnterDeclarationDetails(
          Some(DeclarationDetails(EPU(235), EntryNumber("A11111X"), LocalDate.parse("2020-09-23"))),
          Some(ExampleQuestions())
        )
      }
    }

    "POST /new/declaration-details" should {
      "submit the form and ask next for requestType when entryNumber is for export" in {
        implicit val journeyId: JourneyId = JourneyId()
        journey.setState(EnterDeclarationDetails(None))
        givenAuthorisedForEnrolment(Enrolment("HMRC-XYZ", "$authorisedIdentifierKey$", "foo"))

        val payload = Map(
          "entryDate.year"  -> s"\$y",
          "entryDate.month" -> f"\$m%02d",
          "entryDate.day"   -> f"\$d%02d",
          "epu"             -> "235",
          "entryNumber"     -> "A11111X"
        )

        val result = await(request("/new/declaration-details").post(payload))

        result.status shouldBe 200
        result.body should include(htmlEscapedPageTitle("view.export-questions.requestType.title"))
        result.body should include(htmlEscapedMessage("view.export-questions.requestType.heading"))
        journey.getState shouldBe AnswerExampleQuestionsRequestType(
          ExampleQuestionsStateModel(
            DeclarationDetails(EPU(235), EntryNumber("A11111X"), today),
            ExampleQuestions()
          )
        )
      }

      "submit invalid form and re-display the form page with errors" in {
        implicit val journeyId: JourneyId = JourneyId()
        journey.setState(EnterDeclarationDetails())
        givenAuthorisedForEnrolment(Enrolment("HMRC-XYZ", "$authorisedIdentifierKey$", "foo"))

        val payload = Map(
          "entryDate.year"  -> "202a",
          "entryDate.month" -> "00",
          "entryDate.day"   -> "44",
          "epu"             -> "AAA",
          "entryNumber"     -> "A11X"
        )

        val result = await(request("/new/declaration-details").post(payload))

        result.status shouldBe 200
        result.body should include(htmlEscapedPageTitleWithError("view.declaration-details.title"))
        result.body should include(htmlEscapedMessage("view.declaration-details.heading"))
        journey.getState shouldBe EnterDeclarationDetails()
      }
    }

    "GET /new/export/request-type" should {
      "show the export request type question page" in {
        implicit val journeyId: JourneyId = JourneyId()
        val state = AnswerExampleQuestionsRequestType(
          ExampleQuestionsStateModel(
            DeclarationDetails(EPU(235), EntryNumber("A11111X"), LocalDate.parse("2020-09-23")),
            ExampleQuestions()
          )
        )
        journey.setState(state)
        givenAuthorisedForEnrolment(Enrolment("HMRC-XYZ", "$authorisedIdentifierKey$", "foo"))

        val result = await(request("/new/export/request-type").get())

        result.status shouldBe 200
        result.body should include(htmlEscapedPageTitle("view.export-questions.requestType.title"))
        result.body should include(htmlEscapedMessage("view.export-questions.requestType.heading"))
        journey.getState shouldBe state
      }
    }

    "POST /new/export/request-type" should {
      "submit the form and ask next for routeType" in {
        implicit val journeyId: JourneyId = JourneyId()
        journey.setState(
          AnswerExampleQuestionsRequestType(
            ExampleQuestionsStateModel(
              DeclarationDetails(EPU(235), EntryNumber("A11111X"), LocalDate.parse("2020-09-23")),
              ExampleQuestions()
            )
          )
        )
        givenAuthorisedForEnrolment(Enrolment("HMRC-XYZ", "$authorisedIdentifierKey$", "foo"))

        val payload = Map("requestType" -> "New")

        val result = await(request("/new/export/request-type").post(payload))

        result.status shouldBe 200
        result.body should include(htmlEscapedPageTitle("view.export-questions.routeType.title"))
        result.body should include(htmlEscapedMessage("view.export-questions.routeType.heading"))
        journey.getState shouldBe AnswerExampleQuestionsRouteType(
          ExampleQuestionsStateModel(
            DeclarationDetails(EPU(235), EntryNumber("A11111X"), LocalDate.parse("2020-09-23")),
            ExampleQuestions(requestType = Some(ExampleRequestType.New))
          )
        )
      }

      "submit invalid form and re-display the form page with errors" in {
        implicit val journeyId: JourneyId = JourneyId()
        val state = AnswerExampleQuestionsRequestType(
          ExampleQuestionsStateModel(
            DeclarationDetails(EPU(235), EntryNumber("A11111X"), LocalDate.parse("2020-09-23")),
            ExampleQuestions()
          )
        )
        journey.setState(state)
        givenAuthorisedForEnrolment(Enrolment("HMRC-XYZ", "$authorisedIdentifierKey$", "foo"))

        val payload = Map("requestType" -> "Foo")

        val result = await(request("/new/export/request-type").post(payload))

        result.status shouldBe 200
        result.body should include(htmlEscapedPageTitleWithError("view.export-questions.requestType.title"))
        result.body should include(htmlEscapedMessage("view.export-questions.requestType.heading"))
        journey.getState shouldBe state
      }
    }

    "GET /new/export/route-type" should {
      "show the export route type question page" in {
        implicit val journeyId: JourneyId = JourneyId()
        val state = AnswerExampleQuestionsRouteType(
          ExampleQuestionsStateModel(
            DeclarationDetails(EPU(235), EntryNumber("A11111X"), LocalDate.parse("2020-09-23")),
            ExampleQuestions(requestType = Some(ExampleRequestType.C1601))
          )
        )
        journey.setState(state)
        givenAuthorisedForEnrolment(Enrolment("HMRC-XYZ", "$authorisedIdentifierKey$", "foo"))

        val result = await(request("/new/export/route-type").get())

        result.status shouldBe 200
        result.body should include(htmlEscapedPageTitle("view.export-questions.routeType.title"))
        result.body should include(htmlEscapedMessage("view.export-questions.routeType.heading"))
        journey.getState shouldBe state
      }
    }

    "POST /new/export/route-type" should {
      "submit the form and show check-your-answers page" in {
        implicit val journeyId: JourneyId = JourneyId()
        journey.setState(
          AnswerExampleQuestionsRouteType(
            ExampleQuestionsStateModel(
              DeclarationDetails(EPU(235), EntryNumber("A11111X"), LocalDate.parse("2020-09-23")),
              ExampleQuestions(requestType = Some(ExampleRequestType.C1602))
            )
          )
        )
        givenAuthorisedForEnrolment(Enrolment("HMRC-XYZ", "$authorisedIdentifierKey$", "foo"))

        val payload = Map("routeType" -> "Route3")

        val result = await(request("/new/export/route-type").post(payload))

        result.status shouldBe 200
        result.body should include(htmlEscapedPageTitle("view.export-questions.summary.title"))
        result.body should include(htmlEscapedMessage("view.export-questions.summary.heading"))
        journey.getState shouldBe ExampleQuestionsSummary(
          ExampleQuestionsStateModel(
            DeclarationDetails(EPU(235), EntryNumber("A11111X"), LocalDate.parse("2020-09-23")),
            ExampleQuestions(requestType = Some(ExampleRequestType.C1602), routeType = Some(ExampleRouteType.Route3))
          )
        )
      }

      "submit invalid form and re-display the form page with errors" in {
        implicit val journeyId: JourneyId = JourneyId()
        val state = AnswerExampleQuestionsRouteType(
          ExampleQuestionsStateModel(
            DeclarationDetails(EPU(235), EntryNumber("A11111X"), LocalDate.parse("2020-09-23")),
            ExampleQuestions(requestType = Some(ExampleRequestType.C1602))
          )
        )
        journey.setState(state)
        givenAuthorisedForEnrolment(Enrolment("HMRC-XYZ", "$authorisedIdentifierKey$", "foo"))

        val payload = Map("routeType" -> "Foo")

        val result = await(request("/new/export/route-type").post(payload))

        result.status shouldBe 200
        result.body should include(htmlEscapedPageTitleWithError("view.export-questions.routeType.title"))
        result.body should include(htmlEscapedMessage("view.export-questions.routeType.heading"))
        journey.getState shouldBe state
      }
    }

    "GET /new/export/check-your-answers" should {
      "show the export questions summary page" in {
        implicit val journeyId: JourneyId = JourneyId()

        val state = ExampleQuestionsSummary(
          ExampleQuestionsStateModel(TestData.exportDeclarationDetails, TestData.fullExampleQuestions())
        )
        journey.setState(state)
        givenAuthorisedForEnrolment(Enrolment("HMRC-XYZ", "$authorisedIdentifierKey$", "foo"))

        val result = await(request("/new/export/check-your-answers").get())

        result.status shouldBe 200
        result.body should include(htmlEscapedPageTitle("view.export-questions.summary.title"))
        result.body should include(htmlEscapedMessage("view.export-questions.summary.heading"))
        journey.getState shouldBe state
      }
    }

    "GET /new/confirmation" should {
      "show the confirmation page" in {
        implicit val journeyId: JourneyId = JourneyId()

        val state = $servicePrefixCamel$Confirmation(
          TestData.exportDeclarationDetails,
          TestData.fullExampleQuestions(),
          $servicePrefixCamel$Result("TBC", generatedAt)
        )
        journey.setState(state)
        givenAuthorisedForEnrolment(Enrolment("HMRC-XYZ", "$authorisedIdentifierKey$", "foo"))

        val result = await(request("/new/confirmation").get)

        result.status shouldBe 200
        result.body should include(htmlEscapedPageTitle("view.create-case-confirmation.title"))
        result.body should include(htmlEscapedMessage("view.create-case-confirmation.heading"))

        journey.getState shouldBe state
      }
    }

    "GET /$serviceUrlPrefix$/foo" should {
      "return an error page not found" in {
        implicit val journeyId: JourneyId = JourneyId()
        givenAuthorisedForEnrolment(Enrolment("HMRC-XYZ", "$authorisedIdentifierKey$", "foo"))

        val result = await(request("/foo").get())

        result.status shouldBe 404
        result.body should include("Page not found")
        journey.get shouldBe None
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
