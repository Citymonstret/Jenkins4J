//
// MIT License
//
// Copyright (c) 2019 Alexander Söderberg
//
// Permission is hereby granted, free of charge, to any person obtaining a copy
// of this software and associated documentation files (the "Software"), to deal
// in the Software without restriction, including without limitation the rights
// to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
// copies of the Software, and to permit persons to whom the Software is
// furnished to do so, subject to the following conditions:
//
// The above copyright notice and this permission notice shall be included in all
// copies or substantial portions of the Software.
//
// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
// IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
// FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
// AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
// LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
// OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
// SOFTWARE.
//

package org.incendo.jenkins.json;

import com.google.common.base.Preconditions;
import com.google.gson.*;
import org.incendo.jenkins.objects.ArtifactDescription;
import org.incendo.jenkins.objects.BuildInfo;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Json deserializer for {@link BuildInfo}
 */
final class BuildInfoDeserializer implements JsonDeserializer<BuildInfo> {

    private final JsonJenkinsReader jsonJenkinsReader;

    /**
     * Instantiates a new Build info deserializer.
     *
     * @param jsonJenkinsReader the json jenkins reader
     */
    BuildInfoDeserializer(@NotNull final JsonJenkinsReader jsonJenkinsReader) {
        this.jsonJenkinsReader =
            Preconditions.checkNotNull(jsonJenkinsReader, "JsonJenkinsReader may not be null");
    }

    @Override public BuildInfo deserialize(final JsonElement json, final Type typeOfT,
        final JsonDeserializationContext context) throws JsonParseException {
        final JsonObject jsonObject = json.getAsJsonObject();
        final boolean building = jsonObject.get("building").getAsBoolean();
        final String result = jsonObject.get("result").getAsString();
        final String displayName = jsonObject.get("displayName").getAsString();
        final String fullDisplayName = jsonObject.get("fullDisplayName").getAsString();
        final int id = jsonObject.get("id").getAsInt();
        final long duration = jsonObject.get("duration").getAsLong();
        final long timestamp = jsonObject.get("timestamp").getAsLong();
        final String url = jsonObject.get("url").getAsString();
        final Collection<ArtifactDescription> artifacts = new ArrayList<>();
        final JsonArray artifactArray = jsonObject.get("artifacts").getAsJsonArray();
        for (final JsonElement artifact : artifactArray) {
            artifacts.add(
                this.jsonJenkinsReader.getGson().fromJson(artifact, ArtifactDescription.class));
        }
        final BuildInfo buildInfo =
            new BuildInfo(jsonJenkinsReader.getJenkins(), building, result, displayName,
                fullDisplayName, id, duration, timestamp, url, artifacts);
        artifacts.forEach(artifactInfo -> artifactInfo.setParent(buildInfo));
        return buildInfo;
    }

}
