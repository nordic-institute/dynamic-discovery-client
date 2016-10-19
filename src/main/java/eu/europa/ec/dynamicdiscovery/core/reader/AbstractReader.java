/*
 * Copyright 2016 Dynamic Discovery Client Project
 *
 * Licensed under the EUPL, Version 1.1 or – as soon they
 * will be approved by the European Commission - subsequent
 * versions of the EUPL (the "Licence");
 * You may not use this work except in compliance with the
 * Licence.
 * You may obtain a copy of the Licence at:
 * http://ec.europa.eu/idabc/servlets/Docbb6d.pdf?id=31979
 * Unless required by applicable law or agreed to in
 * writing, software distributed under the Licence is
 * distributed on an "AS IS" basis,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied.
 * See the Licence for the specific language governing
 * permissions and limitations under the Licence.
 */
package eu.europa.ec.dynamicdiscovery.core.reader;

import org.oasis_open.docs.bdxr.ns.smp._2014._07.ServiceGroup;
import org.oasis_open.docs.bdxr.ns.smp._2014._07.ServiceMetadata;
import org.oasis_open.docs.bdxr.ns.smp._2014._07.SignedServiceMetadata;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

/**
 * @author Flavio Santos - CEF-EDELIVERY-SUPPORT@ec.europa.eu
 */
public abstract class AbstractReader implements IMetadataReader {

    protected JAXBContext jaxbContext;

    public AbstractReader() {
        try {
            jaxbContext = JAXBContext.newInstance(new Class[]{ServiceGroup.class, SignedServiceMetadata.class, ServiceMetadata.class});
        } catch (JAXBException exc) {
            throw new RuntimeException(exc.getMessage(), exc);
        }
    }
}
