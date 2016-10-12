package eu.europa.ex.dynamicdiscovery;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import org.junit.Rule;

/**
 * @author Flavio Santos - CEF-EDELIVERY-SUPPORT@ec.europa.eu
 */
public abstract class AbstractTest {

    @Rule
    public WireMockRule wireMockRule = new WireMockRule();
}
