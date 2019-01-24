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
import okhttp3.ResponseBody;
import org.incendo.jenkins.exception.JenkinsBuildNotFoundException;
import org.incendo.jenkins.exception.JenkinsJobNotFoundException;
import org.incendo.jenkins.exception.JenkinsNodeReadException;
import org.incendo.jenkins.objects.BuildInfo;
import org.incendo.jenkins.objects.JobInfo;
import org.incendo.jenkins.objects.MasterNode;
import org.jetbrains.annotations.NotNull;
import retrofit2.Response;
import retrofit2.Retrofit;

import java.util.Locale;

/**
 * Reader that reads data from a {@link JenkinsService} and then
 * parses it into Jenkins objects
 * <p>
 * This class should not be directly interacted with, instead use {@link Jenkins}
 */
public abstract class JenkinsReader {

    private final JenkinsAPIType jenkinsAPIType;
    private final JenkinsService jenkinsService;

    /**
     * Instantiates a new Jenkins reader.
     *
     * @param jenkinsPathProvider the jenkins path provider
     * @param jenkinsAPIType      the jenkins api type
     */
    protected JenkinsReader(@NotNull final JenkinsPathProvider jenkinsPathProvider,
        @NotNull final JenkinsAPIType jenkinsAPIType) {
        Preconditions.checkNotNull(jenkinsPathProvider, "Path provider may not be null");
        Preconditions.checkNotNull(jenkinsAPIType, "API type may not be null");
        this.jenkinsAPIType = jenkinsAPIType;
        final Retrofit retrofit =
            new Retrofit.Builder().baseUrl(jenkinsPathProvider.getBasePath()).build();
        this.jenkinsService = retrofit.create(JenkinsService.class);
    }

    /**
     * Read master view master node.
     *
     * @return the master node
     * @throws JenkinsNodeReadException the jenkins node read exception
     */
    final MasterNode readMasterView() throws JenkinsNodeReadException {
        final String content;
        try {
            final ResponseBody body =
                this.jenkinsService.getMasterNode(this.getAPITypeString()).execute().body();
            if (body == null) {
                throw new NullPointerException("Response body is null");
            }
            content = body.string();
        } catch (final Exception exception) {
            throw new JenkinsNodeReadException("master node", exception);
        }
        return this.readMasterView(content);
    }

    /**
     * Read job info job info.
     *
     * @param jobName the job name
     * @return the job info
     * @throws JenkinsNodeReadException the jenkins node read exception
     */
    final JobInfo readJobInfo(@NotNull final String jobName) throws JenkinsNodeReadException {
        final String content;
        try {
            final Response<ResponseBody> response =
                this.jenkinsService.getJobInfo(jobName, this.getAPITypeString()).execute();
            if (response == null) {
                throw new NullPointerException("Response is null");
            }
            if (response.code() == 404) {
                throw new JenkinsJobNotFoundException(jobName);
            }
            final ResponseBody body = response.body();
            if (body == null) {
                throw new NullPointerException("Response body is null");
            }
            content = body.string();
        } catch (final Exception exception) {
            throw new JenkinsNodeReadException(String.format("job node: %s", jobName), exception);
        }
        return this.readJobInfo(jobName, content);
    }

    /**
     * Read build info build info.
     *
     * @param jobName the job name
     * @param build   the build
     * @return the build info
     * @throws JenkinsNodeReadException the jenkins node read exception
     */
    final BuildInfo readBuildInfo(@NotNull final String jobName, final int build)
        throws JenkinsNodeReadException {
        final String content;
        try {
            final Response<ResponseBody> response =
                this.jenkinsService.getBuildInfo(jobName, build, this.getAPITypeString()).execute();
            if (response == null) {
                throw new NullPointerException("Response is null");
            }
            if (response.code() == 404) {
                throw new JenkinsBuildNotFoundException(jobName, build);
            }
            final ResponseBody body = response.body();
            if (body == null) {
                throw new NullPointerException("Response body is null");
            }
            content = body.string();
        } catch (final Exception exception) {
            throw new JenkinsNodeReadException(String.format("job node: %s", jobName), exception);
        }
        return this.readBuildInfo(jobName, build, content);
    }

    /**
     * Read master view master node.
     *
     * @param rawContent the raw content
     * @return the master node
     * @throws JenkinsNodeReadException the jenkins node read exception
     */
    protected abstract MasterNode readMasterView(@NotNull String rawContent)
        throws JenkinsNodeReadException;

    /**
     * Read job info job info.
     *
     * @param jobName    the job name
     * @param rawContent the raw content
     * @return the job info
     * @throws JenkinsNodeReadException the jenkins node read exception
     */
    protected abstract JobInfo readJobInfo(@NotNull final String jobName,
        @NotNull final String rawContent) throws JenkinsNodeReadException;

    /**
     * Read build info build info.
     *
     * @param jobName    the job name
     * @param build      the build
     * @param rawContent the raw content
     * @return the build info
     * @throws JenkinsNodeReadException the jenkins node read exception
     */
    protected abstract BuildInfo readBuildInfo(@NotNull final String jobName, final int build,
        @NotNull final String rawContent) throws JenkinsNodeReadException;

    @NotNull private String getAPITypeString() {
        return this.jenkinsAPIType.name().toLowerCase(Locale.ENGLISH);
    }

}
