package org.pac4j.core.engine.savedrequest;

import org.pac4j.core.context.ContextHelper;
import org.pac4j.core.util.Pac4jConstants;
import org.pac4j.core.context.WebContext;
import org.pac4j.core.exception.http.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The default {@link SavedRequestHandler} which handles GET and POST requests.
 *
 * @author Jerome LELEU
 * @since 4.0.0
 */
public class DefaultSavedRequestHandler implements SavedRequestHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultSavedRequestHandler.class);

    @Override
    public void save(final WebContext context) {
        final String requestedUrl = getRequestedUrl(context);
        if (ContextHelper.isPost(context)) {
            LOGGER.debug("requestedUrl with data: {}", requestedUrl);
            final String formPost = RedirectionActionHelper.buildFormPostContent(context);
            context.getSessionStore().set(context, Pac4jConstants.REQUESTED_URL, new OkAction(formPost));
        } else {
            LOGGER.debug("requestedUrl: {}", requestedUrl);
            context.getSessionStore().set(context, Pac4jConstants.REQUESTED_URL, new FoundAction(requestedUrl));
        }
    }

    protected String getRequestedUrl(final WebContext context) {
        return context.getFullRequestURL();
    }

    @Override
    public HttpAction restore(final WebContext context, final String defaultUrl) {
        HttpAction requestedAction = new FoundAction(defaultUrl);

        LOGGER.debug("requestedAction: {}", requestedAction.getMessage());
        return RedirectionActionHelper.buildRedirectUrlAction(context, ((FoundAction) requestedAction).getLocation());
    }
}
