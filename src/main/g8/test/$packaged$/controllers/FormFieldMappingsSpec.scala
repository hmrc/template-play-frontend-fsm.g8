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

  "FormFieldMappings" should {

    "validate EPU" in {
      epuMapping.bind(Map("" -> "123")) shouldBe Right(EPU(123))
      epuMapping.bind(Map("" -> "")) should haveOnlyError[EPU]("error.epu.required")
      epuMapping.bind(Map("" -> "1")) should haveOnlyError[EPU]("error.epu.invalid-length")
      epuMapping.bind(Map("" -> "12")) should haveOnlyError[EPU]("error.epu.invalid-length")
      epuMapping.bind(Map("" -> "1224")) should haveOnlyError[EPU]("error.epu.invalid-length")
      epuMapping.bind(Map("" -> "12A")) should haveOnlyError[EPU]("error.epu.invalid-only-digits")
      epuMapping.bind(Map("" -> "AAA")) should haveOnlyError[EPU]("error.epu.invalid-only-digits")
      epuMapping.bind(Map("" -> "A12")) should haveOnlyError[EPU]("error.epu.invalid-only-digits")
      epuMapping.bind(Map("" -> "1A2")) should haveOnlyError[EPU]("error.epu.invalid-only-digits")
      epuMapping.bind(Map("" -> "701")) should haveOnlyError[EPU]("error.epu.invalid-number")
      epuMapping.bind(Map("" -> "999")) should haveOnlyError[EPU]("error.epu.invalid-number")
    }

    "validate EntryNumber" in {
      entryNumberMapping.bind(Map("" -> "000000Z")) shouldBe Right(EntryNumber("000000Z"))
      entryNumberMapping.bind(Map("" -> "A00000Z")) shouldBe Right(EntryNumber("A00000Z"))
      entryNumberMapping.bind(Map("" -> "")) should haveOnlyError[EntryNumber]("error.entryNumber.required")
      entryNumberMapping.bind(Map("" -> "00000Z")) should haveOnlyError[EntryNumber](
        "error.entryNumber.invalid-length"
      )
      entryNumberMapping.bind(Map("" -> "0000Z")) should haveOnlyError[EntryNumber]("error.entryNumber.invalid-length")
      entryNumberMapping.bind(Map("" -> "00000")) should haveOnlyError[EntryNumber](
        "error.entryNumber.invalid-length"
      )
      entryNumberMapping.bind(Map("" -> "0")) should haveOnlyError[EntryNumber](
        "error.entryNumber.invalid-length"
      )
      entryNumberMapping.bind(Map("" -> "Z")) should haveOnlyError[EntryNumber]("error.entryNumber.invalid-length")
      entryNumberMapping.bind(Map("" -> "+")) should haveOnlyError[EntryNumber](
        "error.entryNumber.invalid-only-digits-and-letters"
      )
      entryNumberMapping.bind(Map("" -> "000000Z+")) should haveOnlyError[EntryNumber](
        "error.entryNumber.invalid-only-digits-and-letters"
      )
      entryNumberMapping.bind(Map("" -> "000+000Z")) should haveOnlyError[EntryNumber](
        "error.entryNumber.invalid-only-digits-and-letters"
      )
      entryNumberMapping.bind(Map("" -> "+++++++")) should haveOnlyError[EntryNumber](
        "error.entryNumber.invalid-only-digits-and-letters"
      )
      entryNumberMapping.bind(Map("" -> "++++++Z")) should haveOnlyError[EntryNumber](
        "error.entryNumber.invalid-only-digits-and-letters"
      )
      entryNumberMapping.bind(Map("" -> "00000Z0")) should haveOnlyError[EntryNumber](
        "error.entryNumber.invalid-ends-with-letter"
      )
      entryNumberMapping.bind(Map("" -> "Z000000")) should haveOnlyError[EntryNumber](
        "error.entryNumber.invalid-ends-with-letter"
      )
      entryNumberMapping.bind(Map("" -> "0A0000Z")) should haveOnlyError[EntryNumber](
        "error.entryNumber.invalid-letter-wrong-position"
      )
      entryNumberMapping.bind(Map("" -> "0A000000Z")) should haveOnlyError[EntryNumber](
        "error.entryNumber.invalid-length"
      )
    }

    "validate EntryDate" in {
      entryDateMapping.bind(Map("year" -> "2020", "month" -> "09", "day" -> "21")) shouldBe Right(
        LocalDate.parse("2020-09-21")
      )
      entryDateMapping.bind(Map("year" -> "", "month" -> "", "day" -> "")) should haveOnlyError[LocalDate](
        "error.entryDate.all.required"
      )
      entryDateMapping.bind(Map("year" -> "2020", "month" -> "", "day" -> "")) should haveOnlyError[LocalDate](
        "error.entryDate.day.required"
      )
      entryDateMapping.bind(Map("year" -> "", "month" -> "11", "day" -> "")) should haveOnlyError[LocalDate](
        "error.entryDate.day.required"
      )
      entryDateMapping.bind(Map("year" -> "", "month" -> "", "day" -> "31")) should haveOnlyError[LocalDate](
        "error.entryDate.month.required"
      )
      entryDateMapping.bind(Map("year" -> "2020", "month" -> "12", "day" -> "")) should haveOnlyError[LocalDate](
        "error.entryDate.day.required"
      )
      entryDateMapping.bind(Map("year" -> "2020", "month" -> "13", "day" -> "")) should haveOnlyError[LocalDate](
        "error.entryDate.day.required"
      )
      entryDateMapping.bind(Map("year" -> "2020", "month" -> "13", "day" -> "32")) should haveOnlyError[LocalDate](
        "error.entryDate.all.invalid-value"
      )
      entryDateMapping.bind(Map("year" -> "20", "month" -> "13", "day" -> "32")) should haveOnlyError[LocalDate](
        "error.entryDate.all.invalid-value"
      )
      entryDateMapping.bind(Map("year" -> "2020", "month" -> "2", "day" -> "30")) should haveOnlyError[LocalDate](
        "error.entryDate.day.invalid-value"
      )
      entryDateMapping.bind(Map("year" -> "202", "month" -> "2", "day" -> "28")) should haveOnlyError[LocalDate](
        "error.entryDate.year.invalid-value"
      )
      entryDateMapping.bind(Map("year" -> "202A", "month" -> "1", "day" -> "1")) should haveOnlyError[LocalDate](
        "error.entryDate.year.invalid-value"
      )
      entryDateMapping.bind(Map("year" -> "2020", "month" -> "06", "day" -> "31")) should haveOnlyError[LocalDate](
        "error.entryDate.day.invalid-value"
      )
      entryDateMapping.bind(Map("year" -> "", "month" -> "0A", "day" -> "21")) should haveError[LocalDate](
        "error.entryDate.year.required"
      )
      entryDateMapping.bind(Map("year" -> "2020", "month" -> "0A", "day" -> "2AA")) should haveOnlyError[LocalDate](
        "error.entryDate.all.invalid-value"
      )
      entryDateMapping.bind(Map("year" -> "2021", "month" -> "0A", "day" -> "2AA")) should haveOnlyError[LocalDate](
        "error.entryDate.all.invalid-value"
      )
      entryDateMapping.bind(Map("year" -> "2050", "month" -> "01", "day" -> "01")) should haveOnlyError[LocalDate](
        "error.entryDate.all.invalid-value-future"
      )
      entryDateMapping.bind(Map("year" -> "1970", "month" -> "01", "day" -> "01")) should haveOnlyError[LocalDate](
        "error.entryDate.all.invalid-value-past"
      )
    }

    "validate export requestType" in {
      exportRequestTypeMapping.bind(Map("" -> "New")) shouldBe Right(ExampleRequestType.New)
      exportRequestTypeMapping.bind(Map("" -> "Cancellation")) shouldBe Right(ExampleRequestType.Cancellation)
      exportRequestTypeMapping.bind(Map("" -> "C1601")) shouldBe Right(ExampleRequestType.C1601)
      exportRequestTypeMapping.bind(Map("" -> "C1602")) shouldBe Right(ExampleRequestType.C1602)
      exportRequestTypeMapping.bind(Map("" -> "C1603")) shouldBe Right(ExampleRequestType.C1603)
      exportRequestTypeMapping.bind(Map("" -> "WithdrawalOrReturn")) shouldBe Right(
        ExampleRequestType.WithdrawalOrReturn
      )
      exportRequestTypeMapping.bind(Map("" -> "Foo")) should haveOnlyError[ExampleRequestType](
        "error.exportRequestType.invalid-option"
      )
      exportRequestTypeMapping.bind(Map()) should haveOnlyError[ExampleRequestType](
        "error.exportRequestType.required"
      )
    }

    "validate export routeType" in {
      exportRouteTypeMapping.bind(Map("" -> "Route1")) shouldBe Right(ExampleRouteType.Route1)
      exportRouteTypeMapping.bind(Map("" -> "Route1Cap")) shouldBe Right(ExampleRouteType.Route1Cap)
      exportRouteTypeMapping.bind(Map("" -> "Route2")) shouldBe Right(ExampleRouteType.Route2)
      exportRouteTypeMapping.bind(Map("" -> "Route3")) shouldBe Right(ExampleRouteType.Route3)
      exportRouteTypeMapping.bind(Map("" -> "Route6")) shouldBe Right(ExampleRouteType.Route6)
      exportRouteTypeMapping.bind(Map("" -> "Hold")) shouldBe Right(ExampleRouteType.Hold)
      exportRouteTypeMapping.bind(Map("" -> "Foo")) should haveOnlyError[ExampleRouteType](
        "error.exportRouteType.invalid-option"
      )
      exportRouteTypeMapping.bind(Map()) should haveOnlyError[ExampleRouteType](
        "error.exportRouteType.required"
      )
    }

    "validate export hasPriorityGoods" in {
      exportHasPriorityGoodsMapping.bind(Map("" -> "yes")) shouldBe Right(true)
      exportHasPriorityGoodsMapping.bind(Map("" -> "no")) shouldBe Right(false)
      exportHasPriorityGoodsMapping.bind(Map()) should haveOnlyError[Boolean]("error.exportHasPriorityGoods.required")
    }

    "validate import hasPriorityGoods" in {
      importHasPriorityGoodsMapping.bind(Map("" -> "yes")) shouldBe Right(true)
      importHasPriorityGoodsMapping.bind(Map("" -> "no")) shouldBe Right(false)
      importHasPriorityGoodsMapping.bind(Map()) should haveOnlyError[Boolean]("error.importHasPriorityGoods.required")
    }

    "validate import hasALVS" in {
      importHasALVSMapping.bind(Map("" -> "yes")) shouldBe Right(true)
      importHasALVSMapping.bind(Map("" -> "no")) shouldBe Right(false)
      importHasALVSMapping.bind(Map()) should haveOnlyError[Boolean]("error.importHasALVS.required")
    }

    "validate mandatory vesselName" in {
      mandatoryVesselNameMapping.bind(Map("" -> "Titanic")) shouldBe Right(Some("Titanic"))
      mandatoryVesselNameMapping.bind(Map("" -> "Brian's boat ")) shouldBe Right(Some("Brian's boat"))
      mandatoryVesselNameMapping.bind(Map("" -> " Shipley & West-Yorkshire")) shouldBe Right(
        Some("Shipley & West-Yorkshire")
      )
      mandatoryVesselNameMapping.bind(Map("" -> "DINGY  123")) shouldBe Right(Some("DINGY 123"))
      mandatoryVesselNameMapping.bind(Map("" -> "Me & You")) shouldBe Right(Some("Me & You"))
      mandatoryVesselNameMapping.bind(Map("" -> "   Titanic  ")) shouldBe Right(Some("Titanic"))
      mandatoryVesselNameMapping.bind(Map("" -> "")) should haveOnlyError[Option[String]](
        "error.vesselName.required"
      )
      mandatoryVesselNameMapping.bind(Map("" -> " ")) should haveOnlyError[Option[String]](
        "error.vesselName.required"
      )
      mandatoryVesselNameMapping.bind(Map("" -> "  ")) should haveOnlyError[Option[String]](
        "error.vesselName.required"
      )
      mandatoryVesselNameMapping.bind(Map("" -> "X" * 129)) should haveOnlyError[Option[String]](
        "error.vesselName.invalid-length"
      )
      mandatoryVesselNameMapping.bind(Map("" -> "@X" * 65)) should haveOnlyError[Option[String]](
        "error.vesselName.invalid-characters"
      )
      mandatoryVesselNameMapping.bind(Map("" -> "-+-")) should haveOnlyError[Option[String]](
        "error.vesselName.invalid-characters"
      )
      mandatoryVesselNameMapping.bind(Map("" -> "/")) should haveOnlyError[Option[String]](
        "error.vesselName.invalid-characters"
      )
      mandatoryVesselNameMapping.bind(Map("" -> "a\$\$\$\$\$\$\$")) should haveOnlyError[Option[String]](
        "error.vesselName.invalid-characters"
      )
      mandatoryVesselNameMapping.bind(Map("" -> "B@d name")) should haveOnlyError[Option[String]](
        "error.vesselName.invalid-characters"
      )
    }

    "validate optional vesselName" in {
      optionalVesselNameMapping.bind(Map("" -> "Titanic")) shouldBe Right(Some("Titanic"))
      optionalVesselNameMapping.bind(Map("" -> "Brian's boat ")) shouldBe Right(Some("Brian's boat"))
      optionalVesselNameMapping.bind(Map("" -> " Shipley & West-Yorkshire")) shouldBe Right(
        Some("Shipley & West-Yorkshire")
      )
      optionalVesselNameMapping.bind(Map("" -> "DINGY  123")) shouldBe Right(Some("DINGY 123"))
      optionalVesselNameMapping.bind(Map("" -> "Me & You")) shouldBe Right(Some("Me & You"))
      optionalVesselNameMapping.bind(Map("" -> "   Titanic  ")) shouldBe Right(Some("Titanic"))
      optionalVesselNameMapping.bind(Map("" -> "")) shouldBe Right(None)
      optionalVesselNameMapping.bind(Map("" -> " ")) shouldBe Right(None)
      optionalVesselNameMapping.bind(Map("" -> "  ")) shouldBe Right(None)
      optionalVesselNameMapping.bind(Map("" -> "                        ")) shouldBe Right(None)
      optionalVesselNameMapping.bind(Map("" -> "                        A")) shouldBe Right(Some("A"))
      optionalVesselNameMapping.bind(Map("" -> "X" * 129)) should haveOnlyError[Option[String]](
        "error.vesselName.invalid-length"
      )
      optionalVesselNameMapping.bind(Map("" -> "@X" * 65)) should haveOnlyError[Option[String]](
        "error.vesselName.invalid-characters"
      )
      optionalVesselNameMapping.bind(Map("" -> "-+-")) should haveOnlyError[Option[String]](
        "error.vesselName.invalid-characters"
      )
      optionalVesselNameMapping.bind(Map("" -> "/")) should haveOnlyError[Option[String]](
        "error.vesselName.invalid-characters"
      )
      optionalVesselNameMapping.bind(Map("" -> "a\$\$\$\$\$\$\$")) should haveOnlyError[Option[String]](
        "error.vesselName.invalid-characters"
      )
      optionalVesselNameMapping.bind(Map("" -> "B@d name")) should haveOnlyError[Option[String]](
        "error.vesselName.invalid-characters"
      )
    }

    "validate mandatory dateOfArrival" in {
      mandatoryDateOfArrivalMapping
        .bind(Map("year" -> "2020", "month" -> "12", "day" -> "31")) shouldBe Right(Some(LocalDate.parse("2020-12-31")))
      mandatoryDateOfArrivalMapping
        .bind(Map("year" -> "2020", "month" -> "01", "day" -> "01")) shouldBe Right(Some(LocalDate.parse("2020-01-01")))
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
        .bind(Map("year" -> "2020", "month" -> "", "day" -> "31")) should haveOnlyError(
        "error.dateOfArrival.month.required"
      )
      mandatoryDateOfArrivalMapping
        .bind(Map("year" -> "2020", "month" -> "12", "day" -> "")) should haveOnlyError(
        "error.dateOfArrival.day.required"
      )
      mandatoryDateOfArrivalMapping
        .bind(Map("year" -> "2020", "month" -> "", "day" -> "")) should haveOnlyError(
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
        .bind(Map("year" -> "2020", "month" -> "12", "day" -> "31")) shouldBe Right(
        Some(LocalDate.parse("2020-12-31"))
      )
      optionalDateOfArrivalMapping
        .bind(Map("year" -> "2020", "month" -> "01", "day" -> "01")) shouldBe Right(
        Some(LocalDate.parse("2020-01-01"))
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
        .bind(Map("year" -> "2020", "month" -> "", "day" -> "31")) should haveOnlyError(
        "error.dateOfArrival.month.required"
      )
      optionalDateOfArrivalMapping
        .bind(Map("year" -> "2020", "month" -> "12", "day" -> "")) should haveOnlyError(
        "error.dateOfArrival.day.required"
      )
      optionalDateOfArrivalMapping
        .bind(Map("year" -> "2020", "month" -> "", "day" -> "")) should haveOnlyError(
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

    "validate import contactNameMapping" in {
      importContactNameMapping.bind(Map("" -> "")) shouldBe Right(None)

      importContactNameMapping.bind(Map("" -> "Full Name")) shouldBe Right(Some("Full Name"))
    }

    "validate export contactNameMapping" in {
      exportContactNameMapping.bind(Map("" -> "")) shouldBe Right(None)

      exportContactNameMapping.bind(Map("" -> "Full Name")) shouldBe Right(Some("Full Name"))
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

    "validate case reference number mapping" in {
      caseReferenceNumberMapping.bind(Map("" -> "AA0000000000000000000Z")) shouldBe Right("AA0000000000000000000Z")
      caseReferenceNumberMapping.bind(Map("" -> "")) should haveOnlyError("error.caseReferenceNumber.required")
      caseReferenceNumberMapping.bind(Map("" -> "A")) should haveOnlyError("error.caseReferenceNumber.invalid-value")
      caseReferenceNumberMapping.bind(Map("" -> "AA0000000000000000000")) should haveOnlyError(
        "error.caseReferenceNumber.invalid-value"
      )
      caseReferenceNumberMapping.bind(Map("" -> "AA0000000000000000000Z0")) should haveOnlyError(
        "error.caseReferenceNumber.invalid-value"
      )
    }
  }

}