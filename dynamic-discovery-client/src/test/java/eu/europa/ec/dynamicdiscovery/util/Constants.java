/*
 * (C) Copyright 2016 - European Commission | Dynamic Discovery Client
 *
 * https://ec.europa.eu/cefdigital/code/projects/EDELIVERY/repos/dynamic-discovery-client/browse
 *
 * Licensed under the LGPL, Version 2.1 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     dynamic-discovery\License_LGPL-2.1.txt or https://www.gnu.org/licenses/old-licenses/lgpl-2.1.txt
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * @author Flávio W. R. Santos - CEF-EDELIVERY-SUPPORT@ec.europa.eu
 *
 */
package eu.europa.ec.dynamicdiscovery.util;

public class Constants {

    public static final String SMP_DOMAIN = "http://localhost:8090/cipa-smp-full-webapp/";

    public static final String SMP_DOMAIN_ALIAS = "http://smp.ec.europa.eu/";

    public static final String SERVICE_GROUP_URL_9925_0367302178 = "/cipa-smp-full-webapp/iso6523-actorid-upis%3A%3A9925%3A0367302178";

    public static final String SERVICE_GROUP_URL_URN_POLAND_NCPB = "/cipa-smp-full-webapp/ehealth-actorid-qns%3A%3Aurn%3Apoland%3Ancpb";

    public static final String SERVICE_METADATA_URL_URN_POLAND_NCPB = "/cipa-smp-full-webapp/ehealth-actorid-qns%3A%3Aurn%3Apoland%3Ancpb/services/ehealth-resid-qns%3A%3Aurn%3A%3Aepsos%23%23services%3Aextended%3Aepsos%3A%3A107";

    public static final String SIGNED_SERVICE_METADATA_URL_URN_POLAND_NCPB = SERVICE_METADATA_URL_URN_POLAND_NCPB;

    public static final String SERVICE_METADATA_URL_9915_123456789 = "/cipa-smp-full-webapp/iso6523-actorid-upis%3A%3A9915%3A123456789/services/bdxr-docid-qns%3A%3Aurn%3Aoasis%3Anames%3Aspecification%3Aubl%3Aschema%3Axsd%3ACreditNote-2%3A%3ACreditNote%23%23urn%3Awww.cenbii.eu%3Atransaction%3Abiitrns014%3Aver2.0%3Aextended%3Aurn%3Awww.peppol.eu%3Abis%3Apeppol5a%3Aver2.0%3A%3A2.1";

    public static final String SIGNED_SERVICE_METADATA_URL_9915_123456789 = SERVICE_METADATA_URL_9915_123456789;
}
