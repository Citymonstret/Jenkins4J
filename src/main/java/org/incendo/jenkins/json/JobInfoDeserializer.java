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
import org.incendo.jenkins.objects.BuildDescription;
import org.incendo.jenkins.objects.JobInfo;

import javax.annotation.Nonnull;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Json deserializer for {@link JobInfo}
 */
final class JobInfoDeserializer implements JsonDeserializer<JobInfo> {

    private final JsonJenkinsReader jsonJenkinsReader;

    JobInfoDeserializer(@Nonnull final JsonJenkinsReader jsonJenkinsReader) {
        Preconditions.checkNotNull(jsonJenkinsReader, "JsonJenkinsReader must not be null");
        this.jsonJenkinsReader = jsonJenkinsReader;
    }

    @Override
    public JobInfo deserialize(final JsonElement json, final Type typeOfT,
        final JsonDeserializationContext context)
        throws JsonParseException {
        final JsonObject jsonObject = json.getAsJsonObject();
        final String name = jsonObject.get("name").getAsString();
        final String fullName = jsonObject.get("fullName").getAsString();
        final String displayName = jsonObject.get("displayName").getAsString();
        final String fullDisplayName = jsonObject.get("fullDisplayName").getAsString();
        final String description = jsonObject.get("description").getAsString();
        final String url = jsonObject.get("url").getAsString();
        final Collection<BuildDescription> builds = new ArrayList<>();
        if (jsonObject.has("builds")) {
            final JsonArray buildArray = jsonObject.get("builds").getAsJsonArray();
            for (final JsonElement build : buildArray) {
                builds.add(jsonJenkinsReader.getGson().fromJson(build, BuildDescription.class));
            }
        }
        final BuildDescription lastBuild;
        if (jsonObject.has("lastBuild")) {
            lastBuild = jsonJenkinsReader.getGson().fromJson(jsonObject.get("lastBuild"), BuildDescription.class);
        } else {
            lastBuild = null;
        }
        final BuildDescription lastCompletedBuild;
        if (jsonObject.has("lastCompletedBuild")) {
            lastCompletedBuild = jsonJenkinsReader.getGson().fromJson(jsonObject.get("lastBuild"), BuildDescription.class);
        } else {
            lastCompletedBuild = null;
        }
        final BuildDescription lastFailedBuild;
        if (jsonObject.has("lastFailedBuild")) {
            lastFailedBuild = jsonJenkinsReader.getGson().fromJson(jsonObject.get("lastBuild"), BuildDescription.class);
        } else {
            lastFailedBuild = null;
        }
        final BuildDescription lastSuccessfulBuild;
        if (jsonObject.has("lastSuccessfulBuild")) {
            lastSuccessfulBuild = jsonJenkinsReader.getGson().fromJson(jsonObject.get("lastBuild"), BuildDescription.class);
        } else {
            lastSuccessfulBuild = null;
        }
        final int nextBuildNumber = jsonObject.get("nextBuildNumber").getAsInt();
        return new JobInfo(name, fullName, displayName, fullDisplayName, description, url, builds,
            lastBuild, lastCompletedBuild, lastFailedBuild, lastSuccessfulBuild, nextBuildNumber);
    }

}
