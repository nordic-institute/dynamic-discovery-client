package eu.europa.ex.dynamicdiscovery;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import org.junit.Rule;

/**
 * Created by rodrfla on 06/10/2016.
 */
public abstract class AbstractTest {

    @Rule
    public WireMockRule wireMockRule = new WireMockRule();
}
