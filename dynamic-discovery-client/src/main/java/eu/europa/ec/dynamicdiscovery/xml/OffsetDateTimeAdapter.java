/*
 * Copyright 2017-2023 European Commission | eDelivery Dynamic Discovery Client
 *
 * Licensed under the EUPL, Version 1.2 or – as soon they will be approved by
 * the European Commission - subsequent versions of the EUPL (the "Licence");
 * You may not use this work except in compliance with the Licence.
 *
 * You may obtain a copy of the Licence attached in file: LICENSE-EUPL-v1.2-EN.txt
 *
 * Unless required by applicable law or agreed to in writing, software distributed
 * under the Licence is distributed on an "AS IS" basis,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the Licence for the specific language governing permissions and limitations under the Licence.
 */
package eu.europa.ec.dynamicdiscovery.xml;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.time.OffsetDateTime;

public class OffsetDateTimeAdapter
    extends XmlAdapter<String, OffsetDateTime>
{
    public OffsetDateTime unmarshal(String value) {
        return (eu.europa.ec.dynamicdiscovery.util.DatatypeConverter.parseDateTime(value));
    }

    public String marshal(OffsetDateTime value) {
        return (eu.europa.ec.dynamicdiscovery.util.DatatypeConverter.printDateTime(value));
    }
}
