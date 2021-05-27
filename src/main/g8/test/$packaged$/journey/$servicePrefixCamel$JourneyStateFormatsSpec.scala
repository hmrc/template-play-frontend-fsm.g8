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

import java.time.LocalDate
import play.api.libs.json.{Format, JsResultException, Json}
import $package$.journeys.$servicePrefixCamel$JourneyModel.State
import $package$.journeys.$servicePrefixCamel$JourneyStateFormats
import $package$.models._
import $package$.support.UnitSpec
import $package$.support.JsonFormatTest

class $servicePrefixCamel$JourneyStateFormatsSpec extends UnitSpec {

  implicit val formats: Format[State] = $servicePrefixCamel$JourneyStateFormats.formats
  val generatedAt = java.time.LocalDateTime.of(2018, 12, 11, 10, 20, 30)

  "$servicePrefixCamel$JourneyStateFormats" should {
    "serialize and deserialize state" in new JsonFormatTest[State](info) {
      validateJsonFormat("""{"state":"Start"}""", State.Start)
    }

    "throw an exception when unknown state" in {
      val json = Json.parse("""{"state":"StrangeState","properties":{}}""")
      an[JsResultException] shouldBe thrownBy {
        json.as[State]
      }
    }

  }
}
