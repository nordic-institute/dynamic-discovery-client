package org.w3._2005._08.addressing;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;

import org.w3._2005._08.addressing.AttributedAnyType;
import org.w3._2005._08.addressing.AttributedQNameType;
import org.w3._2005._08.addressing.AttributedURIType;
import org.w3._2005._08.addressing.AttributedUnsignedLongType;
import org.w3._2005._08.addressing.EndpointReferenceType;
import org.w3._2005._08.addressing.MetadataType;
import org.w3._2005._08.addressing.ProblemActionType;
import org.w3._2005._08.addressing.ReferenceParametersType;
import org.w3._2005._08.addressing.RelatesToType;

@XmlRegistry
public class ObjectFactory {
    private static final QName _ProblemIRI_QNAME = new QName("http://www.w3.org/2005/08/addressing", "ProblemIRI");
    private static final QName _MessageID_QNAME = new QName("http://www.w3.org/2005/08/addressing", "MessageID");
    private static final QName _Action_QNAME = new QName("http://www.w3.org/2005/08/addressing", "Action");
    private static final QName _ProblemHeader_QNAME = new QName("http://www.w3.org/2005/08/addressing", "ProblemHeader");
    private static final QName _Metadata_QNAME = new QName("http://www.w3.org/2005/08/addressing", "Metadata");
    private static final QName _RelatesTo_QNAME = new QName("http://www.w3.org/2005/08/addressing", "RelatesTo");
    private static final QName _RetryAfter_QNAME = new QName("http://www.w3.org/2005/08/addressing", "RetryAfter");
    private static final QName _From_QNAME = new QName("http://www.w3.org/2005/08/addressing", "From");
    private static final QName _ReplyTo_QNAME = new QName("http://www.w3.org/2005/08/addressing", "ReplyTo");
    private static final QName _ProblemHeaderQName_QNAME = new QName("http://www.w3.org/2005/08/addressing", "ProblemHeaderQName");
    private static final QName _To_QNAME = new QName("http://www.w3.org/2005/08/addressing", "To");
    private static final QName _FaultTo_QNAME = new QName("http://www.w3.org/2005/08/addressing", "FaultTo");
    private static final QName _ProblemAction_QNAME = new QName("http://www.w3.org/2005/08/addressing", "ProblemAction");
    private static final QName _EndpointReference_QNAME = new QName("http://www.w3.org/2005/08/addressing", "EndpointReference");

    public ObjectFactory() {
    }

    public EndpointReferenceType createEndpointReferenceType() {
        return new EndpointReferenceType();
    }

    public AttributedURIType createAttributedURIType() {
        return new AttributedURIType();
    }

    public AttributedUnsignedLongType createAttributedUnsignedLongType() {
        return new AttributedUnsignedLongType();
    }

    public AttributedAnyType createAttributedAnyType() {
        return new AttributedAnyType();
    }

    public MetadataType createMetadataType() {
        return new MetadataType();
    }

    public RelatesToType createRelatesToType() {
        return new RelatesToType();
    }

    public ProblemActionType createProblemActionType() {
        return new ProblemActionType();
    }

    public AttributedQNameType createAttributedQNameType() {
        return new AttributedQNameType();
    }

    public ReferenceParametersType createReferenceParametersType() {
        return new ReferenceParametersType();
    }

    @XmlElementDecl(
            namespace = "http://www.w3.org/2005/08/addressing",
            name = "ProblemIRI"
    )
    public JAXBElement<AttributedURIType> createProblemIRI(AttributedURIType value) {
        return new JAXBElement(_ProblemIRI_QNAME, AttributedURIType.class, (Class) null, value);
    }

    @XmlElementDecl(
            namespace = "http://www.w3.org/2005/08/addressing",
            name = "MessageID"
    )
    public JAXBElement<AttributedURIType> createMessageID(AttributedURIType value) {
        return new JAXBElement(_MessageID_QNAME, AttributedURIType.class, (Class) null, value);
    }

    @XmlElementDecl(
            namespace = "http://www.w3.org/2005/08/addressing",
            name = "Action"
    )
    public JAXBElement<AttributedURIType> createAction(AttributedURIType value) {
        return new JAXBElement(_Action_QNAME, AttributedURIType.class, (Class) null, value);
    }

    @XmlElementDecl(
            namespace = "http://www.w3.org/2005/08/addressing",
            name = "ProblemHeader"
    )
    public JAXBElement<AttributedAnyType> createProblemHeader(AttributedAnyType value) {
        return new JAXBElement(_ProblemHeader_QNAME, AttributedAnyType.class, (Class) null, value);
    }

    @XmlElementDecl(
            namespace = "http://www.w3.org/2005/08/addressing",
            name = "Metadata"
    )
    public JAXBElement<MetadataType> createMetadata(MetadataType value) {
        return new JAXBElement(_Metadata_QNAME, MetadataType.class, (Class) null, value);
    }

    @XmlElementDecl(
            namespace = "http://www.w3.org/2005/08/addressing",
            name = "RelatesTo"
    )
    public JAXBElement<RelatesToType> createRelatesTo(RelatesToType value) {
        return new JAXBElement(_RelatesTo_QNAME, RelatesToType.class, (Class) null, value);
    }

    @XmlElementDecl(
            namespace = "http://www.w3.org/2005/08/addressing",
            name = "RetryAfter"
    )
    public JAXBElement<AttributedUnsignedLongType> createRetryAfter(AttributedUnsignedLongType value) {
        return new JAXBElement(_RetryAfter_QNAME, AttributedUnsignedLongType.class, (Class) null, value);
    }

    @XmlElementDecl(
            namespace = "http://www.w3.org/2005/08/addressing",
            name = "From"
    )
    public JAXBElement<EndpointReferenceType> createFrom(EndpointReferenceType value) {
        return new JAXBElement(_From_QNAME, EndpointReferenceType.class, (Class) null, value);
    }

    @XmlElementDecl(
            namespace = "http://www.w3.org/2005/08/addressing",
            name = "ReplyTo"
    )
    public JAXBElement<EndpointReferenceType> createReplyTo(EndpointReferenceType value) {
        return new JAXBElement(_ReplyTo_QNAME, EndpointReferenceType.class, (Class) null, value);
    }

    @XmlElementDecl(
            namespace = "http://www.w3.org/2005/08/addressing",
            name = "ProblemHeaderQName"
    )
    public JAXBElement<AttributedQNameType> createProblemHeaderQName(AttributedQNameType value) {
        return new JAXBElement(_ProblemHeaderQName_QNAME, AttributedQNameType.class, (Class) null, value);
    }

    @XmlElementDecl(
            namespace = "http://www.w3.org/2005/08/addressing",
            name = "To"
    )
    public JAXBElement<AttributedURIType> createTo(AttributedURIType value) {
        return new JAXBElement(_To_QNAME, AttributedURIType.class, (Class) null, value);
    }

    @XmlElementDecl(
            namespace = "http://www.w3.org/2005/08/addressing",
            name = "FaultTo"
    )
    public JAXBElement<EndpointReferenceType> createFaultTo(EndpointReferenceType value) {
        return new JAXBElement(_FaultTo_QNAME, EndpointReferenceType.class, (Class) null, value);
    }

    @XmlElementDecl(
            namespace = "http://www.w3.org/2005/08/addressing",
            name = "ProblemAction"
    )
    public JAXBElement<ProblemActionType> createProblemAction(ProblemActionType value) {
        return new JAXBElement(_ProblemAction_QNAME, ProblemActionType.class, (Class) null, value);
    }

    @XmlElementDecl(
            namespace = "http://www.w3.org/2005/08/addressing",
            name = "EndpointReference"
    )
    public JAXBElement<EndpointReferenceType> createEndpointReference(EndpointReferenceType value) {
        return new JAXBElement(_EndpointReference_QNAME, EndpointReferenceType.class, (Class) null, value);
    }
}

