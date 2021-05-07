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

import java.time.LocalDate

import $package$.support.UnitSpec
import $package$.controllers.FormFieldMappings._
import $package$.models._
import $package$.support.FormMappingMatchers
import java.time.LocalTime

class FormFieldMappingsSpec extends UnitSpec with FormMappingMatchers {

  val (y, m, d) = {
    val now = LocalDate.now
    (now.getYear(), now.getMonthValue(), now.getDayOfMonth())
  }

  "FormFieldMappings" should {
    "validate mandatory dateOfArrival" in {
      mandatoryDateOfArrivalMapping
        .bind(Map("year" -> s"\$y", "month" -> "12", "day" -> "31")) shouldBe Right(Some(LocalDate.parse(s"\$y-12-31")))
      mandatoryDateOfArrivalMapping
        .bind(Map("year" -> s"\$y", "month" -> "01", "day" -> "01")) shouldBe Right(Some(LocalDate.parse(s"\$y-01-01")))
      mandatoryDateOfArrivalMapping.bind(Map()) should haveOnlyError[Option[LocalDate]](
        "error.dateOfArrival.all.required"
      )
      mandatoryDateOfArrivalMapping
        .bind(Map("year" -> "", "month" -> "", "day" -> "")) should haveOnlyError[Option[LocalDate]](
        "error.dateOfArrival.all.required"
      )
      mandatoryDateOfArrivalMapping.bind(Map("year" -> "", "day" -> "")) should haveOnlyError[Option[LocalDate]](
        "error.dateOfArrival.all.required"
      )
      mandatoryDateOfArrivalMapping
        .bind(Map("year" -> "", "month" -> "12", "day" -> "31")) should haveOnlyError(
        "error.dateOfArrival.year.required"
      )
      mandatoryDateOfArrivalMapping
        .bind(Map("year" -> s"\$y", "month" -> "", "day" -> "31")) should haveOnlyError(
        "error.dateOfArrival.month.required"
      )
      mandatoryDateOfArrivalMapping
        .bind(Map("year" -> s"\$y", "month" -> "12", "day" -> "")) should haveOnlyError(
        "error.dateOfArrival.day.required"
      )
      mandatoryDateOfArrivalMapping
        .bind(Map("year" -> s"\$y", "month" -> "", "day" -> "")) should haveOnlyError(
        "error.dateOfArrival.day.required"
      )
      mandatoryDateOfArrivalMapping
        .bind(Map("year" -> "", "month" -> "12", "day" -> "")) should haveOnlyError(
        "error.dateOfArrival.day.required"
      )
      mandatoryDateOfArrivalMapping
        .bind(Map("year" -> "", "month" -> "", "day" -> "00")) should haveOnlyError(
        "error.dateOfArrival.month.required"
      )
      mandatoryDateOfArrivalMapping
        .bind(Map("year" -> "XX", "month" -> "13", "day" -> "")) should haveOnlyError(
        "error.dateOfArrival.day.required"
      )
    }

    "validate optional dateOfArrival" in {
      optionalDateOfArrivalMapping
        .bind(Map("year" -> s"\$y", "month" -> "12", "day" -> "31")) shouldBe Right(
        Some(LocalDate.parse(s"\$y-12-31"))
      )
      optionalDateOfArrivalMapping
        .bind(Map("year" -> s"\$y", "month" -> "01", "day" -> "01")) shouldBe Right(
        Some(LocalDate.parse(s"\$y-01-01"))
      )
      optionalDateOfArrivalMapping.bind(Map()) shouldBe Right(None)
      optionalDateOfArrivalMapping
        .bind(Map("year" -> "", "month" -> "", "day" -> "")) shouldBe Right(None)
      optionalDateOfArrivalMapping.bind(Map("year" -> "", "day" -> "")) shouldBe Right(None)
      optionalDateOfArrivalMapping
        .bind(Map("year" -> "", "month" -> "12", "day" -> "31")) should haveOnlyError(
        "error.dateOfArrival.year.required"
      )
      optionalDateOfArrivalMapping
        .bind(Map("year" -> s"\$y", "month" -> "", "day" -> "31")) should haveOnlyError(
        "error.dateOfArrival.month.required"
      )
      optionalDateOfArrivalMapping
        .bind(Map("year" -> s"\$y", "month" -> "12", "day" -> "")) should haveOnlyError(
        "error.dateOfArrival.day.required"
      )
      optionalDateOfArrivalMapping
        .bind(Map("year" -> s"\$y", "month" -> "", "day" -> "")) should haveOnlyError(
        "error.dateOfArrival.day.required"
      )
      optionalDateOfArrivalMapping
        .bind(Map("year" -> "", "month" -> "12", "day" -> "")) should haveOnlyError(
        "error.dateOfArrival.day.required"
      )
      optionalDateOfArrivalMapping
        .bind(Map("year" -> "", "month" -> "", "day" -> "00")) should haveOnlyError(
        "error.dateOfArrival.month.required"
      )

      optionalDateOfArrivalMapping
        .bind(Map("year" -> "XX", "month" -> "13", "day" -> "")) should haveOnlyError(
        "error.dateOfArrival.day.required"
      )
    }

    "validate mandatory timeOfArrival" in {
      mandatoryTimeOfArrivalMapping.bind(Map("hour" -> "12", "minutes" -> "00")) shouldBe Right(
        Some(LocalTime.parse("12:00"))
      )
      mandatoryTimeOfArrivalMapping.bind(Map("hour" -> "00", "minutes" -> "00")) shouldBe Right(
        Some(LocalTime.parse("00:00"))
      )
      mandatoryTimeOfArrivalMapping.bind(Map("hour" -> "", "minutes" -> " ")) should haveOnlyError(
        "error.timeOfArrival.all.required"
      )
      mandatoryTimeOfArrivalMapping.bind(Map()) should haveOnlyError(
        "error.timeOfArrival.all.required"
      )
    }

    "validate optional timeOfArrival" in {
      optionalTimeOfArrivalMapping.bind(Map("hour" -> "00", "minutes" -> "00")) shouldBe Right(
        Some(LocalTime.parse("00:00"))
      )
      optionalTimeOfArrivalMapping.bind(Map("hour" -> "12", "minutes" -> "00")) shouldBe Right(
        Some(LocalTime.parse("12:00"))
      )
      optionalTimeOfArrivalMapping.bind(Map("hour" -> "", "minutes" -> " ")) shouldBe Right(None)
      optionalTimeOfArrivalMapping.bind(Map()) shouldBe Right(None)
    }

    "validate import contactEmailMapping" in {
      importContactEmailMapping.bind(Map("" -> "")) should haveOnlyError(
        "error.contactEmail.required"
      )

      importContactEmailMapping.bind(Map("" -> "12")) should haveOnlyError(
        "error.contactEmail"
      )

      importContactEmailMapping.bind(Map("" -> "12@")) should haveOnlyError(
        "error.contactEmail"
      )

      importContactEmailMapping.bind(Map("" -> "12@c.c")) should haveOnlyError(
        "error.contactEmail"
      )

      importContactEmailMapping.bind(Map("" -> "12@c.ccccc")) should haveOnlyError(
        "error.contactEmail"
      )

      importContactEmailMapping.bind(Map("" -> "test@example.com")) shouldBe Right("test@example.com")
      importContactEmailMapping.bind(Map("" -> "12@c.cc")) shouldBe Right("12@c.cc")
    }

    "validate export contactEmailMapping" in {
      exportContactEmailMapping.bind(Map("" -> "")) should haveOnlyError(
        "error.contactEmail.required"
      )

      exportContactEmailMapping.bind(Map("" -> "12")) should haveOnlyError(
        "error.contactEmail"
      )

      exportContactEmailMapping.bind(Map("" -> "12@")) should haveOnlyError(
        "error.contactEmail"
      )
      exportContactEmailMapping.bind(Map("" -> "12@c.c")) should haveOnlyError(
        "error.contactEmail"
      )

      exportContactEmailMapping.bind(Map("" -> "12@c.ccccc")) should haveOnlyError(
        "error.contactEmail"
      )

      exportContactEmailMapping.bind(Map("" -> "test@example.com")) shouldBe Right("test@example.com")
      exportContactEmailMapping.bind(Map("" -> "12@c.cc")) shouldBe Right("12@c.cc")
    }

    "validate import contactNumberMapping" in {

      importContactNumberMapping.bind(Map("" -> "")) shouldBe Right(None)

      importContactNumberMapping.bind(Map("" -> "12@")) should haveOnlyError(
        "error.contactNumber"
      )

      importContactNumberMapping.bind(Map("" -> "12")) should haveOnlyError(
        "error.contactNumber"
      )

      importContactNumberMapping.bind(Map("" -> "0706897650")) should haveOnlyError(
        "error.contactNumber"
      )

      importContactNumberMapping.bind(Map("" -> "123456789876")) should haveOnlyError(
        "error.contactNumber"
      )

      importContactNumberMapping.bind(Map("" -> "01234567891a!")) should haveOnlyError(
        "error.contactNumber"
      )

      importContactNumberMapping.bind(Map("" -> "+441132432111")) shouldBe Right(Some("01132432111"))
      importContactNumberMapping.bind(Map("" -> "441132432111")) shouldBe Right(Some("01132432111"))
      importContactNumberMapping.bind(Map("" -> "00441132432111")) shouldBe Right(Some("01132432111"))

      importContactNumberMapping.bind(Map("" -> "01132432111")) shouldBe Right(Some("01132432111"))
      importContactNumberMapping.bind(Map("" -> "07930487952")) shouldBe Right(Some("07930487952"))
      importContactNumberMapping.bind(Map("" -> "07132 432111")) shouldBe Right(Some("07132432111"))
      importContactNumberMapping.bind(Map("" -> "07331 543211")) shouldBe Right(Some("07331543211"))
    }

    "validate export contactNumberMapping" in {

      exportContactNumberMapping.bind(Map("" -> "")) shouldBe Right(None)

      exportContactNumberMapping.bind(Map("" -> "12@")) should haveOnlyError(
        "error.contactNumber"
      )

      exportContactNumberMapping.bind(Map("" -> "12")) should haveOnlyError(
        "error.contactNumber"
      )

      exportContactNumberMapping.bind(Map("" -> "040 689 7650")) should haveOnlyError(
        "error.contactNumber"
      )

      exportContactNumberMapping.bind(Map("" -> "0406897650")) should haveOnlyError(
        "error.contactNumber"
      )

      exportContactNumberMapping.bind(Map("" -> "12@34567891")) should haveOnlyError(
        "error.contactNumber"
      )

      exportContactNumberMapping.bind(Map("" -> "01234567891a!")) should haveOnlyError(
        "error.contactNumber"
      )

      exportContactNumberMapping.bind(Map("" -> "+441132432111")) shouldBe Right(Some("01132432111"))
      exportContactNumberMapping.bind(Map("" -> "441132432111")) shouldBe Right(Some("01132432111"))
      exportContactNumberMapping.bind(Map("" -> "00441132432111")) shouldBe Right(Some("01132432111"))

      exportContactNumberMapping.bind(Map("" -> "01132432111")) shouldBe Right(Some("01132432111"))
      exportContactNumberMapping.bind(Map("" -> "01132 432111")) shouldBe Right(Some("01132432111"))
      exportContactNumberMapping.bind(Map("" -> "07331 543211")) shouldBe Right(Some("07331543211"))
    }
  }

}
