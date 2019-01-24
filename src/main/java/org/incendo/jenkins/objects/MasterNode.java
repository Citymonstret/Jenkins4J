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

package org.incendo.jenkins.objects;

import com.google.common.base.Preconditions;
import org.incendo.jenkins.Jenkins;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.CompletableFuture;

/**
 * The type Master node.
 */
@SuppressWarnings({"unused", "WeakerAccess"}) public class MasterNode implements Node, NodePath {

    private final Collection<JobDescription> jobDescriptions;
    private final Jenkins jenkins;

    /**
     * Instantiates a new Master node.
     *
     * @param jenkins         the jenkins
     * @param jobDescriptions the job descriptions
     */
    public MasterNode(@NotNull final Jenkins jenkins,
        @NotNull final Collection<JobDescription> jobDescriptions) {
        this.jenkins = Preconditions.checkNotNull(jenkins, "Jenkins may not be null");
        this.jobDescriptions =
            Preconditions.checkNotNull(jobDescriptions, "Job descriptions may not be null");
    }

    /**
     * Get the jenkins master node url
     *
     * @return jenkins base path
     */
    @Override public String getUrl() {
        return this.jenkins.getJenkinsPathProvider().getBasePath();
    }

    /**
     * Gets job descriptions.
     *
     * @return the job descriptions
     */
    public Collection<JobDescription> getJobDescriptions() {
        return Collections.unmodifiableCollection(this.jobDescriptions);
    }

    /**
     * Gets job info.
     *
     * @param jobName the job name
     * @return the job info
     */
    public CompletableFuture<JobInfo> getJobInfo(@NotNull final String jobName) {
        return this.jenkins.getJobInfo(jobName);
    }

    @Contract(pure = true) @Override public Jenkins getJenkins() {
        return this.jenkins;
    }

}
