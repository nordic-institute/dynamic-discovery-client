/*
 * Copyright 2017-2023 European Commission | CEF eDelivery
 *
 * Licensed under the EUPL, Version 1.2 or – as soon they will be approved by the European Commission - subsequent versions of the EUPL (the "Licence");
 * You may not use this work except in compliance with the Licence.
 *
 * You may obtain a copy of the Licence attached in file: LICENCE-EUPL-v1.2.pdf
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the Licence is distributed on an "AS IS" basis,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the Licence for the specific language governing permissions and limitations under the Licence.
 */
package eu.europa.ec.dynamicdiscovery.model.identifiers;


/**
 * Formatter for the DocumentIdentifier with default null split regular expression and
 * '::' as split separator. For details see the {@link AbstractIdentifierFormatter}
 *
 * @author Joze Rihtarsic
 * @since 2.0
 */
public class DocumentIdentifierFormatter extends AbstractIdentifierFormatter<SMPDocumentIdentifier> {


    @Override
    protected String getSchemeFromObject(SMPDocumentIdentifier object) {
        return object != null ? object.getScheme() : null;
    }

    @Override
    protected String getIdentifierFromObject(SMPDocumentIdentifier object) {
        return object != null ? object.getIdentifier() : null;
    }

    @Override
    protected SMPDocumentIdentifier createObject(String scheme, String identifier) {
        return new SMPDocumentIdentifier(identifier, scheme);
    }

    @Override
    protected void updateObject(SMPDocumentIdentifier identifierObject, String scheme, String identifier) {
        identifierObject.setScheme(scheme);
        identifierObject.setIdentifier(identifier);
    }
}
