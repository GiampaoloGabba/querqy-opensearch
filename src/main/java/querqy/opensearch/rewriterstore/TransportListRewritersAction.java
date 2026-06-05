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

import static querqy.opensearch.rewriterstore.Constants.QUERQY_INDEX_NAME;

import org.opensearch.action.search.SearchRequest;
import org.opensearch.action.search.SearchResponse;
import org.opensearch.action.support.ActionFilters;
import org.opensearch.action.support.HandledTransportAction;
import org.opensearch.cluster.metadata.IndexNameExpressionResolver;
import org.opensearch.cluster.service.ClusterService;
import org.opensearch.common.inject.Inject;
import org.opensearch.core.action.ActionListener;
import org.opensearch.index.query.QueryBuilders;
import org.opensearch.search.SearchHit;
import org.opensearch.search.builder.SearchSourceBuilder;
import org.opensearch.tasks.Task;
import org.opensearch.transport.TransportService;
import org.opensearch.transport.client.Client;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class TransportListRewritersAction extends HandledTransportAction<ListRewritersRequest, ListRewritersResponse> {

    private final Client client;
    private final ClusterService clusterService;

    @Inject
    public TransportListRewritersAction(final TransportService transportService, final ActionFilters actionFilters,
                                        final ClusterService clusterService, final Client client) {
        super(ListRewritersAction.NAME, false, transportService, actionFilters, ListRewritersRequest::new);
        this.clusterService = clusterService;
        this.client = client;
    }

    @Override
    protected void doExecute(final Task task, final ListRewritersRequest request,
                             final ActionListener<ListRewritersResponse> listener) {

        // Check if index exists
        boolean indexExists = clusterService.state().metadata().hasIndex(QUERQY_INDEX_NAME);
        if (!indexExists) {
            // Return empty list if index doesn't exist
            listener.onResponse(new ListRewritersResponse(new ArrayList<>()));
            return;
        }

        // Search for all documents in the querqy index
        final SearchRequest searchRequest = new SearchRequest(QUERQY_INDEX_NAME);
        final SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.matchAllQuery());
        searchSourceBuilder.size(10000); // Reasonable limit for rewriters
        searchSourceBuilder.fetchSource(new String[]{"class"}, null);
        searchRequest.source(searchSourceBuilder);

        client.search(searchRequest, new ActionListener<SearchResponse>() {
            @Override
            public void onResponse(SearchResponse searchResponse) {
                List<ListRewritersResponse.RewriterInfo> rewriters = new ArrayList<>();

                Arrays.stream(searchResponse.getHits().getHits()).forEach(hit -> {
                    String id = hit.getId();
                    Map<String, Object> sourceMap = hit.getSourceAsMap();
                    String className = sourceMap != null ? (String) sourceMap.get("class") : null;
                    rewriters.add(new ListRewritersResponse.RewriterInfo(id, className));
                });

                listener.onResponse(new ListRewritersResponse(rewriters));
            }

            @Override
            public void onFailure(Exception e) {
                listener.onFailure(e);
            }
        });
    }
}
