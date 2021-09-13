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

import $package$.models._
import $package$.support.JsonFormatTest
import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers

class ExampleEnumFormatSpec extends AnyWordSpec with Matchers {

  "ExampleEnumFormats" should {

    "serialize and deserialize ExampleEnum" in new JsonFormatTest[ExampleEnum](info) {
      ExampleEnum.values.size shouldBe 6
      validateJsonFormat("A", ExampleEnum.A)
      validateJsonFormat("B", ExampleEnum.B)
      validateJsonFormat("C", ExampleEnum.C)
      validateJsonFormat("D1", ExampleEnum.D1)
      validateJsonFormat("D2", ExampleEnum.D2)
      validateJsonFormat("D3", ExampleEnum.D3)
    }
  }
}
