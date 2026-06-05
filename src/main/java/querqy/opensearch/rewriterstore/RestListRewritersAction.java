/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package querqy.opensearch.rewriterstore;

import org.opensearch.action.ActionRequestBuilder;
import org.opensearch.rest.BaseRestHandler;
import org.opensearch.rest.RestRequest;
import org.opensearch.rest.action.RestToXContentListener;
import org.opensearch.transport.client.OpenSearchClient;
import org.opensearch.transport.client.node.NodeClient;

import java.util.Collections;
import java.util.List;

import static querqy.opensearch.rewriterstore.Constants.QUERQY_REWRITER_BASE_ROUTE;

public class RestListRewritersAction extends BaseRestHandler {

    @Override
    public String getName() {
        return "List all Querqy rewriters";
    }

    @Override
    public List<Route> routes() {
        return Collections.singletonList(new Route(RestRequest.Method.GET, QUERQY_REWRITER_BASE_ROUTE));
    }

    @Override
    protected RestChannelConsumer prepareRequest(final RestRequest request, final NodeClient client) {
        final ListRewritersRequestBuilder builder = new ListRewritersRequestBuilder(client, ListRewritersAction.INSTANCE,
                new ListRewritersRequest());

        return (channel) -> builder.execute(new RestToXContentListener<>(channel));
    }

    public static class ListRewritersRequestBuilder
            extends ActionRequestBuilder<ListRewritersRequest, ListRewritersResponse> {

        public ListRewritersRequestBuilder(final OpenSearchClient client, final ListRewritersAction action,
                                           final ListRewritersRequest request) {
            super(client, action, request);
        }
    }
}
