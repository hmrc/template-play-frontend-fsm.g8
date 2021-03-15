package $package$.support

import $package$.wiring.AppConfig
import scala.concurrent.duration.Duration
import scala.concurrent.duration._

case class TestAppConfig(
  wireMockBaseUrl: String,
  wireMockPort: Int
) extends AppConfig {

  override val appName: String = "$servicePrefixHyphen$-route-one"

  override val baseInternalCallbackUrl: String = wireMockBaseUrl
  override val baseExternalCallbackUrl: String = wireMockBaseUrl

  override val authBaseUrl: String = wireMockBaseUrl
  override val $servicePrefixcamel$ApiBaseUrl: String = wireMockBaseUrl
  override val upscanInitiateBaseUrl: String = wireMockBaseUrl

  override val createCaseApiPath: String = "/create-case"
  override val updateCaseApiPath: String = "/update-case"

  override val mongoSessionExpiration: Duration = 1.hour

  override val gtmContainerId: Option[String] = None
  override val contactHost: String = wireMockBaseUrl
  override val contactFormServiceIdentifier: String = "dummy"

  override val exitSurveyUrl: String = wireMockBaseUrl + "/dummy-survey-url"
  override val signOutUrl: String = wireMockBaseUrl + "/dummy-sign-out-url"
  override val researchBannerUrl: String = wireMockBaseUrl + "dummy-research-banner-url"
  override val subscriptionJourneyUrl: String = wireMockBaseUrl + "/dummy-subscription-url"

  override val authorisedServiceName: String = "HMRC-XYZ"
  override val authorisedIdentifierKey: String = "$authorisedIdentifierKey$"

  val fileFormats: AppConfig.FileFormats = AppConfig.FileFormats(10, "", "")

  override val timeout: Int = 10
  override val countdown: Int = 2
}
