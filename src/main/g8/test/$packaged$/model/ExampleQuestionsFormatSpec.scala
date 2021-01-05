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

package $package$.model

import $package$.support.UnitSpec
import $package$.models._
import $package$.support.JsonFormatTest

class ExampleQuestionsFormatSpec extends UnitSpec {

  "ExampleQuestionsFormats" should {

    "serialize and deserialize ExampleRequestType" in new JsonFormatTest[ExampleRequestType](info) {

      ExampleRequestType.values.size shouldBe 6

      validateJsonFormat("New", ExampleRequestType.New)
      validateJsonFormat("Cancellation", ExampleRequestType.Cancellation)
      validateJsonFormat("C1601", ExampleRequestType.C1601)
      validateJsonFormat("C1602", ExampleRequestType.C1602)
      validateJsonFormat("C1603", ExampleRequestType.C1603)
      validateJsonFormat("WithdrawalOrReturn", ExampleRequestType.WithdrawalOrReturn)
    }

    "serialize and deserialize ExampleRouteType" in new JsonFormatTest[ExampleRouteType](info) {

      ExampleRouteType.values.size shouldBe 6

      validateJsonFormat("Route1", ExampleRouteType.Route1)
      validateJsonFormat("Route1Cap", ExampleRouteType.Route1Cap)
      validateJsonFormat("Route2", ExampleRouteType.Route2)
      validateJsonFormat("Route3", ExampleRouteType.Route3)
      validateJsonFormat("Route6", ExampleRouteType.Route6)
      validateJsonFormat("Hold", ExampleRouteType.Hold)
    }
  }
}
