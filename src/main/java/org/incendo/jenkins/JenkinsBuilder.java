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
import org.jetbrains.annotations.NotNull;


/**
 * Builder class for {@link Jenkins} instances
 */
@SuppressWarnings("unused") public class JenkinsBuilder {

    private String jenkinsPath;
    private JenkinsAPIType jenkinsAPIType = JenkinsAPIType.JSON;

    /**
     * Instantiates a new Jenkins builder.
     */
    JenkinsBuilder() {
    }

    /**
     * Specify the jenkins path
     *
     * @param jenkinsPath non-null, non-empty url (with optional trailing slash)
     * @return this {@link JenkinsBuilder} instance
     */
    public JenkinsBuilder withPath(@NotNull final String jenkinsPath) {
        Preconditions.checkNotNull(jenkinsPath, "Jenkins path may not be null");
        Preconditions.checkState(!jenkinsPath.isEmpty(), "Jenkins may not be empty");
        if (jenkinsPath.endsWith("/")) {
            this.jenkinsPath = jenkinsPath;
        } else {
            this.jenkinsPath = String.format("%s/", jenkinsPath);
        }
        return this;
    }

    /**
     * Compile the information into a {@link Jenkins} instance
     *
     * @return constructed {@link Jenkins} instance
     */
    public Jenkins build() {
        Preconditions.checkNotNull(jenkinsPath, "Path must be specified");
        final JenkinsPathProvider jenkinsPathProvider = new JenkinsPathProvider(this.jenkinsPath);
        return new Jenkins(jenkinsPathProvider, this.jenkinsAPIType);
    }

}
