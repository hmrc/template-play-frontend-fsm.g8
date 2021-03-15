package $package$.connectors

import play.api.Application
import $package$.stubs.$servicePrefixCamel$ApiStubs
import $package$.support.AppISpec
import uk.gov.hmrc.http._
import $package$.support.TestData

import scala.concurrent.ExecutionContext.Implicits.global

class $servicePrefixCamel$ApiConnectorISpec extends $servicePrefixCamel$ApiConnectorISpecSetup {

  "$servicePrefixCamel$ApiConnector" when {
    "createCase" should {
      "return case reference id if success" in {
        given$servicePrefixCamel$ApiRequestSucceeds()

        val result: $servicePrefixCamel$CaseResponse =
          await(connector.createCase(createCaseRequest))

        result.result shouldBe Some($servicePrefixCamel$Result("A1234567890", generatedAt))
        result.error shouldBe None

        verifyCreateCaseRequestHappened(1)
      }

      "return error code and message if failure" in {
        given$servicePrefixCamel$ApiStub(
          400,
          validRequestOf$servicePrefixCamel$Api(),
          createCaseApiErrorResponseBody("555", "Foo Bar")
        )

        val result: $servicePrefixCamel$CaseResponse =
          await(connector.createCase(createCaseRequest))

        result.result shouldBe None
        result.error shouldBe Some(ApiError("555", Some("Foo Bar")))

        verifyCreateCaseRequestHappened(3)
      }

      "throw exception if returns 500" in {
        given$servicePrefixCamel$ApiStub(500, validRequestOf$servicePrefixCamel$Api(), "")

        an[$servicePrefixCamel$ApiError] shouldBe thrownBy {
          await(connector.createCase(createCaseRequest))
        }

        verifyCreateCaseRequestHappened(3)
      }
    }
  }

}

trait $servicePrefixCamel$ApiConnectorISpecSetup extends AppISpec with $servicePrefixCamel$ApiStubs {

  implicit val hc: HeaderCarrier = HeaderCarrier()

  override def fakeApplication: Application = appBuilder.build()

  lazy val connector: $servicePrefixCamel$ApiConnector =
    app.injector.instanceOf[$servicePrefixCamel$ApiConnector]

  def createCaseRequest =
    $servicePrefixCamel$$servicePrefixCamel$Request(
      declarationDetails = TestData.exportDeclarationDetails,
      questionsAnswers = TestData.fullExampleQuestions(),
      eori = Some("GB123456789012345")
    )

}
