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

package $package$.models

sealed trait ExampleRouteType

object ExampleRouteType extends EnumerationFormats[ExampleRouteType] {

  case object Route1 extends ExampleRouteType
  case object Route1Cap extends ExampleRouteType
  case object Route2 extends ExampleRouteType
  case object Route3 extends ExampleRouteType
  case object Route6 extends ExampleRouteType
  case object Hold extends ExampleRouteType

  val values = Set(Route1, Route1Cap, Route2, Route3, Route6, Hold)
}
