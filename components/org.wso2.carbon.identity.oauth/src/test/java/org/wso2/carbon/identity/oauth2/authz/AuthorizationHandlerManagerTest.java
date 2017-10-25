/*
 * Copyright (c) 2017, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * WSO2 Inc. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.wso2.carbon.identity.oauth2.authz;

import org.apache.axis2.context.ConfigurationContext;
import org.apache.axis2.engine.AxisConfiguration;
import org.apache.oltu.oauth2.common.error.OAuthError;
import org.powermock.reflect.internal.WhiteboxImpl;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.wso2.carbon.identity.core.internal.IdentityCoreServiceComponent;
import org.wso2.carbon.identity.oauth2.dto.OAuth2AuthorizeReqDTO;
import org.wso2.carbon.identity.oauth2.test.utils.CommonTestUtils;
import org.wso2.carbon.utils.ConfigurationContextService;

import static org.testng.AssertJUnit.assertEquals;

public class AuthorizationHandlerManagerTest {

    private AuthorizationHandlerManager authorizationHandlerManager;

    @BeforeMethod
    public void setUp() throws Exception {
        CommonTestUtils.initPrivilegedCarbonContext();
        ConfigurationContextService service = new ConfigurationContextService(new ConfigurationContext(
                new AxisConfiguration()), new ConfigurationContext(new AxisConfiguration()));
        IdentityCoreServiceComponent identityCoreServiceComponent = new IdentityCoreServiceComponent();
        WhiteboxImpl.invokeMethod(identityCoreServiceComponent, "setConfigurationContextService", service);
        authorizationHandlerManager = AuthorizationHandlerManager.getInstance();
    }

    @Test
    public void testGetInstance() throws Exception {
        CommonTestUtils.testSingleton(authorizationHandlerManager, AuthorizationHandlerManager.getInstance());
    }

    @DataProvider(name = "BuildData")
    public Object[][] buildData() {
        return new Object[][] {
                {"response_type"}
        };
    }

    @Test(dataProvider = "BuildData")
    public void testHandleAuthorization(String response) throws Exception {
        OAuth2AuthorizeReqDTO oAuth2AuthorizeReqDTO = new OAuth2AuthorizeReqDTO();
        oAuth2AuthorizeReqDTO.setResponseType(response);
        assertEquals(authorizationHandlerManager.handleAuthorization(oAuth2AuthorizeReqDTO).getErrorCode(),
                OAuthError.CodeResponse.UNSUPPORTED_RESPONSE_TYPE);
    }
}
