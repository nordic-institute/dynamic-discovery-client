package eu.europa.ec.dynamicdiscovery.model.identifiers;

import eu.europa.ec.dynamicdiscovery.model.identifiers.types.EBCorePartyIdFormatterType;

/**
 * Formatter for the ParticipantIdentifier with default "ebCoreParty" split regular expression and
 * '::' as split separator. For details see the {@link AbstractIdentifierFormatter}
 *
 * @author Joze Rihtarsic
 * @since 2.0
 */
public class ParticipantIdentifierFormatter extends AbstractIdentifierFormatter<SMPParticipantIdentifier> {

    public ParticipantIdentifierFormatter() {

        this.formatterTypes.add(new EBCorePartyIdFormatterType());
    }

    public void setWildcardEnabled(boolean enable){
        this.formatterTypes.forEach(formatterType -> formatterType.setWildcardEnabled(enable));
        getDefaultFormatter().setWildcardEnabled(enable);
    }

    @Override
    protected String getSchemeFromObject(SMPParticipantIdentifier object) {
        return object != null ? object.getScheme() : null;
    }

    @Override
    protected String getIdentifierFromObject(SMPParticipantIdentifier object) {
        return object != null ? object.getIdentifier() : null;
    }

    @Override
    protected SMPParticipantIdentifier createObject(String scheme, String identifier) {
        return new SMPParticipantIdentifier(identifier, scheme);
    }

    @Override
    protected void updateObject(SMPParticipantIdentifier identifierObject, String scheme, String identifier) {
        identifierObject.setScheme(scheme);
        identifierObject.setIdentifier(identifier);
    }
}
