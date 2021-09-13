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

import $package$.utils.EnumerationFormats

sealed trait ExampleEnum

object ExampleEnum extends EnumerationFormats[ExampleEnum] {

  case object A extends ExampleEnum
  case object B extends ExampleEnum
  case object C extends ExampleEnum
  case object D1 extends ExampleEnum
  case object D2 extends ExampleEnum
  case object D3 extends ExampleEnum

  val values = Set(A, B, C, D1, D2, D3)
}
