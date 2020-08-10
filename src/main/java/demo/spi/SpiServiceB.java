package demo.spi;

public class SpiServiceB implements SpiService {
    @Override
    public void hello() {
        System.out.println("SpiServiceB.hello");
    }
}
