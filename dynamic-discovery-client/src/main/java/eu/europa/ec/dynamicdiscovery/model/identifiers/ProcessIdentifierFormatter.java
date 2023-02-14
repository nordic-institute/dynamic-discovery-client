package eu.europa.ec.dynamicdiscovery.model.identifiers;



/**
 * Formatter for the ProcessIdentifier with default null split regular expression and
 * '::' as split separator. For details see the {@link AbstractIdentifierFormatter}
 *
 * @author Joze Rihtarsic
 * @since 2.0
 */
public class ProcessIdentifierFormatter extends AbstractIdentifierFormatter<SMPProcessIdentifier> {


    @Override
    protected String getSchemeFromObject(SMPProcessIdentifier object) {
        return object != null ? object.getScheme() : null;
    }

    @Override
    protected String getIdentifierFromObject(SMPProcessIdentifier object) {
        return object != null ? object.getIdentifier() : null;
    }

    @Override
    protected SMPProcessIdentifier createObject(String scheme, String identifier) {
        return new SMPProcessIdentifier(identifier, scheme);
    }

    @Override
    protected void updateObject(SMPProcessIdentifier identifierObject, String scheme, String identifier) {
        identifierObject.setScheme(scheme);
        identifierObject.setIdentifier(identifier);
    }
}
