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

import $package$.connectors._
import $package$.journeys.$servicePrefixCamel$JourneyModel.State._
import $package$.journeys.$servicePrefixCamel$JourneyModel.Transitions._
import $package$.journeys.$servicePrefixCamel$JourneyModel.{start => _, _}
import $package$.models._
import $package$.services.$servicePrefixCamel$JourneyService
import $package$.support.{InMemoryStore, StateMatchers, UnitSpec}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.reflect.ClassTag
import scala.util.Try
import java.time.LocalDate

class $servicePrefixCamel$JourneyModelSpec extends UnitSpec with StateMatchers[State] with TestData {

  import scala.concurrent.duration._
  override implicit val defaultTimeout: FiniteDuration = 60 seconds
  // dummy journey context
  case class DummyContext()
  implicit val dummyContext: DummyContext = DummyContext()

  "$servicePrefixCamel$JourneyModel" when {
    "at state Start" should {
      "stay at Start when start" in {
        given(Start) when start(eoriNumber) should thenGo(Start)
      }

      "fail if any other transition requested" in {
        given(Start) shouldFailWhen submittedDeclarationDetails(eoriNumber)(
          exportDeclarationDetails
        )
      }
    }

    "at state EnterDeclarationDetails" should {
      "go to AnswerExampleQuestionsRequestType when submitted declaration details for export" in {
        given(EnterDeclarationDetails(None)) when submittedDeclarationDetails(eoriNumber)(
          exportDeclarationDetails
        ) should thenGo(
          AnswerExampleQuestionsRequestType(ExampleQuestionsStateModel(exportDeclarationDetails, ExampleQuestions()))
        )
      }

      "go to ExampleQuestionsSummary when submitted declaration details for export and answers are complete" in {
        given(
          EnterDeclarationDetails(
            declarationDetailsOpt = None,
            exportQuestionsAnswersOpt = Some(completeExampleQuestionsAnswers)
          )
        ) when submittedDeclarationDetails(eoriNumber)(
          exportDeclarationDetails
        ) should thenGo(
          ExampleQuestionsSummary(
            ExampleQuestionsStateModel(
              exportDeclarationDetails,
              completeExampleQuestionsAnswers
            )
          )
        )
      }
    }

    "at state AnswerExampleQuestionsRequestType" should {
      for (requestType <- ExampleRequestType.values) {
        s"go to AnswerExampleQuestionsRouteType when submitted request type \${ExampleRequestType.keyOf(requestType).get}" in {
          given(
            AnswerExampleQuestionsRequestType(ExampleQuestionsStateModel(exportDeclarationDetails, ExampleQuestions()))
          ) when submittedExampleQuestionsAnswerRequestType(eoriNumber)(
            requestType
          ) should thenGo(
            AnswerExampleQuestionsRouteType(
              ExampleQuestionsStateModel(exportDeclarationDetails, ExampleQuestions(requestType = Some(requestType)))
            )
          )
        }

        s"go to ExampleQuestionsSummary when submitted request type \${ExampleRequestType.keyOf(requestType).get} and answers are complete" in {
          given(
            AnswerExampleQuestionsRequestType(
              ExampleQuestionsStateModel(exportDeclarationDetails, completeExampleQuestionsAnswers)
            )
          ) when submittedExampleQuestionsAnswerRequestType(eoriNumber)(
            requestType
          ) should thenGo(
            ExampleQuestionsSummary(
              ExampleQuestionsStateModel(
                exportDeclarationDetails,
                completeExampleQuestionsAnswers.copy(requestType = Some(requestType))
              )
            )
          )
        }
      }

    }

    "at state AnswerExampleQuestionsRouteType" should {
      for (routeType <- ExampleRouteType.values) {
        s"go to AnswerExampleQuestionsHasPriorityGoods when submitted route \${ExampleRouteType.keyOf(routeType).get}" in {
          given(
            AnswerExampleQuestionsRouteType(
              ExampleQuestionsStateModel(
                exportDeclarationDetails,
                ExampleQuestions(requestType = Some(ExampleRequestType.New))
              )
            )
          ) when submittedExampleQuestionsAnswerRouteType(eoriNumber)(
            routeType
          ) should thenGo(
            ExampleQuestionsSummary(
              ExampleQuestionsStateModel(
                exportDeclarationDetails,
                ExampleQuestions(requestType = Some(ExampleRequestType.New), routeType = Some(routeType))
              )
            )
          )
        }

        s"go to ExampleQuestionsSummary when submitted route \${ExampleRouteType.keyOf(routeType).get} and answers are complete" in {
          given(
            AnswerExampleQuestionsRouteType(
              ExampleQuestionsStateModel(exportDeclarationDetails, completeExampleQuestionsAnswers)
            )
          ) when submittedExampleQuestionsAnswerRouteType(eoriNumber)(
            routeType
          ) should thenGo(
            ExampleQuestionsSummary(
              ExampleQuestionsStateModel(
                exportDeclarationDetails,
                completeExampleQuestionsAnswers.copy(routeType = Some(routeType))
              )
            )
          )
        }
      }

    }

    "at state $servicePrefixCamel$Confirmation" should {
      "go to Start when start" in {
        given(
          $servicePrefixCamel$Confirmation(
            exportDeclarationDetails,
            completeExampleQuestionsAnswers,
            $servicePrefixCamel$Result("A1234567890", generatedAt)
          )
        ) when start(eoriNumber) should thenGo(Start)
      }

      "go to clean EnterDeclarationDetails when going back" in {
        given(
          $servicePrefixCamel$Confirmation(
            exportDeclarationDetails,
            completeExampleQuestionsAnswers,
            $servicePrefixCamel$Result("A1234567890", generatedAt)
          )
        ) when backToEnterDeclarationDetails(eoriNumber) should thenGo(EnterDeclarationDetails())
      }
    }

    "at state CaseAlreadyExists" should {
      "go to Start when start" in {
        given(
          CaseAlreadyExists("A1234567890")
        ) when start(eoriNumber) should thenGo(Start)
      }

      "go to clean EnterDeclarationDetails when going back" in {
        given(
          CaseAlreadyExists("A1234567890")
        ) when backToEnterDeclarationDetails(eoriNumber) should thenGo(EnterDeclarationDetails())
      }
    }
  }

  case class given[S <: State: ClassTag](initialState: S)
      extends $servicePrefixCamel$JourneyService[DummyContext] with InMemoryStore[(State, List[State]), DummyContext] {

    await(save((initialState, Nil)))

    def withBreadcrumbs(breadcrumbs: State*): this.type = {
      val (state, _) = await(fetch).getOrElse((Start, Nil))
      await(save((state, breadcrumbs.toList)))
      this
    }

    def when(transition: Transition): (State, List[State]) =
      await(super.apply(transition))

    def shouldFailWhen(transition: Transition) =
      Try(await(super.apply(transition))).isSuccess shouldBe false

    def when(merger: Merger[S], state: State): (State, List[State]) =
      await(super.modify { s: S => merger.apply((s, state)) })
  }
}

trait TestData {

  val eoriNumber = "foo"
  val correlationId = "123"
  val generatedAt = java.time.LocalDateTime.of(2018, 12, 11, 10, 20, 0)

  val exportDeclarationDetails = DeclarationDetails(EPU(123), EntryNumber("Z00000Z"), LocalDate.parse("2020-09-23"))
  val invalidDeclarationDetails = DeclarationDetails(EPU(123), EntryNumber("0000000"), LocalDate.parse("2020-09-23"))

  val completeExampleQuestionsAnswers = ExampleQuestions(
    requestType = Some(ExampleRequestType.New),
    routeType = Some(ExampleRouteType.Route3)
  )

}
