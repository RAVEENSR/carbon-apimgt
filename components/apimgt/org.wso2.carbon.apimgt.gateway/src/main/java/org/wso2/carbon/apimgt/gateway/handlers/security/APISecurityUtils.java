/*
 *  Copyright WSO2 Inc.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package org.wso2.carbon.apimgt.gateway.handlers.security;

import org.apache.synapse.MessageContext;
import org.apache.synapse.core.axis2.Axis2MessageContext;
import org.wso2.carbon.apimgt.impl.APIConstants;

import java.util.Map;

public class APISecurityUtils {

    public static final String API_AUTH_CONTEXT = "__API_AUTH_CONTEXT";

    private static String keyValidatorClientType;

    /**
     * Add AuthenticationContext information into a validated request. This method does not
     * allow overriding existing AuthenticationContext information on the request. Therefore
     * this should only be used with newly validated requests. It shouldn't be used to modify
     * already validated requests.
     *
     * @param synCtx        A newly authenticated request
     * @param authContext   AuthenticationContext information to be added
     * @param contextHeader A security context header to be set on the request (possibly null)
     */
    public static void setAuthenticationContext(MessageContext synCtx,
                                                AuthenticationContext authContext,
                                                String contextHeader) {
        synCtx.setProperty(API_AUTH_CONTEXT, authContext);
        synCtx.setProperty(APIConstants.API_KEY_TYPE, authContext.getKeyType());
        if (authContext.getIssuer() != null) {
            synCtx.setProperty(APIConstants.KeyManager.ISSUER, authContext.getIssuer());
        }
        if (contextHeader != null && authContext.getCallerToken() != null) {
            Map transportHeaders = (Map) ((Axis2MessageContext) synCtx).getAxis2MessageContext()
                    .getProperty(org.apache.axis2.context.MessageContext.TRANSPORT_HEADERS);
            transportHeaders.put(contextHeader, authContext.getCallerToken());
        }
    }

    /**
     * Add AuthenticationContext information into a validated request. This method does not
     * allow overriding existing AuthenticationContext information on the request. Therefore
     * this should only be used with newly validated requests. It shouldn't be used to modify
     * already validated requests.
     *
     * @param synCtx        A newly authenticated request
     * @param authContext   AuthenticationContext information to be added
     */
    public static void setAuthenticationContext(MessageContext synCtx,
                                                AuthenticationContext authContext) {
        synCtx.setProperty(API_AUTH_CONTEXT, authContext);
        synCtx.setProperty(APIConstants.API_KEY_TYPE, authContext.getKeyType());
    }


    /**
     * Retrieve the AuthenticationContext information from the request. If the request hasn't
     * been validated yet, this method will return null.
     *
     * @param synCtx Current message
     * @return An AuthenticationContext instance or null
     */
    public static AuthenticationContext getAuthenticationContext(MessageContext synCtx) {
        return (AuthenticationContext) synCtx.getProperty(API_AUTH_CONTEXT);
    }

}
