package utils;

import java.nio.ByteOrder;
import sun.misc.Unsafe;

/**
 * 检查CPU是大端还是小端
 */
 class CheckCpuUtils {

  private static final Unsafe unsafe = Unsafe.getUnsafe();

  /**
   * 返回大端还是小端
   */
  public  ByteOrder checkCpu() {
    long a = unsafe.allocateMemory(8);
    boolean isBigEnd = true;
    ByteOrder byteOrder;
    try {
      unsafe.putLong(a, 0x0102030405060708L);
      byte b = unsafe.getByte(a);
      switch (b) {
        case 0x01:
          byteOrder = ByteOrder.BIG_ENDIAN;
          break;
        case 0x08:
          byteOrder = ByteOrder.LITTLE_ENDIAN;
          break;
        default: {
          assert false;
          byteOrder = null;
        }
      }
    } finally {
      unsafe.freeMemory(a);
    }

    return byteOrder;
  }

  public static void main(String[] args) {
    CheckCpuUtils checkCpuUtils = new CheckCpuUtils();
    System.out.println(checkCpuUtils.checkCpu().toString());
  }
}
