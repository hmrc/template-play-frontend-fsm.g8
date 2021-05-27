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

import $package$.journeys.$servicePrefixCamel$JourneyModel.State._
import $package$.journeys.$servicePrefixCamel$JourneyModel.Transitions._
import $package$.journeys.$servicePrefixCamel$JourneyModel.{start => _, _}
import $package$.services.$servicePrefixCamel$JourneyService
import $package$.support.InMemoryStore
import $package$.support.StateMatchers
import $package$.support.UnitSpec

import scala.concurrent.ExecutionContext.Implicits.global
import scala.reflect.ClassTag
import scala.util.Try

class $servicePrefixCamel$JourneyModelSpec extends UnitSpec with StateMatchers[State] {

  import scala.concurrent.duration._
  override implicit val defaultTimeout: FiniteDuration = 60 seconds
  // dummy journey context
  case class DummyContext()
  implicit val dummyContext: DummyContext = DummyContext()

  "$servicePrefixCamel$JourneyModel" when {
    "at state Start" should {
      "stay at Start when start" in {
        given(Start) when start should thenGo(Start)
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
