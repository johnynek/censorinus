package github.gphat.censorinus

import org.scalatest._
import github.gphat.censorinus.dogstatsd.Encoder

class DogStatsDEncoderSpec extends FlatSpec with Matchers {

  "DogStatsD Encoder" should "encode gauges" in {

    val g = GaugeMetric(name = "foobar", value = 1.0, tags = Array("foo:bar"))
    Encoder.encode(g).get should be ("foobar:1|g|#foo:bar")
  }

  it should "encode counters" in {
    val m = CounterMetric(name = "foobar", value = 1.0)
    Encoder.encode(m).get should be ("foobar:1|c")

    // Counter with optional sample rate
    val m1 = CounterMetric(name = "foobar", value = 1.0, sampleRate = 0.5)
    Encoder.encode(m1).get should be ("foobar:1|c|@0.5")
  }

  it should "encode timers" in {
    val m = TimerMetric(name = "foobar", value = 1.0)
    Encoder.encode(m).get should be ("foobar:1|ms")

    // Counter with optional sample rate
    val m1 = TimerMetric(name = "foobar", value = 1.0, sampleRate = 0.5)
    Encoder.encode(m1).get should be ("foobar:1|ms|@0.5")
  }

  it should "encode histograms" in {
    val m = HistogramMetric(name = "foobar", value = 1.0)
    Encoder.encode(m).get should be ("foobar:1|h")

    // Counter with optional sample rate
    val m1 = HistogramMetric(name = "foobar", value = 1.0, sampleRate = 0.5)
    Encoder.encode(m1).get should be ("foobar:1|h|@0.5")
  }

  it should "encode sets" in {
    val m = SetMetric(name = "foobar", value = "fart")
    Encoder.encode(m).get should be ("foobar:fart|s")
  }

  it should "encode service checks" in {
    val now = System.currentTimeMillis() / 1000L
    val m = ServiceCheckMetric(
      name = "foobar", status = DogStatsDClient.SERVICE_CHECK_OK, tags = Array("foo:bar"),
      hostname = Some("fart"), timestamp = Some(now), message = Some("wheeee")
    )
    Encoder.encode(m).get should be ("_sc|foobar|0|d:%d|h:fart|#foo:bar|m:wheeee".format(now))
  }
}
