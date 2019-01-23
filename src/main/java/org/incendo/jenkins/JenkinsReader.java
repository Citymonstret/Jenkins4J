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
import org.incendo.jenkins.exception.JenkinsNodeReadException;
import org.incendo.jenkins.objects.JobInfo;
import org.incendo.jenkins.views.MasterView;
import retrofit2.Retrofit;

import javax.annotation.Nonnull;
import java.util.Locale;

/**
 * Reader that reads data from a {@link JenkinsService} and then
 * parses it into Jenkins objects
 *
 * This class should not be directly interacted with, instead use {@link Jenkins}
 */
public abstract class JenkinsReader {

    private final JenkinsAPIType jenkinsAPIType;
    private final JenkinsService jenkinsService;

    protected JenkinsReader(@Nonnull final JenkinsPathProvider jenkinsPathProvider,
        @Nonnull final JenkinsAPIType jenkinsAPIType) {
        Preconditions.checkNotNull(jenkinsPathProvider, "Path provider must not be null");
        Preconditions.checkNotNull(jenkinsAPIType, "API type must not be null");
        this.jenkinsAPIType = jenkinsAPIType;
        final Retrofit retrofit = new Retrofit.Builder().baseUrl(jenkinsPathProvider.getBasePath())
            .build();
        this.jenkinsService = retrofit.create(JenkinsService.class);
    }

    final MasterView readMasterView() throws JenkinsNodeReadException {
        final String content;
        try {
            content = this.jenkinsService.getMasterNode(this.getAPITypeString()).execute().body().string();
        } catch (final Exception exception) {
            throw new JenkinsNodeReadException("master node", exception);
        }
        return this.readMasterView(content);
    }

    final JobInfo readJobInfo(@Nonnull final String jobName) throws JenkinsNodeReadException {
        final String content;
        try {
            content = this.jenkinsService.getJobInfo(jobName, this.getAPITypeString()).execute().body().string();
        } catch (final Exception exception) {
            throw new JenkinsNodeReadException(String.format("job node: %s", jobName), exception);
        }
        return this.readJobInfo(jobName, content);
    }

    protected abstract MasterView readMasterView(@Nonnull String rawContent) throws JenkinsNodeReadException;

    protected abstract JobInfo readJobInfo(@Nonnull final String jobName, @Nonnull final String rawContent)
        throws JenkinsNodeReadException;

    private String getAPITypeString() {
        return this.jenkinsAPIType.name().toLowerCase(Locale.ENGLISH);
    }

}
