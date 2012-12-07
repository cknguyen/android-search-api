/**
 * Copyright 2012 Muzima Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.mclinic.search.sample.resolver;

import java.net.URLConnection;

import com.burkeware.search.api.Context;
import com.burkeware.search.api.resolver.Resolver;
import com.burkeware.search.api.util.ResolverUtil;
import com.mclinic.search.sample.ServerConfigRegistry;

public abstract class AbstractResolver implements Resolver {

    private ServerConfigRegistry registry;

    public AbstractResolver() {
        registry = Context.getInstance(ServerConfigRegistry.class);
    }

    protected String getServer() {
        return registry.getEntryValue("server");
    }

    @Override
    public URLConnection authenticate(final URLConnection connection) {
        String basicAuth =
                ResolverUtil.getBasicAuth(registry.getEntryValue("username"), registry.getEntryValue("password"));
        connection.setRequestProperty("Authorization", basicAuth);
        return connection;
    }
}
