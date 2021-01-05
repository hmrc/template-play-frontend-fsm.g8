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

package $package$.controllers

import $package$.support.UnitSpec
import $package$.models._
import $package$.support.FormValidator

class ExampleQuestionsFormSpec extends UnitSpec with FormValidator {

  "Example questions forms" should {

    "bind some requestType and return ExampleRequestType and fill it back" in {
      val form = $servicePrefixCamel$JourneyController.ExampleRequestTypeForm
      validate(form, Map("requestType" -> "New"), ExampleRequestType.New)
      validate(form, Map("requestType" -> "Cancellation"), ExampleRequestType.Cancellation)
      validate(form, Map("requestType" -> "C1601"), ExampleRequestType.C1601)
      validate(form, Map("requestType" -> "C1602"), ExampleRequestType.C1602)
      validate(form, Map("requestType" -> "C1603"), ExampleRequestType.C1603)
      validate(form, Map("requestType" -> "WithdrawalOrReturn"), ExampleRequestType.WithdrawalOrReturn)
      validate(form, "requestType", Map(), Seq("error.exportRequestType.required"))
      validate(form, "requestType", Map("requestType" -> "Foo"), Seq("error.exportRequestType.invalid-option"))
    }

    "bind some routeType and return ExampleRouteType and fill it back" in {
      val form = $servicePrefixCamel$JourneyController.ExampleRouteTypeForm
      validate(form, Map("routeType" -> "Route1"), ExampleRouteType.Route1)
      validate(form, Map("routeType" -> "Route1Cap"), ExampleRouteType.Route1Cap)
      validate(form, Map("routeType" -> "Route2"), ExampleRouteType.Route2)
      validate(form, Map("routeType" -> "Route3"), ExampleRouteType.Route3)
      validate(form, Map("routeType" -> "Route6"), ExampleRouteType.Route6)
      validate(form, Map("routeType" -> "Hold"), ExampleRouteType.Hold)
      validate(form, "routeType", Map(), Seq("error.exportRouteType.required"))
      validate(form, "routeType", Map("routeType" -> "Foo"), Seq("error.exportRouteType.invalid-option"))
    }
  }
}
