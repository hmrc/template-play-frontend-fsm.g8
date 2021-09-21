package $package$.stubs

import com.github.tomakehurst.wiremock.client.WireMock._
import $package$.support.WireMockSupport

trait AuthStubs {
  me: WireMockSupport =>

  def givenRequestIsNotAuthorised(mdtpDetail: String): AuthStubs = {
    stubFor(
      post(urlEqualTo("/auth/authorise"))
        .willReturn(
          aResponse()
            .withStatus(401)
            .withHeader("WWW-Authenticate", s"""MDTP detail="\$mdtpDetail"""")
        )
    )
    this
  }

  case class Enrolment(serviceName: String, identifierName: String, identifierValue: String)

  def givenAuthorisedForEnrolment(enrolment: Enrolment): AuthStubs = {
    stubFor(
      post(urlEqualTo("/auth/authorise"))
        .atPriority(1)
        .withRequestBody(
          equalToJson(
            s"""
               |{
               |  "authorise": [
               |    { "identifiers":[], "state":"Activated", "enrolment": "\${enrolment.serviceName}" },
               |    { "authProviders": ["GovernmentGateway"] }
               |  ],
               |  "retrieve":["optionalCredentials","authorisedEnrolments"]
               |}
           """.stripMargin,
            true,
            true
          )
        )
        .willReturn(
          aResponse()
            .withStatus(200)
            .withBody(s"""
                         |{
                         |"optionalCredentials": {"providerId": "12345-credId", "providerType": "GovernmentGateway"},
                         |"authorisedEnrolments": [
                         |  { "key":"\${enrolment.serviceName}", "identifiers": [
                         |    {"key":"\${enrolment.identifierName}", "value": "\${enrolment.identifierValue}"}
                         |  ]}
                         |]}
          """.stripMargin)
        )
    )

    stubFor(
      post(urlEqualTo("/auth/authorise"))
        .atPriority(2)
        .willReturn(
          aResponse()
            .withStatus(401)
            .withHeader("WWW-Authenticate", "MDTP detail=\\"InsufficientEnrolments\\"")
        )
    )
    this
  }

  def givenAuthorisedForStride(strideGroup: String, strideUserId: String): AuthStubs = {
    stubFor(
      post(urlEqualTo("/auth/authorise"))
        .atPriority(1)
        .withRequestBody(
          equalToJson(
            s"""
               |{
               |  "authorise": [
               |    {
               |      "identifiers": [],
               |      "state": "Activated",
               |      "enrolment": "\$strideGroup"
               |    },
               |    {
               |      "authProviders": [
               |        "PrivilegedApplication"
               |      ]
               |    }
               |  ],
               |  "retrieve": ["optionalCredentials","allEnrolments"]
               |}
           """.stripMargin,
            true,
            true
          )
        )
        .willReturn(
          aResponse()
            .withStatus(200)
            .withBody(s"""
                         |{
                         |  "optionalCredentials":{
                         |    "providerId": "\$strideUserId",
                         |    "providerType": "PrivilegedApplication"
                         |  },
                         |  "allEnrolments":[
                         |    {"key":"\$strideGroup"}
                         |  ]
                         |}
       """.stripMargin)
        )
    )

    stubFor(
      post(urlEqualTo("/auth/authorise"))
        .atPriority(2)
        .willReturn(
          aResponse()
            .withStatus(401)
            .withHeader("WWW-Authenticate", "MDTP detail=\\"InsufficientEnrolments\\"")
        )
    )
    this
  }

  def givenAuthorised[A]: AuthStubs = {
    stubFor(
      post(urlEqualTo("/auth/authorise"))
        .atPriority(1)
        .withRequestBody(
          equalToJson(
            s"""
               |{
               |  "authorise": [
               |    { "authProviders": ["GovernmentGateway"] }
               |  ]
               |}
           """.stripMargin,
            true,
            true
          )
        )
        .willReturn(
          aResponse()
            .withStatus(200)
            .withBody(s"""{}""".stripMargin)
        )
    )

    stubFor(
      post(urlEqualTo("/auth/authorise"))
        .atPriority(2)
        .willReturn(
          aResponse()
            .withStatus(401)
            .withHeader("WWW-Authenticate", "MDTP detail=\\"InsufficientEnrolments\\"")
        )
    )
    this
  }

  def givenDummySubscriptionUrl: AuthStubs = {
    stubFor(
      get(urlEqualTo("/dummy-subscription-url")).willReturn(
        aResponse().withStatus(200)
      )
    )
    this
  }

  def verifyAuthoriseAttempt(): Unit =
    verify(1, postRequestedFor(urlEqualTo("/auth/authorise")))

  def verifySubscriptionAttempt(): Unit =
    verify(1, getRequestedFor(urlEqualTo("/dummy-subscription-url")))

}
