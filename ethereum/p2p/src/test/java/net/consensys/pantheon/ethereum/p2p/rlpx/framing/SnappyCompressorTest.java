package net.consensys.pantheon.ethereum.p2p.rlpx.framing;

import static io.netty.buffer.ByteBufUtil.decodeHexDump;
import static org.assertj.core.api.Assertions.assertThat;

import java.nio.charset.StandardCharsets;

import org.junit.Test;

public class SnappyCompressorTest {

  @Test
  public void roundTrip() {
    String input = "Uncompressed sample text for round-trip compression/decompression";
    input = input + input + input + input; // Give it some repetition for good sample data
    final byte[] data = input.getBytes(StandardCharsets.UTF_8);
    final SnappyCompressor snappy = new SnappyCompressor();
    assertThat(snappy.decompress(snappy.compress(data))).isEqualTo(data);
  }

  @Test
  public void roundTripEmptyByteArray() {
    final byte[] data = new byte[0];
    final SnappyCompressor snappy = new SnappyCompressor();
    assertThat(snappy.decompress(snappy.compress(data))).isEqualTo(data);
  }

  @Test(expected = NullPointerException.class)
  public void compressNull() {
    final SnappyCompressor snappy = new SnappyCompressor();
    snappy.compress(null);
  }

  @Test(expected = NullPointerException.class)
  public void decompressNull() {
    final SnappyCompressor snappy = new SnappyCompressor();
    snappy.decompress(null);
  }

  @Test(expected = NullPointerException.class)
  public void uncompressedLengthNull() {
    final SnappyCompressor snappy = new SnappyCompressor();
    snappy.uncompressedLength(null);
  }

  @Test
  public void roundTripEthereumData() {
    // First data set.
    byte[] compressed =
        decodeHexDump(
            "ab01a8f8a9f8a74083282fff82945194fc2c4d8f95002c14ed0a7a"
                + "a65102cac9e5953b5e80b844a9059cbb00000015024c3463934897d356b8659cbdfe15209e3bc32291"
                + "05151d3a0100f04a45636408bcb6e0001ca06a4ed94062719ae58d392b253268da005a4fb2d8d33b19"
                + "ec84a7312a34ecbfc2a0055c660cc59f5dad52ae4d6fd5f2fc081d706ee0bce4195ecfff07a1f85d1b"
                + "d6");

    byte[] decompressed =
        decodeHexDump(
            "f8a9f8a74083282fff82945194fc2c4d8f95002c14ed0a7aa651"
                + "02cac9e5953b5e80b844a9059cbb0000000000000000000000003463934897d356b8659cbdfe15209e"
                + "3bc322910500000000000000000000000000000000000000000000000045636408bcb6e0001ca06a4e"
                + "d94062719ae58d392b253268da005a4fb2d8d33b19ec84a7312a34ecbfc2a0055c660cc59f5dad52ae"
                + "4d6fd5f2fc081d706ee0bce4195ecfff07a1f85d1bd6");

    final SnappyCompressor snappy = new SnappyCompressor();
    assertThat(snappy.compress(decompressed)).isEqualTo(compressed);
    assertThat(snappy.decompress(compressed)).isEqualTo(decompressed);
    assertThat(snappy.decompress(snappy.compress(decompressed))).isEqualTo(decompressed);
    assertThat(snappy.compress(snappy.decompress(compressed))).isEqualTo(compressed);

    // Second data set.
    compressed =
        decodeHexDump(
            "ac01a8f8aaf8a880843b9aca0082ea609466186008c1050627f979d464eab"
                + "b258860563dbe80b844a9059cbb000019024c7cecb041d044ae699f9830b53256c7e1446430a3191e3"
                + "20100f04b02b5e3af16b188000025a03f691708219e6d099c0c022ac86c6745b98bce1417a94c32d2e"
                + "e5a4e48c0e550a05df314c4202ac2aff5fd13bd5ede29b6967ffdb3063b203c571641fa8dd11c5c");

    decompressed =
        decodeHexDump(
            "f8aaf8a880843b9aca0082ea609466186008c1050627f979d464eabb258"
                + "860563dbe80b844a9059cbb0000000000000000000000007cecb041d044ae699f9830b53256c7e1446"
                + "430a3000000000000000000000000000000000000000000000002b5e3af16b188000025a03f6917082"
                + "19e6d099c0c022ac86c6745b98bce1417a94c32d2ee5a4e48c0e550a05df314c4202ac2aff5fd13bd5"
                + "ede29b6967ffdb3063b203c571641fa8dd11c5c");

    assertThat(snappy.compress(decompressed)).isEqualTo(compressed);
    assertThat(snappy.decompress(compressed)).isEqualTo(decompressed);
    assertThat(snappy.decompress(snappy.compress(decompressed))).isEqualTo(decompressed);
    assertThat(snappy.compress(snappy.decompress(compressed))).isEqualTo(compressed);
  }
}
