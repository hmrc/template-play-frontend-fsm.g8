package $package$.controllers

import $package$.journeys.$servicePrefixCamel$JourneyModel.State._
import $package$.journeys.$servicePrefixCamel$JourneyModel.root

import scala.concurrent.ExecutionContext.Implicits.global
import $package$.journeys.$servicePrefixCamel$JourneyModel.State.EnterDeclarationDetails

class LanguageSwitchControllerISpec extends $servicePrefixCamel$JourneyISpec {

  "LanguageSwitchController" when {

    "GET /language/cy" should {
      "show change language to cymraeg" in {
        implicit val journeyId: JourneyId = JourneyId()
        journey.setState(root)
        givenAuthorisedForEnrolment(Enrolment("HMRC-XYZ", "$authorisedIdentifierKey$", "foo"))
        val result = await(request("/language/cymraeg").get())
        result.status shouldBe 200
        journey.getState shouldBe EnterDeclarationDetails()
        result.body should include("Change the language to English")
      }
    }

    "GET /language/engligh" should {
      "show change language to english" in {
        implicit val journeyId: JourneyId = JourneyId()
        journey.setState(root)
        givenAuthorisedForEnrolment(Enrolment("HMRC-XYZ", "$authorisedIdentifierKey$", "foo"))
        val result = await(request("/language/englisg").get())
        result.status shouldBe 200
        journey.getState shouldBe EnterDeclarationDetails()
        result.body should include("Newid yr iaith ir Gymraeg")
      }
    }

    "GET /language/xxx" should {
      "show change language to default English if unknown" in {
        implicit val journeyId: JourneyId = JourneyId()
        journey.setState(root)
        givenAuthorisedForEnrolment(Enrolment("HMRC-XYZ", "$authorisedIdentifierKey$", "foo"))
        val result = await(request("/language/xxx").get())
        result.status shouldBe 200
        journey.getState shouldBe EnterDeclarationDetails()
        result.body should include("Newid yr iaith ir Gymraeg")
      }
    }
  }
}
