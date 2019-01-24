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
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.incendo.jenkins.Jenkins;
import org.incendo.jenkins.JenkinsAPIType;
import org.incendo.jenkins.JenkinsPathProvider;
import org.incendo.jenkins.JenkinsReader;
import org.incendo.jenkins.exception.JenkinsNodeReadException;
import org.incendo.jenkins.objects.*;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;


/**
 * {@link JenkinsReader} using Google Gson
 */
public final class JsonJenkinsReader extends JenkinsReader {

    private final Jenkins jenkins;
    private final Gson gson;

    /**
     * Instantiates a new Json jenkins reader.
     *
     * @param jenkins             the jenkins
     * @param jenkinsPathProvider the jenkins path provider
     */
    public JsonJenkinsReader(@NotNull final Jenkins jenkins,
        @NotNull JenkinsPathProvider jenkinsPathProvider) {
        super(jenkinsPathProvider, JenkinsAPIType.JSON);
        this.jenkins = Preconditions.checkNotNull(jenkins, "Jenkins may not be null");
        this.gson = new GsonBuilder()
            .registerTypeAdapter(JobDescription.class, new JobDescriptionDeserializer())
            .registerTypeAdapter(MasterNode.class, new MasterNodeDeserializer(this))
            .registerTypeAdapter(BuildDescription.class, new BuildDescriptionDeserializer())
            .registerTypeAdapter(JobInfo.class, new JobInfoDeserializer(this))
            .registerTypeAdapter(ArtifactDescription.class, new ArtifactDescriptionDeserializer())
            .registerTypeAdapter(BuildInfo.class, new BuildInfoDeserializer(this)).create();
    }

    @Override protected MasterNode readMasterView(@NotNull final String rawContent)
        throws JenkinsNodeReadException {
        return gson.fromJson(rawContent, MasterNode.class);
    }

    @Override
    protected JobInfo readJobInfo(@NotNull final String jobName, final @NotNull String rawContent)
        throws JenkinsNodeReadException {
        return gson.fromJson(rawContent, JobInfo.class);
    }

    @Override protected BuildInfo readBuildInfo(@NotNull final String jobName, final int build,
        @NotNull String rawContent) throws JenkinsNodeReadException {
        return gson.fromJson(rawContent, BuildInfo.class);
    }

    /**
     * Gets gson.
     *
     * @return the gson
     */
    @Contract(pure = true) Gson getGson() {
        return this.gson;
    }

    /**
     * Gets jenkins.
     *
     * @return the jenkins
     */
    @Contract(pure = true) Jenkins getJenkins() {
        return this.jenkins;
    }

}
