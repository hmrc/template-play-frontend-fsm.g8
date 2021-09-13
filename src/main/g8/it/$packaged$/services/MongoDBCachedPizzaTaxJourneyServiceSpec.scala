package $package$.services

import $package$.support.AppISpec
import uk.gov.hmrc.http.HeaderCarrier
import scala.concurrent.ExecutionContext.Implicits.global
import java.util.UUID

class MongoDBCached$servicePrefix$JourneyServiceSpec extends AppISpec {

  lazy val service: MongoDBCached$servicePrefix$JourneyService =
    app.injector.instanceOf[MongoDBCached$servicePrefix$JourneyService]

  import service.model.{State, Transitions}

  implicit val hc: HeaderCarrier =
    HeaderCarrier()
      .withExtraHeaders("$servicePrefix$Journey" -> UUID.randomUUID.toString)

  "MongoDBCached$servicePrefix$JourneyService" should {
    "apply start transition" in {
      await(service.apply(Transitions.start)) shouldBe ((State.Start, Nil))
    }
  }

}
