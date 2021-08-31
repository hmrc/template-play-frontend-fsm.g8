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

import $package$.journeys.$servicePrefix$JourneyModel
import $package$.support._

class $servicePrefix$JourneyModelSpec extends UnitSpec with JourneyModelSpec {

  override val model = $servicePrefix$JourneyModel

  import model.{State, Transitions}

  "$servicePrefix$JourneyModel" when {
    "at state Start" should {
      "stay at Start when start" in {
        given(State.Start) when Transitions.start should thenGo(State.Start)
      }
    }
  }
}
