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
import org.incendo.jenkins.objects.JobDescription;
import org.incendo.jenkins.objects.MasterNode;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Json deserializer for {@link MasterNode}
 * {@inheritDoc}
 */
final class MasterNodeDeserializer implements JsonDeserializer<MasterNode> {

    private final JsonJenkinsReader jsonJenkinsReader;

    /**
     * Instantiates a new Master node deserializer.
     *
     * @param jsonJenkinsReader the json jenkins reader
     */
    MasterNodeDeserializer(@NotNull final JsonJenkinsReader jsonJenkinsReader) {
        Preconditions.checkNotNull(jsonJenkinsReader, "JsonJenkinsReader may not me null");
        this.jsonJenkinsReader = jsonJenkinsReader;
    }

    @NotNull @Override public MasterNode deserialize(@NotNull final JsonElement json, final Type typeOfT,
        final JsonDeserializationContext context) throws JsonParseException {
        final JsonObject jsonObject = json.getAsJsonObject();
        final JsonArray jobsArray = jsonObject.get("jobs").getAsJsonArray();
        final Collection<JobDescription> jobDescriptions = new ArrayList<>(jobsArray.size());
        for (final JsonElement element : jobsArray) {
            final JobDescription description =
                jsonJenkinsReader.getGson().fromJson(element, JobDescription.class);
            jobDescriptions.add(description);
        }
        final MasterNode masterNode =
            new MasterNode(this.jsonJenkinsReader.getJenkins(), jobDescriptions);
        jobDescriptions.forEach(jobDescription -> jobDescription.setParent(masterNode));
        return masterNode;
    }

}
