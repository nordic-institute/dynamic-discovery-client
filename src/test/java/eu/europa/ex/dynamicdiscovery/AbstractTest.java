package eu.europa.ex.dynamicdiscovery;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import eu.europa.ex.dynamicdiscovery.service.DNSService;
import org.junit.Before;
import org.junit.Rule;
import org.mockito.InjectMocks;

/**
 * Created by rodrfla on 06/10/2016.
 */
public abstract class AbstractTest {

    private static boolean initialized;

    @InjectMocks
    protected DNSService dnsService;



    @Before
    public void setup() throws Exception {
        if (!initialized) {
            initialized = true;
        }
    }


}
