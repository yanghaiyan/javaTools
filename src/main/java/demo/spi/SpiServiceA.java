package demo.spi;

public class SpiServiceA implements SpiService {
    @Override
    public void hello() {
        System.out.println("SpiServiceA.hello");
    }
}
