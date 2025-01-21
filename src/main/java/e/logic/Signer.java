package e.logic;

import java.io.InputStream;
import java.io.IOException;
import org.springframework.stereotype.Service;
import org.bouncycastle.cms.CMSSignedData;
import org.bouncycastle.cms.CMSSignedDataGenerator;
import org.bouncycastle.cms.CMSException;

@Service
public class Signer {

    public void sign(InputStream content) {
        try (content) {
            sign_internal(content);
        } catch (IOException | CMSException exception) {
            throw new RuntimeException(exception);
        }
    }

    private void sign_internal(InputStream content) throws CMSException {
        CMSSignedDataGenerator generator = new CMSSignedDataGenerator();
        CMSSignedData data = generator.generate(null);
    }
}
