package eu.europa.ex.dynamicdiscovery;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import org.junit.Before;
import org.junit.Rule;
import org.mockito.MockitoAnnotations;

/**
 * Created by rodrfla on 06/10/2016.
 */
public abstract class AbstractTest {

    private static boolean initialized;

    @Rule
    public WireMockRule wireMockRule;


    @Before
    public void setup() throws Exception {
        if (!initialized) {
            MockitoAnnotations.initMocks(this);
            wireMockRule = new WireMockRule(8080);
            wireMockRule.start();
            initialized = true;
        }
    }


}
