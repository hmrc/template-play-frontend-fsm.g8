package $package$.stubs

import com.github.tomakehurst.wiremock.client.WireMock._
import com.github.tomakehurst.wiremock.stubbing.StubMapping
import play.mvc.Http.HeaderNames
import $package$.support.WireMockSupport

import java.time.LocalDateTime

trait $servicePrefixCamel$ApiStubs {
  me: WireMockSupport =>

  def validRequestOf$servicePrefixCamel$Api(): String =
    requestBodyOf$servicePrefixCamel$Api

  lazy val generatedAt = LocalDateTime.of(2020, 2, 29, 15, 29, 28)

  val requestBodyOf$servicePrefixCamel$Api: String =
    s"""{
       |"declarationDetails":{},
       |"questionsAnswers":{},
       |"eori":"GB123456789012345"
       |}""".stripMargin

  def caseApiSuccessResponseBody(caseReferenceNumber: String = "A1234567890"): String =
    s"""{
       |  "correlationId": "",
       |  "result": {
       |      "caseId": "\$caseReferenceNumber",
       |      "generatedAt": "\${generatedAt.toString}"
       |  } 
       |}""".stripMargin

  def createCaseApiErrorResponseBody(errorCode: String, errorMessage: String): String =
    s"""{
       |  "correlationId": "",
       |  "error": {
       |      "errorCode": "\$errorCode",
       |      "errorMessage": "\$errorMessage"
       |  } 
       |}""".stripMargin

  def given$servicePrefixCamel$ApiRequestSucceeds(): StubMapping =
    given$servicePrefixCamel$ApiStub(200, validRequestOf$servicePrefixCamel$Api(), caseApiSuccessResponseBody())

  def givenAnExternalServiceError(): StubMapping =
    given$servicePrefixCamel$ApiErrorStub(500, validRequestOf$servicePrefixCamel$Api())

  def given$servicePrefixCamel$ApiStub(httpResponseCode: Int, requestBody: String, responseBody: String): StubMapping =
    stubFor(
      post(urlEqualTo(s"/create-case"))
        .withHeader(HeaderNames.CONTENT_TYPE, containing("application/json"))
        .withRequestBody(equalToJson(requestBody, true, true))
        .willReturn(
          aResponse()
            .withStatus(httpResponseCode)
            .withHeader("Content-Type", "application/json")
            .withBody(responseBody)
        )
    )

  def given$servicePrefixCamel$ApiErrorStub(httpResponseCode: Int, requestBody: String): StubMapping =
    stubFor(
      post(urlEqualTo(s"/create-case"))
        .withHeader(HeaderNames.CONTENT_TYPE, containing("application/json"))
        .withRequestBody(equalToJson(requestBody, true, true))
        .willReturn(
          aResponse()
            .withStatus(httpResponseCode)
        )
    )

  def verifyCreateCaseRequestHappened(times: Int = 1) {
    verify(times, postRequestedFor(urlEqualTo("/create-case")))
  }

}
