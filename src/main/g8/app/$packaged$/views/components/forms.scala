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

package $package$.views.components

import javax.inject.{Inject, Singleton}

@Singleton
class forms @Inject() (
  val formWithCSRF: uk.gov.hmrc.govukfrontend.views.html.helpers.formWithCSRF,
  val fieldset: $package$.views.html.components.fieldset,
  val errorSummary: $package$.views.html.components.errorSummary,
  val inputText: $package$.views.html.components.inputText,
  val inputNumber: $package$.views.html.components.inputNumber,
  val inputHidden: $package$.views.html.components.inputHidden,
  val inputDate: $package$.views.html.components.inputDate,
  val inputCheckboxes: $package$.views.html.components.inputCheckboxes,
  val inputRadio: $package$.views.html.components.inputRadio,
  val inputTime: $package$.views.html.components.inputTime,
  val textarea: $package$.views.html.components.textarea
)
