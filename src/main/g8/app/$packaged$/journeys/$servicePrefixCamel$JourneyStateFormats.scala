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

package $package$.journeys

import play.api.libs.json._
import $package$.journeys.$servicePrefixCamel$JourneyModel.State
import $package$.journeys.$servicePrefixCamel$JourneyModel.State._
import uk.gov.hmrc.play.fsm.JsonStateFormats

object $servicePrefixCamel$JourneyStateFormats extends JsonStateFormats[State] {

  val enterDeclarationDetailsFormat = Json.format[EnterDeclarationDetails]
  val answerExampleQuestionsRequestTypeFormat = Json.format[AnswerExampleQuestionsRequestType]
  val answerExampleQuestionsRouteTypeFormat = Json.format[AnswerExampleQuestionsRouteType]
  val exportQuestionsSummaryFormat = Json.format[ExampleQuestionsSummary]
  val createCaseConfirmationFormat = Json.format[$servicePrefixCamel$Confirmation]
  val caseAlreadyExistsFormat = Json.format[CaseAlreadyExists]

  override val serializeStateProperties: PartialFunction[State, JsValue] = {
    case s: EnterDeclarationDetails           => enterDeclarationDetailsFormat.writes(s)
    case s: AnswerExampleQuestionsRequestType => answerExampleQuestionsRequestTypeFormat.writes(s)
    case s: AnswerExampleQuestionsRouteType   => answerExampleQuestionsRouteTypeFormat.writes(s)
    case s: ExampleQuestionsSummary           => exportQuestionsSummaryFormat.writes(s)
    case s: $servicePrefixCamel$Confirmation        => createCaseConfirmationFormat.writes(s)
    case s: CaseAlreadyExists                 => caseAlreadyExistsFormat.writes(s)
  }

  override def deserializeState(stateName: String, properties: JsValue): JsResult[State] =
    stateName match {
      case "Start"                             => JsSuccess(Start)
      case "EnterDeclarationDetails"           => enterDeclarationDetailsFormat.reads(properties)
      case "AnswerExampleQuestionsRequestType" => answerExampleQuestionsRequestTypeFormat.reads(properties)
      case "AnswerExampleQuestionsRouteType"   => answerExampleQuestionsRouteTypeFormat.reads(properties)
      case "ExampleQuestionsSummary"           => exportQuestionsSummaryFormat.reads(properties)
      case "$servicePrefixCamel$Confirmation"        => createCaseConfirmationFormat.reads(properties)
      case "CaseAlreadyExists"                 => caseAlreadyExistsFormat.reads(properties)
      case "WorkInProgressDeadEnd"             => JsSuccess(WorkInProgressDeadEnd)
      case _                                   => JsError(s"Unknown state name \$stateName")
    }
}
