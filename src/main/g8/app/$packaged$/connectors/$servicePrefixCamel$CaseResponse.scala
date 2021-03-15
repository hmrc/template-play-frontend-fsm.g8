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

import play.api.libs.json.{Format, Json}

import java.time.LocalDateTime
import scala.util.Success
import scala.util.Failure
import scala.util.Try

case class $servicePrefixCamel$Result(caseId: String, generatedAt: LocalDateTime)

object $servicePrefixCamel$Result {
  implicit val formats: Format[$servicePrefixCamel$Result] =
    Json.format[$servicePrefixCamel$Result]
}
case class $servicePrefixCamel$CaseResponse(
  // Identifier associated with a request,
  correlationId: String,
  // Represents an error occurred
  error: Option[ApiError] = None,
  // Represents the result
  result: Option[$servicePrefixCamel$Result] = None
)

object $servicePrefixCamel$CaseResponse {
  implicit val formats: Format[$servicePrefixCamel$CaseResponse] =
    Json.format[$servicePrefixCamel$CaseResponse]

  final def shouldRetry(response: Try[$servicePrefixCamel$CaseResponse]): Boolean =
    response match {
      case Success(result) if result.error.map(_.errorCode).exists(_.matches("5\\\\d\\\\d")) => true
      case Failure($servicePrefixCamel$ApiError(e))                                            => true
      case _                                                                             => false
    }

  final def errorMessage(response: $servicePrefixCamel$CaseResponse): String =
    s"\${response.error.map(_.errorCode).getOrElse("")} \${response.error.flatMap(_.errorMessage).getOrElse("")}"
}
