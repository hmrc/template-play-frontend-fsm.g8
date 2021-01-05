package $package$.support

import $package$.models._
import java.time.LocalDate

object TestData {

  val exportDeclarationDetails = DeclarationDetails(EPU(123), EntryNumber("Z00000Z"), LocalDate.parse("2020-09-23"))
  val importDeclarationDetails = DeclarationDetails(EPU(123), EntryNumber("000000Z"), LocalDate.parse("2020-09-23"))
  val invalidDeclarationDetails = DeclarationDetails(EPU(123), EntryNumber("0000000"), LocalDate.parse("2020-09-23"))

  def fullExampleQuestions() =
    ExampleQuestions(
      requestType = Some(ExampleRequestType.New),
      routeType = Some(ExampleRouteType.Route3)
    )
}
