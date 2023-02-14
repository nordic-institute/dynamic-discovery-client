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
