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

public final class JobDescription {

    private final String jenkinsClass;
    private final String name;
    private final String url;
    private final String color;

    public JobDescription(@Nonnull final String jenkinsClass, @Nonnull final String name,
        @Nonnull final String url, @Nonnull final String color) {
        Preconditions.checkNotNull(jenkinsClass, "Jenkins class may not be null");
        Preconditions.checkNotNull(name, "Name may not be null");
        Preconditions.checkNotNull(url, "Url may not be null");
        Preconditions.checkNotNull(color, "Color may not be null");
        this.jenkinsClass = jenkinsClass;
        this.name = name;
        this.url = url;
        this.color = color;
    }

    public String getJenkinsClass() {
        return this.jenkinsClass;
    }

    public String getName() {
        return this.name;
    }

    public String getUrl() {
        return this.url;
    }

    public String getColor() {
        return this.color;
    }

    @Override public String toString() {
        return "JobDescription{" + "jenkinsClass='" + jenkinsClass + '\'' + ", name='" + name + '\''
            + ", url='" + url + '\'' + ", color='" + color + '\'' + '}';
    }

}
