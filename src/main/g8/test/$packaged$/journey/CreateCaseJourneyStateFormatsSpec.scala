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

package $package$.journey

import java.time.LocalDate
import play.api.libs.json.{Format, JsResultException, Json}
import $package$.connectors.$servicePrefixCamel$Result
import $package$.journeys.$servicePrefixCamel$JourneyModel.State
import $package$.journeys.$servicePrefixCamel$JourneyStateFormats
import $package$.models._
import $package$.support.UnitSpec
import $package$.support.JsonFormatTest

class $servicePrefixCamel$JourneyStateFormatsSpec extends UnitSpec {

  implicit val formats: Format[State] = $servicePrefixCamel$JourneyStateFormats.formats
  val generatedAt = java.time.LocalDateTime.of(2018, 12, 11, 10, 20, 30)

  "$servicePrefixCamel$JourneyStateFormats" should {
    "serialize and deserialize state" in new JsonFormatTest[State](info) {
      validateJsonFormat("""{"state":"Start"}""", State.Start)
      validateJsonFormat(
        """{"state":"EnterDeclarationDetails","properties":{"declarationDetailsOpt":{"epu":"123","entryNumber":"100000Z","entryDate":"2000-01-01"}}}""",
        State.EnterDeclarationDetails(
          Some(DeclarationDetails(EPU(123), EntryNumber("100000Z"), LocalDate.parse("2000-01-01")))
        )
      )
      validateJsonFormat(
        """{"state":"AnswerExampleQuestionsRequestType","properties":{"model":{"declarationDetails":{"epu":"123","entryNumber":"Z00000Z","entryDate":"2020-10-05"},"exportQuestionsAnswers":{}}}}""",
        State.AnswerExampleQuestionsRequestType(
          ExampleQuestionsStateModel(
            DeclarationDetails(EPU(123), EntryNumber("Z00000Z"), LocalDate.parse("2020-10-05")),
            ExampleQuestions()
          )
        )
      )
      validateJsonFormat(
        """{"state":"AnswerExampleQuestionsRouteType","properties":{"model":{"declarationDetails":{"epu":"123","entryNumber":"Z00000Z","entryDate":"2020-10-05"},"exportQuestionsAnswers":{"requestType":"New"}}}}""",
        State.AnswerExampleQuestionsRouteType(
          ExampleQuestionsStateModel(
            DeclarationDetails(EPU(123), EntryNumber("Z00000Z"), LocalDate.parse("2020-10-05")),
            ExampleQuestions(requestType = Some(ExampleRequestType.New))
          )
        )
      )
      validateJsonFormat(
        """{"state":"ExampleQuestionsSummary","properties":{"model":{
          |"declarationDetails":{"epu":"123","entryNumber":"Z00000Z","entryDate":"2020-10-05"},
          |"exportQuestionsAnswers":{"requestType":"C1601","routeType":"Route2"}}}}""".stripMargin,
        State.ExampleQuestionsSummary(
          ExampleQuestionsStateModel(
            DeclarationDetails(EPU(123), EntryNumber("Z00000Z"), LocalDate.parse("2020-10-05")),
            ExampleQuestions(
              requestType = Some(ExampleRequestType.C1601),
              routeType = Some(ExampleRouteType.Route2)
            )
          )
        )
      )

      validateJsonFormat(
        s"""{"state":"$servicePrefixCamel$Confirmation","properties":{"declarationDetails":{"epu":"123","entryNumber":"000000Z","entryDate":"2020-10-05"},
           |"questionsAnswers":{"export":{"requestType":"New","routeType":"Route2"}},
           |"result":{"caseId":"7w7e7wq87ABDFD78wq7e87","generatedAt":"\${generatedAt.toString}"}}}""".stripMargin,
        State.$servicePrefixCamel$Confirmation(
          DeclarationDetails(EPU(123), EntryNumber("000000Z"), LocalDate.parse("2020-10-05")),
          exportQuestions,
          $servicePrefixCamel$Result("7w7e7wq87ABDFD78wq7e87", generatedAt)
        )
      )
      validateJsonFormat(
        """{"state":"CaseAlreadyExists","properties":{"caseReferenceId":"7w7e7wq87ABDFD78wq7e87"}}""".stripMargin,
        State.CaseAlreadyExists("7w7e7wq87ABDFD78wq7e87")
      )
    }

    "throw an exception when unknown state" in {
      val json = Json.parse("""{"state":"StrangeState","properties":{}}""")
      an[JsResultException] shouldBe thrownBy {
        json.as[State]
      }
    }

  }

  val exportQuestions = ExampleQuestions(
    requestType = Some(ExampleRequestType.New),
    routeType = Some(ExampleRouteType.Route2)
  )
}
