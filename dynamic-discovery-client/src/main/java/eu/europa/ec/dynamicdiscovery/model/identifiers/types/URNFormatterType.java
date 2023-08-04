package eu.europa.ec.dynamicdiscovery.model.identifiers.types;

import java.util.regex.Pattern;


/**
 * Special case TemplateFormatterType for custom urn identifiers
 * The format is defined by template: "${scheme}:${identifier}" with separator character ':'.
 *
 * @author Joze RIHTARSIC
 * @since 2.0
 */
public class URNFormatterType extends TemplateFormatterType {

    public URNFormatterType(Pattern splitRegularExpression) {
        super(null, "${scheme}:${identifier}", splitRegularExpression);
    }
}
