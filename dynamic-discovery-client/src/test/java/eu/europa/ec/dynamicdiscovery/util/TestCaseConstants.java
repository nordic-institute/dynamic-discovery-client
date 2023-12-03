/*
 * #%L
 * dynamic-discovery-cli
 * %%
 * Copyright (C) 2016 - 2023 European Commission | eDelivery | Dynamic Discovery Client
 * %%
 * Licensed under the LGPL, Version 2.1 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * [PROJECT_HOME]\license\lgpl2-1\license.txt or https://www.gnu.org/licenses/old-licenses/lgpl-2.1.txt
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package eu.europa.ec.dynamicdiscovery.util;

import eu.europa.ec.dynamicdiscovery.model.SMPTransportProfile;
import eu.europa.ec.dynamicdiscovery.model.identifiers.SMPDocumentIdentifier;
import eu.europa.ec.dynamicdiscovery.model.identifiers.SMPParticipantIdentifier;
import eu.europa.ec.dynamicdiscovery.model.identifiers.SMPProcessIdentifier;

/**
 * @author Flávio W. R. Santos
 */
public class TestCaseConstants {

    //  public static final String SMP_APP_CONTEXT="cipa-smp-full-webapp/";

    public static final String SMP_STATIC_DOMAIN = "http://localhost:8090/cipa-smp-full-webapp/";
    public static final String SMP_DOMAIN = "http://localhost:8090/";

    public static final String SMP_DOMAIN_ALIAS = "http://smp.ec.europa.eu/";

    public static final String SERVICE_GROUP_URL_9925_0367302178 = "/iso6523-actorid-upis%3A%3A9925%3A0367302178";

    public static final String SERVICE_GROUP_URL_URN_POLAND_NCPB = "/ehealth-actorid-qns%3A%3Aurn%3Apoland%3Ancpb";

    public static final String SERVICE_METADATA_URL_URN_POLAND_NCPB = "/ehealth-actorid-qns%3A%3Aurn%3Apoland%3Ancpb/services/ehealth-resid-qns%3A%3Aurn%3A%3Aepsos%23%23services%3Aextended%3Aepsos%3A%3A107";

    public static final String SIGNED_SERVICE_METADATA_URL_URN_POLAND_NCPB = SERVICE_METADATA_URL_URN_POLAND_NCPB;

    public static final String SERVICE_METADATA_URL_9915_123456789 = "/iso6523-actorid-upis%3A%3A9915%3A123456789/services/bdxr-docid-qns%3A%3Aurn%3Aoasis%3Anames%3Aspecification%3Aubl%3Aschema%3Axsd%3ACreditNote-2%3A%3ACreditNote%23%23urn%3Awww.cenbii.eu%3Atransaction%3Abiitrns014%3Aver2.0%3Aextended%3Aurn%3Awww.peppol.eu%3Abis%3Apeppol5a%3Aver2.0%3A%3A2.1";

    // ---------------------------------------------------------
    // The identifiers below matches the XML examples
    public static final SMPParticipantIdentifier PARTICIPANT_IDENTIFIER_ISO6253_01 = new SMPParticipantIdentifier("9925:0367302178", "iso6523-actorid-upis");
    public static final SMPParticipantIdentifier PARTICIPANT_IDENTIFIER_ISO6253_02 = new SMPParticipantIdentifier("9915:123456789", "iso6523-actorid-upis");
    public static final SMPDocumentIdentifier DOCUMENT_IDENTIFIER_001 = new SMPDocumentIdentifier("urn::epsos:services##epsos-21", "bdx-docid-qns");
    public static final SMPDocumentIdentifier DOCUMENT_IDENTIFIER_002 = new SMPDocumentIdentifier("urn:oasis:names:specification:ubl:schema:xsd:CreditNote-2::CreditNote##urn:www.cenbii.eu:transaction:biitrns014:ver2.0:extended:urn:www.peppol.eu:bis:peppol5a:ver2.0::2.1", "bdx-docid-qns");


    public static final SMPProcessIdentifier PROCESS_IDENTIFIER_01 = new SMPProcessIdentifier("urn:epsosPatientService::List", "ehealth-procid-qns");
    public static final SMPProcessIdentifier PROCESS_IDENTIFIER_02 = new SMPProcessIdentifier("urn:www.cenbii.eu:profile:bii05:ver2.0", "cenbii-procid-qns");
    public static final SMPTransportProfile TRANSPORT_PROFILE_01 = new SMPTransportProfile("bdxr-transport-ebms3-as4-v1p0");
    public static final SMPTransportProfile TRANSPORT_PROFILE_02 = new SMPTransportProfile("urn:ihe:iti:2013:xcpd");

    public static final String WILDCARD_SCHEME = "bdx-docid-wildcard";

    public static final String PEPPOL_DOCTYPE_WILDCARD = "peppol-doctype-wildcard";
    public static final String BUSDOX_DOCID_QNS = "busdox-docid-qns";
    public static final String WILDCARD_CHARACTER = "*";



}
