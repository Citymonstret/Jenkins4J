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

import javax.annotation.Nonnull;

public final class BuildDescription {

    private final String jenkinsClass;
    private final int number;
    private final String url;

    public BuildDescription(@Nonnull final String jenkinsClass, final int number,
        @Nonnull final String url) {
        Preconditions.checkNotNull(jenkinsClass, "Jenkins class may not be null");
        Preconditions.checkNotNull(url, "Url may not be null");
        this.jenkinsClass = jenkinsClass;
        this.number = number;
        this.url = url;
    }

    public String getJenkinsClass() {
        return this.jenkinsClass;
    }

    public int getNumber() {
        return this.number;
    }

    public String getUrl() {
        return this.url;
    }

    @Override public String toString() {
        return "BuildDescription{" + "jenkinsClass='" + jenkinsClass + '\'' + ", number=" + number
            + ", url='" + url + '\'' + '}';
    }

}
