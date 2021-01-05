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

sealed trait ExampleRequestType

object ExampleRequestType extends EnumerationFormats[ExampleRequestType] {

  case object New extends ExampleRequestType
  case object Cancellation extends ExampleRequestType
  case object WithdrawalOrReturn extends ExampleRequestType
  case object C1601 extends ExampleRequestType
  case object C1602 extends ExampleRequestType
  case object C1603 extends ExampleRequestType

  val values = Set(New, Cancellation, WithdrawalOrReturn, C1601, C1602, C1603)
}
