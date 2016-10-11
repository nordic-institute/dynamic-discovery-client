package eu.europa.ex.dynamicdiscovery;

/**
 * Created by rodrfla on 10/10/2016.
 */
public class Constants {

    public static final String COMPLETE_SERVICE_GROUP_URL_0088_123456789101112 = "/cipa-smp-full-webapp/complete/iso6523-actorid-upis%3A%3A0088%3A5798000000997";
    public static final String COMPLETE_SERVICE_GROUP_BODY_0088_123456789101112 = "<ns0:CompleteServiceGroup xmlns:ns2=\"http://www.w3.org/2005/08/addressing\" xmlns:ns1=\"http://busdox.org/transport/identifiers/1.0/\" xmlns:ns3=\"http://www.w3.org/2000/09/xmldsig#\" xmlns:ns0=\"http://busdox.org/serviceMetadata/publishing/1.0/\">\n" +
            "<ns0:ServiceGroup>\n" +
            "<ns1:ParticipantIdentifier scheme=\"iso6523-actorid-upis\">0088:123456789101112</ns1:ParticipantIdentifier>\n" +
            "<ns0:ServiceMetadataReferenceCollection>\n" +
            "<ns0:ServiceMetadataReference href=\"http://localhost:7001/iso6523-actorid-upis%3A%3A0088%3A123456789101112/services/busdox-docid-qns%3A%3Aurn%3Aoasis%3Anames%3Aspecification%3Aubl%3Aschema%3Axsd%3AInvoice-2%3A%3AInvoice%23%23urn%3Awww.cenbii.eu%3Atransaction%3Abiicoretrdm010%3Aver1.0%3A%23urn%3Awww.peppol.eu%3Abis%3Apeppol4a%3Aver1.0%3A%3A2.0\"/>\n" +
            "</ns0:ServiceMetadataReferenceCollection>\n" +
            "</ns0:ServiceGroup>\n" +
            "<ns0:ServiceMetadata>\n" +
            "<ns0:ServiceInformation>\n" +
            "<ns1:ParticipantIdentifier scheme=\"iso6523-actorid-upis\">0088:123456789101112</ns1:ParticipantIdentifier>\n" +
            "<ns1:DocumentIdentifier scheme=\"busdox-docid-qns\">\n" +
            "urn:oasis:names:specification:ubl:schema:xsd:Invoice-2::Invoice##urn:www.cenbii.eu:transaction:biicoretrdm010:ver1.0:#urn:www.peppol.eu:bis:peppol4a:ver1.0::2.0\n" +
            "</ns1:DocumentIdentifier>\n" +
            "<ns0:ProcessList/>\n" +
            "</ns0:ServiceInformation>\n" +
            "</ns0:ServiceMetadata>\n" +
            "</ns0:CompleteServiceGroup>";

    public static final String SERVICE_GROUP_URL_9925_0367302178 = "/cipa-smp-full-webapp/iso6523-actorid-upis%3A%3A9925%3A0367302178";
    public static final String SERVICE_GROUP_BODY_9925_0367302178  = "<ServiceGroup xmlns=\"http://busdox.org/serviceMetadata/publishing/1.0/\" xmlns:ids=\"http://busdox.org/transport/identifiers/1.0/\">\n" +
            "<ids:ParticipantIdentifier scheme=\"busdox-actorid-upis\">9925:0367302178</ids:ParticipantIdentifier>\n" +
            "<ServiceMetadataReferenceCollection>\n" +
            "<ServiceMetadataReference href=\"http://smp.fe.babelway.net/iso6523-actorid-upis::9925%3A0367302178/services/busdox-docid-qns%3A%3Aurn%3Aoasis%3Anames%3Aspecification%3Aubl%3Aschema%3Axsd%3ACreditNote-2%3A%3ACreditNote%23%23urn%3Awww.cenbii.eu%3Atransaction%3Abiitrns014%3Aver2.0%3Aextended%3Aurn%3Awww.peppol.eu%3Abis%3Apeppol5a%3Aver2.0%3A%3A2.1\"/>\n" +
            "<ServiceMetadataReference href=\"http://smp.fe.babelway.net/iso6523-actorid-upis::9925%3A0367302178/services/busdox-docid-qns%3A%3Aurn%3Aoasis%3Anames%3Aspecification%3Aubl%3Aschema%3Axsd%3AInvoice-2%3A%3AInvoice%23%23urn%3Awww.cenbii.eu%3Atransaction%3Abiitrns010%3Aver2.0%3Aextended%3Aurn%3Awww.peppol.eu%3Abis%3Apeppol5a%3Aver2.0%3A%3A2.1\"/>\n" +
            "<ServiceMetadataReference href=\"http://smp.fe.babelway.net/iso6523-actorid-upis::9925%3A0367302178/services/busdox-docid-qns%3A%3Aurn%3Aoasis%3Anames%3Aspecification%3Aubl%3Aschema%3Axsd%3AInvoice-2%3A%3AInvoice%23%23urn%3Awww.cenbii.eu%3Atransaction%3Abiitrns010%3Aver2.0%3Aextended%3Aurn%3Awww.peppol.eu%3Abis%3Apeppol4a%3Aver2.0%3A%3A2.1\"/>\n" +
            "</ServiceMetadataReferenceCollection>\n" +
            "<Extension/>\n" +
            "</ServiceGroup>";
}
