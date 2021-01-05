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

package $package$.views

import javax.inject.Singleton
import play.api.data.Form
import play.api.i18n.Messages
import uk.gov.hmrc.govukfrontend.views.viewmodels.radios.RadioItem
import $package$.models._
import $package$.controllers.routes.$servicePrefixCamel$JourneyController
import uk.gov.hmrc.govukfrontend.views.viewmodels.summarylist.SummaryList

@Singleton
class ExampleQuestionsViewContext
    extends RadioItemsHelper with CheckboxItemsHelper with SummaryListRowHelper with DateTimeFormatHelper
    with DeclarationDetailsHelper {

  def exportRequestTypeItems(form: Form[_])(implicit messages: Messages): Seq[RadioItem] =
    radioItems[ExampleRequestType](
      "export-questions",
      "requestType",
      Seq(
        ExampleRequestType.New,
        ExampleRequestType.Cancellation,
        ExampleRequestType.WithdrawalOrReturn,
        ExampleRequestType.C1601,
        ExampleRequestType.C1602,
        ExampleRequestType.C1603
      ),
      form
    )

  def exportRouteTypeItems(form: Form[_])(implicit messages: Messages): Seq[RadioItem] =
    radioItems[ExampleRouteType](
      "export-questions",
      "routeType",
      Seq(
        ExampleRouteType.Route1,
        ExampleRouteType.Route1Cap,
        ExampleRouteType.Route2,
        ExampleRouteType.Route3,
        ExampleRouteType.Route6,
        ExampleRouteType.Hold
      ),
      form
    )

  def summaryListOfExampleQuestions(exportQuestions: ExampleQuestions)(implicit messages: Messages): SummaryList = {

    val requestTypeRows = Seq(
      summaryListRow(
        label = "summary.export-questions.requestType",
        value = exportQuestions.requestType
          .flatMap(ExampleRequestType.keyOf)
          .map(key => messages(s"form.export-questions.requestType.\$key"))
          .getOrElse("-"),
        visuallyHiddenText = Some("summary.export-questions.requestType"),
        action = ($servicePrefixCamel$JourneyController.showAnswerExampleQuestionsRequestType, "site.change")
      )
    )

    val routeTypeRows =
      Seq(
        summaryListRow(
          label = "summary.export-questions.routeType",
          value = exportQuestions.routeType
            .flatMap(ExampleRouteType.keyOf)
            .map(key => messages(s"form.export-questions.routeType.\$key"))
            .getOrElse("-"),
          visuallyHiddenText = Some("summary.export-questions.routeType"),
          action = ($servicePrefixCamel$JourneyController.showAnswerExampleQuestionsRouteType, "site.change")
        )
      )

    SummaryList(
      requestTypeRows ++ routeTypeRows
    )
  }

}
