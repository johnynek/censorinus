package github.gphat.censorinus

import java.net.{InetSocketAddress,SocketException}
import java.nio.ByteBuffer
import java.nio.channels.UnresolvedAddressException
import java.nio.channels.DatagramChannel
import java.nio.charset.StandardCharsets

class UDPSender(
  hostname: String = "localhost",
  port: Int = MetricSender.DEFAULT_STATSD_PORT,
  allowExceptions: Boolean = false
) extends MetricSender {

  lazy val clientSocket = DatagramChannel.open.connect(new InetSocketAddress(hostname, port))

  def send(message: String): Unit = {
    try {
      clientSocket.write(ByteBuffer.wrap(message.getBytes(StandardCharsets.UTF_8)))
    } catch {
      case se @ (_ : SocketException | _ : UnresolvedAddressException) => {
        // Check if we're allowing exceptions and rethrow if so. We didn't use
        // a guard on the case because then we'd need a second case to catch
        // the !allowExceptions case!
        if(allowExceptions) {
          throw se
        }
      }
    }
  }

  def shutdown: Unit = clientSocket.close
}
