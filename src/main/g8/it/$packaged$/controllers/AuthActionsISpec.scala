package $package$.controllers

import play.api.mvc.Result
import play.api.mvc.Results._
import play.api.test.FakeRequest
import play.api.test.Helpers._
import play.api.{Application, Configuration, Environment}
import uk.gov.hmrc.auth.core.AuthConnector
import uk.gov.hmrc.http.{HeaderCarrier, SessionKeys}
import $package$.support.AppISpec

import scala.concurrent.Future

class AuthActionsISpec extends AuthActionISpecSetup {

  "withAuthorisedWithStrideGroup" should {
    "call body with a valid authProviderId" in {

      givenAuthorisedForStride("TBC", "StrideUserId")

      val result = TestController.testAuthorisedWithStrideGroup("TBC")

      status(result) shouldBe 200
      bodyOf(result) should include("StrideUserId")
    }

    "redirect to log in page when user not enrolled for the service" in {
      givenAuthorisedForStride("TBC", "StrideUserId")

      val result = TestController.testAuthorisedWithStrideGroup("OTHER")
      status(result) shouldBe 303
      redirectLocation(result).get should include("/stride/sign-in")
    }

    "redirect to log in page when user not authenticated" in {
      givenRequestIsNotAuthorised("SessionRecordNotFound")

      val result = TestController.testAuthorisedWithStrideGroup("TBC")
      status(result) shouldBe 303
      redirectLocation(result).get should include("/stride/sign-in")
    }

    "redirect to log in page when user authenticated with different provider" in {
      givenRequestIsNotAuthorised("UnsupportedAuthProvider")

      val result = TestController.testAuthorisedWithStrideGroup("TBC")
      status(result) shouldBe 303
      redirectLocation(result).get should include("/stride/sign-in")
    }

    "redirect to login page when stride group is 'Any'" in {
      givenAuthorisedForStride("ANY", "StrideUserId")
      val result = TestController.testAuthorisedWithStrideGroup("ANY")
      status(result) shouldBe 303
      redirectLocation(result).get should include("/stride/sign-in")
    }

    "redirect to subscription journey when insufficient enrollments" in {
      givenRequestIsNotAuthorised("InsufficientEnrolments")
      val result = TestController.testAuthorizedWithEnrolment("serviceName", "serviceKey")
      status(result) shouldBe 303
      redirectLocation(result).get should include("/subscription")
    }

    "redirect to government gateway login when authorization fails" in {
      givenRequestIsNotAuthorised("IncorrectCredentialStrength")
      val result = TestController.testAuthorizedWithEnrolment("serviceName", "serviceKey")
      status(result) shouldBe 303
      redirectLocation(result).get should include("/bas-gateway/sign-in")
    }
  }

  "authorisedWithEnrolment" should {
    "authorize when enrolment granted" in {
      givenAuthorisedForEnrolment(Enrolment("serviceName", "serviceKey", "serviceIdentifierFoo"))
      val result = TestController.testAuthorizedWithEnrolment("serviceName", "serviceKey")
      status(result) shouldBe 200
      bodyOf(result) should be("serviceIdentifierFoo")
    }

    "redirect to subscription journey when insufficient enrollments" in {
      givenRequestIsNotAuthorised("InsufficientEnrolments")
      val result = TestController.testAuthorizedWithEnrolment("serviceName", "serviceKey")
      status(result) shouldBe 303
      redirectLocation(result).get should include("/subscription")
    }

    "redirect to government gateway login when authorization fails" in {
      givenRequestIsNotAuthorised("IncorrectCredentialStrength")
      val result = TestController.testAuthorizedWithEnrolment("serviceName", "serviceKey")
      status(result) shouldBe 303
      redirectLocation(result).get should include(
        "/bas-gateway/sign-in?continue_url=%2F&origin=$serviceNameHyphen$"
      )
    }
  }

  "authorisedWithoutEnrolment" should {
    "authorize even when insufficient enrollments" in {
      givenAuthorised
      val result = TestController.testAuhorizedWithoutEnrolment
      status(result) shouldBe 200
      bodyOf(result) should be("none")
    }

    "redirect to government gateway login when authorization fails" in {
      givenRequestIsNotAuthorised("IncorrectCredentialStrength")
      val result = TestController.testAuhorizedWithoutEnrolment
      status(result) shouldBe 303
      redirectLocation(result).get should include(
        "/bas-gateway/sign-in?continue_url=%2F&origin=$serviceNameHyphen$"
      )
    }
  }
}

trait AuthActionISpecSetup extends AppISpec {

  override def fakeApplication: Application = appBuilder.build()

  object TestController extends AuthActions {

    override def authConnector: AuthConnector = app.injector.instanceOf[AuthConnector]

    override def config: Configuration = app.injector.instanceOf[Configuration]

    override def env: Environment = app.injector.instanceOf[Environment]

    import scala.concurrent.ExecutionContext.Implicits.global

    implicit val hc: HeaderCarrier = HeaderCarrier()
    implicit val request = FakeRequest().withSession(SessionKeys.authToken -> "Bearer XYZ")

    def testAuthorisedWithStrideGroup[A](group: String): Result =
      await(super.authorisedWithStrideGroup(group) { pid =>
        Future.successful(Ok(pid))
      })

    def testAuthorizedWithEnrolment[A](serviceName: String, identifierKey: String): Result =
      await(super.authorisedWithEnrolment(serviceName, identifierKey) { res =>
        Future.successful(Ok(res))
      })

    def testAuhorizedWithoutEnrolment[A]: Result =
      await(super.authorisedWithoutEnrolment { res =>
        Future.successful(Ok(res.getOrElse("none")))
      })

    override def toSubscriptionJourney(continueUrl: String): Result = Redirect("/subscription")
  }

}