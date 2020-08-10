package demo.spi;

import java.util.Iterator;
import java.util.ServiceLoader;


public class SpiTest {

    public static void testSpi() {
        ServiceLoader<SpiService> serviceLoader = ServiceLoader.load(SpiService.class);

        Iterator<SpiService> iterator = serviceLoader.iterator();
        while (iterator.hasNext()) {
            SpiService spiService = iterator.next();

            spiService.hello();
        }
    }

    public static void main(String[] args) {
        System.out.println("begin");
        testSpi();
    }
}
