package eu.europa.ex.dynamicdiscovery;

import org.junit.Before;

/**
 * Created by rodrfla on 06/10/2016.
 */
public abstract class AbstractTest {

    private static boolean initialized;

    @Before
    public void setup() throws Exception {
        if (!initialized) {
            initialized = true;
        }
    }


}
