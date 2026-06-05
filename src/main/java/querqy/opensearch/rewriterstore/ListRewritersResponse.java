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

import org.opensearch.core.action.ActionResponse;
import org.opensearch.core.common.io.stream.StreamInput;
import org.opensearch.core.common.io.stream.StreamOutput;
import org.opensearch.core.xcontent.ToXContentObject;
import org.opensearch.core.xcontent.XContentBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ListRewritersResponse extends ActionResponse implements ToXContentObject {

    private final List<RewriterInfo> rewriters;

    public ListRewritersResponse(final List<RewriterInfo> rewriters) {
        this.rewriters = rewriters != null ? rewriters : new ArrayList<>();
    }

    public ListRewritersResponse(final StreamInput in) throws IOException {
        super(in);
        int size = in.readVInt();
        this.rewriters = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            rewriters.add(new RewriterInfo(in));
        }
    }

    @Override
    public void writeTo(final StreamOutput out) throws IOException {
        out.writeVInt(rewriters.size());
        for (RewriterInfo info : rewriters) {
            info.writeTo(out);
        }
    }

    public List<RewriterInfo> getRewriters() {
        return rewriters;
    }

    @Override
    public XContentBuilder toXContent(final XContentBuilder builder, final Params params) throws IOException {
        builder.startObject();
        builder.startArray("rewriters");
        for (RewriterInfo info : rewriters) {
            info.toXContent(builder, params);
        }
        builder.endArray();
        builder.endObject();
        return builder;
    }

    public static class RewriterInfo implements ToXContentObject {
        private final String id;
        private final String className;

        public RewriterInfo(final String id, final String className) {
            this.id = id;
            this.className = className;
        }

        public RewriterInfo(final StreamInput in) throws IOException {
            this.id = in.readString();
            this.className = in.readOptionalString();
        }

        public void writeTo(final StreamOutput out) throws IOException {
            out.writeString(id);
            out.writeOptionalString(className);
        }

        public String getId() {
            return id;
        }

        public String getClassName() {
            return className;
        }

        @Override
        public XContentBuilder toXContent(final XContentBuilder builder, final Params params) throws IOException {
            builder.startObject();
            builder.field("id", id);
            if (className != null) {
                builder.field("class", className);
            }
            builder.endObject();
            return builder;
        }
    }
}
