package e.helper;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicInteger;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

public class AppGenerator implements IdentifierGenerator {
    private static AtomicInteger value;

    public static void init(int value) {
        AppGenerator.value = new AtomicInteger(value);
    }
    
    public static int get() {
        return AppGenerator.value.get();
    }
    
    public static int generate() {
        return AppGenerator.value.incrementAndGet();
    }

    @Override
    public Serializable generate(SharedSessionContractImplementor session, Object object) {
        return AppGenerator.generate();
    }
}
