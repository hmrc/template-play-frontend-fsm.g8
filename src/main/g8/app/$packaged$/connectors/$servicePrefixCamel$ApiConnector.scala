/*
 * Copyright 2021 HM Revenue & Customs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package $package$.connectors

import java.net.URL

import com.codahale.metrics.MetricRegistry
import com.kenshoo.play.metrics.Metrics
import javax.inject.{Inject, Singleton}
import $package$.wiring.AppConfig
import uk.gov.hmrc.http._
import scala.concurrent.duration._

import scala.concurrent.{ExecutionContext, Future}
import akka.actor.ActorSystem

/**
  * Connects to the backend $servicePrefixHyphen$-route-one service API.
  */
@Singleton
class $servicePrefixCamel$ApiConnector @Inject() (
  appConfig: AppConfig,
  http: HttpGet with HttpPost,
  metrics: Metrics,
  val actorSystem: ActorSystem
) extends ReadSuccessOrFailure[$servicePrefixCamel$CaseResponse] with HttpAPIMonitor with Retries {

  val baseUrl: String = appConfig.$servicePrefixcamel$ApiBaseUrl
  val createCaseApiPath = appConfig.createCaseApiPath
  val updateCaseApiPath = appConfig.updateCaseApiPath

  override val kenshooRegistry: MetricRegistry = metrics.defaultRegistry

  def createCase(
    request: $servicePrefixCamel$$servicePrefixCamel$Request
  )(implicit hc: HeaderCarrier, ec: ExecutionContext): Future[$servicePrefixCamel$CaseResponse] =
    retry(1.second, 2.seconds)($servicePrefixCamel$CaseResponse.shouldRetry, $servicePrefixCamel$CaseResponse.errorMessage) {
      monitor(s"ConsumedAPI-$servicePrefixHyphen$-create-case-api-POST") {
        http
          .POST[$servicePrefixCamel$$servicePrefixCamel$Request, $servicePrefixCamel$CaseResponse](
            new URL(baseUrl + createCaseApiPath).toExternalForm,
            request
          )
          .recoverWith {
            case e: Throwable =>
              Future.failed($servicePrefixCamel$ApiError(e))
          }
      }
    }

}

case class $servicePrefixCamel$ApiError(e: Throwable) extends RuntimeException(e)
