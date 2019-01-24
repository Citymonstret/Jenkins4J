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

package org.incendo.jenkins;

import com.google.common.base.Preconditions;
import org.incendo.jenkins.json.JsonJenkinsReader;
import org.incendo.jenkins.objects.BuildInfo;
import org.incendo.jenkins.objects.JobDescription;
import org.incendo.jenkins.objects.JobInfo;
import org.incendo.jenkins.objects.MasterNode;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.concurrent.CompletableFuture;

/**
 * Jenkins4J instance class. Instances are retrieved using a
 * {@link JenkinsBuilder}, which can be initialized using {@link #newBuilder()}
 */
@SuppressWarnings({"WeakerAccess", "unused"}) public class Jenkins {

    private final JenkinsPathProvider jenkinsPathProvider;
    private final JenkinsReader jenkinsReader;

    /**
     * Instantiates a new Jenkins.
     *
     * @param jenkinsPathProvider the jenkins path provider
     * @param jenkinsAPIType      the jenkins api type
     */
    Jenkins(@NotNull final JenkinsPathProvider jenkinsPathProvider,
        @NotNull final JenkinsAPIType jenkinsAPIType) {
        Preconditions.checkNotNull(jenkinsPathProvider, "Path provider may not be null");
        this.jenkinsPathProvider = jenkinsPathProvider;
        final JenkinsReader jenkinsReader;
        if (jenkinsAPIType == JenkinsAPIType.JSON) {
            this.jenkinsReader = new JsonJenkinsReader(this, this.jenkinsPathProvider);
        } else {
            throw new IllegalArgumentException(
                String.format("Unimplemented API type: %s", jenkinsAPIType));
        }
    }

    /**
     * Create a new {@link JenkinsBuilder}
     *
     * @return new {@link JenkinsBuilder}
     */
    public static JenkinsBuilder newBuilder() {
        return new JenkinsBuilder();
    }

    /**
     * Get all available job descriptions from the Jenkins master node
     *
     * @return the job descriptions
     */
    public CompletableFuture<Collection<JobDescription>> getJobDescriptions() {
        return getMasterNode().thenApply(MasterNode::getJobDescriptions);
    }

    /**
     * Get information about the Jenkins master node
     *
     * @return the master node
     */
    public CompletableFuture<MasterNode> getMasterNode() {
        return CompletableFuture.supplyAsync(Jenkins.this.jenkinsReader::readMasterView);
    }

    /**
     * Get information about a Jenkins job
     *
     * @param jobName the job name
     * @return the job info
     */
    public CompletableFuture<JobInfo> getJobInfo(@NotNull final String jobName) {
        return CompletableFuture.supplyAsync(() -> Jenkins.this.jenkinsReader.readJobInfo(jobName));
    }

    /**
     * Get information about a Jenkins job build
     *
     * @param jobName the job name
     * @param build   the build
     * @return the build info
     */
    public CompletableFuture<BuildInfo> getBuildInfo(@NotNull final String jobName,
        final int build) {
        return CompletableFuture
            .supplyAsync(() -> Jenkins.this.jenkinsReader.readBuildInfo(jobName, build));
    }

    /**
     * Get the jenkins path provider used in this {@link Jenkins} instance
     *
     * @return path provider
     */
    public JenkinsPathProvider getJenkinsPathProvider() {
        return this.jenkinsPathProvider;
    }
}
