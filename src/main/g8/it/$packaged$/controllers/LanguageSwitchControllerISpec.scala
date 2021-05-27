package $package$.controllers

import play.api.Application
import $package$.support.ServerISpec

class LanguageSwitchControllerISpec extends LanguageSwitchControllerISpecSetup {

  "LanguageSwitchController" when {

    "GET /language/cy" should {
      "show change language to cymraeg" in {
        givenAuthorisedForEnrolment(Enrolment("HMRC-XYZ", "$authorisedIdentifierKey$", "foo"))
        val result = await(request("/language/cymraeg").get())
        result.status shouldBe 200
        result.body should include("Change the language to English")
      }
    }

    "GET /language/engligh" should {
      "show change language to english" in {
        givenAuthorisedForEnrolment(Enrolment("HMRC-XYZ", "$authorisedIdentifierKey$", "foo"))
        val result = await(request("/language/englisg").get())
        result.status shouldBe 200
        result.body should include("Newid yr iaith ir Gymraeg")
      }
    }

    "GET /language/xxx" should {
      "show change language to default English if unknown" in {
        givenAuthorisedForEnrolment(Enrolment("HMRC-XYZ", "$authorisedIdentifierKey$", "foo"))
        val result = await(request("/language/xxx").get())
        result.status shouldBe 200
        result.body should include("Newid yr iaith ir Gymraeg")
      }
    }
  }
}

class LanguageSwitchControllerISpecSetup extends ServerISpec {

  override def fakeApplication: Application = appBuilder.build()

  final def request(path: String) =
    wsClient
      .url(s"\$baseUrl\$path")

}
