package aima.core.search.local

import aima.core.search.local.SimulatedAnnealingSearch.{OverTimeStepLimit, Temperature, TemperatureResult}
import aima.core.search.local.time.TimeStep
import org.scalacheck.{Arbitrary, Gen}
import org.specs2.ScalaCheck
import org.specs2.mutable.Specification

import scala.util.{Success, Try}

/**
  * @author Shawn Garner
  */
class SimulatedAnnealingSearchSpec extends Specification with ScalaCheck {

  "BasicSchedule" >> {
    import aima.core.search.local.SimulatedAnnealingSearch.BasicSchedule.schedule
    import aima.core.search.local.time.TimeStep.Implicits._

    def increment(ts: TimeStep, times: Int): Try[TimeStep] = times match {
      case 0 => Success(ts)
      case _ => ts.step.flatMap(increment(_, times - 1))
    }

    "lower limit check" in {
      schedule(TimeStep.start) match {
        case Temperature(t) => t must beCloseTo(19.1d within 3.significantFigures)
        case other          => ko(other.toString)
      }
    }

    "upper limit check" in {
      increment(TimeStep.start, 98).map(schedule(_)) match {
        case Success(Temperature(t)) => t must beCloseTo(0.232d within 3.significantFigures)
        case other                   => ko(other.toString)
      }

    }

    "over limit check" in {
      increment(TimeStep.start, 99).map(schedule(_)) must beSuccessfulTry[TemperatureResult](OverTimeStepLimit)
    }

    implicit val arbTimeStep: Arbitrary[TimeStep] = Arbitrary {
      for {
        numSteps <- Gen.choose[Int](0, 98)
      } yield increment(TimeStep.start, numSteps).get
    }

    "must not create negative temperature" >> prop { ts: TimeStep =>
      schedule(ts) match {
        case Temperature(t) => t must be_>(0.00d)
        case other          => ko(other.toString)
      }
    }

  }

}